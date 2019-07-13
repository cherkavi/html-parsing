package parser_implementation;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import database.ConnectWrap;
import database.StaticConnector;
import database.wrap_mysql.Actions;
import database.wrap_mysql.EActionState;
import database.wrap_mysql.Parse_record;
import database.wrap_mysql.Parse_session;
import database.wrap_mysql.Section;
import database.wrap_mysql.Shop_list;

import shop_list.html.parser.engine.EParseState;
import shop_list.html.parser.engine.ESessionResult;
import shop_list.html.parser.engine.IManager;
import shop_list.html.parser.engine.logger.ELoggerLevel;
import shop_list.html.parser.engine.logger.ILogger;
import shop_list.html.parser.engine.record.Record;

/** класс, который служит прослойкой для работы с базой данных */
public class DatabaseProxy {
	/**
	 * @param connection - соединение с базой данных 
	 * @param logger - логгер 
	 * @param sessionId - уникальный номер сессии 
	 * @param result - результат окончания парсинга
	 * @return
	 * <ul>
	 * 	<li>true - успешно сохранен</li>
	 * 	<li>false - ошибка сохранения</li>
	 * </ul>
	 */
	public boolean saverEndSession(IManager manager, Connection connection,ILogger logger, Integer sessionId, ESessionResult result){
		try{
			
			connection.createStatement().executeUpdate("update parse_session set id_parse_result="+result.getDatabaseKod()+" where id="+sessionId);
			connection.commit();
/*			Session session=connector.getSession();
			Parse_session record=(Parse_session)session.get(Parse_session.class, sessionId);
			record.setId_parse_result(result.getDatabaseKod());
			session.beginTransaction();
			session.update(record);
			session.getTransaction().commit();
*/			
			
			return true;
		}catch(Exception ex){
			logger.error(manager, "DatabaseProxy#saverEndSession Exception:"+ex.getMessage());
			return false;
		}
	}
	
	
	
	/** 
	 * Сделать запись в базу данных сообщения логгера 
	 * @param statement - полученный вызовом метода {@link DatabaseProxy#loggerGetPreparedStatement()}
	 * @param sessionId
	 * @param loggerLevel
	 * @param message ( полное, не обрезанное; в этом методе может произойти "укорачивание" данного сообщения из-за ограничений базы данных )
	 * @return
	 */
	public boolean loggerSaveValue(PreparedStatement statement, Integer sessionId, ELoggerLevel loggerLevel, String message) {
		try{
			// System.out.println("Statement for save: "+statement);
			statement.clearParameters();
			statement.setInt(1, sessionId);
			statement.setInt(2, loggerLevel.getKod());
			statement.setString(3, prepareStringValue(message,255));
			statement.executeUpdate();
			statement.getConnection().commit();
			return true;
		}catch(Exception ex){
			System.out.println("DatabaseProxy#loggerSaveValue Exception: "+ex.getMessage()+"\n"+message);
			return false;
		}
	}
	
	/** проверить строку на валидность сохранения в базе данных 
	 * @param value - значение для сохранения
	 * @param limit - ограничение кол-ва символов 
	 * @return подготовленное для сохранения значения 
	 * */
	private String prepareStringValue(String value, int limit){
		if(value==null){
			return "";
		}else if(value.length()>limit){
			return value.substring(0,limit);
		}else{
			return value;
		}
	}

	/**
	 * получить PreparedStatement для сохранения в базе данных 
	 * <br>
	 * парный метод с {@link DatabaseProxy#loggerSaveValue}
	 * @return
	 */
	public PreparedStatement loggerGetPreparedStatement() {
		try{
			return StaticConnector.getConnectWrap().getConnection().prepareStatement("insert into logger (id_session, id_logger_level, logger_message ) values (?,?,?)");
		}catch(Exception ex){
			System.err.println("DatabaseProxy#initDatabaseTools Exception:"+ex.getMessage());
			return null;
		}
	}

