package com.cherkashin.vitaliy.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import database.ConnectWrap;

public class Find {
	
	private void error(String message){
		System.err.println("com.cherkashin.vitaliy.dao.Find"+message);
	}
	
	/**
	 * получить результат поиска
	 * @param connector - соединение с базой данных   
	 * @param findString - строка поиска
	 * @param begin - номер первой строки в выходном запросе
	 * @param count - кол-во элементов в выходном запросе
	 * @param usdCourse -  курс доллара ( сколько гривен за один доллар )
	 * @return 
	 * <ul>
	 * 	<li><b>empty list</b> -  нет данных для отображения </li>
	 * 	<li><b>list</b> -  данные для отображения </li>
	 * </ul>
	 */
	public ArrayList<SearchingResult> findCommodity(ConnectWrap connector, 
														   String findString, 
														   int begin, 
														   int count, 
														   float usdCourse){
		ArrayList<SearchingResult> resultList=new ArrayList<SearchingResult>();
		try{
			StringBuffer query=new StringBuffer();
			query.append("	select  parse_record.id,	\n");
			query.append("	        shop_list.start_page,	\n");
			query.append("	        parse_record.name,	\n");
			query.append("	        parse_record.amount,	\n");
			query.append("	        ifnull(parse_record.amount, parse_record.amount_usd*"+usdCourse+") counted, \n");
			query.append("	        parse_record.amount_usd,	\n");
			query.append("	        ifnull(parse_record.amount_usd, parse_record.amount/"+usdCourse+") counted_usd \n");
			query.append("	from parse_record	\n");
			query.append("	inner join parse_session on parse_session.id=parse_record.id_session	\n");
			query.append("	inner join shop_list on shop_list.id=parse_session.id_shop	\n");
			query.append("	where 1=1 \n");
			StringTokenizer elements=new StringTokenizer(findString);
			
			while(elements.hasMoreTokens()){
				query.append(" and (parse_record.name like ('%"+elements.nextToken().replace("'", "''")+"%')) \n");
			}
			query.append("  order by counted \n");
			query.append("  limit "+begin+","+count+" \n");
			// System.out.println(">>> "+query.toString());
			ResultSet rs=connector.getConnection().createStatement().executeQuery(query.toString());
			fillSearchingResult(rs, resultList);
			rs.getStatement().close();
		}catch(Exception ex){
			error("#findCommodity Exception:"+ex.getMessage());
		}
		return resultList;
	}

	/** наполнить переданный список данными из базы данных  */
	private void fillSearchingResult(ResultSet rs, ArrayList<SearchingResult> list){
		list.clear();
		try{
			while(rs.next()){
				try{
					list.add(new SearchingResult(rs.getInt(1),
												 rs.getString(2),
												 rs.getString(3),
												 rs.getFloat(4), 
												 rs.getFloat(5), 
												 rs.getFloat(6),
												 rs.getFloat(7)
												 )
							);
				}catch(Exception ex){
					error("fillSearchingResult getData Exception: "+ex.getMessage());
				}
			}
		}catch(Exception ex){
			error("fillSearchingResult get Exception: "+ex.getMessage());
		}
	}

	/**
	 * получить результат поиска
	 * @param connector - соединение с базой данных   
	 * @param findString - строка поиска
	 * @return 
	 * <ul>
	 * 	<li><b>empty list</b> -  нет данных для отображения </li>
	 * 	<li><b>list</b> -  данные для отображения </li>
	 * </ul>
	 */
	public int findCommodityGetCount(ConnectWrap connector, String findString){
		int returnValue=0;
		try{
			StringBuffer query=new StringBuffer();
			query.append("	select  count(parse_record.id)	\n");
			query.append("	from parse_record	\n");
			query.append("	inner join parse_session on parse_session.id=parse_record.id_session	\n");
			query.append("	inner join shop_list on shop_list.id=parse_session.id_shop	\n");
			query.append("	where 1=1 \n");
			StringTokenizer elements=new StringTokenizer(findString);
			while(elements.hasMoreTokens()){
				query.append(" and (parse_record.name like ('%"+elements.nextToken().replace("'", "''")+"%')) \n");
			}
			// System.out.println(">>> "+query.toString());
			ResultSet rs=connector.getConnection().createStatement().executeQuery(query.toString());
			rs.next();
			returnValue=rs.getInt(1);
			rs.getStatement().close();
		}catch(Exception ex){
			error("#findCommodityGetCount Exception:"+ex.getMessage());
		}
		return returnValue;
	}


