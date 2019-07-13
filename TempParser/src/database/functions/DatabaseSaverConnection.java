package database.functions;

import java.sql.Connection;


import java.sql.PreparedStatement;

import org.hibernate.Session;

import database.ConnectWrap;
import database.StaticConnector;
import shop_list.html.parser.engine.ESessionResult;
import shop_list.html.parser.engine.logger.ILogger;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.saver.ISaver;

public class DatabaseSaverConnection implements ISaver{
	/** логгер  */
	private ILogger logger=null;
	/** соединение с базой данных  */
	private ConnectWrap connector=null;
	
	/** класс-обертка для работы с базой данных  */
	private DatabaseProxy proxy=new DatabaseProxy();
	
	
	public DatabaseSaverConnection(ILogger logger){
		connector=StaticConnector.getConnectWrap();
		this.logger=logger;
	}
	
	@Override
	public boolean endSession(Integer sessionId, ESessionResult result) {
		boolean returnValue=false;
		if(proxy.saverEndSession(null, connector.getConnection(),this.logger, sessionId, result)){
			if(saveRecord!=null){
				try{
					saveRecord.close();
				}catch(Exception ex){};
				saveRecord=null;
			}
			returnValue=true;
		}else{
			System.err.println("FirebirdSaverConnection#endSession Exception: ");
			returnValue=false;
		}
		this.connector.close();
		return returnValue;
	}

	@Override
	public boolean saveRecord(Integer sessionId, Integer sectionId, Record record) {
		Connection connection=connector.getConnection();
		return proxy.saverSaveRecord(null, connection, saveRecord, logger, sessionId, sectionId, record);
		
	}

	private PreparedStatement saveRecord=null;
	
	@Override
	public Integer startNewSession(int shopId, String description) {
		if(this.connector==null){
			this.connector=StaticConnector.getConnectWrap();
		}
		Session session=connector.getSession();
		return proxy.saverStartNewSession(null, session, logger,shopId, description);
	}

	@Override
	public Integer getSectionId(String sectionName) {
		if(this.connector==null){
			this.connector=StaticConnector.getConnectWrap();
		}
		Session session=this.connector.getSession();
		return proxy.saverGetSectionId(session, logger, sectionName);
	}

	@Override
	public int getShopId(String url) {
		if(this.connector==null){
			this.connector=StaticConnector.getConnectWrap();
		}
		return this.proxy.saverGetShopId(this.connector.getSession(), logger, url);
		
	}

	@Override
	protected void finalize() throws Throwable {
		if(this.connector!=null){
			try{
				this.connector.close();
			}catch(Exception ex){};
		}
		super.finalize();
	}
}