	/**  получить объект для записи в базу данных 
	 * @param idSession - уникальный номер сессии, по которой происходит запись
	 * @param idSection - уникальный номер секции ( раздела сайта ), по которой происходит запись
	 * @return
	 */
	public Parse_record getDatabaseRecord(Record parseRecord, Integer idSession, Integer idSection){
		Parse_record record=new Parse_record();
		record.setId_session(idSession);
		record.setId_section(idSection);
		record.setName(parseRecord.getName());
		record.setUrl(parseRecord.getUrl());
		record.setDescription(parseRecord.getDescription());
		record.setAmount(parseRecord.getPrice());
		record.setAmount_usd(parseRecord.getPriceUsd());
		record.setAmount_euro(parseRecord.getPriceEuro());
		return record;
	}
	

	/** сохранить одну запись в базе данных 
	 * @param connection - соединение с базой данных 
	 * @param saveRecord - PreparedStatement, который используется для сохранения 
	 * @param logger - логгер 
	 * @param sessionId - номер сессии
	 * @param sectionId - номер секции 
	 * @param record - запись, которая должна быть сохранена 
	 * @return
	 */
	public boolean saverSaveRecord(IManager manager, Connection connection, PreparedStatement saveRecord, ILogger logger, Integer sessionId, Integer sectionId, Record record) {
		try{
			if(saveRecord==null){
				saveRecord=connection.prepareStatement("insert into parse_record(id_session, id_section, url, name, description, amount, amount_usd, amount_euro) values(?,?,?,?,?,?,?,?)");
			}
			saveRecord.clearParameters();
			saveRecord.setInt(1, sessionId);
			saveRecord.setInt(2, sectionId);
			Parse_record databaseRecord=getDatabaseRecord(record, sessionId, sectionId);
			saveRecord.setString(3, databaseRecord.getUrl());
			saveRecord.setString(4, databaseRecord.getName());
			saveRecord.setString(5, databaseRecord.getDescription());
			if(databaseRecord.getAmount()!=null){
				saveRecord.setFloat(6, databaseRecord.getAmount());
			}else{
				saveRecord.setNull(6, Types.FLOAT);
			}
			if(databaseRecord.getAmount_usd()!=null){
				saveRecord.setFloat(7, databaseRecord.getAmount_usd());
			}else{
				saveRecord.setNull(7, Types.FLOAT);
			}
			if(databaseRecord.getAmount_euro()!=null){
				saveRecord.setFloat(8, databaseRecord.getAmount_euro());
			}else{
				saveRecord.setNull(8, Types.FLOAT);
			}
			saveRecord.executeUpdate();
			saveRecord.getConnection().commit();
			/*session.beginTransaction();
			session.save(record.getDatabaseRecord(sessionId, sectionId));
			session.getTransaction().commit();*/
			return true;
		}catch(Exception ex){
			logger.error(manager, "DatabaseProxy#saverSaveRecord Exception: "+ex.getMessage());
			try{
				connection.rollback();
			}catch(Exception exInner){};
			return false;
		}
	}


	/** 
	 * создать новую сессию по указанному парсеру
	 * @param manager - парсер 
	 * @param session - сессия связи с базой данных 
	 * @param logger - логгер для вывода сообщений 
	 * @param shopId - уникальный id магазина
	 * @param description - описание 
	 * @return новый код сессии 
	 */
	public Integer saverStartNewSession(IManager manager, Session session, ILogger logger,
			int shopId, String description) {
		try{
			Parse_session record=new Parse_session();
			record.setDescription(description);
			record.setId_parse_result(null);
			record.setId_shop(shopId);
			record.setParse_begin(new Date());
			session.beginTransaction();
			session.save(record);
			session.getTransaction().commit();
			return record.getId();
		}catch(Exception ex){
			logger.error(manager, ex.getMessage());
			return null;
		}
	}


