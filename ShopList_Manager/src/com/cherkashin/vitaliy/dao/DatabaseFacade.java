package com.cherkashin.vitaliy.dao;

import java.io.Serializable;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Order;

import database.ConnectWrap;
import database.wrap_mysql.Actions;
import database.wrap_mysql.Current_action;
import database.wrap_mysql.Shop_list;

public class DatabaseFacade implements Serializable{
	private final static long serialVersionUID=1L;
	
	// private Logger logger=Logger.getLogger(this.getClass());
	private ILogger logger=new ILogger(){
		private final static long serialVersionUID=1L;
		@Override
		public void debug(String message) {
			System.out.println("DEBUG:"+message);
		}

		@Override
		public void info(String message) {
			System.out.println("INFO:"+message);
		}

		@Override
		public void warn(String message) {
			System.err.println("WARN:"+message);
		}

		@Override
		public void error(String message) {
			System.err.println("ERROR:"+message);
		}
	};
	
	/** получить список последних Actions */
	@SuppressWarnings("unchecked")
	public List<Actions> getActions(ConnectWrap connector, int size){
		try{
			return (List<Actions>)connector.getSession().createCriteria(Actions.class).addOrder(Order.desc("id")).setMaxResults(size).list();
		}catch(Exception ex){
			logger.error("#getActions Exception:"+ex.getMessage());
			return null;
		}
	}

	/** удалить действие по указанному номеру */
	public boolean removeAction(ConnectWrap connector, int actionId) {
		try{
			connector.getConnection().createStatement().executeUpdate("delete from actions where id="+actionId);
			connector.getConnection().createStatement().executeUpdate("delete from logger where  logger.id_session in (select id_parse_session from current_action where current_action.id_actions="+actionId+") ");
			connector.getConnection().createStatement().executeUpdate("delete from parse_record where parse_record.id_session in (select id_parse_session from current_action where current_action.id_actions="+actionId+") ");
			connector.getConnection().createStatement().executeUpdate("delete from parse_session where parse_session.id in (select id_parse_session from current_action where current_action.id_actions="+actionId+") ");
			connector.getConnection().createStatement().executeUpdate("delete from current_action where id_actions="+actionId);
			connector.getConnection().commit();
			return true;
		}catch(Exception ex){
			logger.error("#removeActions Exception:"+ex.getMessage());
			return false;
		}
	}

	/**
	 * @param connectWrap - соединение с базой данных
	 * @param listOfStartPage - map из <им€ парсера, стартова€ страница>
	 * @return 
	 * <ul>
	 * 	<li><b></b></li>
	 * 	<li><b></b></li>
	 * </ul>
	 * */
	public Integer createAction(ConnectWrap connectWrap, 
								HashMap<String,String> listParsers) {
		Session session=connectWrap.getSession();
		session.beginTransaction();
		try{
			// создать объект Actions
			Actions actions=new Actions();
			actions.setDate_write(new Date());
			actions.setId_action_state(4);
			session.save(actions);
			
			// создать объекты Current_action
			Iterator<String> classNames=listParsers.keySet().iterator();
			while(classNames.hasNext()){
				// создать объект CurrentActions
				String className=classNames.next();
				String startPage=listParsers.get(className);
				Current_action currentAction=new Current_action();
				currentAction.setId_actions(actions.getId());
				currentAction.setId_shop(this.getShopIdByStartPage(connectWrap, startPage));
				currentAction.setParserClassName(className);
				session.save(currentAction);
			}
			session.getTransaction().commit();
			return actions.getId();
		}catch(Exception ex){
			session.getTransaction().rollback();
			logger.error("#createAction Exception: "+ex.getMessage());
			return null;
		}
	}
	
