package html_parser;

import java.sql.Connection;

import java.util.ArrayList;
import html_parser.page.PageWalker;
import html_parser.page.PageWalkerAware;
import html_parser.record.Record;
import html_parser.record_processor.RecordProcessor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import database.Connector;

import sites.autozvuk_com_ua.AvtozvukPageWalker;
import sites.autozvuk_com_ua.AvtozvukPageWalkerAware;
import sites.autozvuk_com_ua.AvtozvukSaver;
import sites.autozvuk_com_ua.AvtozvukSectionXml;

/** класс, который на основании XML документа позволяет "прогуляться" по всем записям в указанной секции, обязательные аттрибуты каждого узла - "href","caption" */
public class WalkSection {
	/** слушатели, которые принимают решение по поводу сканирования очередной секции */
	private ArrayList<ScanSectionFilter> allowListeners=new ArrayList<ScanSectionFilter>();
	
	/** добавить слушателя, который принимает решение по поводу парсинга очередной страницы */
	public void addAllowListener(ScanSectionFilter listener){
		this.allowListeners.add(listener);
	}
	/** удалить слушателя, который принимает решение на парсинг очередной секции */
	public void removeAllowListener(ScanSectionFilter listener){
		this.allowListeners.remove(listener);
	}

	/** решение, нужно ли продолжать парсить указанный элемент дерева */
	private boolean isProcessAllow(Element element){
		boolean returnValue=true;
		for(int counter=0;counter<this.allowListeners.size();counter++){
			if(this.allowListeners.get(counter).isFilter(element)==false){
				returnValue=false;
				break;
			}
		}
		return returnValue;
	}
	