	/**
	 * Получить код раздела (секции) по которой происходит парсинг 
	 * @param session - сессия соединения с базой данных 
	 * @param logger - логгер 
	 * @param sectionName - имя секции 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer saverGetSectionId(Session session, ILogger logger, String sectionName) {
		try{
			List<Section> list=(List<Section>)session.createCriteria(Section.class).add(Restrictions.eq("name", sectionName)).list();
			if(list.size()==0){
				Section section=new Section();
				section.setName(sectionName);
				session.beginTransaction();
				session.save(section);
				session.getTransaction().commit();
				return section.getId();
			}else{
				return list.get(0).getId();
			}
		}catch(Exception ex){
			logger.error(null, "DatabaseProxy#saverGetSectionId Exception:"+ex.getMessage());
			return 0;
		}
	}

	/**
	 * Получить уникальный номер магазина
	 * @param session - сессия с базой данных 
	 * @param logger - логгер 
	 * @param url - полный путь к стартовой странице магазина 
	 * @return - уникальный код из базы для данного магазина 
	 */
	@SuppressWarnings("unchecked")
	public int saverGetShopId(Session session, ILogger logger, String url) {
		try{
			try{
				List<Shop_list> list=(List<Shop_list>)session.createCriteria(Shop_list.class).add(Restrictions.eq("start_page", url)).list();
				if(list.size()==0){
					Shop_list shop=new Shop_list();
					shop.setName(null);
					shop.setStart_page(url);
					shop.setDescription(null);
					session.beginTransaction();
					session.save(shop);
					session.getTransaction().commit();
					return shop.getId();
				}else{
					return list.get(0).getId();
				}
			}catch(Exception ex){
				logger.error(null, "getShopId Exception: "+ex.getMessage());
				return 0;
			}
		}catch(Exception ex){
			logger.error(null, "DatabaseProxy#saverGetSectionId Exception:"+ex.getMessage());
			return 0;
		}
	}


