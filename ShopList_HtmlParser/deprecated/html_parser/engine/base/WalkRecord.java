package html_parser.engine.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.dom4j.DocumentException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dappit.Dapper.parser.MozillaParser;
import com.dappit.Dapper.parser.ParserException;

import html_parser.element.base.HtmlPage;
import html_parser.element.base.HtmlRecord;

/**
 * @author cherkashinv
 *
 */
public abstract class WalkRecord {
	/** �������� � ������� */
	protected HtmlPage page;
	/** Node � ������� */
	protected Node rootNode;
	/** Charset */
	protected String charsetName;
	
	/** ������� ������ �� ��������� HtmlPage - �������� � ������� 
	 * @param page - �������� ��� �������� ������
	 * @param charsetName - ��������� ������ 
	 * @param xpathToBlockData - ���� � ����� � ������� ( ���������� ��������, ����������� ������� �����������, ��������: ���� �������� ���������� � �������, � ������� �������� - TR, �� ������ ����� ������ �� ������� ������� ���� (TBody)
	 * @param useXPath - true - ������������ XPath, false - ����������� ������
	 * @throws Exception - ���� �� ������� �������� ���� � �������� 
	 * */
	public WalkRecord(HtmlPage page, String charsetName, String xpathToBlockData, boolean useXPath) throws Exception{
		this.page=page;
		this.charsetName=charsetName;
		if(useXPath==true){
			this.rootNode=this.getNodeWithDataBlockByXPath(charsetName, xpathToBlockData);
		}else{
			this.rootNode=this.getNodeWithDataBlock(charsetName, xpathToBlockData);
		}
	}
	
	
	/** �������� ��������� ������ ������ �� �������� 
	 * @return ��������� ������, ��� null, ���� ������ ��������� 
	 * */
	public abstract HtmlRecord getNextRecord();
	
	
	/** �������� ����� ������ �� ��������� Reader � ������� ��������� 
	 * @param reader - Reader
	 * @param carsetName - ��������� ��� �������������� � ����� 
	 * */
	protected byte[] getBytes(Reader reader, String charsetName) throws IOException{
		StringBuffer output=new StringBuffer();
		//new InputStreamReader(new FileInputStream(this.pathToFile),charsetName)
		BufferedReader br=new BufferedReader(reader,1024);
		String currentLine=null;
		while((currentLine=br.readLine())!=null){
			output.append(currentLine);
		}
		return output.toString().getBytes(charsetName);
		
	}
	
	
	/**
	 * �������� XML Node � ������ ������, � ������� ��������� ��� ������ 
	 * @param reader - �� �������� 
	 * @param charsetName - ��������� ��� ������ ������ 
	 * @param xpathToBlockData -���� � ���� XML � ����� � �������� 
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParserException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	protected  Node getNodeWithDataBlockByXPath(String charsetName, 
							  			 String xpathToBlockData) throws IOException, 
							  								  DocumentException, 
							  								  ParserException, 
							  								  ParserConfigurationException, 
							  								  XPathExpressionException {
		byte[] data=this.getBytes(this.page.getReader(), charsetName);
		MozillaParser parser = new MozillaParser();
		Document doc = parser.parse( data, charsetName); // windows-1251
		XPathFactory factory=XPathFactory.newInstance();
		XPath field_xpath=factory.newXPath();
		/*XPathExpression expression=field_xpath.compile(xpathToBlockData);
		return (Node)expression.evaluate(doc,XPathConstants.NODE);*/
		return (Node)field_xpath.evaluate(xpathToBlockData, doc, XPathConstants.NODE);
	}

	/**
	 * �������� XML Node � ������ ������, � ������� ��������� ��� ������ 
	 * @param reader - �� �������� 
	 * @param charsetName - ��������� ��� ������ ������ 
	 * @param xpathToBlockData -���� � ���� XML � ����� � �������� 
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParserException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	protected  NodeList getNodeListWithDataBlockByXPath(String charsetName, 
							  			 String xpathToBlockData) throws IOException, 
							  								  DocumentException, 
							  								  ParserException, 
							  								  ParserConfigurationException, 
							  								  XPathExpressionException {
		byte[] data=this.getBytes(this.page.getReader(), charsetName);
		MozillaParser parser = new MozillaParser();
		Document doc = parser.parse( data, charsetName); // windows-1251
		XPathFactory factory=XPathFactory.newInstance();
		XPath field_xpath=factory.newXPath();
		/*XPathExpression expression=field_xpath.compile(xpathToBlockData);
		return (Node)expression.evaluate(doc,XPathConstants.NODE);*/
		return (NodeList)field_xpath.evaluate(xpathToBlockData, doc, XPathConstants.NODESET);
	}
	
	
	protected  Node getNodeWithDataBlock(String charsetName, 
 			 							 String xpathToBlockData) throws IOException, 
 								  DocumentException, 
 								  ParserException, 
 								  ParserConfigurationException, 
 								  XPathExpressionException {
		byte[] data=this.getBytes(this.page.getReader(), charsetName);
		MozillaParser parser = new MozillaParser();
		Document doc = parser.parse( data, charsetName); // windows-1251
		StringTokenizer token=new StringTokenizer(xpathToBlockData.replaceAll("//", "/"),"/");
		
		/*XPathExpression expression=field_xpath.compile(xpathToBlockData);
		return (Node)expression.evaluate(doc,XPathConstants.NODE);*/
		return (Node)getNodeFromDocumentByStringTokenizer(doc, token);
	}

	private Node getNodeFromDocumentByStringTokenizer(Document document, StringTokenizer token){
		Node currentNode=document;
		while(token.hasMoreTokens()){
			String nextElement=token.nextToken();
			int searchIndex=1;
			if(nextElement.indexOf('[')>0){
				searchIndex=Integer.parseInt(nextElement.substring(nextElement.indexOf('[')+1,nextElement.indexOf(']')));
				nextElement=nextElement.substring(0,nextElement.indexOf('['));
			}
			if(currentNode.hasChildNodes()){
				// ���� ��������, �������� �� ��� � XPath ����
				int findIndex=0; // ������ � ������ /tr[3] - ������ ������� � �������� ������
				NodeList list=currentNode.getChildNodes();
				for(int counter=0;counter<list.getLength();counter++){
					if(list.item(counter).getNodeName().equals(nextElement)){
						findIndex++;
						if(findIndex==searchIndex){
							currentNode=list.item(counter);
							continue;
						}
					}
				}
			}else{
				// ��� ���������, � ���� ��� ����
				return null;
			}
		}
		return currentNode;
	}
	
	
	
	/** �������� ��� �������� childName �� ���� ������� �������� node
	 * @param node - ����, ������� ������ ��� ������ ���������
	 * @param childName - �������� ������� ����� ������ 
	 * @return ��������� ��� ��������� ��������� 
	 * */
	protected ArrayList<Node> getAllElementsByName(Node node, String childNodeName){
		ArrayList<Node> list=new ArrayList<Node>();
		putAllElementIntoList(list, node, childNodeName);
		return list;
	}
	
	private void putAllElementIntoList(ArrayList<Node> list, Node node, String childNodeName){
		if(node.getNodeName().equalsIgnoreCase(childNodeName)){
			list.add(node);
		}
		if(node.hasChildNodes()){
			NodeList nodeList=node.getChildNodes();
			for(int counter=0;counter<nodeList.getLength();counter++){
				putAllElementIntoList(list,nodeList.item(counter),childNodeName);
			}
		}
	}

	
}
