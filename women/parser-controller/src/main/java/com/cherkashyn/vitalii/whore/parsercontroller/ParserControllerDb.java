package com.cherkashyn.vitalii.whore.parsercontroller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cherkashyn.vitalii.whore.exceptions.StorageException;
import com.cherkashyn.vitalii.whore.interfaces.parsercontroller.ParserController;

public class ParserControllerDb implements ParserController{
	private String url; 
	private String login; 
	private String password; 
	private String className;
	private Connection connection;
	
	private static enum Status{
		PREPARED,
		START,
		END,
		TERMINATE
	}
	
	public ParserControllerDb(String url, String login, String password,String className) {
		super();
		this.url = url;
		this.login = login;
		this.password = password;
		this.className = className;
	}

	private static String RECORD="insert into parser_session (session_id, status, description) values (?,?,?)";
	private PreparedStatement record=null;

	
	private int createRecord(Integer sessionId, Status status, String description) throws StorageException {
		// init 
		if(!isConnected(connection) ){
			this.connection=createConnection();
			this.record=null;
		}
		if(this.record==null){
			try {
				this.record=this.connection.prepareStatement(RECORD, Statement.RETURN_GENERATED_KEYS);
			} catch (SQLException e) {
				throw new StorageException("can't create prepared statement for query: "+RECORD);
			}
		}
		
		try {
			this.record.clearParameters();
			if(sessionId==null){
				this.record.setNull(1, java.sql.Types.INTEGER);
			}else{
				this.record.setInt(1, sessionId);
			}
			this.record.setString(2, status.toString());
			if(description==null){
				this.record.setNull(3, java.sql.Types.VARCHAR);
			}else{
				this.record.setString(3, description);
			}
			this.record.executeUpdate();
			this.connection.commit();
		} catch (SQLException e) {
			throw new StorageException("can't insert record into : "+RECORD, e );
		}

		int returnValue=0;
		ResultSet rs;
		try {
			rs = record.getGeneratedKeys();
			rs.next();
			returnValue=rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			throw new StorageException("can't retrieve generated keys "+RECORD, e);
		}
		return returnValue;
	}

	
	@Override
	public int createSessionNumber(String startPage) throws StorageException {
		return createRecord(null, Status.PREPARED, startPage);
	}

	@Override
	public void notifyBegin(int sessionId) throws StorageException {
		createRecord(sessionId, Status.START, null);
	}

	@Override
	public void notifyEnd(int sessionId) throws StorageException {
		createRecord(sessionId, Status.END, null);
	}

	@Override
	public void notifyTerminate(int sessionId, String errorMessage) throws StorageException {
		createRecord(sessionId, Status.TERMINATE, errorMessage);
	}
	
	
	private boolean isConnected(Connection jdbcConnection) {
		try {
			if(jdbcConnection==null || jdbcConnection.isClosed()){
				return false;
			}
		} catch (SQLException e) {
			// try to close quietly
			try {
				jdbcConnection.close();
			} catch (SQLException e1) {
			}
			return false;
		}
		return true;
	}
	
	
	private Connection createConnection() throws StorageException{
		try {
			Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new StorageException("can't load class by name: "+className);
		}
		try{
			Connection connection=DriverManager.getConnection(url, login, password);
			connection.setAutoCommit(false);
			return connection;
		}catch(SQLException ex){
			throw new StorageException("can't connect to DB: "+url);
		}
	}
	

}