	/** создать новую запись в таблице Actions и вернуть код текущего Actions  */
	public int getNewActionId() {
		int returnValue=0;
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			Actions actions=new Actions();
			Session session=connector.getSession();
			session.beginTransaction();
			session.save(actions);
			session.getTransaction().commit();
			returnValue=actions.getId();
		}catch(Exception ex){
			System.err.println("DatabaseProxy#getNewActionId Exception:"+ex.getMessage());
			returnValue=0;
		}finally{
			connector.close();
		}
		return returnValue;
	}


	/** проверить таблицу Actions на наличие в ней указанного ID */
	public boolean isActionsIdExists(int actionsId) {
		boolean returnValue=false;
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select * from actions where id="+actionsId);
			if(rs.next()){
				returnValue=true;
			}else{
				returnValue=false;
			}
		}catch(Exception ex){
			System.err.println("DatabaseProxy#isActionsIdExists Exception:"+ex.getMessage());
			returnValue=false;
		}finally{
			connector.close();
		}
		return returnValue;
	}



	/** проставить для парсеров, которые есть в базе данных и имеют состояния парсинга - значения этого парсинга
	 * @param actionsId - уникальный номер Action.ID - по которому происходит обновление данных
	 * @param listOfParser - список всех парсеров, которые нужно обновить 
	 * */
	public void setParserState(int actionsId, ArrayList<IManager> listOfParser) {
		if(listOfParser!=null){
			ConnectWrap connector=StaticConnector.getConnectWrap();
			try{
				String query="select parse_session.id_parse_result from current_action inner join parse_session on parse_session.id=current_action.id_session where current_action.id_actions="+actionsId+" and parse_session.id_shop=?";			
				PreparedStatement statement=connector.getConnection().prepareStatement(query);
				for(int counter=0;counter<listOfParser.size();counter++){
					IManager parser=listOfParser.get(counter);
					if(parser!=null){
						try{
							statement.clearParameters();
							statement.setInt(1, parser.getShopId());
							ResultSet rs=statement.executeQuery();
							if(rs.next()){
								/** PARSE_RESULT
								 * 0 - unknown 
								 * 1 - error
								 * 2 - ok
								 * 3 - stopped
								 */
								int parseResult=rs.getInt(1);
								switch(parseResult){
									case 0: {parser.setParseState(EParseState.READY);};break;
									case 1: {parser.setParseState(EParseState.DONE_ERROR);};break;
									case 2: {parser.setParseState(EParseState.DONE_OK);};break;
									case 3: {parser.setParseState(EParseState.READY);};break;
								}
							}
							rs.close();
						}catch(Exception ex){
							System.err.println("DatabaseProxy#setParserState Exception: "+ex.getMessage());
						}
					}
				}
			}catch(Exception ex){
				System.err.println("DatabaseProxy#setParserState Exception:"+ex.getMessage());
			}finally{
				connector.close();
			}
		}else{
			System.err.println("DatabaseProxy#setParserState listOfParser is Empty ");
		}
	}


	/* проверить CURRENT_ACTION.ID_ACTION на появление новых парсеров в каталоге, которых нет в базе - дописать в базу
	 * @param  actionsId - уникальный номер Action
	 * @param  listOfParser - список всех доступных парсеров 
	 * 
	public void checkForNewParsers(int actionsId, ArrayList<IManager> listOfParser) {
		if(listOfParser!=null){
			ConnectWrap connector=ConnectorSingleton.getConnectWrap();
			try{
				String query="select parse_session.id_parse_result from current_action inner join parse_session on parse_session.id=current_action.id_session where current_action.id_actions="+actionsId+" and parse_session.id_shop=?";
				
				PreparedStatement statement=connector.getConnection().prepareStatement(query);
				for(int counter=0;counter<listOfParser.size();counter++){
					IManager parser=listOfParser.get(counter);
					if(parser!=null){
						try{
							statement.clearParameters();
							statement.setInt(1, parser.getShopId());
							ResultSet rs=statement.executeQuery();
							if(rs.next()==false){
								System.out.println("Появился новый парсер, которого не было до этого в сессии");
								
							}
							rs.close();
						}catch(Exception ex){
							System.err.println("DatabaseProxy#setParserState Exception: "+ex.getMessage());
						}
					}
				}
			}catch(Exception ex){
				System.err.println("DatabaseProxy#setParserState Exception:"+ex.getMessage());
			}finally{
				connector.close();
			}
		}else{
			System.err.println("DatabaseProxy#setParserState listOfParser is Empty ");
		}
	}*/


	/** записать данное действие как остановленное  
	 * @param actionsId - код текущего Action
	 * @param state - назначаемое состояние данного Action  
	 * */
	public void writeActionAs(int actionsId, EActionState state) {
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			Connection connection=connector.getConnection();
			connection.createStatement().executeUpdate("update actions set id_action_state="+state.getKod()+" where id="+actionsId);
			connection.commit();
		}catch(Exception ex){
			System.err.println("DatabaseProxy#writeActionAs Exception:"+ex.getMessage());
		}finally{
			connector.close();
		}
			
	}


	/** вставить по данному ACTIONS.ID  еще один сессионный номер, если он отсутствует в списке */
	public void writeToCurrentAction(int actionsId, Integer sessionId) {
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			Connection connection=connector.getConnection();
			ResultSet rs=connection.createStatement().executeQuery("select * from current_action where current_action.id_actions="+actionsId+" and current_action.id_session="+sessionId);
			if(rs.next()==false){
				rs.close();
				connection.createStatement().executeUpdate("insert into current_action(id_actions, id_session) values("+actionsId+","+sessionId+")");
				connection.commit();
			}else{
				// запись уже есть 
				rs.close();
			}
		}catch(Exception ex){
			System.err.println("DatabaseProxy#writeToCurrentAction Exception:"+ex.getMessage());
		}finally{
			connector.close();
		}
	}

}
