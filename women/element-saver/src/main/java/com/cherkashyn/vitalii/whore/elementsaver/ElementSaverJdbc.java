package com.cherkashyn.vitalii.whore.elementsaver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherkashyn.vitalii.whore.domain.WhoreElement;
import com.cherkashyn.vitalii.whore.exceptions.StorageException;
import com.cherkashyn.vitalii.whore.exceptions.ValidationException;
import com.cherkashyn.vitalii.whore.interfaces.elementsaver.ElementSaver;

public class ElementSaverJdbc implements ElementSaver{
	private static Logger LOGGER=LoggerFactory.getLogger(ElementSaverJdbc.class); 
	private String url; 
	private String login; 
	private String password; 
	private String className;
	private Connection connection;
	
	
	
	public ElementSaverJdbc(String url, String login, String password,
			String className) {
		super();
		this.url = url;
		this.login = login;
		this.password = password;
		this.className = className;
	}


	@Override
	public void saveElement(WhoreElement elementForSave) throws ValidationException, StorageException {
		if(!isConnected(connection)){
			connection=createConnection();
			psDescriptions=null;
			psMainData=null;
			psPhones=null;
			psPrices=null;
			psServices=null;
			psImages=null;
		}
		
		if(elementForSave.getSessionId()==0){
			throw new ValidationException("can't save element without session id: "+elementForSave);
		}
		Integer id=saveMainData(elementForSave);
		savePhones(id, elementForSave);
		savePrices(id, elementForSave);
		saveDescriptions(id, elementForSave);
		saveServices(id, elementForSave);
		saveImages(id, elementForSave);
		try {
			connection.commit();
		} catch (SQLException e) {
			throw new StorageException("can't commit transation for save element: "+e.getMessage(), e);
		}
		LOGGER.debug("element was saved:"+elementForSave.getUrl());
	}
	
	private PreparedStatement psImages=null;
	private final static String IMAGES="insert into human_images ( human_id, image_id ) values (?,?)";
	private void saveImages(Integer id, WhoreElement elementForSave) throws StorageException {
		if(psImages==null){
			try {
				psImages=this.connection.prepareStatement(IMAGES);
			} catch (SQLException e) {
				throw new StorageException("can't create prepared statement "+IMAGES, e);
			}
		}
		try{
			if(elementForSave.getImages()!=null){
				for(String eachImage:elementForSave.getImages()){
					String trimImageId=StringUtils.trimToNull(eachImage);
					if(trimImageId==null){
						continue;
					}
					psImages.clearParameters();
					psImages.setInt(1, id);
					psImages.setString(2, trimImageId);
					psImages.executeUpdate();
				}
			}
		}catch(SQLException ex){
			throw new StorageException("can't insert data into "+IMAGES, ex);
		}
	}

	private PreparedStatement psServices=null;
	private final static String SERVICES="insert into human_services ( human_id, service ) values (?,?)";
	private void saveServices(Integer id, WhoreElement elementForSave) throws StorageException {
		if(psServices==null){
			try {
				psServices=this.connection.prepareStatement(SERVICES);
			} catch (SQLException e) {
				throw new StorageException("can't create prepared statement "+SERVICES, e);
			}
		}
		try{
			if(elementForSave.getDescriptions()!=null){
				for(String eachService:elementForSave.getServices()){
					String clearService=StringUtils.trimToNull(eachService);
					if(clearService==null){
						continue;
					}
					psServices.clearParameters();
					psServices.setInt(1, id);
					psServices.setString(2, clearService);
					psServices.executeUpdate();
				}
			}
		}catch(SQLException ex){
			throw new StorageException("can't insert data into "+SERVICES, ex);
		}
	}


