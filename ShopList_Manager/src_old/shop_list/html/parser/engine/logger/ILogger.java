package shop_list.html.parser.engine.logger;

import shop_list.html.parser.engine.IManager;

public interface ILogger {
	/** установить текущий уровень логгирования 
 * <ul>
 * 	<li>TRACE</li>
 * 	<li>DEBUG</li>
 * 	<li>I NFO</li>
 * 	<li>WARN</li>
 * 	<li>ERROR</li>
 * </ul> 
	 * */
	public void setLevel(ELoggerLevel level);
	
	/** трассирующие сообщения  
	 * @param owner - собственник 
	 * @param message - сообщение 
	 */
	public void trace(IManager owner, String message);
	
	/** отладочные сообщения  
	 * @param owner - собственник 
	 * @param message - сообщение 
	 * */
	public void debug(IManager owner, String message);
	
	/** информационные сообщения 
	 * @param owner - собственник 
	 * @param message - сообщение 
	 * */
	public void info(IManager owner, String message);
	
	/** предупреждающие сообщения  
	 * @param owner - собственник 
	 * @param message - сообщение 
	 * */
	public void warn(IManager owner, String message);
	
	/** ошибочные сообщения 
	 * @param owner - собственник 
	 * @param message - сообщение 
	 * */
	public void error(IManager owner, String message);
}
