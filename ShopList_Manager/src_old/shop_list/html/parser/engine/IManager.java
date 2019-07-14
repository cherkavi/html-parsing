package shop_list.html.parser.engine;

import shop_list.html.parser.engine.logger.ILogger;
import shop_list.html.parser.engine.saver.ISaver;

/** ��������� ���������� ��� ��������� ������ �� ���������� �������  */
public interface IManager {
	/** ����� �������� 
	 * @param callback - �����, ������� ������� ������� ����� ��������� ��������
	 * @return
	 * <li> <b>null</b> - ������ ������ </li>
	 * <li> <b>PARSE_SESSION.ID</b> - ���������� ����� </li>
	 * */
	public Integer start(IDetectEndOfParsing callback);

	/** ����� �������� 
	 * @return
	 * <li> <b>null</b> - ����� ������� �����������  </li>
	 * <li> <b>text</b> - �������� ������, ��-�� ������� ����� �� ����������� </li>
	 * */
	public String pause();

	/** ��������� �������� 
	 * @return
	 * <li> <b>null</b> - ��������� ������ </li>
	 * <li> <b>text</b> - �������� ������ ��������� </li>
	 * */
	public String stop();
	
	/** �������� �������� ��������� �������� � ������ ������ ( �� �������� ������� ������� ) 
	 * @return {@link EParseState} 
	 * */
	public EParseState getParseState();
	
	/** ���������� ��������� �������� ( �� �������� ������� ������� ) 
	 * @param {@link EParseState} 
	 * */
	public void setParseState(EParseState state);
	
	/** ���������� ������������ ������ */
	public void setSaver(ISaver saver);
	
	/** ���������� ������  */
	public void setLogger(ILogger logger);
	
	/** ���������� ������ ���� � Mozilla �������  */
	public void setMozillaParserPath(String path);
	
	/** �������� �������� ������� ( �� ������� ����� ����������� ���������� "���������" ������� �������  */
	public String getShopUrlStartPage();
	
	/** �������� ���������� ������������� ��������, �� �������� ���������� ������� */
	public Integer getShopId();
	
	/** �������� ���������� ������������� ������ ��������  */
	public Integer getSessionId();
}