	/**  получить список уникальных идентификаторов магазинов по их стартовым страницам
	 * @param connector
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unused")
	private ArrayList<Integer> getShopIdList(ConnectWrap connector, ArrayList<String> list){
		ArrayList<Integer> returnValue=new ArrayList<Integer>();
		for(String startPage:list){
			Integer shopId=this.getShopIdByStartPage(connector, startPage);
			if(shopId!=null){
				returnValue.add(shopId);
			}
		}
		return returnValue;
	}
	
	private Integer getShopIdByStartPage(ConnectWrap connector, String startPage){
		Integer returnValue=null;
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select * from shop_list where start_page like '"+startPage.replaceAll("'", "''")+"'");
			if(rs.next()){
				returnValue=rs.getInt("id");
				if(returnValue.intValue()==0)returnValue=null;
			}else{
				logger.debug("shop does not found, create it "+startPage);
				// магазин не найден - создать и записать 
				Shop_list shop=new Shop_list();
				shop.setStart_page(startPage);
				connector.getSession().save(shop);
				returnValue=shop.getId();
			}
			rs.getStatement().close();
		}catch(Exception ex){
			returnValue=null;
			logger.error("#getShopIdByStartPage Exception:"+ex.getMessage());
		}
		return returnValue;
	}

	/**   ол-во записей, ( распарсенные ) в текущий момент в базе */
	public int getStatisticsRecordCount(ConnectWrap connector, Integer actionId) {
		try{
			StringBuffer query=new StringBuffer();
			query.append("select count(parse_record.id) from current_action\n");
			query.append("inner join parse_record on parse_record.id_session=current_action.id_parse_session\n");
			query.append("where current_action.id_actions="+actionId);
			ResultSet rs=connector.getConnection().createStatement().executeQuery(query.toString());
			rs.next();
			int returnValue=rs.getInt(1);
			rs.getStatement().close();
			return returnValue;
		}catch(Exception ex){
			return 0;
		}
	}

	/** –аспарсено магазинов в данный момент */
	public int getStatisticsParsetPosition(ConnectWrap connector, Integer actionId) {
		try{
			StringBuffer query=new StringBuffer();
			query.append("select count(*) from current_action inner join parse_session on current_action.id_parse_session=parse_session.id where current_action.id_actions="+actionId);
			ResultSet rs=connector.getConnection().createStatement().executeQuery(query.toString());
			rs.next();
			int returnValue=rs.getInt(1);
			rs.getStatement().close();
			return returnValue;
		}catch(Exception ex){
			return 0;
		}
	}

	/** ¬сего парсеров в текущей задаче */
	public int getStatisticsParserAll(ConnectWrap connector, Integer actionId) {
		try{
			StringBuffer query=new StringBuffer();
			query.append("select count(*) from current_action left join parse_session on current_action.id_parse_session=parse_session.id where current_action.id_actions="+actionId);
			ResultSet rs=connector.getConnection().createStatement().executeQuery(query.toString());
			rs.next();
			int returnValue=rs.getInt(1);
			rs.getStatement().close();
			return returnValue;
		}catch(Exception ex){
			return 0;
		}
	}
	
	/** обертка дл€ отображени€ ошибочно загруженных парсеров */
	public class WrapErrorParsers implements Serializable{
		private final static long serialVersionUID=1L;
		
		private int idParseSession;
		private String startPage;
		private int count=0;
		
		public WrapErrorParsers(int idParseSesssion, String startPage, int count){
			this.idParseSession=idParseSesssion;
			this.startPage=startPage;
			this.count=count;
		}

		public int getIdParseSession() {
			return idParseSession;
		}

		public String getStartPage() {
			return startPage;
		}

		public int getCount() {
			return count;
		}
	}

	/** обертка дл€ отображени€ парсеров, загруженных с сообщени€ми об ошибках в логгере */
	public class WrapErrorLogger implements Serializable{
		private final static long serialVersionUID=1L;
		
		private int idParseSession;
		private String startPage;
		private int countRecord;
		private int countLogger;
		
