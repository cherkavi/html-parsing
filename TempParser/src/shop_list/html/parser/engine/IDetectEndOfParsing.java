package shop_list.html.parser.engine;

///** ��������� �������� */
public interface IDetectEndOfParsing {
	/** ����� ������� ��������� �������� 
	 * @param manager - ������, ������� ���������� �������
	 * @param parseEndEvent - ��������� ��������� ��������
	 * */
	public void endParsing(IManager manager, EParseState parseEndEvent);
}
