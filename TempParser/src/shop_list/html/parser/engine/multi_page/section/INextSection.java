package shop_list.html.parser.engine.multi_page.section;

/** ��������� ��� ��������� ��������� ��������  */
public interface INextSection {
	/** �������� html-����� ��������� ��������  */
	public String getUrlToNextPage();
	
	/** �������� ������ ��� ������  */
	public String getName();
	
	/** �������� ����� ����� � ������ - ������ ��������  */
	public String getUrl();
}
