package html_parser.page;

/** �����, ������� "�����" ��� ��������� ������ ��� ��������� ��������� ��������, ��� �������� ������������� ������������ ��������  */
public abstract class PageWalkerAware {
	/** ������ �� ��������, ������� ����� ����� � �������
	 * @param number - ����� ��������, ������� ������ ���� 
	 * @return ������ ������ �� ������������ ����� 
	 */
	public abstract String getUrl(int number);
	
	/** ��������� ����� �������� Url ��� ������ ������ 
	 * @param baseHref - ����� �������� URL 
	 * */
	public abstract void reset(String baseHref);
}
