package shop_list.html.parser.engine.multi_page.section_finder.section_extractor;

/** ������, ������� "�����" ��������� � ������ ������� ��������� ����� �� ����� ��������*/
public abstract class CurrentResourceFromElementExtractor implements IResourceFromElementExtractor{

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
