package process_exchange.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/** серверный процесс по обмену данными с удаленным клиенским процессом  */
public class ServiceServer extends Thread{
	private Socket socket;
	private BufferedReader reader=null;
	private BufferedWriter writer=null;
	private ICommand command=null;

	/**  */
	private void debug(Object value){
		System.out.println("DEBUG: ServiceServer: "+value);
	}
	private void error(Object value){
		System.out.println("ERROR: ServiceServer: "+value);
	}
	
	public ServiceServer(Socket socket, ICommand command) throws Exception{
		this.socket=socket;
		this.command=command;
		reader=new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		writer=new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
		start();
	}
	
	@Override
	public void run(){
		// прочесть из Socket строку
		try{
			String task=reader.readLine();
			debug("String from client:"+task );
			task=this.command.execute(task);
			debug("String to client:"+task);
			writer.write(task);
			writer.flush();
			reader.close();
			writer.close();
		}catch(Exception ex){
			error("ServiceServer Exception: "+ex.getMessage());
		}finally{
			try{
				socket.close();
			}catch(Exception ex){};
		}
	}
	
}
