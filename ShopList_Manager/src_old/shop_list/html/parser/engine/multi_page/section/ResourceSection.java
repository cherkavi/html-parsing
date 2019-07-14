package shop_list.html.parser.engine.multi_page.section;

/** ������/������ ��� �����, ������� ��������� �� ������ �������, ������� �������� ��������  */
public abstract class ResourceSection {
	/** �������� ������� HTML �������� */
	private String basePage;
	/** ������ ��� ������ */
	private String name;
	/** ������ �� ������ �������� ������ */
	private String url;
	
	/** ������/������ ��� �����, ������� ��������� �� ������ �������, ������� �������� �������� (�������, � ���� �������, �������� ������)
	 * @param name - ������������ ������� ( ������ )
	 * @param url - ������ ����� ������ �������� ��� �������� 
	 *  */
	public ResourceSection(String name, String url ){
		this.name=name;
		this.url=url;
	}

	/** ������/������ ��� �����, ������� ��������� �� ������ �������, ������� �������� �������� (�������, � ���� �������, �������� ������)
	 * @param basePage - �������� ������� HTML �������� 
	 * @param name - ������������ ������� ( ������ )
	 * @param url - ������ ����� ������ �������� ��� �������� 
	 *  */
	public ResourceSection(String basePage, String name, String url ){
		this.name=name;
		this.url=url;
	}
	
	/** �������� ��� ������� */
	public String getName() {
		return name;
	}

	/** �������� ������ ������ �� ������  */
	public String getUrl() {
		return url;
	}
	
	/** �������� HTML-����� ������� �������� */
	public String getBasePage(){
		return this.basePage;
	}
	
	/** �������� html-����� ��������� ��������  */
	public abstract String getUrlToNextPage();
}
