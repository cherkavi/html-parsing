package shop_list_manager;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/** �������, ������� �������� ParserManager 
 * ����������� ������� �� ���� ParserManager-a:
 * <br>
 * &lttask&gt <br>
 * &nbsp&nbsp&nbsp &ltcommand&gt <br>
 *   {@link #COMMAND_EXIT} || {@link #COMMAND_PARSE_START} || {@link #COMMAND_PARSE_STOP} <br>
 * &nbsp&nbsp&nbsp &lt/command&gt <br>
 * &nbsp&nbsp&nbsp &ltargument&gt <br>
 * 	��������� ��� ������� <br> 
 * &nbsp&nbsp&nbsp &lt/argument&gt <br>
 * &lt/task&gt
 * 
 * <br>
 * �������, ������� ������� �� ParserManager-a � ����� 
 * &lttask&gt <br>
 * &nbsp&nbsp&nbsp &ltcommand&gt <br>
 *   {@link #RETURN_OK} || {@link #RETURN_CANCEL} || {@link #RETURN_UNKNOWN} <br>
 * &nbsp&nbsp&nbsp &lt/command&gt <br>
 * &nbsp&nbsp&nbsp &ltargument&gt <br>
 * 	��������� ��� ������� <br> 
 * &nbsp&nbsp&nbsp &lt/argument&gt <br>
 * &lt/task&gt
 * */
public enum EParserManagerCommands {
	/** ���������� �������  
	 * <br>
	 * ������� �������� - ACTIONS.ID, ���� ��������� ���������� ������������� �������, ����� ���������� ������ ������ ACTION
	 * */
	COMMAND_PARSE_START,
	/** ������������� �������, ����� ���� ����������� �������� {@link #COMMAND_PARSE_START} � ���������� ������ ACTION ������� ����� ����������� */
	COMMAND_PARSE_STOP,
	/** ������ �� ��������� ��������� �������� ( ���������� � ��������� ��������� ��� ������ )  */
	COMMAND_GET_STATE,
	/** ��������� ������ �������, ���������� ��� � �����  */
	COMMAND_EXIT,
	
	
	/** ������� �������� � ������� */
	RETURN_OK,
	/** ������ ����������� �������  */
	RETURN_CANCEL,
	/** ������� �� ����������  */
	RETURN_UNKNOWN;

	/** �������� �� ���������� ������ XML ����, � ���������� ���  */
	public static EParserManagerCommands getCommandsFromString(String value) {
		Document document=getXmlFromString(value);
		if(document!=null){
			String commandName=getSubElementAsString(document,"//task/command");
			if(commandName!=null){
				EParserManagerCommands command=EParserManagerCommands.valueOf(commandName);
				command.setArgument(getSubElementAsString(document,"//task/argument"));
				return command;
			}else{
				System.out.println("//task/command not found");
				return EParserManagerCommands.RETURN_UNKNOWN;
			}
		}else{
			System.out.println("XML does not recognized");
			return EParserManagerCommands.RETURN_UNKNOWN;
		}
	}
	
	/** ��������, ������� ��� ������� ������ � ��������/������� */
	private String argument;
	
	/** ��������, ������� ��� ������� ������ � ��������/������� */
	public String getArgument(){
		return this.argument;
	}
	
	/** ��������, ������� ��� ������� ������ � ��������/������� */
	public void setArgument(String argument){
		this.argument=argument;
	}
	
	
	/**
	 * �������� ������ �� ����������� 
	 * @param parentElement - ������������ �������
	 * @param path - ���� � ����������� 
	 * @return ������ ������ ���� �� null
	 */
	private static String getSubElementAsString(Document parentElement, String path){
		String returnValue=null;
		try{
			returnValue=getSubElement(parentElement, path).getTextContent().trim();
		}catch(Exception ex){
			returnValue=null;
		}
		return returnValue;
	}
	
	/** �������� ���������� �� ��������� �������� �� ��������� XPath ����
	 * @param parentElement - ������������ ������� 
	 * @param path - ���� XPath
	 * @return - ��������� Element ��� null
	 */
	private static Element getSubElement(Node parentElement, String path){
		try{
			XPath xpath=XPathFactory.newInstance().newXPath();
			Object returnValue=xpath.evaluate(path, parentElement,XPathConstants.NODE);
			if(returnValue instanceof Element){
				return (Element)returnValue;
			}else{
				return null;
			}
		}catch(Exception ex){
			return null;
		}
	}

	/** 
	 * �������� �� String, ���������� ��� XML ����� ������ Document
	 * @param value ������, ���������� XML �����
	 * @return null, ���� ��������� ������ ��������, ���� �� ��� Document
	 */
	private static Document getXmlFromString(String value){
		Document return_value=null;
		javax.xml.parsers.DocumentBuilderFactory document_builder_factory=javax.xml.parsers.DocumentBuilderFactory.newInstance();
        // ���������� ������������� ���������� Parser-��
        document_builder_factory.setValidating(false);
        try {
            // ��������� �����������
            javax.xml.parsers.DocumentBuilder parser=document_builder_factory.newDocumentBuilder();
            // Parse ��������
            return_value=parser.parse(new ByteArrayInputStream(value.getBytes("UTF-8")));
        }catch(Exception ex){
        	System.err.println("getXmlFromString Exception: "+ex.getMessage());
        }
		return return_value;
	}

	/** �������� XML ������ �� ��������� �������� ��������  */
	public String getXmlString() {
		try{
			Document destination_document=null;
			
			// create empty XML document
			javax.xml.parsers.DocumentBuilderFactory document_builder_factory=javax.xml.parsers.DocumentBuilderFactory.newInstance();
			document_builder_factory.setValidating(false);
			document_builder_factory.setIgnoringComments(true);
			javax.xml.parsers.DocumentBuilder document_builder=document_builder_factory.newDocumentBuilder();
			destination_document=document_builder.newDocument();
			
			Element task=destination_document.createElement("task");
			
			destination_document.appendChild(task);
			
			Element command=destination_document.createElement("command");
			command.setTextContent(this.toString());
			task.appendChild(command);
			
			Element argument=destination_document.createElement("argument");
			argument.setTextContent( (this.getArgument()==null)?"":this.getArgument());
			task.appendChild(argument);
			
			return this.getStringFromXmlDocument(destination_document);
		}catch(Exception ex){
			return null;
		}
	}
	
	private String getStringFromXmlDocument(Document document){
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
	
}
