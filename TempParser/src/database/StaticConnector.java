package database;

public class StaticConnector {
	private static Connector connector;
	/** полное имя драйвера  */
	public static DatabaseDrivers driver;
	/** полный путь к базе данных */
	public static String url;
	/** имя пользователя */
	public static String userName;
	/** пароль */
	public static String password;
	/** размер Пула данных  */
	public static int poolSize=20;
	
	/** получить соединение с базой данных  */
	public static ConnectWrap getConnectWrap(){
		if(connector==null){
			try{
				// в этом месте должны быть установлены (драйвер, URL, имя пользователя, пароль )
				connector=new Connector(driver, url, userName, password, poolSize);
			}catch(Exception ex){
				System.err.println("StaticConnector init Exception: "+ex.getMessage());
				System.exit(1);
			}
		}
		return connector.getConnector();
	}
}
