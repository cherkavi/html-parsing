package process_exchange.client;

/** ��������� ���������� ��������� ��������  */
public class ResultWrap {
	private String value;
	private Client.EConnectResult connectResult;
	
	/** ��������� ���������� ��������� ��������  */
	public ResultWrap(){
		value=null;
	}
	
	/** ��������� ���������� ��������� ��������  */
	public ResultWrap(String value){
		this.value=value;
	}

	/** �������� ��������  �������  */ 
	public String getValue() {
		return value;
	}

	/** ���������� �������� ��� �������  */
	public void setValue(String value) {
		this.value = value;
	}
	
	/** ���������� ��������� ���������� ��������  */
	public void setConnectResult(Client.EConnectResult connectResult){
		this.connectResult=connectResult;
	}
	
	/** �������� ��������� ���������� �������� */
	public Client.EConnectResult getConnectResult(){
		return this.connectResult;
	}
}
