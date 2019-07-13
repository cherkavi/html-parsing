package test;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.cyberneko.html.parsers.DOMParser;
import org.lobobrowser.html.parser.*;
import org.lobobrowser.html.test.*;
import org.lobobrowser.html.*;


import org.w3c.tidy.Tidy;

import javax.xml.xpath.*;


import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.multi_page.debug.PrintElement;
import shop_list.html.parser.engine.parser.IParser;
import shop_list.html.parser.engine.parser.MozillaAlternativeParser;
import shop_list.html.parser.engine.parser.NekoParser;
import shop_list.html.parser.engine.parser.ParserUtility;
import shop_list.html.parser.engine.reader.HttpReader;


import com.dappit.Dapper.parser.MozillaParser;

public class XPathValidator {

	/** получить на основании указанного URL org.w3c.Document 
	 * @param urlPath - полный путь к HTTP ресурсу
	 * @param charsetName - наименование таблицы кодировки (windows-1251)
	 * */ 
	private static Document getDocumentByUrlMozilla(String urlPath,String charsetName, boolean cutScripts){
		Document returnValue=null;
		HttpReader reader=null;
		MozillaParser parser = null;
		try{
			parser = new MozillaParser();
		}catch(Exception ex){};
		int errorCounter=0;
		while(returnValue==null){
			try{
				reader=new HttpReader(urlPath, cutScripts);
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

	
	/** получить на основании указанного URL org.w3c.Document 
	 * @param urlPath - полный путь к HTTP ресурсу
	 * @param charsetName - наименование таблицы кодировки (windows-1251)
	 */
	private static Document getDocumentByUrlTidy(String urlPath,String charsetName){
		HttpReader reader=new HttpReader(urlPath);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            final Tidy tidy = new Tidy();
            tidy.setQuiet(true);
            // tidy.setShowWarnings(true);
            // tidy.setErrout(new PrintWriter(System.err));
            tidy.setXmlOut(true);
            // tidy.setForceOutput(true);
            document = tidy.parseDOM(reader.getInputStream(urlPath), null);
            return document;
        }catch(Exception ex){
        	System.err.println("Exception: "+ex.getMessage());
        	return null;
        }finally {
        	try{
        		reader.closeReader();
        	}catch(Exception ex){};
        }	
    }
    
	
	/** получить на основании указанного URL org.w3c.Document 
	 * @param urlPath - полный путь к HTTP ресурсу
	 * @param charsetName - наименование таблицы кодировки (windows-1251)
	 */
	private static Document getDocumentByUrlCobra(String urlPath,String charsetName){
		HttpReader reader=new HttpReader(urlPath);
        try {
    		// Disable most Cobra logging.
            UserAgentContext uacontext = new SimpleUserAgentContext();
            // In this case we will use a standard XML document
            // as opposed to Cobra's HTML DOM implementation.
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            // Here is where we use Cobra's HTML parser.            
            HtmlParser parser = new HtmlParser(uacontext, document);
            parser.parse(reader.getReader(charsetName));
            return document;
        }catch(Exception ex){
        	System.err.println("Exception: "+ex.getMessage());
        	return null;
        }finally {
        	try{
        		reader.closeReader();
        	}catch(Exception ex){};
        }	
    }

	/** получить на основании указанного URL org.w3c.Document 
	 * @param urlPath - полный путь к HTTP ресурсу
	 * @param charsetName - наименование таблицы кодировки (windows-1251)
	 * */
	private static Document getDocumentByUrlNeko(String urlPath,String charsetName){
		DOMParser parser = new DOMParser();
		try {
            // DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // DocumentBuilder builder = factory.newDocumentBuilder();
			// Document document = builder.newDocument();
			parser.parse(urlPath);
			return parser.getDocument();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
    }
	
	/** классический XPath */
	private static Node getNodeFromDocument(Document document, String xpathString){
		try{
			XPath xpath = XPathFactory.newInstance().newXPath();
			return (Node) xpath.evaluate(xpathString, document, XPathConstants.NODE);		
		}catch(Exception ex){
			System.err.println("#getNodeFromDocument Exception: "+ex.getMessage());
			return null;
		}
	}
	
	
	/** INFO исправление глюков XPath - в случае, когда не берет путь из XPath */
	protected static Node getNodeWithDataBlock(Node node,
				 						 	   String xpathToBlockData)  {
		StringTokenizer token=new StringTokenizer(xpathToBlockData.replaceAll("//", "/"),"/");
		Node returnValue=null;
		returnValue=(Node)getNodeFromDocumentByStringTokenizer(node, token);
		return returnValue;
	}
	
	private static boolean debug=true;
	
	private static Node getNodeFromDocumentByStringTokenizer(Node document, StringTokenizer token){
		StringBuffer buffer=new StringBuffer();
		Node currentNode=document;
		main_cycle:while(token.hasMoreTokens()){
			String nextElement=token.nextToken();
			buffer.append("/");
			buffer.append(nextElement);
			if(debug==true)System.out.println("nextElement:"+buffer.toString());
			int searchIndex=1;
			if(nextElement.indexOf('[')>0){
				searchIndex=Integer.parseInt(nextElement.substring(nextElement.indexOf('[')+1,nextElement.indexOf(']')));
				nextElement=nextElement.substring(0,nextElement.indexOf('['));
			}
			if(currentNode.hasChildNodes()){
				// есть элементы, проверка на имя в XPath пути
				int findIndex=0; // индекс в поиске /tr[3] - третий элемент с заданным именем
				NodeList list=currentNode.getChildNodes();
				if(debug==true){
					for(int counter=0;counter<list.getLength();counter++){
						if(list.item(counter) instanceof Element){
							Element element=(Element)list.item(counter);
							String id=element.getAttribute("id");
							String className=element.getAttribute("class");
							System.out.println("          "+counter+"/"+list.getLength()+": "+ list.item(counter).getNodeName()+ "   "+(id.equals("")?"":"id:"+id) +(className.equals("")?"":element.getNodeName()+" (class:"+className+")") );
						}else{
							System.out.println("          "+counter+"/"+list.getLength()+": "+list.item(counter).getNodeName());
						}
					}
				}
				for(int counter=0;counter<list.getLength();counter++){
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
							String id=((Element)childsList.item(counter)).getAttribute("id");
							if((id!=null)&&(id.equals("")))id=null;
							String className=((Element)childsList.item(counter)).getAttribute("class");
							if((className!=null)&&(className.equals("")))className=null;
							System.out.println((counter+1)+"/"+ childsList.getLength()+" : "+((Element)childsList.item(counter)).getNodeName()
												+((id==null)?"":"  id:"+id)
												+((className==null)?"":"  class:"+className));
						}
					}
					// вывод текста 
					// System.out.println("currentNode#getTextContent: "+currentNode.getTextContent());
				}
				currentNode=null;
				break main_cycle;
			}else{
				System.out.println("Leaf, elements does not exists");
				return null;
			}
		}
		System.out.println("------------------------");
		return currentNode;
	}
	
	public static void main(String[] args){
		Node node=null;
		boolean printNode=true;
		boolean isList=true;
		
		String outputFile="c:\\out_element.tree";
		ParserUtility.debug=true;
		String charset=ECharset.WIN_1251.getName();
		String url="http://shop.sven.ua/component/option,com_virtuemart/page,shop.browse/category_id,166/Itemid,2/"
		;
		// String xpath="/HTML/BODY/TABLE/TBODY/TR[2]/TD[1]/TABLE/TBODY/TR[1]/TD[3]/TABLE/TBODY/TR/TD/DIV[1]";
		   String xpath="/html/body/div/table[@id=\"main\"]/tbody/tr/td[@class=\"wrapper\"]/table/tbody/tr[2]/td/table/tbody/tr[@valign=\"top\"]/td[3]/div[@id=\"centerpadding\"]/div[@id=\"product_list\"]/div[*]/div[@class=\"product_cell\"]/div/div[2]/h3/a";
		;
		/*
		if(args.length>0){
			if(args.length<4){
				System.out.println("<URL> <Charset> <XPath> <[debug]:true|false> <outputFile>");
			}else{
				if(args.length==4){
					printNode=false;
					url=args[0];
					charset=args[1];
					xpath=args[2];
					ParserUtility.debug=Boolean.parseBoolean(args[3]);
				}
				if(args.length>=5){
					url=args[0];
					charset=args[1];
					xpath=args[2];
					ParserUtility.debug=Boolean.parseBoolean(args[3]);
					printNode=(!args[4].trim().equals(""));
					outputFile=args[4].trim();
				}
			}
		}*/
		isList=((xpath.indexOf('*')>0)||(xpath.indexOf('@')>0));
		try{
			// Tidy
			// node=getNodeFromDocument(getDocumentByUrlTidy(url, charset), xpath);System.out.println("Node: "+node);

			// ------------------------
			
			// Neko
			// node=getNodeFromDocumentByStringTokenizer(getDocumentByUrlNeko(url, charset), new StringTokenizer(xpath.replaceAll("//", "/"),"/"));System.out.println("Node: "+node);

			// Neko List
			// IParser parser=new NekoParser();printNode=false;ArrayList<Node> list=parser.getNodeListFromUrl(url, charset, xpath);for(int counter=0;counter<list.size();counter++)System.out.println(counter+" : "+list.get(counter));
			
			// ------------------------
			// Mozilla
			MozillaParser.init();IParser parser=new MozillaAlternativeParser(null);
			// IParser parser=new MozillaAlternativeParser("D:\\java_lib\\HtmlParser\\MozillaParser-v-0-3-0\\dist\\windows\\mozilla\\");
			// IParser parser=new MozillaAlternativeParser(null);
			if(isList==false){
				// MozillaParser.init();
				node=null;node=parser.getNodeFromUrl(url, charset, xpath);
				if(node!=null){
					if(node instanceof Element){
						System.out.println(0+" : "+((Element)node).getTagName());
					}else{
						System.out.println("Node: "+node);
					}
				}else{
					System.out.println("NOT FOUND");
				}
			}
			
			// Mozilla List
			if(isList==true){
				ArrayList<Node> list=parser.getNodeListFromUrl(url, charset, xpath);
				if(list.size()==0){
					System.out.println("NOT FOUND");
					printNode=false;
				}else{
					for(int counter=0;counter<list.size();counter++){
						if(list.get(counter) instanceof Element){
							System.out.println(counter+" : "+((Element)list.get(counter)).getTagName());
						}
					}
					node=list.get(0);
					printNode=true;
				}
			}
			
			if(printNode==true){
				PrintElement printer=new PrintElement(outputFile); 
				printer.outputAsTree(node);
			}
		}catch(Exception ex){
			System.err.println("Exception: "+ex.getMessage());
		}
		System.out.println("-end-");
	}
}


