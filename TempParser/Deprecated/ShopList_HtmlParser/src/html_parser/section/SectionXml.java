package html_parser.section;

import html_parser.Parser;

import java.io.StringWriter;
import java.io.Writer;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/** класс, который служит для получения на основании страницы XML документа, который представляет из себя дерево всех элементов
 * {@literal" <root> 
 * 	<leaf href="" caption="">
 * 		<leaf href="" caption="">
 * 			
 * 		</leaf>
 * 	</leaf>
 * </root>
 * "}
 * */
public abstract class SectionXml {
	private Parser parser=null;
	public SectionXml(){
		this.parser=new Parser();
	}
	
	/** получить XML документ, который содержит дерево всех данных в виде листов и узлов */
	public abstract Document getXmlDocument();
	
	/** получить новый пустой документ 
	 * @throws ParserConfigurationException - если не удалось создать Новый документ  
	 * */
	protected Document getNewEmptyDocument() {
		javax.xml.parsers.DocumentBuilderFactory document_builder_factory=null;
		try{
			document_builder_factory=javax.xml.parsers.DocumentBuilderFactory.newInstance();
			document_builder_factory.setValidating(false);
			document_builder_factory.setIgnoringComments(true);
			javax.xml.parsers.DocumentBuilder document_builder=document_builder_factory.newDocumentBuilder();
			return document_builder.newDocument();
		}catch(Exception ex){
			System.err.println("SectionXml#getNewEmptyDocument Exception: "+ex.getMessage());
			return null;
		}
	}
	
	/** получить корневой элемент, который является обязательным для документа */
	protected Element getRootElement(Document document){
		return document.createElement("root");
	}
	
	/** получить элемент, на основании текстовых значений */
	protected Element getElement(Document document, String href, String caption){
		Element element=document.createElement("leaf");
		element.setAttribute("href", href);
		element.setAttribute("caption", caption);
		return element;
		
	}
	
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
	
	
	/** получить элемент из удалённого источника элемент в виде строки  
	 * @param urlPath - путь к удаленному 
	 * @param charsetName - кодировка удаленной страницы 
	 * @param xpath - путь в формате XPath
	 * @return
	 */
	protected String getSectionAsString(String urlPath, String charsetName, String xpath){
		return this.parser.getStringFromUrl(urlPath, charsetName, xpath);
	}
	
	/** получить элемент из удалённого источника элемент в виде строки  
	 * @param urlPath - путь к удаленному 
	 * @param charsetName - кодировка удаленной страницы 
	 * @param xpath - путь в формате XPath
	 * @return
	 */
	protected Node getNodeFormUrl(String urlPath, String charsetName, String xpath){
		return this.parser.getNodeFromUrl(urlPath, charsetName, xpath);
	}
	
	protected Node getNodeFromUrlAlternative(String urlPath, String charsetName, String xpath){
		return this.parser.getNodeFromUrlAlternative(urlPath, charsetName, xpath);
	}
	
	protected Node getNodeFromNodeAlternative(Node node, String xpath){
		return this.parser.getNodeFromNodeAlternative(node, xpath);
	}
}
