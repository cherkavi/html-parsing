package shop_list.html.parser.engine.parser;

import java.io.StringWriter;
import java.io.Writer;
import java.util.StringTokenizer;

//import org.jaxen.XPath;
//import org.jaxen.dom.DOMXPath;
/* HTML Parser Cobra
import org.lobobrowser.html.parser.DocumentBuilderImpl;
import org.lobobrowser.html.parser.InputSourceImpl;
import org.lobobrowser.html.test.SimpleUserAgentContext;
*/

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import shop_list.html.parser.engine.reader.HttpReader;

import com.dappit.Dapper.parser.MozillaParser;

//import com.dappit.Dapper.parser.MozillaParser;

/** объект, который содержит функционал для парсинга HTML страниц, т.е. получения org.w3c.Node из удалённых HTML страниц  */
public class Parser {
	private static boolean debug=false;
	/** парсер для удаленных HTML файлов  */
	public Parser(){
		try{
			MozillaParser.init();
		}catch(Exception ex){
			System.err.println("Parser#constructor Exception:"+ex.getMessage());
		}
	}
	
	/** парсер для удаленных HTML файлов  */
	public Parser(String pathToMozillaParser){
		try{
			if(pathToMozillaParser!=null){
				MozillaParser.init(null, pathToMozillaParser);
			}else{
				MozillaParser.init();
			}
		}catch(Exception ex){
			System.err.println("Parser#constructor Exception:"+ex.getMessage());
		};
	}
	
	
	/** получить org.w3c.Node из Url
	 * @param urlPath - адрес
	 * @param charsetName - кодировка
	 * @param xpath - полный XPath
	 * @return - null - если произошла какого-либо рода ошибка
	 */
	public Node getNodeFromUrl(String urlPath, String charsetName, String xpath){
/*        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder =null;
        URL url =null;
        InputStream in = null;
        try {
        	builder= factory.newDocumentBuilder();
        	url=new URL(urlPath);
            UserAgentContext uacontext = new SimpleUserAgentContext();
        	in=url.openConnection().getInputStream();
            Reader reader = new InputStreamReader(in, charsetName);
            Document document = builder.newDocument();
            // Here is where we use Cobra's HTML parser.            
            HtmlParser parser = new HtmlParser(uacontext, document);
            parser.parse(reader);
            // Now we use XPath to locate "a" elements that are
            // descendents of any "html" element.
            XPath xpathFactory = XPathFactory.newInstance().newXPath();
            return (Node) xpathFactory.evaluate(xpath, document, XPathConstants.NODE);
            //return xpathFactory.evaluate(xpath, document);
		}catch(Exception ex){
			System.err.println("Parser#getNodeFromUrl Exception: "+ex.getMessage());
			return null;
		}finally{
			try{
				in.close();
			}catch(Exception ex){};
		}
*/
		
		try{
			Document doc=this.getDocumentByUrl(urlPath, charsetName);
			// #1
			//XPathFactory factory=XPathFactory.newInstance();
			//XPath field_xpath=factory.newXPath();
			//XPathExpression expression=field_xpath.compile(xpath);
			//Node returnValue=(Node)expression.evaluate(doc,XPathConstants.NODE);
			
			// #2
			Node returnValue=this.getNodeWithDataBlock(doc, xpath);
			return returnValue;
			
			// #3
			//Path field_xpath = new DOMXPath(xpath);
			//return (Node)field_xpath.selectSingleNode(doc);
		}catch(Exception ex){
			System.err.println("Parser#getNodeFromUrl Exception "+ex.getMessage());
			return null;
		}finally{
		}
	}
	
