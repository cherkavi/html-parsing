package shop_list.html.parser.engine.reader;

import java.io.ByteArrayOutputStream;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

// import sun.net.www.protocol.http.HttpURLConnection;


/** класс, для чтения данных со страницы */
public class HttpReader implements ParserReader{
	private String path=null;
	private HttpURLConnection connection=null;
	public HttpReader(String path){
		this.path=path;
	}

	public HttpReader(){
	}
	
	public Reader getReader(){
		return getReader(this.path);
	}
	
	/** подсоединиться к URL  и получить Reader,
	 * path должен обязательно содержить тип протокола: http:// или другой */
	
	public Reader getReader(String path){
		Reader returnValue=null;
		try{
			connection=(HttpURLConnection)(new URL(path)).openConnection();
			connection.setDoInput(true);
			//connection.setDoOutput(true);
			connection.connect();
			return new InputStreamReader(connection.getInputStream());
		}catch(Exception ex){
			out("HttpReader Exception:"+ex.getMessage());
		}
		return returnValue;
	}
	
	public byte[] getBytes() throws IOException {
		connection=(HttpURLConnection)(new URL(path)).openConnection();
		connection.setDoInput(true);
		//connection.setDoOutput(true);
		connection.connect();
		InputStream inputStream=connection.getInputStream();
		ByteArrayOutputStream output=new ByteArrayOutputStream();
		
		byte[] buffer=new byte[4096];
		int byteCount=0;
		while((byteCount=inputStream.read(buffer))!=(-1)){
			output.write(buffer,0,byteCount);
		}
		return output.toByteArray();
	}
	
	
	private void closeConnection(){
		try{
			this.getReader().close();
		}catch(Exception ex){};
		try{
			this.connection.disconnect();
		}catch(Exception ex){
			err("HttpReader Exception:"+ex.getMessage());
		}
	}
	
	public void closeReader(){
		this.closeConnection();
	}
	
	public void finalize(){
		this.closeConnection();
	}

	@Override
	public byte[] getBytes(String charsetName) throws IOException {
		return getBytes();
	}
	
	private void out(Object information){
		System.out.print("HttpReader");
		System.out.print(" DEBUG ");
		System.out.println(information);
	}
	private void err(Object information){
		System.err.print("HttpReader");
		System.err.print(" ERROR ");
		System.err.println(information);
	}
}