	/** пройтись по всем записям, которые указаны в XML документе 
	 * @param xmlDocument - документ, который содержит все элементы {@literal"<leaf caption="" href="">"} 
	 * @param urlPrefix - префикс, который нужно добавлять для получения адреса секции (href)
	 * @param processParent - нужно ли "парсить" узлы, которые являются родительскими, т.е. содержат другие узлы (true) или же нужно парсить только "листы" без родительских компонентов (false)
	 * @param processLeaf - нужно ли "парсить" конечные узлы дерева - листья
	 * @param pageWalker - парсер страницы, который "вынимает" из страницы необходимые данные
	 * @param pageWalkerAware - объект, который получает ссылки на следующие страницы
	 * @param delay - задержки, которые необходимо делать для нормального(эмуляция пользователя) чтения данных из сети
	 * @param postProcessor - обработка полученных записей перед сохранения  
	 */
	public void walk(Document xmlDocument, 
					 String urlPrefix, 
					 boolean processParent,
					 boolean processLeaf, 
					 PageWalker pageWalker,
					 PageWalkerAware pageWalkerAware,
					 Delay delay,
					 RecordProcessor preProcessor,
					 Saver saver){
		if(saver!=null)saver.begin();
		// перебрать все листы, которые переданы в xmlDocument
		NodeList nodes=xmlDocument.getChildNodes().item(0).getChildNodes();
		for(int counter=0;counter<nodes.getLength();counter++){
			//System.out.println(this.getStringFromXmlDocument(nodes.item(counter)));
			walkNode(nodes.item(counter),
					 urlPrefix, 
					 processParent,
					 processLeaf, 
					 pageWalker, 
					 pageWalkerAware, 
					 delay,
					 preProcessor,
					 saver);
		}
		if(saver!=null)saver.finish();
	}
	/*
	private String getStringFromXmlDocument(Node document){
		Writer out=null;
		try{
			javax.xml.transform.TransformerFactory transformer_factory = javax.xml.transform.TransformerFactory.newInstance();  
			javax.xml.transform.Transformer transformer = transformer_factory.newTransformer();  
			javax.xml.transform.dom.DOMSource dom_source = new javax.xml.transform.dom.DOMSource(document); // Pass in your document object here  
			out=new StringWriter();
			//string_writer = new Packages.java.io.StringWriter();  
			javax.xml.transform.stream.StreamResult stream_result = new javax.xml.transform.stream.StreamResult(out);  
			transformer.transform(dom_source, stream_result);  
		}catch(Exception ex){
			System.err.println("getStringFromXmlDocument:"+ex.getMessage());
		}
		return (out==null)?"":out.toString();
	}*/

	
	/** пройтись по всем веткам, по всем записям  */
	private void walkNode(Node node,
						  String urlPrefix,
						  boolean processParent,
						  boolean processLeaf,
						  PageWalker pageWalker, 
						  PageWalkerAware pageWalkerAware, 
						  Delay delay,
						  RecordProcessor preProcessor,
						  Saver saver){
		if(node.hasChildNodes()){
			//System.out.println("has child");
			// есть дочерние элементы
			NodeList list=node.getChildNodes();
			for(int counter=0;counter<list.getLength();counter++){
				this.walkNode(list.item(counter),
							  urlPrefix,
							  processParent,
							  processLeaf,
							  pageWalker,
							  pageWalkerAware, 
							  delay,
							  preProcessor,
							  saver);
			}
			if(processParent){
				this.processNode(node,urlPrefix, pageWalker,pageWalkerAware,delay,preProcessor, saver);
			};
		}else{
			//System.out.println("this is leaf");
			// данный элемент является листом, т.е. не имеет дочерних элементов
			// node
			if(processLeaf){
				this.processNode(node,urlPrefix,pageWalker,pageWalkerAware, delay,preProcessor, saver);
			}
		}
	}
	
	
	/** обработать данный элемент из элемента дерева, которое содержит все секции
	 * @param node - узел, который будет
	 * @param urlPrefix - префикс URL к которому нужно добавлять данные 
	 * @param pageWalker - объект для чтения страниц
	 * @param pageWalkerAware - объект для перемещения между страницами 
	 * @param delay - задержка для чтения
	 * @param saver - объект, который предназначен для сохранения Records 
	 */
	private void processNode(Node node,
							 String urlPrefix,
							 PageWalker pageWalker,
							 PageWalkerAware pageWalkerAware, 
							 Delay delay,
							 RecordProcessor preProcessor,
							 Saver saver){
		if(node instanceof Element){
			Element element=(Element)node;
			String href=element.getAttribute("href");
			String caption=element.getAttribute("caption");
			System.out.println(">>> Section:"+caption+"   Href: "+href);
			if(this.isProcessAllow(element)){
				pageWalker.updatePageWalkerAware(pageWalkerAware);
				pageWalkerAware.reset(urlPrefix+href);
				// получить код первой страницы
				while(pageWalker.hasMoreElements()){
					// полученный элемент распарсить
					ArrayList<Record> block=pageWalker.nextElement();
					if(preProcessor!=null){
						preProcessor.beforeSave(block);
					}
					// пробежаться по Saver-у
					if(saver!=null){
						for(int counter=0;counter<block.size();counter++){
							//System.out.println(block.get(counter).toString());
							if(saver.save(caption, block.get(counter))==false){
								System.err.println("WalkSection save Error:");
							}
						}
					}
					if(preProcessor!=null){
						preProcessor.afterSave(block);
					}
					System.out.println(">>> NextPage ( after "+pageWalker.getLastUrl()+") ");
					// уснуть, если нужно, для возможного сокрытия парсинга - время обращения к одному и тому же серверу
					try{
						Thread.sleep(delay.getDelayReadPage());
					}catch(Exception ex){};
				}
				try{
					Thread.sleep(delay.getDelayReadSection());
				}catch(Exception ex){};
			}else{
				// отмена парсинга очередного элемента - запрет слушателей  
			}
		}else{
			// промежуточный элемент
		}
	}
	
	
	public static void main(String[] args){
/*		System.out.println("begin");
		// объект, который читает страницы 
		PageWalker pageWalker=new AvtozvukPageWalker();
		// объект, который даёт номер следующей страницы 
		PageWalkerAware pageWalkerAware=new AvtozvukPageWalkerAware();
		// объект, который выдаёт XML на основании рубрикатора/дерева элементов  
		AvtozvukSectionXml avtozvuk=new AvtozvukSectionXml();
		Document treeXml=avtozvuk.getXmlDocument();
		// объект-задержка для эмуляции чтения данных человеком, а не парсером 
		Delay delay=new Delay(5,2);
		// объект для сохранения данных 
		Connection connection=null;
		try{
			Connector connector=new Connector("V:/eclipse_workspace/ShopList_HtmlParser/shop_list.gdb");
			connection=connector.getConnector().getConnection();
		}catch(Exception ex){
			System.out.println("WalkSection#main getConnection Exception: "+ex.getMessage());
		}
		AvtozvukSaver saver=new AvtozvukSaver(connection);
		//saver.resetAllRecord();
		
		WalkSection walk=new WalkSection();
		// установить фильтр на парсинг 
		walk.addAllowListener(new ScanSectionFilter(){
			private boolean parse=false;
			@Override
			public boolean isFilter(Element element) {
				if(element.getAttribute("caption").indexOf("Кроссоверы")>=0){
					parse=true;
				}
				return this.parse;
			}
		});
		walk.walk(treeXml, 
				  "http://avtozvuk.ua", 
				  true, 
				  true,
				  pageWalker, 
				  pageWalkerAware, 
				  delay,
				  null,
				  saver
				  );
		try{
			connection.close();
		}catch(Exception ex){};
		System.out.println("end");
*/		
	}
}
