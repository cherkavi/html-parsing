package database.connector;
import java.io.File;

import java.io.IOException;
import java.sql.Connection;
import org.hibernate.Session;

import database.wrap.*;


/** класс, который производит соединение с базой данных, посредстом Hibernate и Connection */
public class Connector {
	// INFO - место присоединения всех классов для активации отображения объектов базы данных на объекты программы
	private Class<?>[] classOfDatabase=new Class[]{
												   Parse_record.class,
												   Parse_session.class,
												   Section.class,
												   Shop_list.class,
												   Actions.class,
												   };
	private IConnector connector=null;
	private HibernateConnection hibernateConnection;
	
	/** произвести соединение с базой данных посредством точного указания файла */
	public Connector(File file) throws Exception {
		while(true){
			if(file.exists()==false){
				throw new IOException("file is not exists:"+file.getAbsolutePath());
			}
			// попытка определения Firebird 
			if(file.getName().toUpperCase().endsWith(".GDB")){
				System.out.println("Connector created:");
				try{
					this.connector=new FirebirdConnection(file);
				}catch(Exception ex){
					
				}
				hibernateConnection=new HibernateConnection("org.hibernate.dialect.FirebirdDialect",classOfDatabase);
				break;
			}
			throw new Exception("algorithm is not found");
		}
	}

	/** произвести соединение с базой данных посредством полного пути к файлу*/
	public Connector(String path) throws Exception {
		this.connector=new FirebirdConnection(path);
		hibernateConnection=new HibernateConnection("org.hibernate.dialect.FirebirdDialect",classOfDatabase);
		System.out.println("Connector created:");
	}
	
	/** получить Hibernate Session */
	private Session openSession(Connection connection){
		return this.hibernateConnection.openSession(connection);
	}
	
	/** получить соединение с базой данных */
	private Connection getConnection(){
		return this.connector.getConnection();
	}
	
	public ConnectWrap getConnector(){
		Connection connection=this.getConnection();
		Session session=this.openSession(connection);
		return new ConnectWrap(connection,session);
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("-- begin --");
		Connector connector=new Connector("D:\\eclipse_workspace\\TempParser\\database\\SHOP_LIST_PARSE.GDB");
		System.out.println("Connector: "+connector.getConnector());
		System.out.println("Session: "+connector.openSession(connector.getConnection()));
		System.out.println("-- end --");

		/*DatabaseMetaData metaData=connection.getMetaData();
		ResultSet rs=metaData.getClientInfoProperties();
		int columnCount=rs.getMetaData().getColumnCount();
		while(rs.next()){
			for(int counter=0;counter<columnCount;counter++){
				System.out.print(counter+" : "+rs.getString(counter));
			}
			System.out.println();
		}*/
	}
}
