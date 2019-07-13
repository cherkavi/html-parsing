package database.connector;

/** ������-�������� ��� ��������� ����������� � ����� ������  
 * <br>
 * ����� ����� � ���� ������ - ������, ������� ������� Connection-�
 * */
public class ConnectorSingleton {
	private static Connector connector;
	/** ������ ���� � ����� ���� ������, ������ ���� ������ ����� �������������� ������ ��������� ��������� {@link ConnectorSingleton#getConnectWrap()} */
	public static String pathToDatabase;
	
	/** ������-�������� ��� ��������� ����������� � ����� ������  
	 * <br>
	 * <small> ���� ���������� ������� - ���������� ����� ������ ������� ������� ������������ ���� {@link ConnectorSingleton#pathToDatabase} </small>
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
