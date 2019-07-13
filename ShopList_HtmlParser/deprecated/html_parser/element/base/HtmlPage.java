package html_parser.element.base;

import java.io.Reader;
/** �����-�������� ��� ���� ������� Html, ������� ���������� Walker */
public class HtmlPage {
	private Reader reader;
	private String url;
	
	/** Html �������� � ����� */
	public HtmlPage(String url, Reader reader){
		this.url=url;
		this.reader=reader;
	}
	
	/** �������� Reader �� ��������� */
	public Reader getReader(){
		return this.reader;
	}
	
	/** �������� URL ������ ��������, �� ������� ������� Reader */
	public String getUrl(){
		return this.url;
	}
	
	/** ������� ������ �������� */
	public void close(){
		if(this.reader!=null){
			try{
				this.reader.close();
			}catch(Exception ex){
			}
		}
	}
}
