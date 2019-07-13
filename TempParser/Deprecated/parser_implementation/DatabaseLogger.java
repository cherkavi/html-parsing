package parser_implementation;

import java.sql.PreparedStatement;

import shop_list.html.parser.engine.IManager;
import shop_list.html.parser.engine.logger.ELoggerLevel;
import shop_list.html.parser.engine.logger.ILogger;

/** ���������� ������, ������� ��� ������� ���������� � ������� ������ �� ������ ���������:
 * <ul>
 * 	<li>TRACE</li>
 * 	<li>DEBUG</li>
 * 	<li>INFO.</li>
 * 	<li>WARN</li>
 * 	<li>ERROR</li>
 * </ul> 
 */
public class DatabaseLogger implements ILogger{
	private DatabaseProxy proxy=new DatabaseProxy();
	
	@Override
	public void debug(IManager owner, String message) {
		out(levelDebug,owner, message);
	}

	@Override
	public void error(IManager owner, String message) {
		out(levelError,owner, message);
	}

	@Override
	public void info(IManager owner, String message) {
		out(levelInfo,owner, message);
	}

	@Override
	public void trace(IManager owner, String message) {
		out(levelTrace,owner, message);
	}

	@Override
	public void warn(IManager owner, String message) {
		out(levelWarn,owner, message);
	}
	
	
	
	private void out(ELoggerLevel level, Object owner, String message){
		if(currentLevel.isOutput(level)){
			if(owner instanceof IManager){
				if(this.statement==null)this.statement=this.initDatabaseTools();
				if(this.saveValue(this.statement, ((IManager)owner).getSessionId(),level,message)==false){
					this.statement=this.initDatabaseTools();
					// try again
					if(this.statement!=null){
						this.saveValue(this.statement, ((IManager)owner).getSessionId(),level,message);
					}else{
						System.err.println("DatabaseLogger Error: ������ ������������� ������������ ���� ������ ");
					}
				}
			}else{
				System.err.println("Logger was called by an unknown object:"+message);
			}
			((IManager)owner).getSessionId();
		}
	}
	
	/** ������������������� ����������� ��� ������� � ���� ������ - {@link #statement} 
	 * @return
	 * <ul>
	 * 	<li> <b>true</b> - ����������� ������������ ������������������� </li>
	 * 	<li> <b>false</b> - ����������� �� ������������������� </li>
	 * </ul>
	 * */
	private PreparedStatement initDatabaseTools() {
		return proxy.loggerGetPreparedStatement();
	}

	/** ��������� ��������� �������� 
	 * @param statement - �������������� ������ ��� ������ � ���� ������ 
	 * @param sessionId - ���������� ����� ������ 
	 * @param loggerLevel - ������� ������������ 
	 * @param prepareStringValue - ������ ��� ������� � ���� ������ 
	 * @return
	 */
	private boolean saveValue(PreparedStatement statement, Integer sessionId, ELoggerLevel loggerLevel, String prepareStringValue) {
		if(proxy.loggerSaveValue(statement, sessionId, loggerLevel, prepareStringValue)==true){
			if(this.repeatToConsole==true){
				System.out.println(prepareStringValue);
			}
			return true;
		}else{
			return false;
		}
	}

	
	/** �������������� �������������� Statement ��� ������ �������� */
	private PreparedStatement statement;

	/** ������� ������� */
	private ELoggerLevel currentLevel=ELoggerLevel.TRACE;
	
	private ELoggerLevel levelTrace=ELoggerLevel.TRACE;
	private ELoggerLevel levelDebug=ELoggerLevel.DEBUG;
	private ELoggerLevel levelInfo=ELoggerLevel.INFO;
	private ELoggerLevel levelWarn=ELoggerLevel.WARN;
	private ELoggerLevel levelError=ELoggerLevel.ERROR;
	
	@Override
	public void setLevel(ELoggerLevel level) {
		this.currentLevel=level;
	}

	/** ���� ������������� ���������� ������ ��������� �� ������� ����� */
	private boolean repeatToConsole=false;
	
	/** ������ ��� ���������� ������ � ���� ������ 
	 * @param repeatToConsole - ����� �� ����������� ������ �� �������
	 *  */
	public DatabaseLogger(boolean repeatToConsole){
		this.repeatToConsole=repeatToConsole;
	}
}