	/** получить на основании указанного URL org.w3c.Document 
	 * @param urlPath - полный путь к HTTP ресурсу
	 * @param charsetName - наименование таблицы кодировки (windows-1251)
	 * */ 
	private Document getDocumentByUrl(String urlPath,String charsetName){
		Document returnValue=null;
		HttpReader reader=null;
		MozillaParser parser = null;
		try{
			parser = new MozillaParser();
		}catch(Exception ex){};
		int errorCounter=0;
		while(returnValue==null){
			try{
				reader=new HttpReader(urlPath);
				returnValue= parser.parse( reader.getBytes(), charsetName); // windows-1251
			}catch(Exception ex){
				String message=ex.getMessage();
				if(message.indexOf("Failed to get unicode decoder for charset")>=0){
					System.err.println("Parser#getDocumentByUrl Check your Charset:"+charsetName);
					System.err.println("Parser#getDocumentByUrl Exception "+ex.getMessage()+" may be server is restarted ");
					break;
				}else{
					System.err.println("Parser#getDocumentByUrl Exception "+ex.getMessage()+" may be server is restarted ");
					errorCounter++;
					if(errorCounter>10){
						System.err.println("Parser#getDocumentByUrl Exception Repeat error reader beagest 10 attempt");
						break;
					}
					try{
						Thread.sleep(5000);
					}catch(Exception exInner){};
				}
			}finally{
				try{
					reader.closeReader();
				}catch(Exception ex){};
			}
		}
		return returnValue;
	}
	
	/*
	private Document getDocumentByUrl(String urlHttp, String charset){
		Document returnValue=null;
        InputStream in = null;
		try {
			Logger.getLogger("org.lobobrowser").setLevel(Level.WARNING);
			UserAgentContext uacontext = new SimpleUserAgentContext();
			DocumentBuilderImpl builder = new DocumentBuilderImpl(uacontext);
            in = new URL(urlHttp).openConnection().getInputStream();
            
        	InputSourceImpl inputSource = new InputSourceImpl(new InputStreamReader(in, charset), urlHttp);
            returnValue = builder.parse(inputSource);
            
            //HTMLDocumentImpl document = (HTMLDocumentImpl) d;
            //HTMLCollection images = document.getImages();
            //int length = images.getLength();
            //for(int i = 0; i < length; i++) {
            //    System.out.println("- Image#" + i + ": " + images.item(i));
            //}
        }catch(Exception ex){
        	System.err.println("EnterPoint#getDocumentFromUrl Exception:"+ex.getMessage());
        }finally {
        	try{
        		in.close();
        	}catch(Exception ex){};
            
        }
        return returnValue;
	}
	*/
	
	
	
/*	private void testDocument(Document doc, XPath xpath){
		try{
			// "/html/body/div/div[3]/div/div/form/table/tbody"
			//Node returnValue=(Node)xpath.evaluate("/body", doc,XPathConstants.NODE);
			Node returnValue=this.getNodeWithDataBlock(doc, "/html/body/div[2]/div[3]/div/div/form/table/tbody");
			System.out.println(this.getStringFromXmlDocument(returnValue));
		}catch(Exception ex){
			System.err.println("Parser#testDocument: "+ex.getMessage());
		}
	}

	private void printStringToFile(String filePath, String value){
		try{
			BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
			writer.write(value);
			writer.flush();
			writer.close();
		}catch(Exception ex){
			System.err.println("Exception: "+ex.getMessage());
		}
	}
	
*/	
	/** получить String из Url
	 * @param urlPath - адрес
	 * @param charsetName - кодировка {@link java.nio.charset.Charset}
	 * @param xpath - полный XPath
	 * @return - null - если произошла какого-либо рода ошибка
	 */
	public String getStringFromUrl(String urlPath, String charsetName, String xpath){
		Node node=this.getNodeFromUrl(urlPath, charsetName, xpath);
		if(node==null){
			return null;
		}else{
			return this.getStringFromXmlDocument(node);
		}
	}
	
	/** преобразовать Node в String */
	protected String getStringFromXmlDocument(Node document){
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
	}

	
	/** INFO исправление глюков XPath - в случае, когда не берет путь из XPath */
	protected  Node getNodeWithDataBlock(Node node,
				 						 String xpathToBlockData)  {
		StringTokenizer token=new StringTokenizer(xpathToBlockData.replaceAll("//", "/"),"/");
		Node returnValue=null;
		returnValue=(Node)getNodeFromDocumentByStringTokenizer(node, token);
		return returnValue;
	}
	
