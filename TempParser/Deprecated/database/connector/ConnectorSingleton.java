package database.connector;

/** объект-одиночка для получения соденинения с базой данных  
 * <br>
 * точка входа в базу данных - объект, который раздает Connection-ы
 * */
public class ConnectorSingleton {
	private static Connector connector;
	/** полный путь к файлу базы данных, должен быть указан перед использованием метода получения содинения {@link ConnectorSingleton#getConnectWrap()} */
	public static String pathToDatabase;
	
	/** объект-одиночка для получения соденинения с базой данных  
	 * <br>
	 * <small> если вызывается впервые - необходимо перед первым вызовом указать расположение базы {@link ConnectorSingleton#pathToDatabase} </small>
	 * */
	public static ConnectWrap getConnectWrap(){
		if(connector==null){
			try{
				connector=new Connector(pathToDatabase);
			}catch(Exception ex){
				System.err.println("ConnectorSingleton#getConnectWrap Exception:"+ex.getMessage());
			}
			
		}
		return connector.getConnector();
	}
}
