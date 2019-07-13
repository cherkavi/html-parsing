package shop_list.html.parser.engine.multi_page.section_finder.element_extractor;

import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;

/** ������, ������� "�����" ��������� � ������ ������� ��������� ����� �� ����� ��������  */
public abstract class CurrentUrlFromElementExtractor implements IUrlFromElementExtractor{
	private String url=null;
	
	/** ���������� ������� URL  */
	public void setCurrentUrl(String url){
		this.url=url;
	}
	
	/** �������� ������� (������������� {@link #setCurrentUrl(String)} URL ) */
	protected String getCurrentUrl(){
		return this.url;
	}

}