		/**обертка дл€ отображени€ парсеров, загруженных с сообщени€ми об ошибках в логгере
		 * @param idParseSesssion
		 * @param startPage
		 * @param countRecord
		 * @param countLogger
		 */
		public WrapErrorLogger(int idParseSesssion, String startPage, int countRecord, int countLogger){
			this.idParseSession=idParseSesssion;
			this.startPage=startPage;
			this.countRecord=countRecord;
			this.countLogger=countLogger;
		}

		public int getIdParseSession() {
			return idParseSession;
		}

		public String getStartPage() {
			return startPage;
		}

		public int getCountRecord(){
			return this.countRecord;
		}
		
		public int getCountLogger(){
			return this.countLogger;
		}
	}
	
	/** обертка дл€ отображени€ парсеров, загруженных с сообщени€ми об ошибках в логгере */
	public class WrapParseResult implements Serializable{
		private final static long serialVersionUID=1L;
		private String startPage;
		private String result;
		private int recordCount;
		
		/** обертка дл€ отображени€ парсеров, загруженных с сообщени€ми об ошибках в логгере 
		 * @param startPage - стартова€ страница
		 * @param result - результат парсинга
		 * @param recordCount - кол-во записей в парсинге
		 */
		public WrapParseResult(String startPage, String result, int recordCount){
			this.startPage=startPage;
			this.result=result;
			this.recordCount=recordCount;
		}

		/** получить стартовую страницу */
		public String getStartPage() {
			return startPage;
		}

		/** получить наименование результата */
		public String getResult() {
			return result;
		}

		/** получить кол-во записей  */
		public int getRecordCount() {
			return recordCount;
		}
	}
	
	
	public class LoggerInfo implements Serializable{
		private final static long serialVersionUID=1L;
		private String levelName;
		private String message;
		
		public LoggerInfo(String levelName, String message){
			this.levelName=levelName;
			this.message=message;
		}

		public String getLevelName() {
			return levelName;
		}

		public String getMessage() {
			return message;
		}
	}
	
	/**  ол-во магазинов, которые распарсены с ошибками */
	public ArrayList<WrapErrorParsers> getStatisticsErrorParsers(ConnectWrap connector, Integer actionId) {
		ArrayList<WrapErrorParsers> returnValue=new ArrayList<WrapErrorParsers>();
		try{
			StringBuffer query=new StringBuffer();
			query.append("select current_action.id, parse_session.id, shop_list.start_page, (select count(*) from parse_record where id_session=parse_session.id) \n");
			query.append("from current_action \n");
			query.append("inner join parse_session on current_action.id_parse_session=parse_session.id \n");
			query.append("inner join shop_list on shop_list.id=parse_session.id_shop \n");
			query.append("where id_parse_result=1 and current_action.id_actions="+actionId);
			ResultSet rs=connector.getConnection().createStatement().executeQuery(query.toString());
			while(rs.next()){
				returnValue.add(new WrapErrorParsers(rs.getInt(2), rs.getString(3), rs.getInt(4)));
			}
			return returnValue;
		}catch(Exception ex){
			return returnValue;
		}
	}

