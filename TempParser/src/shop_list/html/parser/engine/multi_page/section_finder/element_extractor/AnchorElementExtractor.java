package shop_list.html.parser.engine.multi_page.section_finder.element_extractor;

import org.w3c.dom.Element;

import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;

/** ��������� �� �������� A ( anchor ) href ���������  */
public class AnchorElementExtractor implements IUrlFromElementExtractor{

	/** ��������� �� �������� A ( anchor ) href ���������  */
	public AnchorElementExtractor(){
		this(null);
	}
	
	/** ��������� �� �������� A ( anchor ) href ���������  
	 * @param preambula - ����� �� ���������� � �������� ( � ������ ) ������ ( ���� �������� �� �������� ������������� ���� )
	 * */
	public AnchorElementExtractor(String preambula){
		this.preambula=null;
	}
	
	private String preambula=null;
	
	
	/** �������� �� ������ ������ */
	protected boolean isStringEmpty(String value){
		return (value==null)||(value.trim().equals(""));
	}
	
	@Override
	public String getUrlFromElement(Element element) {
		String url=element.getAttribute("href");
		if(isStringEmpty(url))return null;
		if(preambula!=null){
			return preambula+url;
		}else{
			return url;
		}
	}
	
}
