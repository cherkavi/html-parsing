package html_parser.reader;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class FileReader implements ParserReader{
	private String pathToFile=null;
	private Reader reader=null;
	
	public FileReader(String pathToFile){
		this.pathToFile=pathToFile;
	}
	
	public byte[] getBytes() throws IOException {
		ByteArrayOutputStream output=new ByteArrayOutputStream();
		FileInputStream fis=new FileInputStream(this.pathToFile);
		byte[] buffer=new byte[4096];
		int byteCount=0;
		while((byteCount=fis.read(buffer))!=(-1)){
			output.write(buffer,0,byteCount);
		}
		return output.toByteArray();
	}
	
	public byte[] getBytes(String charsetName) throws IOException{
		StringBuffer output=new StringBuffer();
		if(this.reader==null){
			this.getReader(charsetName);
		}
		//new InputStreamReader(new FileInputStream(this.pathToFile),charsetName)
		BufferedReader br=new BufferedReader(this.reader,1024);
		String currentLine=null;
		while((currentLine=br.readLine())!=null){
			output.append(currentLine);
		}
		return output.toString().getBytes(charsetName);
		
	}

	@Override
	public Reader getReader(String charset) {
		try{
			if(charset!=null){
				this.reader=new InputStreamReader(new FileInputStream(this.pathToFile),charset);
			}else{
				this.reader=new InputStreamReader(new FileInputStream(this.pathToFile));
			}
			return this.reader;
		}catch(Exception ex){
			return null;
		}
	}
	
	public void closeReader(){
		try{
			this.reader.close();
		}catch(Exception ex){};
	}
}
