package sites.autozvuk_com_ua;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import image_parser.ImagePathAware;

public class AvtozvukImagePathAwareSaver extends ImagePathAware{
	/** последний полученный ID*/
	private Integer currentId=null;
	/** последний полученный URL */
	private String lastUrl=null;
	/** соединение с базой данных  */
	private Connection connection;
	/** для получения следующего набора данных */
	private PreparedStatement getNextResultSet;
	/** для сохранения данных */
	private PreparedStatement saveValues;
	/** каталог для сохранения */
	private String directoryForSave;
	/**  преамбула для Http соединения  */
	private String urlPrefix;
	
	/** 
	 * @param urlPrefix - преамбула для Http 
	 * @param connection - соединение с базой данных 
	 * @param directoryForSave - каталог для сохранения данных 
	 */
	public AvtozvukImagePathAwareSaver(String urlPrefix, Connection connection, String directoryForSave){
		this.urlPrefix=urlPrefix;
		if(this.urlPrefix==null){
			this.urlPrefix="";
		}
		this.connection=connection;
		this.directoryForSave=directoryForSave.trim();
		if(!this.directoryForSave.endsWith("\\")){
			this.directoryForSave=this.directoryForSave+"\\";
		}
	}
	
	@Override
	public String getNextUrl() {
		this.currentId=null;
		this.lastUrl=null;
		try{
			ResultSet rs=this.getNextResultSet.executeQuery();
			if(rs.next()){
				this.currentId=rs.getInt(AvtozvukSaver.fieldId);
				this.lastUrl=rs.getString(AvtozvukSaver.fieldImageUrl);
				if((this.lastUrl!=null)&&(!this.lastUrl.equals(""))){
					this.lastUrl=this.urlPrefix+this.lastUrl;
				}
			}
			rs.close();
		}catch(Exception ex){
			
		}
		return this.lastUrl;
	}

	@Override
	public boolean saveLastGetUrl(InputStream inputStream) {
		boolean returnValue=false;
		try{
			String fileName=this.getFileNameByUrl(this.lastUrl);
			if(fileName==null){
				throw new Exception("full path is not recognized ");
			};
			// сохранить файл на диске 
			if(!this.saveInputStreamToFile(this.directoryForSave+fileName, inputStream)){
				throw new Exception("file is not save ");
			}
			// сохранить 
			this.saveValues.clearParameters();
			this.saveValues.setString(1, fileName);
			this.saveValues.setInt(2, this.currentId);
			this.saveValues.executeUpdate();
			this.connection.commit();
			returnValue=true;
		}catch(Exception ex){
			returnValue=false;
			System.err.println("AvtozvukImagePathAwareSaver#saveLastGetUrl: "+ex.getMessage());
		}
		return returnValue;
	}

	/** получить путь для полного сохранения файла, на основании URL*/
	private String getFileNameByUrl(String url){
		String returnValue=null;
		try{
			int index=url.lastIndexOf("/");
			if(index>=0){
				returnValue=url.substring(index+1);
			};
		}catch(Exception ex){
			System.err.println("AvtozvukImagePathAwareSaver#getFullPathByUrl Exception: "+ex.getMessage());
		}
		return returnValue;
	}
	
	private boolean saveInputStreamToFile(String fullPath, InputStream inputStream) throws Exception{
		FileOutputStream fos=new FileOutputStream(fullPath);
		byte[] buffer=new byte[1024];
		int readedByte=0;
		
		while((readedByte=inputStream.read(buffer))>=0){
			fos.write(buffer,0,readedByte);
		}
		fos.flush();
		fos.close();
		return true;
	}
	
	
	@Override
	public boolean begin() {
		boolean returnValue=false;
		try{
			// создать PreparedStatement для получения очередной записи
			this.getNextResultSet=this.connection.prepareStatement("select first 1 * from "+AvtozvukSaver.tableName+" where "+AvtozvukSaver.tableName+".image_path is null or "+AvtozvukSaver.tableName+"."+AvtozvukSaver.fieldImagePath+"=''");
			
			// создать PreparedStatement для сохранения данных
			this.saveValues=this.connection.prepareStatement("update "+AvtozvukSaver.tableName+" set "+AvtozvukSaver.tableName+"."+AvtozvukSaver.fieldImagePath+"=? where "+AvtozvukSaver.tableName+"."+AvtozvukSaver.fieldId+"=?");
			
			returnValue=true;
		}catch(Exception ex){
			returnValue=false;
			System.err.println("AvtozvukImagePathAwareSaver#begin: "+ex.getMessage());
		}
		return returnValue;
	}

	@Override
	public boolean end() {
		boolean returnValue=false;
		try{
			this.getNextResultSet.close();
			this.saveValues.close();
			returnValue=true;
		}catch(Exception ex){
			returnValue=false;
			System.err.println("AvtozvukImagePathAwareSaver#end Exception: "+ex.getMessage());
		}
		return returnValue;
	}

	@Override
	public boolean saveLastGetUrlAsError() {
		boolean returnValue=false;
		try{
			this.saveValues.clearParameters();
			this.saveValues.setString(1, "[error]");
			this.saveValues.setInt(2, this.currentId);
			this.saveValues.executeUpdate();
			this.connection.commit();
			returnValue=true;
		}catch(Exception ex){
			
		}
		return returnValue;
	}
	
}
