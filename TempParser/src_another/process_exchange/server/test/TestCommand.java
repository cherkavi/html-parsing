package process_exchange.server.test;

import process_exchange.server.ICommand;

public class TestCommand implements ICommand{

	@Override
	public String execute(String value) {
		return reverseString(value);
	}

	/** обратить строку */
	private String reverseString(String value){
		try{
			Thread.sleep(2000);
		}catch(Exception ex){};
		if((value==null)||(value.length()==0)){
			return value;
		}
		if(value.length()==0){
			return value;
		}
		StringBuffer returnValue=new StringBuffer();
		for(int counter=value.length()-1;counter>=0;counter--){
			returnValue.append(value.charAt(counter));
		}
		return returnValue.toString();
	}
	

}
