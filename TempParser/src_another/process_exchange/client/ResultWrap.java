package process_exchange.client;

/** результат выполнения удаленной операции  */
public class ResultWrap {
	private String value;
	private Client.EConnectResult connectResult;
	
	/** результат выполнения удаленной операции  */
	public ResultWrap(){
		value=null;
	}
	
	/** результат выполнения удаленной операции  */
	public ResultWrap(String value){
		this.value=value;
	}

	/** получить значение  объекта  */ 
	public String getValue() {
		return value;
	}

	/** установить значение для объекта  */
	public void setValue(String value) {
		this.value = value;
	}
	
	/** установить результат выполнения операции  */
	public void setConnectResult(Client.EConnectResult connectResult){
		this.connectResult=connectResult;
	}
	
	/** получить результат выполнения операции */
	public Client.EConnectResult getConnectResult(){
		return this.connectResult;
	}
}
