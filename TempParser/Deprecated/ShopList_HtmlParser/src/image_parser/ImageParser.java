package image_parser;

import java.io.InputStream;

import java.net.URL;
import java.sql.Connection;
import database.Connector;

import sites.autozvuk_com_ua.AvtozvukImagePathAwareSaver;
import sun.net.www.protocol.http.HttpURLConnection;

/** ������, ������� �������� ����������� �� ���� � ��������� �� � ���������
 * <br>
 * <i>������ ��������� �� �����������, � �� �� html �������� � MIME ������������ ����������� ( ��������: MIME=img/jpeg )</i>
 * */
public class ImageParser {
	/*
	 * 
		;	%3B
		?	%3F
		/	%2F
		:	%3A
		#	%23
		&	%24
		=	%3D
		+	%2B
		$	%26
		,	%2C
		<space>	%20 or +
		%	%25
		<	%3C
		>	%3E
		~	%7E
		%	%25	 
	*/
	private String getValidUrl(String url){
		return url.replaceAll(" ", "%20");
	}
	
	public boolean parse(ImagePathAware imagePathAwareSaver, DelayImage delay){
		boolean returnValue=false;
		String nextUrl=null;
		HttpURLConnection connection=null;
		InputStream inputStream=null;
		imagePathAwareSaver.begin();
		while((nextUrl=imagePathAwareSaver.getNextUrl())!=null){
			try{
				// �������� InputStream �� URL
				connection=(HttpURLConnection)(new URL(this.getValidUrl(nextUrl))).openConnection();
				connection.setDoInput(true);
				//connection.setDoOutput(true);
				connection.connect();
				try{
					inputStream=connection.getInputStream();
					// �������� ��������� ������ 
					if(imagePathAwareSaver.saveLastGetUrl(inputStream)==true){
						System.out.println("Saver ..."+nextUrl);
					}else{
						System.err.println("Error on save ..."+nextUrl);
					}
				}catch(Exception ex){
					// �������� ������ ��� �������� �����������
					imagePathAwareSaver.saveLastGetUrlAsError();
					throw ex;
				}
			}catch(Exception ex){
				System.err.println("ImageParser#parse Exception: "+ex.getMessage());
				
			}finally{
				// ������� �����
				try{
					inputStream.close();
				}catch(Exception ex){};
				// ������� Url
				try{
					connection.disconnect();
				}catch(Exception ex){};
			}
			try{
				Thread.sleep(delay.getDelayMs());
			}catch(Exception ex){};
		}
		imagePathAwareSaver.end();
		return returnValue;
	}
	

	public static void main(String[] args){
		ImageParser imageParser=new ImageParser();
		Connection connection=null;
		try{
			connection=(new Connector("V:/eclipse_workspace/ShopList_HtmlParser/shop_list.gdb")).getConnector().getConnection();
		}catch(Exception ex){};
		imageParser.parse(new AvtozvukImagePathAwareSaver("http://avtozvuk.ua/",connection,"c:\\temp_image\\"),new DelayImage(200));
		try{
			connection.close();
		}catch(Exception ex){};
		
	}
}
