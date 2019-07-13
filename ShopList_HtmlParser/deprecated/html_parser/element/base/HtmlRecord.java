package html_parser.element.base;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.dom4j.DocumentException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dappit.Dapper.parser.ParserException;

/** �����-�������� ��� ������(Html ������) �� ����� ������ HtmlBlock*/
public class HtmlRecord {
	private String value;
	private XPath xpath=null;
	
	public HtmlRecord(String value){
		this.value=value;
		init();
	}
	
	public HtmlRecord(){
		init();
	}

	private void init(){
		XPathFactory factory=XPathFactory.newInstance();
		this.xpath=factory.newXPath();
	}
	
	@Override
	public String toString(){
		return this.value;
	}
	
	/** ���������� Node �� ��������� XPath ��������� <br>
	 *  <b>Important:  
	 *  <li>  example expression="//td[1]/div[2]" - ������������ ����� ����� ���������</li>
	 *  <li>  example expression="td[1]/div[2]" - ������������ ������� Node</li>
	 *  </b>
	 * @param node - ������������ ���� ������
	 * @param expression - ��������� ��� ������
	 * @return Node ��� null - ���� ��������� ������ ��������
	 * */
	protected Node getNodeByXPath(Node node, String expression){
		try{
			XPathExpression xpathExpression=this.xpath.compile(expression);
			Object object=xpathExpression.evaluate(node,XPathConstants.NODE);
			return (Node)object;
		}catch(Exception ex){
			System.err.println("getNodeByXPath error:"+ex.getMessage());
			return null;
		}
	}
	
	/** ���������� Node �� ��������� XPath ��������� <br>
	 *  <b>Important:  
	 *  <li>  example expression="//td[1]/div[2]" - ������������ ����� ����� ���������</li>
	 *  <li>  example expression="td[1]/div[2]" - ������������ ������� Node</li>
	 *  </b>
	 * @param node - ������������ ���� ������
	 * @param expression - ��������� ��� ������
	 * @return Node ��� null - ���� ��������� ������ ��������
	 * */
	protected NodeList getNodeListByXPath(Node node, String expression){
		try{
			XPathExpression xpathExpression=this.xpath.compile(expression);
			Object object=xpathExpression.evaluate(node,XPathConstants.NODESET);
			return (NodeList)object;
		}catch(Exception ex){
			System.err.println("getNodeByXPath error:"+ex.getMessage());
			return null;
		}
	}
	
	
	/** �������� ���������� �� �������� - ����� XPath ��� ������ � Mozzila Html Parser*/
	protected  Node getSubNode(Node node,
							   String xpathToBlockData) throws IOException, 
							   									DocumentException, 
							   									ParserException, 
							   									ParserConfigurationException, 
							   									XPathExpressionException {
		StringTokenizer token=new StringTokenizer(xpathToBlockData.replaceAll("//", "/"),"/");
		/*XPathExpression expression=field_xpath.compile(xpathToBlockData);
		return (Node)expression.evaluate(doc,XPathConstants.NODE);*/
		return (Node)getNodeFromDocumentByStringTokenizer(node, token);
	}
	
	

	private Node getNodeFromDocumentByStringTokenizer(Node node,
												      StringTokenizer token) {
		Node currentNode = node;
		while (token.hasMoreTokens()) {
			String nextElement = token.nextToken();
			int searchIndex = 1;
			if (nextElement.indexOf('[') > 0) {
				searchIndex = Integer.parseInt(nextElement.substring(nextElement.indexOf('[') + 1, nextElement.indexOf(']')));
				nextElement = nextElement.substring(0, nextElement.indexOf('['));
			}
			if (currentNode.hasChildNodes()) {
				// ���� ��������, �������� �� ��� � XPath ����
				int findIndex = 0; // ������ � ������ /tr[3] - ������ ������� �
								   // �������� ������
				NodeList list = currentNode.getChildNodes();
				for (int counter = 0; counter < list.getLength(); counter++) {
					if (list.item(counter).getNodeName().equals(nextElement)) {
						findIndex++;
						if (findIndex == searchIndex) {
							currentNode = list.item(counter);
							continue;
						}
					}
				}
			} else {
				// ��� ���������, � ���� ��� ����
				return null;
			}
		}
		return currentNode;
	}
	
}
