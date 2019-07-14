package shop_list.html.parser.engine.logger;

import shop_list.html.parser.engine.IManager;

/** консольный логгер, который все события отображает с помощью вывода на косоль сообщений:
 * <ul>
 * 	<li>TRACE</li>
 * 	<li>DEBUG</li>
 * 	<li>I NFO</li>
 * 	<li>WARN</li>
 * 	<li>ERROR</li>
 * </ul> 
 */
public class ConsoleLogger implements ILogger{

	@Override
	public void debug(IManager owner, String message) {
		// System.out.println("DEBUG "+owner.getClass().getName().toString()+" : "+message);\
		out(levelDebug,owner, message);
	}

	@Override
	public void error(IManager owner, String message) {
		out(levelError,owner, message);
		// System.out.println("ERROR "+owner.getClass().getName().toString()+" : "+message);
	}

	@Override
	public void info(IManager owner, String message) {
		out(levelInfo,owner, message);
		// System.out.println("INFO "+owner.getClass().getName().toString()+" : "+message);
	}

	@Override
	public void trace(IManager owner, String message) {
		out(levelTrace,owner, message);
		// System.out.println("TRACE "+owner.getClass().getName().toString()+" : "+message);
	}

	@Override
	public void warn(IManager owner, String message) {
		out(levelWarn,owner, message);
		// System.out.println("WARN "+owner.getClass().getName().toString()+" : "+message);
	}
	
	private void out(ELoggerLevel level, IManager owner, String message){
		if(currentLevel.isOutput(level)){
			System.out.println(level.toString()+" "+owner.getClass().getName().toString()+" : "+message);
		}
	}

	/** текущий уровень */
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

}
