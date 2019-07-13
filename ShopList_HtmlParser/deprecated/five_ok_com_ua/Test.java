package five_ok_com_ua;

import java.io.Reader;

import java.nio.charset.Charset;
import java.util.ArrayList;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import html_parser.element.base.HtmlPage;
import html_parser.element.base.HtmlRecord;
import html_parser.engine.base.WalkRecord;
import html_parser.reader.HttpReader;

/** 
 * URL: 5OK.com.ua
 * Краткое описание ресурса данного магазина:
 * Есть разделы:
		Бытовая техника
			http://www.5ok.com.ua/Catalogs_133.html
	
		Аудио Видео Hi-Fi Автоэлектроника
			http://www.5ok.com.ua/Catalogs_134.html
		
		Телефония, цифровое фото и портативная электроника
			http://www.5ok.com.ua/Catalogs_135.html
	
		Климатическая техника и водонагреватели
			http://www.5ok.com.ua/Catalogs_136.html
	
		Компьютерная и офисная техника 
			http://www.5ok.com.ua/Catalogs_137.html

			
	каждый раздел содержит в себе подразделы в виде ссылок на первую страницу подраздела,
	Например:
	Бытовая техника:http://www.5ok.com.ua/Catalogs_133.html
	содержит в себе одну из ссылок:
		Холодильники
		http://www.5ok.com.ua/Products_138_142_0_0_0_0_0_1.html - первая страница
		http://www.5ok.com.ua/Products_138_142_0_0_0_0_0_9.html - девятая страница
		http://www.5ok.com.ua/Products_138_142_0_0_0_0_0_14.html - последняя страница
		http://www.5ok.com.ua/Products_138_142_0_0_0_0_0_15.html - страница без данных
	
		то есть сигнал к окончанию - страница без данных
 */

public class Test {
	public static void main(String[] args){
		// получение ссылок на подкаталоги из страниц с каталогами:
		/*ArrayList<String> urls=new ArrayList<String>();
		urls.add("http://www.5ok.com.ua/Catalogs_133.html");
		urls.add("http://www.5ok.com.ua/Catalogs_134.html");
		urls.add("http://www.5ok.com.ua/Catalogs_135.html");
		urls.add("http://www.5ok.com.ua/Catalogs_136.html");
		urls.add("http://www.5ok.com.ua/Catalogs_137.html");
		printAllSubSection(urls);*/
		
		HttpReader pageReader=new HttpReader();
		String pathToUrl="http://www.5ok.com.ua/Products_138_142_0_0_0_0_0_1.html";
		try{
			FiveOkRecord recordWalk=new FiveOkRecord(new HtmlPage(pathToUrl, pageReader.getReader(pathToUrl)),
				     								 "windows-1251",
				     								 "//html/body/form/table/tbody/tr[3]/td[2]/div/table[2]/tbody",
				     								 false
				     								 );
			HtmlRecord record=null;
			while( (record=recordWalk.getNextRecord())!= null){
				System.out.println(record);
			}
		}catch(Exception ex){
			System.err.println("create Walker for page: "+pathToUrl+"  Error :"+ex.getMessage());
		}
	}
	
	private static void printAllSubSection(ArrayList<String> urls){
		HttpReader reader=new HttpReader();
		for(int counter=0;counter<urls.size();counter++){
			try{
				WalkRecord walkRecord=new FiveOK_Record_SubSection(urls.get(counter),
																   reader.getReader(urls.get(counter)),
																   "windows-1251",//"windows-1251", 
																   "//html/body/form/table/tbody/tr[3]/td[2]/table[4]/tbody",
																   false);
				HtmlRecord record=null;
				while( (record=walkRecord.getNextRecord())!=null){
					System.out.println(record.toString());
				};
			}catch(Exception ex){
				System.err.println("WalkRecord create false: "+ex.getMessage());
			}
		}
		
	}
}

/** прочесть из страницы готовые для заливки в базу данные*/
class FiveOkRecord extends WalkRecord{
	private NodeList listOfElement;
	private int index=(-1);
	/** прочесть из страницы готовые для заливки данные в базу */
	public FiveOkRecord(HtmlPage page,
						String charsetName,
						String xpathToBlockData,
						boolean useXPath) throws Exception{
		super(page,charsetName, xpathToBlockData,useXPath);
		listOfElement=this.rootNode.getChildNodes();
	}
	
	private final static long serialVersionUID=1L;
	@Override
	public HtmlRecord getNextRecord() {
		index++;
		if(index<this.listOfElement.getLength()){
			return new FiveOkHtmlRecord(this.listOfElement.item(index),"utf-8");
		}else{
			return null;
		}
	}
}


class FiveOkHtmlRecord extends HtmlRecord{
	private Node node;
	private String price;
	private String name;
	private String charsetName;
	
	public FiveOkHtmlRecord(Node node,String charsetName){
		this.node=node;
		this.charsetName=charsetName;
		this.parseData();
	}
	
	private void parseData(){
		try{
			this.name=new String(this.getSubNode(node, "td[2]/a[1]").getTextContent().getBytes(),this.charsetName);
		}catch(Exception ex){
			System.err.println("Exception:"+ex.getMessage());
		}
		try{
			this.price=new String(this.getSubNode(node, "td[3]/div[1]").getTextContent().getBytes(),this.charsetName);
		}catch(Exception ex){
			System.err.println("Exception:"+ex.getMessage());
		}
	}
	
	@Override
	public String toString(){
		return this.name+"."+this.price;
	}
}


/** прочесть из страницы все подэлементы, которые касаются ссылок на подразделы */
class FiveOK_Record_SubSection extends WalkRecord{
	private ArrayList<Node> listOfNode=null;
	private int nodeIndex=0;
	
	public FiveOK_Record_SubSection(String url,Reader reader, String charset, String pathToXmlBlockData, boolean useXPath) throws Exception{
		super(new HtmlPage(url,reader),charset,pathToXmlBlockData,useXPath);
		try {
			listOfNode=this.getAllElementsByName(this.rootNode, "a");
		} catch (Exception e) {
			throw new Exception("Node is null");
		} 
	}

	private final static long serialVersionUID=1L;
	@Override
	public HtmlRecord getNextRecord() {
		if(this.listOfNode!=null){
			if(nodeIndex<this.listOfNode.size()){
				HtmlRecord record=new HtmlRecord(new String(this.listOfNode.get(nodeIndex).getTextContent().getBytes(),Charset.forName("utf-8"))
												 +" ;  "
												 +((Attr)this.listOfNode.get(nodeIndex).getAttributes().getNamedItem("href")).getValue()
												 );
				nodeIndex++;
				return record;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
}
