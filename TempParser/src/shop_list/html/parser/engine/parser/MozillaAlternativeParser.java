package shop_list.html.parser.engine.parser;

import java.util.ArrayList;
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

import shop_list.html.parser.engine.reader.HttpReader;

import com.dappit.Dapper.parser.MozillaParser;

//import com.dappit.Dapper.parser.MozillaParser;

/** ������, ������� �������� ���������� ��� �������� HTML �������, �.�. ��������� org.w3c.Node �� �������� HTML �������  */
public class MozillaAlternativeParser implements IParser{
	public static Integer repeatAttempt=new Integer(0);
	private ParserUtility utility=new ParserUtility();
	/* ������ ��� ��������� HTML ������  
	public MozillaAlternativeParser(){
		try{
			MozillaParser.init();
		}catch(Exception ex){
			System.err.println("Parser#constructor Exception:"+ex.getMessage());
		}
	}
	*/
	/** ����� �� �������� ������� �� ����� ������ �� ���������� ��������� */
	private boolean cutScripts;
	
	/** ������ ��� ��������� HTML ������  */
	public MozillaAlternativeParser(String pathToMozillaParser){
		this(pathToMozillaParser, false);
	}
	
	/** ������ ��� ��������� HTML ������  */
	public MozillaAlternativeParser(String pathToMozillaParser, boolean cutScripts){
		this.cutScripts=cutScripts;
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
	
	/** �������� org.w3c.Node �� Url
	 * @param urlPath - �����
	 * @param charsetName - ���������
	 * @param xpath - ������ XPath ( �������� tr[0$] - ��������� Tag tr, tr[1$] - ����� ���������� tr )
	 * @return - null - ���� ��������� ������-���� ���� ������
	 */
	public Node getNodeFromUrl(String urlPath, String charsetName, String xpath)  throws Exception{
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
		if(urlPath==null)return null;
		Document doc=this.getDocumentByUrl(urlPath, charsetName, false);
		// #1
		//XPathFactory factory=XPathFactory.newInstance();
		//XPath field_xpath=factory.newXPath();
		//XPathExpression expression=field_xpath.compile(xpath);
		//Node returnValue=(Node)expression.evaluate(doc,XPathConstants.NODE);
		
		// #2
		Node returnValue=this.utility.getNodeWithDataBlockAlternative(doc, xpath);
		return returnValue;
		
		// #3
		//Path field_xpath = new DOMXPath(xpath);
		//return (Node)field_xpath.selectSingleNode(doc);
	}
	
	/** �������� �� ��������� ���������� URL org.w3c.Document 
	 * @param urlPath - ������ ���� � HTTP �������
	 * @param charsetName - ������������ ������� ��������� (windows-1251)
	 * */ 
	private Document getDocumentByUrl(String urlPath,String charsetName, boolean cutScripts){
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
				returnValue= parser.parse( reader.getBytes(urlPath), charsetName); // windows-1251
			}catch(Exception ex){
				String message=ex.getMessage();
				if(message.indexOf("Failed to get unicode decoder for charset")>=0){
					System.err.println("MozillaAlternativeParser#getDocumentByUrl Check your Charset:"+charsetName);
					System.err.println("MozillaAlternativeParser#getDocumentByUrl Exception "+ex.getMessage()+" may be server is restarted ");
					break;
				}else{
					System.err.println("MozillaAlternativeParser#getDocumentByUrl Exception "+ex.getMessage()+" may be server is restarted ");
					errorCounter++;
					if(errorCounter>repeatAttempt){
						System.err.println("MozillaAlternativeParser#getDocumentByUrl Exception Repeat error reader beagest 10 attempt");
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
	/** �������� String �� Url
	 * @param urlPath - �����
	 * @param charsetName - ��������� {@link java.nio.charset.Charset}
	 * @param xpath - ������ XPath
	 * @return - null - ���� ��������� ������-���� ���� ������
	private String getStringFromUrl(String urlPath, String charsetName, String xpath) {
		try{
			Node node=this.getNodeFromUrl(urlPath, charsetName, xpath);
			if(node==null){
				return null;
			}else{
				return this.getStringFromXmlDocument(node);
			}
		}catch(Exception ex){
			return null;
		}
	}

	 *
	 */
	
	/** ������������� Node � String 
	private String getStringFromXmlDocument(Node document){
		return utility.getStringFromXmlDocument(document);
	}
	*/

	
	
	
	/** �������� �� Node ��� ����������, �������� ���������� Xpath - ����������� ������ ������������ API ( �������� tr[0$] - ��������� Tag tr, tr[1$] - ����� ���������� tr ) */
	public Node getNodeFromNode(Node node, String xpath){
		return utility.getNodeWithDataBlockAlternative(node, xpath);
	}
	
	/** �������� ��������� ������������� ������ ��� ���-���� ���������� ����
	 * @param node - ���� XML
	 * @param xpath - ���� � ���-���� 
	 * @param defaultValue - �������� ��-���������, � ������ �� ���������� ���-���� 
	 * @return
	 */
	public String getTextContentFromNode(Node node, String xpath, String defaultValue){
		Node tempNode=this.getNodeFromNode(node, xpath);
		if(tempNode!=null){
			return tempNode.getTextContent();
		}else{
			return defaultValue;
		}
	}

	public static void main(String[] args){
		// MozillaAlternativeParser parser=new MozillaAlternativeParser(null);
		// System.out.println(parser.getStringFromUrl("http://xmljs.sourceforge.net", "iso-8859-1", "/"));
	}

	/** �������� ���-�� �������� ���������, ������� ����� ��������� ��� ����  
	 * @param element - �������, ������� ����������� �� ��������� �������� ��������� 
	 * @param childTagName - ��� ��������� ��������, �� ������� ����������� ������ �������� 
	 * */
	public int getChildElementCount(Node node, String childTagName) {
		return utility.getChildElementCount(node, childTagName);
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
		Document doc=this.getDocumentByUrl(urlPath, charsetName, this.cutScripts);
		return getNodeListFromNode(doc, xpath);
	}

	
}
