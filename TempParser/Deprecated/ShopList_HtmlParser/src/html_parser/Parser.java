package html_parser;

import html_parser.reader.HttpReader;


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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dappit.Dapper.parser.MozillaParser;

//import com.dappit.Dapper.parser.MozillaParser;

/** объект, который содержит функционал для парсинга HTML страниц, т.е. получения org.w3c.Node из удалённых HTML страниц  */
public class Parser {
	
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
			System.out.println("Parser#getNodeFromUrl Exception "+ex.getMessage());
			return null;
		}finally{
		}
	}
	
	/** получить на основании указанного URL org.w3c.Document */ 
	private Document getDocumentByUrl(String urlPath,String charsetName){
		Document returnValue=null;
		HttpReader reader=null;
		MozillaParser parser = null;
		try{
			parser = new MozillaParser();
		}catch(Exception ex){};
		while(returnValue==null){
			try{
				reader=new HttpReader(urlPath);
				returnValue= parser.parse( reader.getBytes(), charsetName); // windows-1251
			}catch(Exception ex){
				System.out.println("Parser#getDocumentByUrl Exception "+ex.getMessage()+" may be server is restarted ");
				try{
					Thread.sleep(5000);
				}catch(Exception exInner){};
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
	 * @param charsetName - кодировка
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
			//System.out.println("nextElement:"+nextElement);
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
					//System.out.println(">>>"+list.item(counter).getNodeName());
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

	public static void main(String[] args){
		Parser parser=new Parser();
		System.out.println(parser.getStringFromUrl("http://xmljs.sourceforge.net", "iso-8859-1", "/"));
	}

	
}
