package process_exchange.server;

import java.net.ServerSocket;
import java.net.Socket;

import process_exchange.server.test.TestCommand;

public class Server {
	public static void main(String[] args){
		System.out.println("start");
		int port=0;
		if((args!=null)&&(args.length>0)){
			try{
				port=Integer.parseInt(args[0]);
			}catch(Exception ex){
				port=2010;
			}
		}else{
			port=2010;
		}
		try{
			new Server(port, new TestCommand());
		}catch(Exception ex){
			// возможно, адрес уже используется 
			System.err.println("Server#main Exception: "+ex.getMessage());
			System.exit(1);
		}
		System.out.println("End: ");
	}
	
	private void debug(Object value){
		System.out.println("Server DEBUG: "+value);
	}
	
	/** объект, который поднимает слушателя порта, и воспринимает все входящие сообщения 
	 * @throws возможно адрес уже используется
	 * */
	public Server(int port, ICommand command) throws Exception {
		ServerSocket server=new ServerSocket(port);
		while(true){
			debug("Wait for client connect...");
			Socket socket=server.accept();
			debug("Client connected");
			new ServiceServer(socket, command);
		}
	}
}

