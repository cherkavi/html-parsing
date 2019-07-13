package shop_list.html.parser.engine.saver;

import shop_list.html.parser.engine.ESessionResult;
import shop_list.html.parser.engine.record.Record;

/** ��������� ��� ���������� ���������� ������ �� ��������� ������� */
public interface ISaver {
	/** �������� ���������� ��� ��������  �� ��������� ������ URL
	 * @param shopUrl - URL ��������
	 * @return ��� �� ���� ������ �� ������� �������� 
	 * */
	public int getShopId(String shopUrl);
	
	/** ����� ������ ����� ��������
	 * @param idShop - ���������� ��� ��������, �� �������� ��������� �������   
	 * @param description - �������� ������� ��������
	 * @return 
	 * <li><b>value</b> - ���������� ����� ������ ������ </li>
	 * <li><b>null</b> - ������ �������� </li>
	 * */
	public Integer startNewSession(int shopId, String description);
	
	/** �������� ���������� ��� ������ �� ��������� �����  
	 * @param sectionName - ��� ������ 
	 * */
	public Integer getSectionId(String sectionName);
	
	/** 
	 * ��������� ������ �� ���������� ������ 
	 * @param sessionId - ����� ������ 
	 * @param section - ������, ������� �������� ������ ������ 
	 * @param record - ������, ������� ������ ���� ��������� 
	 * @return 
	 * <li><b>true</b> - ������ ������� ������� </li>
	 * <li><b>false</b> - ������ �������� </li>
	 */
	public boolean saveRecord(Integer sessionId,Integer sectionId, Record record);
	
	/** �������� ���� ��������� ������ ������  
	 * @param sessionId - ���������� ��� ����������� ������
	 * @param result ��������� ������ ������ 
	 * */
	public boolean endSession(Integer sessionId, ESessionResult result);
}
