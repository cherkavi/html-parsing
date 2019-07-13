package shop_list.html.parser.engine.multi_page.section_finder;

import java.util.ArrayList;

import org.w3c.dom.Node;

import shop_list.html.parser.engine.parser.IParser;

/** �������� ������ �� ��������� URL ������ */
public class NodeListFinderByUrl {
	private IParser parser;
	private String charset;
	private String[] xpath;
	
	/** �������� ������ �� ��������� URL ������ 
	 * @param parser - ������ 
	 * @param charset - ��������� �������� 
	 * @param xpath - ���� XPath, ������� ������ ���� �������� � ����������
	 */
	public NodeListFinderByUrl(IParser parser, String charset, String ... xpath){
		this.parser=parser;
		this.charset=charset;
		this.xpath=xpath;
	}
	
	/** �������� ������ �� ��������� URL  */
	public ArrayList<Node> getNodeListByUrl(String url){
		ArrayList<Node> returnValue=new ArrayList<Node>();
		try{
			if(this.xpath.length==1){
				return this.parser.getNodeListFromUrl(url, charset, xpath[0]);
			}else{
				for(int index=0;index<this.xpath.length;index++){
					ArrayList<Node> currentValue=this.parser.getNodeListFromUrl(url, charset, xpath[index]);
					if(currentValue!=null){
						returnValue.addAll(currentValue);
					}
				}
				return returnValue;
			}
		}catch(Exception ex){
			return null;
		}
	}
}