	private PreparedStatement psDescriptions=null;
	private final static String DESCRIPTIONS="insert into human_description( human_id, parameter, description ) values (?,?,?)";
	private void saveDescriptions(Integer id, WhoreElement elementForSave) throws StorageException {
		if(psDescriptions==null){
			try {
				psDescriptions=this.connection.prepareStatement(DESCRIPTIONS);
			} catch (SQLException e) {
				throw new StorageException("can't create prepared statement "+DESCRIPTIONS, e);
			}
		}
		try{
			if(elementForSave.getDescriptions()!=null){
				for(Map.Entry<String, String> eachDescription:elementForSave.getDescriptions().entrySet()){
					if(eachDescription.getKey()==null || eachDescription.getValue()==null){
						continue;
					}
					psDescriptions.clearParameters();
					psDescriptions.setInt(1, id);
					psDescriptions.setString(2, eachDescription.getKey());
					psDescriptions.setString(3, eachDescription.getValue());
					psDescriptions.executeUpdate();
				}
			}
		}catch(SQLException ex){
			throw new StorageException("can't insert data into "+DESCRIPTIONS, ex);
		}
	}


	private PreparedStatement psPrices=null;
	private final static String PRICES="insert into human_prices( human_id, service_time, service_price ) values (?,?,?)";
	private void savePrices(Integer id, WhoreElement elementForSave) throws StorageException {
		if(psPrices==null){
			try {
				psPrices=this.connection.prepareStatement(PRICES);
			} catch (SQLException e) {
				throw new StorageException("can't create prepared statement "+PRICES, e);
			}
		}
		try{
			if(elementForSave.getPrices()!=null){
				for(Map.Entry<Integer, String> eachPrice:elementForSave.getPrices().entrySet()){
					if(eachPrice.getKey()==null || eachPrice.getValue()==null){
						continue;
					}
					psPrices.clearParameters();
					psPrices.setInt(1, id);
					if(eachPrice.getKey()!=null){
						psPrices.setString(2, eachPrice.getKey().toString());
					}else{
						psPrices.setNull(2, java.sql.Types.VARCHAR);
					}
					psPrices.setString(3, eachPrice.getValue());
					psPrices.executeUpdate();
				}
			}
		}catch(SQLException ex){
			throw new StorageException("can't insert data into "+PRICES, ex);
		}
	}

	private PreparedStatement psPhones=null;
	private final static String PHONES="insert into human_phones( human_id, phone ) values (?,?)";
	private void savePhones(Integer id, WhoreElement elementForSave) throws StorageException {
		if(psPhones==null){
			try {
				psPhones=this.connection.prepareStatement(PHONES);
			} catch (SQLException e) {
				throw new StorageException("can't create prepared statement "+PHONES, e);
			}
		}
		try{
			if(elementForSave.getPhones()!=null){
				for(String eachPhone:elementForSave.getPhones()){
					String realPhone=StringUtils.trimToNull(eachPhone);
					if(realPhone==null){
						continue;
					}
					psPhones.clearParameters();
					psPhones.setInt(1, id);
					psPhones.setString(2, realPhone);
					psPhones.executeUpdate();
				}
			}
		}catch(SQLException ex){
			throw new StorageException("can't insert data into "+PHONES, ex);
		}
	}

	private PreparedStatement psMainData=null;
	private final static String MAIN_DATA="insert into human( session_id, url, name ) values (?, ?, ?)";
	
	private Integer saveMainData(WhoreElement elementForSave) throws StorageException{
		if(psMainData==null){
			try {
				psMainData=this.connection.prepareStatement(MAIN_DATA, Statement.RETURN_GENERATED_KEYS);
			} catch (SQLException e) {
				throw new StorageException("can't create prepared statement "+MAIN_DATA, e);
			}
		}
		try {
			psMainData.clearParameters();
			psMainData.setInt(1, elementForSave.getSessionId());
			psMainData.setString(2, elementForSave.getUrl());
			psMainData.setString(3, elementForSave.getName());
			psMainData.executeUpdate();
		} catch (SQLException e) {
			throw new StorageException("can't insert data into "+MAIN_DATA, e);
		}
		int returnValue=0;
		ResultSet rs;
		try {
			rs = psMainData.getGeneratedKeys();
			rs.next();
			returnValue=rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			throw new StorageException("can't retrieve generated keys "+MAIN_DATA, e);
		}
		return returnValue;
	}


	// --------- connection utils block:begin --------------------
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
	// --------- connection utils block:end --------------------



}
