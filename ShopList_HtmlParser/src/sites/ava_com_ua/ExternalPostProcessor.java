package sites.ava_com_ua;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ExternalPostProcessor {
	public static void main(String[] args){
		System.out.println("begin");
		ArrayList<String> listOfElement=new ArrayList<String>();
		listOfElement.add("VIDEO");
		listOfElement.add("PROCESSOR");
		listOfElement.add("MEMORY");
		listOfElement.add("MB");
		listOfElement.add("HDD");
		
		String pathToDatabase="V:/eclipse_workspace/ShopList_HtmlParser/shop_list.gdb";
		Connection connection=connectToDatabase(pathToDatabase);
		Connection connectionInsert=connectToDatabase(pathToDatabase);
		
		Finder finder=new Finder(connection,connectionInsert,listOfElement);
		//finder.processJoin();
		finder.fileFind("C:\\computer_shop_photo\\");
		try{
			connectionInsert.close();
		}catch(Exception ex){
			System.err.println("Connection: "+ex.getMessage());
		}
		try{
			connection.close();
		}catch(Exception ex){
			System.err.println("Connection: "+ex.getMessage());
		}
		System.out.println("end");
	}
	
	/** получить соединение с базой данных по ссылке*/
    private static Connection connectToDatabase(String path_to_database){
        Connection returnValue=null;
        String driverName = "org.firebirdsql.jdbc.FBDriver";
        String database_protocol="jdbc:firebirdsql://";
        String database_dialect="?sql_dialect=3";
        String database_server="localhost";
        String database_port="3050";
        //String databaseURL = "jdbc:firebirdsql://localhost:3050/d:/work/sadik/sadik.gdb?sql_dialect=3";
        String databaseURL=database_protocol+database_server+":"+database_port+"/"+path_to_database+database_dialect;
        try{
            Class.forName(driverName);
            returnValue=java.sql.DriverManager.getConnection(databaseURL,"SYSDBA","masterkey");
            returnValue.setAutoCommit(false);
        }catch(SQLException sqlexception){
        	System.out.println("SQLException: "+sqlexception.getMessage());
        }catch(ClassNotFoundException classnotfoundexception){
        	System.out.println("Error load class "+classnotfoundexception);
        }
        return returnValue;
    }

}


class Finder {
	private Connection connection;
	private Connection insertConnection;
	private ArrayList<String> listOfTable;
	
	public Finder(Connection connection,Connection insertConnection, ArrayList<String> listOfTable){
		this.connection=connection;
		this.insertConnection=insertConnection;
		this.listOfTable=listOfTable;
	}
	
	public void processJoin(){
		for(int counter=0;counter<listOfTable.size();counter++){
			String table=listOfTable.get(counter);
			System.out.println(table);
			ResultSet rs=null;
			PreparedStatement ps=null;
			PreparedStatement insertStatement=null;
			try{
				rs=this.connection.createStatement().executeQuery("select * from "+table+" order by id");
				ps=this.connection.prepareStatement("select photo.id from photo where photo.full_name=(select first 1 name from "+table+" where "+table+".id=?)");
				insertStatement=this.insertConnection.prepareStatement("update "+table+" set id_photo=? where id=?");
				while(rs.next()){
					int id=rs.getInt("ID");
					ps.setInt(1, id);
					ResultSet currentResultSet=ps.executeQuery();
					if(currentResultSet.next()){
						// найдено соответствие 
						insertStatement.clearParameters();
						insertStatement.setInt(1, currentResultSet.getInt("id"));
						insertStatement.setInt(2, id);
						insertStatement.executeUpdate();
						insertStatement.getConnection().commit();
					}
					currentResultSet.close();
					System.out.println(id);
				}
			}catch(Exception ex){
				System.out.println("Finder#process Exception:"+ex.getMessage());
			}finally{
				try{
					insertStatement.close();
				}catch(Exception ex){};
				try{
					ps.close();
				}catch(Exception ex){};
				try{
					rs.close();
				}catch(Exception ex){};
			}
		}
	}

	/** поиск соответствующих файлов и ссылок на них в таблицах */
	public void fileFind(String pathToDirectory) {
		if(!pathToDirectory.endsWith("\\")){
			pathToDirectory=pathToDirectory+"\\";
		}
		ResultSet list=null;
		PreparedStatement statementCorrection=null;
		for(int counter=0;counter<this.listOfTable.size();counter++){
			try{
				String currentTable=this.listOfTable.get(counter);
				list=this.connection.createStatement().executeQuery("select photo.filename, "+currentTable+".id from "+currentTable+" inner join photo on "+currentTable+".id_photo=photo.id order by "+currentTable+".id");
				statementCorrection=this.insertConnection.prepareStatement("update "+currentTable+" set id_photo=null where id=?");
				while(list.next()){
					String fileName=list.getString("filename");
					if((fileName==null)||(!isFileExists(pathToDirectory+list.getString("filename")))){
						// файл не существует - удалить ссылку на фото
						statementCorrection.setInt(1, list.getInt("id"));
						statementCorrection.executeUpdate();
						statementCorrection.getConnection().commit();
						System.out.println("Not found: "+list.getString("filename"));
					}
				}
				
			}catch(Exception ex){
				System.err.println("Finder#fileFind Exception:"+ex.getMessage());
			}finally{
				try{
					list.getStatement().close();
				}catch(Exception ex){};
				try{
					statementCorrection.close();
				}catch(Exception ex){};
			}
		}
	}
	
	private boolean isFileExists(String path){
		boolean returnValue=false;
		try{
			File file=new File(path);
			returnValue=(file.exists())&&(file.isFile());
		}catch(Exception ex){
			System.err.println("isFileExists Exception: "+ex.getMessage());
		}
		return returnValue;
	}
}