	/** получить текущий курс валют */
	public float getCurrentCourse(ConnectWrap connector, Date date) {
		float returnValue=0;
		try{
			PreparedStatement ps=connector.getConnection().prepareStatement("SELECT usd_course FROM currency_value c where time_set<=? order by id desc limit 0,1");
			ps.setTimestamp(1, new Timestamp(date.getTime()));
			ResultSet rs=ps.executeQuery();
			rs.next();
			returnValue=rs.getFloat(1);
			ps.close();
		}catch(Exception ex){
			error("#getCurrentCourse Exception: "+ex.getMessage());
			returnValue=0;
		}
		return returnValue;
	}

	/**
	 * получить полную ссылку на магазин 
	 * @param connector - соединение с базой данных
	 * @param value - значение 
	 * @return
	 */
	public String getShopUrl(ConnectWrap connector, Integer value) {
		String returnValue=null;
		try{
			StringBuffer query=new StringBuffer();
			query.append("select  parse_record.id, shop_list.start_page, parse_record.url from parse_record \n");
			query.append("inner join parse_session on parse_session.id=parse_record.id_session \n");
			query.append("inner join shop_list on shop_list.id=parse_session.id_shop \n");
			query.append("where parse_record.id="+value);
			ResultSet rs=connector.getConnection().createStatement().executeQuery(query.toString());
			if(rs.next()){
				return getDomainFromUrl(rs.getString("start_page"));
			}else{
				return null;
			}
		}catch(Exception ex){
			error("#getShopUrl Exception: "+ex.getMessage());
		}
		return returnValue;
	}

	/**
	 * получить полную ссылку на позицию в магазине 
	 * @param connector - соединение с базой данных
	 * @param value - значение 
	 * @return
	 */
	public String getPositionUrl(ConnectWrap connector, Integer value) {
		String returnValue=null;
		try{
			StringBuffer query=new StringBuffer();
			query.append("select  parse_record.id, shop_list.start_page, parse_record.url from parse_record \n");
			query.append("inner join parse_session on parse_session.id=parse_record.id_session \n");
			query.append("inner join shop_list on shop_list.id=parse_session.id_shop \n");
			query.append("where parse_record.id="+value);
			ResultSet rs=connector.getConnection().createStatement().executeQuery(query.toString());
			if(rs.next()){
				String positionUrl=rs.getString("url");
				if(positionUrl==null)return rs.getString("start_page");
				return getDomainFromUrl(rs.getString("start_page"))+ (positionUrl.startsWith("/")? positionUrl : "/"+positionUrl);
			}else{
				return null;
			}
		}catch(Exception ex){
			error("#getShopUrl Exception: "+ex.getMessage());
		}
		return returnValue;
	}
	
	/**
	 * преобразовать URL 
	 * @param fullUrl http://www.elsys.kiev.ua/index.html
	 * @return http://www.elsys.kiev.ua
	 */
	private String getDomainFromUrl(String fullUrl){
		try{
			fullUrl=fullUrl.trim();
			if(fullUrl.matches(".*[a-zA-Z0-9]\\/[a-zA-Z0-9].*")){
				int indexDouble=fullUrl.indexOf("//");
				int index=0;
				if(indexDouble>0){
					index=fullUrl.indexOf('/', indexDouble+2);
				}else{
					index=fullUrl.indexOf('/');
				}
				return fullUrl.substring(0,index);
			}else{
				return fullUrl;
			}
		}catch(Exception ex){
			return null;
		}
	}
	
	public static void main(String[] args){
		String url="http://www.elsys.kiev.ua/index.html";
		Find find=new Find();
		System.out.println(find.getDomainFromUrl(url));
	}
}