	/** ћагазины, которые имеют в логе сообщени€ об ошибках */
	public ArrayList<WrapErrorLogger> getStatisticsLogger(
			ConnectWrap connector, Integer actionId, int loggerLevel) {
		ArrayList<WrapErrorLogger> returnValue=new ArrayList<WrapErrorLogger>();
		try{
			StringBuffer query=new StringBuffer();
			
			query.append("select ps.id, shop_list.start_page, (select count(parse_record.id) from parse_record where parse_record.id_session=ps.id), count(*) \n");
			query.append("from current_action \n");
			query.append("inner join parse_session ps on current_action.id_parse_session=ps.id \n");
			query.append("inner join shop_list on shop_list.id=ps.id_shop \n");
			query.append("inner join logger on logger.id_session=ps.id and logger.id_logger_level="+loggerLevel+" \n");
			query.append("where current_action.id_actions="+actionId+"\n");
			query.append("group by ps.id, shop_list.start_page \n");
			ResultSet rs=connector.getConnection().createStatement().executeQuery(query.toString());
			while(rs.next()){
				returnValue.add(new WrapErrorLogger(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
			}
			return returnValue;
		}catch(Exception ex){
			logger.error("#getStatisticsLogger Exception:"+ex.getMessage());
			return returnValue;
		}
	}

	public ArrayList<LoggerInfo> getStatisticsLoggerDetail(ConnectWrap connector, Integer sessionId, int minLoggerLevel) {
		ArrayList<LoggerInfo> returnValue=new ArrayList<LoggerInfo>();
		try{
			StringBuffer query=new StringBuffer();
			query.append("select  logger_level.name, logger.logger_message\n");
			query.append("from logger\n");
			query.append("inner join logger_level on logger_level.id=logger.id_logger_level and logger.id_logger_level>="+minLoggerLevel+"\n");
			query.append("inner join parse_session on parse_session.id=logger.id_session \n");
			query.append("inner join shop_list on shop_list.id=parse_session.id_shop \n");
			query.append("where id_session="+sessionId+"\n");
			// System.out.println(query.toString());
			ResultSet rs=connector.getConnection().createStatement().executeQuery(query.toString());
			while(rs.next()){
				returnValue.add(new LoggerInfo(rs.getString(1), rs.getString(2)));
			}
			return returnValue;
		}catch(Exception ex){
			logger.error("#getStatisticsLogger Exception:"+ex.getMessage());
			return returnValue;
		}
	}

	/** получить на основании номера сессии стартовую страницу магазина */
	public String getShopStartPageBySessionId(ConnectWrap connector, Integer sessionId) {
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select shop_list.start_page from parse_session inner join shop_list on shop_list.id=parse_session.id_shop where parse_session.id="+sessionId);
			rs.next();
			String returnValue=rs.getString(1);
			rs.getStatement().close();
			return returnValue;
		}catch(Exception ex){
			logger.error("#getShopStartPageBySessionId Exception:"+ex.getMessage());
			return null;
		}
	}

	/** получить результаты текущего парсинга на основании номер Action  */
	public ArrayList<WrapParseResult> getStatisticsParseResult(ConnectWrap connector, Integer actionId) {
		ArrayList<WrapParseResult> returnValue=new ArrayList<WrapParseResult>();
		try{
			StringBuffer query=new StringBuffer();
			query.append("select shop_list.start_page, parse_result.name, (select count(*) from parse_record where parse_record.id_session=ps.id) \n");
			query.append("from current_action \n");
			query.append("inner join shop_list on shop_list.id=current_action.id_shop \n");
			query.append("left join parse_session ps on ps.id=current_action.id_parse_session \n");
			query.append("left join parse_result on parse_result.id=ps.id_parse_result \n");
			query.append("where current_action.id_actions="+actionId+"\n");
			query.append("order by current_action.id \n");
			ResultSet rs=connector.getConnection().createStatement().executeQuery(query.toString());
			while(rs.next()){
				returnValue.add(new WrapParseResult(rs.getString(1), rs.getString(2), rs.getInt(3)));
			}
			rs.getStatement().close();
			return returnValue;
		}catch(Exception ex){
			logger.error("#getStatisticsParseResult Exception:"+ex.getMessage());
			return null;
		}
	}

	/** обнулить все данные */
	public boolean resetData(ConnectWrap connector) {
		Connection connection=connector.getConnection();
		try{
			Statement statement=connection.createStatement();
			statement.executeUpdate("delete from actions");
			statement.executeUpdate("delete from current_action");
			statement.executeUpdate("delete from logger");
			statement.executeUpdate("delete from parse_record");
			statement.executeUpdate("delete from parse_session");
			statement.executeUpdate("delete from section");
			connection.commit();
			return true;
		}catch(Exception ex){
			try{
				connection.close();
			}catch(Exception exInner){};
			return false;
		}
	}
}

interface ILogger extends Serializable{
	public void debug(String message);
	public void info(String message);
	public void warn(String message);
	public void error(String message);
}
