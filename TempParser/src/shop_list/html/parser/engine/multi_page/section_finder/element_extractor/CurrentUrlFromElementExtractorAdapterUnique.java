package shop_list.html.parser.engine.multi_page.section_finder.element_extractor;

/** ������, ������� "�����" ��������� � ������ ������� ��������� ����� �� ����� ��������  
 * <br />
 * ����������� ��������� ����� ������ ������� : {@link #addNewUrl(String)} 
 */
public abstract class CurrentUrlFromElementExtractorAdapterUnique extends CurrentUrlFromElementExtractor{
	private String url=null;
	private UniqueUrlController controller=new UniqueUrlController();

	/** ������, ������� "�����" ��������� � ������ ������� ��������� ����� �� ����� ��������  
	 * <br />
	 * ����������� ��������� ����� ������ ������� : {@link #addNewUrl(String)} 
	 */
	public CurrentUrlFromElementExtractorAdapterUnique(){
	}
	
	/** ���������� ������� URL  */
	public void setCurrentUrl(String url){
		this.url=url;
	}
	
	/** �������� ������� (������������� {@link #setCurrentUrl(String)} URL ) */
	protected String getCurrentUrl(){
		return this.url;
	}

	/**
	 *   �������� ����� URL � ������ ���������� ������� 
	 * @param url - url ��� ���������� 
	 * @return
	 * <ul>
	 * 	<li><b>true</b> - ���������� ������ URL ������� �����������</li>
	 * 	<li><b>true</b> - ������ URL ��� ���� � ������ </li>
	 * </ul>
	 */
	public boolean addNewUrl(String url){
		return this.controller.addNewUrl(url);
	}
}
