package shop_list.html.parser.engine.parser;

import java.util.ArrayList;

import org.w3c.dom.Node;

/** ��������� �������� ������� HTML  */
public interface IParser {
	/** �������� org.w3c.Node �� Url
	 * @param urlPath - �����
	 * @param charsetName - ���������
	 * @param xpath - ������ XPath
	 * @return - null - ���� ��������� ������-���� ���� ������
	 * @throws - ���� ��������� ������ ��������
	 */
	public Node getNodeFromUrl(String urlPath, String charsetName, String xpath) throws Exception;
	
	/** �������� �� Node ��� ����������, �������� ���������� Xpath - ����������� ������ ������������ API */
	public Node getNodeFromNode(Node node, String xpath);
	
	/** �������� org.w3c.Node �� Url
	 * @param urlPath - �����
	 * @param charsetName - ���������
	 * @param xpath - ������ XPath
	 * @return - null - ���� ��������� ������-���� ���� ������
	 * @throws - ���� ��������� ������ ��������
	 */
	public ArrayList<Node> getNodeListFromUrl(String urlPath, String charsetName, String xpath) throws Exception;
	
	/** �������� �� Node ��� ����������, �������� ���������� Xpath - ����������� ������ ������������ API */
	public ArrayList<Node> getNodeListFromNode(Node node, String xpath);

	
	/** �������� ��������� ������������� ������ ��� ���-���� ���������� ����
	 * @param node - ���� XML
	 * @param xpath - ���� � ���-���� 
	 * @param defaultValue - �������� ��-���������, � ������ �� ���������� ���-���� 
	 * @return
	 */
	public String getTextContentFromNode(Node node, String xpath, String defaultValue);
	
	/** �������� ���-�� �������� ���������, ������� ����� ��������� ��� ����  
	 * @param element - �������, ������� ����������� �� ��������� �������� ��������� 
	 * @param childTagName - ��� ��������� ��������, �� ������� ����������� ������ �������� 
	 * */
	public int getChildElementCount(Node node, String childTagName);
	
}
