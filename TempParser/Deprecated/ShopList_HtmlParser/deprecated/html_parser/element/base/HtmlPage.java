package html_parser.element.base;

import java.io.Reader;
/** класс-родитель для всех страниц Html, которые возвращает Walker */
public class HtmlPage {
	private Reader reader;
	private String url;
	
	/** Html страница с сайта */
	public HtmlPage(String url, Reader reader){
		this.url=url;
		this.reader=reader;
	}
	
	/** получить Reader по страницам */
	public Reader getReader(){
		return this.reader;
	}
	
	/** получить URL данной страницы, по которой получен Reader */
	public String getUrl(){
		return this.url;
	}
	
	/** закрыть данную страницу */
	public void close(){
		if(this.reader!=null){
			try{
				this.reader.close();
			}catch(Exception ex){
			}
		}
	}
}
