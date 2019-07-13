package shop_list.html.parser.engine.multi_page.section;

/** ������/������ ��� �����, ������� ��������� �� ������ �������, ������� �������� �������� (�������, � ���� �������, �������� ������)
 * <br><b>������� ���� ������� - ��������� ��������� ������ ��������� ( ������� �������� ������ )</b>
 *  */
public abstract class NextSection implements INextSection {
	/** �������� ������� HTML �������� */
	private String basePage;
	/** ������ ��� ������ */
	private String name;
	/** ������ �� ������ �������� ������ */
	private String url;
	
	/** ������/������ ��� �����, ������� ��������� �� ������ �������, ������� �������� �������� (�������, � ���� �������, �������� ������)
	 * <br><b>������� ���� ������� - ��������� ��������� ������ ��������� ( ������� �������� ������ )</b>
	 * @param name - ������������ ������� ( ������ )
	 * @param url - ������ ����� ������ �������� ��� �������� 
	 *  */
	public NextSection(String name, String url ){
		this.name=name;
		if(this.name!=null)this.name=this.name.trim();
		this.url=url;
	}

	/** ������/������ ��� �����, ������� ��������� �� ������ �������, ������� �������� �������� (�������, � ���� �������, �������� ������)
	 * @param basePage - �������� ������� HTML �������� 
	 * @param name - ������������ ������� ( ������ )
	 * @param url - ������ ����� ������ �������� ��� �������� 
	 *  */
	public NextSection(String basePage, String name, String url ){
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
	
	/** ���������� ������� ������ �� �������� */
	public void setUrl(String url){
		this.url=url;
	}
	
	/** �������� HTML-����� ������� �������� */
	public String getBasePage(){
		return this.basePage;
	}
	
	/** �������� html-����� ��������� ��������  */
	@Override
	public abstract String getUrlToNextPage();
}
