package sites.ava_com_ua;

import java.io.File;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.hibernate.Session;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import sun.net.www.protocol.http.HttpURLConnection;

import database.ConnectWrap;
import database.Connector;
import database.wrap.Photo;

import html_parser.Parser;
import html_parser.record.Record;
import html_parser.record_processor.RecordProcessor;

public class AvaRecordProcessorImageSaver extends RecordProcessor {
	private Connector connector;
	private String charset;
	private Parser parser;
	private String urlPrefix="http://ava.com.ua";
	private String hddPath;
	
	public AvaRecordProcessorImageSaver(Connector connector, String charset, String hddPath){
		this.connector=connector;
		this.charset=charset;
		this.parser=new Parser();
		this.hddPath=hddPath;
	}
	
	@Override
	public void afterSave(ArrayList<Record> block) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeSave(ArrayList<Record> list) {
		// �������� �������� �� ������� ��������� ������� �������
		if(list!=null){
			for(int counter=0;counter<list.size();counter++){
				if(list.get(counter) instanceof AvaRecord){
					this.processRecord((AvaRecord)list.get(counter));
				}
			}
		}
	}
	
	
	/** ���������� ���������� ������ */
	private void processRecord(AvaRecord record){
		Node node=this.parser.getNodeFromUrlAlternative(record.getUrl(), this.charset, "/html/body/div[2]/div[3]/div[4]");
		if(node!=null){
			try{
				// �������� ������ ��� /h1 getText()
				Node objectName=this.parser.getNodeFromNodeAlternative(node, "/h1");
				// ��������� Node � ���� ������
				Node imageNode=this.parser.getNodeFromNodeAlternative(node, "/div/table/tbody/tr/td/div/a/img");
				String pathToImage=((Element)imageNode).getAttribute("src");
				saveImage(objectName.getTextContent().trim(), pathToImage);
			}catch(Exception ex){
				System.err.println("Error in read data - empty");
			}
		}else{
			System.err.println("AvaRecordProcessor#processRecord data not find ");
		}
	}

	/** ��������� ����������� �� ����� � � ���� ������ 
	 * @param fullName - ������ ��� ��������
	 * @param url - HTTP path
	 */
	private void saveImage(String fullName, String url){
		if(url!=null){
			// �������� ��� �����
			String fileName=this.getPathFromUrl(url);
			// ��������� �� ������� ����� �� �����  
			while(fileExists(this.hddPath+fileName)){
				fileName=generateNewFileName(fileName);
			}
			// �������� ����������� � ��������� ��� �� �����
			if(saveImageToHdd(urlPrefix+url,this.hddPath+fileName)==true){
				// ������� ������ � ���� ������ 
				saveImageToDatabase(fullName, url,fileName);
			}
		}
	}
	
	/** ��������� ����������� � ���� ������ */
	private boolean saveImageToDatabase(String fullName, String url, String fileName){
		boolean returnValue=false;
		ConnectWrap connectWrap=this.connector.getConnector();
		try{
			Session session=connectWrap.getSession();
			Photo photo=new Photo();
			photo.setFilename(fileName);
			photo.setHttpPath(url);
			photo.setFullName(fullName);
			session.beginTransaction();
			session.save(photo);
			session.getTransaction().commit();
			returnValue=true;
		}catch(Exception ex){
			System.err.println("AvaRecordProcessImageSaver#saveImageToDatabase Exception: "+ex.getMessage());
		}finally{
			connectWrap.close();
		}
		return returnValue;
	}
	
	
	/** �������� ��� ����� �� ������ */
	private String getPathFromUrl(String url){
		int lastIndex=url.lastIndexOf("/");
		return url.substring(lastIndex+1);
	}
	
	/** �������� ����� � ������� ������� ����� � ��������*/
	private boolean fileExists(String path){
		boolean returnValue=false;
		try{
			File file=new File(path);
			returnValue=file.exists();
		}catch(Exception ex){};
		return returnValue;
	}
	
	/** ������������� ����� ��� ��� �����*/
	private String generateNewFileName(String fileName){
		int dotPosition=fileName.lastIndexOf('.');
		String name=fileName.substring(0,dotPosition);
		String extension=fileName.substring(dotPosition+1);
		return name+"_."+extension;
	}
	
	/** �������� �������� URL, ���� ���������� ������� */
	private String getValidUrl(String url){
		return url.replaceAll(" ", "%20");
	}

	private final static int repeatCount=10;
	
	/** ��������� ����������� �� ����� 
	 * @param url - HTTP URL 
	 * @param fullPath ������ ���� � ����� �� ����� 
	 * @return
	 */
	private boolean saveImageToHdd(String url,String fullPath){
		boolean returnValue=false;
		HttpURLConnection connection=null;
		InputStream inputStream=null;
		try{
			int counter=0;
			main: while(true){
				// �������� InputStream �� URL
				try{
					counter++;
					if(counter>repeatCount){
						break main;
					}
					connection=(HttpURLConnection)(new URL(this.getValidUrl(url))).openConnection();
					connection.setDoInput(true);
					//connection.setDoOutput(true);
					connection.connect();
					inputStream=connection.getInputStream();
					this.saveInputStreamToFile(fullPath, inputStream);
					break main;
				}catch(Exception ex){
					// ������ ������ ������ �� URL, �������� ������ ������������
					System.err.println("AvaRecordProcessorImageSaver#saveImageToHdd Exception: "+ex.getMessage());
					try{
						Thread.sleep(5000);
					}catch(Exception exTimer){};
				}
			}
			returnValue=true;
		}catch(Exception ex){
			System.err.println("AvaRecordProcessImageSaver#saveImageToHdd Exception: "+ex.getMessage());
			returnValue=false;
		}finally{
			try{
				inputStream.close();
			}catch(Exception exInner){};
			try{
				connection.disconnect();
			}catch(Exception exInner){};
		}
		return returnValue;
	}

	/** ��������� ����� �� HttpUrlConnection � ����� */
	private boolean saveInputStreamToFile(String fullPath, InputStream inputStream) throws Exception{
		FileOutputStream fos=new FileOutputStream(fullPath);
		byte[] buffer=new byte[1024];
		int readedByte=0;
		
		while((readedByte=inputStream.read(buffer))>=0){
			fos.write(buffer,0,readedByte);
		}
		fos.flush();
		fos.close();
		return true;
	}
	
}
