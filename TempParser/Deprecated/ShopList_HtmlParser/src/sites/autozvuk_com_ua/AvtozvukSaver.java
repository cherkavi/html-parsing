package sites.autozvuk_com_ua;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import html_parser.Saver;
import html_parser.record.Record;

/** объект, который служит для сохранения записей */
public class AvtozvukSaver extends Saver {
	public static final String tableName="AVTOZVUK";
	public static final String fieldImageUrl="IMAGE_URL";
	public static final String fieldId="ID";
	public static final String fieldImagePath="IMAGE_PATH";
	protected String[] columnNames=new String[]{ "DESCRIPTION",fieldImageUrl,       "NAME","PRICE",  "PRICE_USD",         "URL",fieldImagePath};
	protected String[] columnTypes=new String[]{"VARCHAR(200)","VARCHAR(70)","VARCHAR(70)","FLOAT",      "FLOAT","VARCHAR(100)","VARCHAR(255)"};
	private Connection connection;
	/** вставляет данные в базу данных */
	private PreparedStatement insertStatement;
	/** возвращает очередной ID*/	
	private PreparedStatement idGenerator;
	
	public AvtozvukSaver(Connection connection){
		this.connection=connection;
	}
	
	@Override
	public boolean finish() {
		try{
			this.connection.commit();
		}catch(Exception ex){};
		try{
			this.insertStatement.close();
		}catch(Exception ex){};
		try{
			this.idGenerator.close();
		}catch(Exception ex){};
		return true;
	}

	@Override
	public boolean resetAllRecord() {
		boolean returnValue=false;
		try{
			// удалить таблицу
			try{
				connection.createStatement().executeUpdate("drop table "+tableName);
			}catch(Exception ex){};
			// удалить генератор
			try{
				connection.createStatement().executeUpdate("drop generator gen_"+tableName+"_id");
			}catch(Exception ex){};
			connection.commit();
			
			// создать таблицу
			StringBuffer query=new StringBuffer();
			query.append("CREATE TABLE "+tableName+"( ");
			query.append("ID INTEGER NOT NULL, DATE_WRITE TIMESTAMP, SECTION_NAME VARCHAR(70), ");
			for(int counter=0;counter<this.columnNames.length;counter++){
				query.append(" "+this.columnNames[counter]+" "+this.columnTypes[counter]);
				if(counter!=(this.columnNames.length-1)){
					query.append(", \n");
				}
			}
			query.append(" ) ");
			connection.createStatement().executeUpdate(query.toString());
			// создать генератор 
			connection.createStatement().executeUpdate("create generator gen_"+tableName+"_id");
			connection.commit();
			 
			returnValue=true;
		}catch(Exception ex){
			System.err.println("AvtozvukSaver#resetAllRecord Exception: "+ex.getMessage());
		}
		return returnValue;
	}

	@Override
	public boolean save(String sectionName, Record record) {
		boolean returnValue=false;
		if(record instanceof AvtozvukRecord){
			try{
				AvtozvukRecord newRecord=(AvtozvukRecord)record;
				// создать запрос и наполнить его данными
				ResultSet rsGenerator=this.idGenerator.executeQuery();
				rsGenerator.next();
				this.insertStatement.clearParameters();
				this.insertStatement.setInt(1, rsGenerator.getInt(1));
				this.insertStatement.setTimestamp(2, new Timestamp((new Date()).getTime()));
				this.insertStatement.setString(3,sectionName);
				this.insertStatement.setString(4, newRecord.getDescription());
				this.insertStatement.setString(5, newRecord.getImageUrl());
				this.insertStatement.setString(6, newRecord.getName());
				this.insertStatement.setFloat(7, newRecord.getPrice());
				this.insertStatement.setFloat(8, newRecord.getPriceUSD());
				this.insertStatement.setString(9, newRecord.getUrl());
				this.insertStatement.setNull(10, java.sql.Types.VARCHAR);
				this.insertStatement.executeUpdate();
				rsGenerator.close();
				this.connection.commit();
				returnValue=true;
			}catch(Exception ex){
				System.err.println("AvtozvukSaver#save Exception: "+ex.getMessage());
			}
		}
		return returnValue;
	}

	@Override
	public boolean begin() {
		boolean returnValue=false;
		try{
			StringBuffer query=new StringBuffer();
			query.append("INSERT INTO "+tableName+" ("+fieldId+", DATE_WRITE, SECTION_NAME, ");
			for(int counter=0;counter<this.columnNames.length;counter++){
				query.append(this.columnNames[counter]);
				if(counter!=(this.columnNames.length-1)){
					query.append(", ");
				}
			};
			query.append(") VALUES(?,?,?, ");
			for(int counter=0;counter<this.columnNames.length;counter++){
				query.append("?");
				if(counter!=(this.columnNames.length-1)){
					query.append(", ");
				}
			};
			query.append(") ");
			this.insertStatement=this.connection.prepareStatement(query.toString());
			
			idGenerator=this.connection.prepareStatement("select gen_id(gen_"+tableName+"_id,1) from rdb$database");
			returnValue=true;
		}catch(Exception ex){
			System.err.println("AvtozvukSaver#begin: "+ex.getMessage());
		}
		return returnValue;
	}

}
