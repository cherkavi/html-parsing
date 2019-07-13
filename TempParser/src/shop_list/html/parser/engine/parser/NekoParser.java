package shop_list.html.parser.engine.parser;

import java.util.ArrayList;

import java.util.StringTokenizer;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import shop_list.html.parser.engine.reader.HttpReader;

public class NekoParser implements IParser{
	public static Integer repeatAttempt=new Integer(0);
	private ParserUtility utility=new ParserUtility ();
	
	public NekoParser(){
	}
	
	@Override
	public int getChildElementCount(Node node, String childTagName) {
		return utility.getChildElementCount(node, childTagName);
	}

	/** получить на основании указанного URL org.w3c.Document 
	 * @param urlPath - полный путь к HTTP ресурсу
	 * @param charsetName - наименование таблицы кодировки (windows-1251)
	 */
	private Document getDocumentByUrlNeko(String urlPath,String charsetName){
		int errorCounter=0;
		DOMParser parser = new DOMParser();
		HttpReader httpReader=new HttpReader(urlPath);
		while(true){
			try{
	            // DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            // DocumentBuilder builder = factory.newDocumentBuilder();
				// Document document = builder.newDocument();

				parser.parse(new InputSource(httpReader.getReader(charsetName)));
				return parser.getDocument();
			}catch(Exception ex){
				String message=ex.getMessage();
				if((message!=null)&&(message.indexOf("Failed to get unicode decoder for charset")>=0)){
					System.err.println("NekoParser#getDocumentByUrl Check your Charset:"+charsetName);
					System.err.println("NekoParser#getDocumentByUrl Exception "+ex.getMessage()+" may be server is restarted ");
					return null;
				}else{
				}
				System.err.println("NekoParser#getDocumentByUrl Exception "+ex.getMessage()+" may be server is restarted ");
				errorCounter++;
				if(errorCounter>repeatAttempt){
					System.err.println("NekoParser#getDocumentByUrl Exception Repeat error reader beagest 10 attempt");
					return null;
				}
				try{
					Thread.sleep(5000);
				}catch(Exception exInner){};
				
			}
		}
    }
	
	
	@Override
	public Node getNodeFromNode(Node node, String xpath) {
		try{
			// import javax.xml.xpath.*;
			// XPathExpression expression=field_xpath.compile(xpath);
			// return (Node)expression.evaluate(node,XPathConstants.NODE);
			
			return this.utility.getNodeWithDataBlockAlternative(node, xpath);
		}catch(Exception ex){
			return null;
		}
	}

	// private XPath field_xpath=XPathFactory.newInstance().newXPath();
	
	@Override
	public Node getNodeFromUrl(String urlPath, String charsetName, String xpath) throws Exception{
		if(urlPath==null)return null;
		Document doc=this.getDocumentByUrlNeko(urlPath, charsetName);
		// XPathExpression expression=field_xpath.compile(xpath);
		// return (Node)expression.evaluate(doc,XPathConstants.NODE);
		Node returnValue=this.utility.getNodeWithDataBlockAlternative(doc, xpath);
		return returnValue;
	}
	
	/** INFO исправление глюков XPath - в случае, когда не берет путь из XPath 
	private  Node getNodeWithDataBlockAlternative(Node node,
				 						 String xpathToBlockData) throws Exception {
		StringTokenizer token=new StringTokenizer(xpathToBlockData.replaceAll("//", "/"),"/");
		Node returnValue=null;
		returnValue=(Node)getNodeFromDocumentByStringTokenizer(node, token);
		return returnValue;
	}
	*/
/*
	private Node getNodeFromDocumentByStringTokenizer(Node document, StringTokenizer token) throws Exception {
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
					if(list.item(counter).getNodeName().equalsIgnoreCase(nextElement)){
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
	
*/
	
	@Override
	public String getTextContentFromNode(Node node, String xpath, String defaultValue) {
		Node tempNode=this.getNodeFromNode(node, xpath);
		if(tempNode!=null){
			return tempNode.getTextContent();
		}else{
			return defaultValue;
		}
	}

	@Override
	public ArrayList<Node> getNodeListFromNode(Node node, String xpath) {
		ArrayList<Node> returnValue=new ArrayList<Node>();
		if(node!=null){
			this.utility.getNodeListByTokenList(returnValue, 
												node, 
												this.utility.getArrayListFromTokens(new StringTokenizer(xpath.replaceAll("//", "/"),"/")));
		}
		return returnValue;
	}


	@Override
	public ArrayList<Node> getNodeListFromUrl(String urlPath, String charsetName, String xpath) throws Exception {
		if(urlPath==null)return null;
		Document doc=this.getDocumentByUrlNeko(urlPath, charsetName);
		return getNodeListFromNode(doc, xpath);
	}

}
