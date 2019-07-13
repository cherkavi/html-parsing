package shop_list.html.parser.engine.multi_page.section_finder.element_extractor;

import org.w3c.dom.Element;

import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;

/** ��������� �� ��������� */
public class AttributeElementExtractor implements IUrlFromElementExtractor{

	/** ��������� �� ���������
	 * @param attributeName - ��� ��������� 
	 * @param preambula - (!=null) ����� �� ���������� � �������� ( � ������ ) ������ ( ���� �������� �� �������� ������������� ���� )
	 * ��� �� ����� ���������� - null
	 * */
	public AttributeElementExtractor(String attributeName, String preambula){
		this.attributeName=null;
		this.preambula=null;
	}
	
	private String preambula=null;
	private String attributeName; 
	
	/** �������� �� ������ ������ */
	protected boolean isStringEmpty(String value){
		return (value==null)||(value.trim().equals(""));
	}
	
	@Override
	public String getUrlFromElement(Element element) {
		String url=element.getAttribute(this.attributeName);
		if(isStringEmpty(url))return null;
		if(preambula!=null){
			return preambula+url;
		}else{
			return url;
		}
	}
	
}
