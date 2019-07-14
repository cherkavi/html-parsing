package shop_list.html.parser.engine.logger;

import shop_list.html.parser.engine.IManager;

public interface ILogger {
	/** ���������� ������� ������� ������������ 
 * <ul>
 * 	<li>TRACE</li>
 * 	<li>DEBUG</li>
 * 	<li>I NFO</li>
 * 	<li>WARN</li>
 * 	<li>ERROR</li>
 * </ul> 
	 * */
	public void setLevel(ELoggerLevel level);
	
	/** ������������ ���������  
	 * @param owner - ����������� 
	 * @param message - ��������� 
	 */
	public void trace(IManager owner, String message);
	
	/** ���������� ���������  
	 * @param owner - ����������� 
	 * @param message - ��������� 
	 * */
	public void debug(IManager owner, String message);
	
	/** �������������� ��������� 
	 * @param owner - ����������� 
	 * @param message - ��������� 
	 * */
	public void info(IManager owner, String message);
	
	/** ��������������� ���������  
	 * @param owner - ����������� 
	 * @param message - ��������� 
	 * */
	public void warn(IManager owner, String message);
	
	/** ��������� ��������� 
	 * @param owner - ����������� 
	 * @param message - ��������� 
	 * */
	public void error(IManager owner, String message);
}
