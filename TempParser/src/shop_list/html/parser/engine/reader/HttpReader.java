package shop_list.html.parser.engine.reader;

import java.io.ByteArrayOutputStream;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import shop_list.html.parser.engine.reader.utility.CutBytes;



/** класс, дл€ чтени€ данных со страницы */
public class HttpReader implements ParserReader{
	private HttpURLConnection connection=null;
	private String path;
	private boolean cutScripts=false;
	
	/** объект, дл€ чтени€ данных со страницы */
	public HttpReader(String path){
		this(path, false);
		
	}

	/** объект, дл€ чтени€ данных со страницы 
	 * @param path - полный путь к объекту
	 * @param cutScripts - нужно ли вырезать скрипты (&ltscript&gt)
	 * */
	public HttpReader(String path, boolean cutScripts){
		this.path=path;
		this.cutScripts=cutScripts;
	}
	
	/** подсоединитьс€ к URL  и получить Reader,
	 * path должен об€зательно содержить тип протокола: http:// или другой */
	public Reader getReader(){
		return this.getReader(null);
	}
	
	/** подсоединитьс€ к URL  и получить Reader,
	 * path должен об€зательно содержить тип протокола: http:// или другой */
	public Reader getReader(String charsetName){
		if(connection!=null){
			try{
				if(charsetName!=null){
					return new InputStreamReader(connection.getInputStream(), charsetName);
				}else{
					return new InputStreamReader(connection.getInputStream());
				}
			}catch(Exception ex){
				try{
					connection.disconnect();
					connection=null;
				}catch(Exception exInner){};
			}
		}
		try{
			this.connectToUrl(path);
			if(charsetName!=null){
				return new InputStreamReader(connection.getInputStream(), charsetName);
			}else{
				return new InputStreamReader(connection.getInputStream());
			}
		}catch(Exception ex){
			out("HttpReader Exception:"+ex.getMessage());
			return null;
		}
	}

	/** преобразовать возможные UTF строки в символы (подобные этому: %¬0)  */
	private String checkPathForUtfLetters(String value){
		if(isStringHasRussianLetter(value)){
			try{
				StringBuffer destination=new StringBuffer();
				for(int counter=0;counter<value.length();counter++){
					String findString=value.substring(counter, counter+1);
					if(isStringHasRussianLetter(findString)){
						destination.append(urlEncode(findString));
					}else{
						destination.append(findString);
					}
				}
				return destination.toString();
			}catch(Exception ex){
				return value;
			}
		}else{
			return value;
		}
	}
	
	/** декодировать строку  */
	private String urlEncode(String value){
		try{
			return URLEncoder.encode(value,"UTF-8");
		}catch(Exception ex){
			return value;
		}
	}

	private boolean isStringHasRussianLetter(String value){
		return value.replaceAll("[^а-€^ј-я^ ]*", "").length()>0;
	}
	
	public static void main(String[] args){
		System.out.println("-begin-");
		// http://tehnoroom.com.ua/1-%D0%9D%D0%BE%D1%83%D1%82%D0%B1%D1%83%D0%BA%D0%B8.html
		// http://tehnoroom.com.ua/1-%D0%9D%D0%BE%D1%83%D1%82%D0%B1%D1%83%D0%BA%D0%B8.html
		// String target="http://tehnoroom.com.ua/1-Ќоутбуки.html";
		// System.out.println(checkPathForUtfLetters(target));
		System.out.println("--end--");
	}
	
	
	private void connectToUrl(String path) throws IOException{
		
		connection=(HttpURLConnection)(new URL(checkPathForUtfLetters(path))).openConnection();
		connection.setDoInput(true);
		//connection.setDoOutput(true);
		// connection.setRequestMethod("GET");
		/*
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; ru; rv:1.9.0.13) Gecko/2009073022 Firefox/3.0.13 (.NET CLR 3.5.30729) sputnik 2.0.1.41");
		connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;");
		connection.setRequestProperty("Accept-Language", "ru,en;q=0.7,en-us;q=0.3");
		connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
		connection.setRequestProperty("Accept-Charset", "windows-1251,utf-8;q=0.7,*;q=0.7");
		connection.setRequestProperty("Keep-Alive", "300");
		connection.setRequestProperty("Connection", "keep-alive");
		*/
		//connection.setDoOutput(true);
		connection.connect();
	}
	
	
	/** подсоединитьс€ к URL  и получить Reader,
	 * path должен об€зательно содержить тип протокола: http:// или другой */
	public InputStream getInputStream(String path){
		if(connection!=null){
			try{
				return connection.getInputStream();
			}catch(Exception ex){
				try{
					connection.disconnect();
					connection=null;
				}catch(Exception exInner){};
			}
		}
		try{
			connectToUrl(path);
			return connection.getInputStream();
		}catch(Exception ex){
			out("HttpReader Exception:"+ex.getMessage());
			return null;
		}
	}
	
	public byte[] getBytes() throws IOException {
		connectToUrl(this.path);
		InputStream inputStream=connection.getInputStream();
		ByteArrayOutputStream output=new ByteArrayOutputStream();
		
		/*byte[] buffer=new byte[4096];
		int byteCount=0;
		while((byteCount=inputStream.read(buffer))!=(-1)){
			output.write(buffer,0,byteCount);
		}*/
		if(this.cutScripts==false){
			this.copyFromInputToOutput(inputStream, output);
		}else{
			CutBytes cutBytes=new CutBytes();
			cutBytes.copyFromInputToOutput(inputStream, 
										   output, 
										   new ArrayList<String>(){
											private final static long serialVersionUID=1L;
											   {
												   this.add("<script");
											   }
										   },
										   new ArrayList<String>(){
											private final static long serialVersionUID=1L;
												   {
													   this.add("</script>");
													   this.add("/>");
												   }
										   }
			);		
		}
		
		
		return output.toByteArray();
	}
	
	
	
	private void copyFromInputToOutput(InputStream inputStream, OutputStream outputStream) throws IOException{
		byte[] buffer=new byte[4096];
		int byteCount=0;
		while((byteCount=inputStream.read(buffer))!=(-1)){
			outputStream.write(buffer,0,byteCount);
		}
	}
	
	
	
	private void closeConnection(){
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