	private Node getNodeFromDocumentByStringTokenizer(Node document, StringTokenizer token){
		Node currentNode=document;
		main_cycle:while(token.hasMoreTokens()){
			String nextElement=token.nextToken();
			if(debug==true)System.out.println("nextElement:"+nextElement);
			int searchIndex=1;
			if(nextElement.indexOf('[')>0){
				searchIndex=Integer.parseInt(nextElement.substring(nextElement.indexOf('[')+1,nextElement.indexOf(']')));
				nextElement=nextElement.substring(0,nextElement.indexOf('['));
			}
			if(currentNode.hasChildNodes()){
				// есть элементы, проверка на имя в XPath пути
				int findIndex=0; // индекс в поиске /tr[3] - третий элемент с заданным именем
				NodeList list=currentNode.getChildNodes();
				for(int counter=0;counter<list.getLength();counter++){
					if(debug==true)System.out.println(">>>"+list.item(counter).getNodeName());
					if(list.item(counter).getNodeName().equals(nextElement)){
						//System.out.println(list.item(counter).getNodeName()+"==="+nextElement);
						findIndex++;
						if(findIndex==searchIndex){
							currentNode=list.item(counter);
							continue main_cycle;
						}
					}
				}
				// элемент не найден по указанному пути
				// INFO вывод ошибочного значения  
				if(debug==true){
					
					if(currentNode instanceof Element){
						String id=((Element)currentNode).getAttribute("id");
						System.out.println("CurrentNode: "+nextElement+"   >>> "+id);
					}else{
						System.out.println("CurrentNode: "+nextElement);
					}
					NodeList childsList=currentNode.getChildNodes();
					for(int counter=0;counter<childsList.getLength();counter++){
						if(childsList.item(counter) instanceof Element){
							System.out.println(counter+" : "+((Element)childsList.item(counter)).getNodeName()+"  id:"+((Element)childsList.item(counter)).getAttribute("id"));
						}
					}
					System.out.println("currentNode#getTextContent: "+currentNode.getTextContent());
				}
				currentNode=null;
				break main_cycle;
			}else{
				// нет элементов, а путь еще есть
				return null;
			}
		}
		return currentNode;
	}
	
	
	public Node getNodeFromUrlAlternative(String urlPath, String charsetName,
			String xpath) {
		return getNodeWithDataBlock(this.getDocumentByUrl(urlPath, charsetName),xpath);
	}
	
	/** получить из Node еще подэлемент, согласно указанному Xpath - исправление глюков стандартного API */
	public Node getNodeFromNodeAlternative(Node node, String xpath){
		return getNodeWithDataBlock(node, xpath);
	}
	
	/** получить текстовое представление данных для под-узла указанного узла
	 * @param node - узел XML
	 * @param xpath - путь к под-узлу 
	 * @param defaultValue - значение по-умолчанию, в случае не нахождения под-узла 
	 * @return
	 */
	public String getTextContentFromNodeAlternative(Node node, String xpath, String defaultValue){
		Node tempNode=this.getNodeFromNodeAlternative(node, xpath);
		if(tempNode!=null){
			return tempNode.getTextContent();
		}else{
			return defaultValue;
		}
	}

	public static void main(String[] args){
		Parser parser=new Parser();
		System.out.println(parser.getStringFromUrl("http://xmljs.sourceforge.net", "iso-8859-1", "/"));
	}

	/** получить кол-во дочерних элементов, которые имеют указанное имя тэга  
	 * @param element - элемент, который проверяется на вхождение дочерних элементов 
	 * @param childTagName - имя дочернего элемента, на которое проверяется данное значение 
	 * */
	public int getChildElementCount(Node node, String childTagName) {
		int returnValue=0;
		if((node!=null)&&(node instanceof Element)&&( ((Element)node).hasChildNodes() )){
			NodeList list=((Element)node).getChildNodes();
			for(int counter=0;counter<list.getLength();counter++){
				Node currentNode=list.item(counter);
				if((currentNode!=null)&&(currentNode instanceof Element)){
					if(((Element)currentNode).getTagName().equalsIgnoreCase(childTagName)){
						returnValue++;
					}
				}
			}
		}else{
			returnValue=0;
		}
		
		return returnValue;
	}

	
}
