package shop_list_manager;

import java.util.ArrayList;

import database.DatabaseProxy;
import database.connector.ConnectorSingleton;


import process_exchange.server.ICommand;
import process_exchange.server.Server;
import shop_list.html.parser.engine.EParseState;
import shop_list.html.parser.engine.IDetectEndOfParsing;
import shop_list.html.parser.engine.IManager;
import shop_list.html.parser.engine.logger.DatabaseLogger;
import shop_list.html.parser.engine.logger.ELoggerLevel;
import shop_list.html.parser.engine.logger.ILogger;
import shop_list.html.parser.engine.saver.DatabaseSaverConnection;

/**   
 * Управляющий процессом парсинга 
 * */
public class ParserManager implements ICommand, Runnable, IDetectEndOfParsing{
	/** объект, который обеспечивает работу с классами парсеров  */
	private DirectoryClassLoader directoryClassLoader;
	/** основной поток, который занимается запуском парсинга и ожидания завершения парсинга */
	private Thread threadMain=null;
	/** флаг, который "говорит" о том что поток продолжает свою работу  */
	private volatile boolean flagRun=false;
	/** кол-во одновременно запускаемых парсеров  */
	private int parserExecuted=1;
	/** аргумент, который был передан с командой START */
	private String startArgument=null;
	/** уровень логгирования по-умолчанию */
	private ELoggerLevel loggerLevel;
	/**   
	 * FIXME start point
	 * Управляющий процессом парсинга  
	 * @param pathToDatabase - полный путь к базе данных 
	 * @param pathToDirectory - полный путь к каталогу 
	 * @param portInput - порт, который нужно слушать для получения заданий
	 * @param parserExecuted - кол-во одновременно запускаемых парсеров
	 * @param level - уровень логгирования   
	 * */
	public ParserManager(String pathToDatabase, String pathToDirectory, int portInput, int parserExecuted, ELoggerLevel level){
		ConnectorSingleton.pathToDatabase=pathToDatabase;
		loggerLevel=level;
		this.directoryClassLoader=new DirectoryClassLoader(pathToDirectory);
		this.parserExecuted=parserExecuted;
		try{
			new Server(portInput, this);
		}catch(Exception ex){
			debug("возможно уже запущена одна версия программы"); 
			error("ParserManager run Exception: "+ex.getMessage());
		}
	}
	
	/** выполнить команду  
	 * @param command ( {@link EParserManagerCommands} )
	 * @param argument - аргумент для команды
	 * */
	private EParserManagerCommands execute(EParserManagerCommands command){
		// IMPORTANT вызывается из другого потока - нужна потокобезопасность;
		if(command!=null){
			debug("EParserManagerCommands:"+command.toString());
			switch(command){
				case COMMAND_PARSE_START: {
					return this.startParser(command.getArgument());
				}
				case COMMAND_PARSE_STOP: {
					return this.stopParser(command.getArgument());
				}
				case COMMAND_GET_STATE:{
					if(this.inProcess()){
						EParserManagerCommands returnCommand=EParserManagerCommands.RETURN_OK;
						returnCommand.setArgument("IN_PROCESS");
						return returnCommand;
					}else{
						EParserManagerCommands returnCommand=EParserManagerCommands.RETURN_OK;
						returnCommand.setArgument("WAIT_FOR_START");
						return returnCommand;
					}
				}
				case COMMAND_EXIT:{
					new Thread(){
						public void run(){
							try{
								Thread.sleep(3000);
								debug("prorgam end");
								System.exit(0);
							}catch(Exception ex){};
						}
					}.start();
					return EParserManagerCommands.RETURN_OK;
				}
				default:
					return EParserManagerCommands.RETURN_UNKNOWN;
			}
		}else{
			return EParserManagerCommands.RETURN_UNKNOWN;
		}
	}
	
	/** старт парсинга  
	 * @return результат выполнения команды 
	 * */
	private EParserManagerCommands startParser(String argument){
		// вызывается из другого потока - нужна потокобезопасность 
		debug("Execute start Parser Argument:"+argument);
		if(this.inProcess()==false){
			flagRun=true;
			threadMain=new Thread(this);
			this.startArgument=argument;
			threadMain.start();
		}
		return EParserManagerCommands.RETURN_OK;
	}
	
	/** находится ли данный поток в состоянии парсинга */
	private boolean inProcess(){
		if(this.threadMain==null){
			return false;
		}else{
			return this.threadMain.isAlive();
		}
	}
	
	/** остановка парсинга  
	 * @return результат выполнения команды 
	 * */
	private EParserManagerCommands stopParser(String argument){
		// вызывается из другого потока - нужна потокобезопасность 
		debug("Execute stop Parser Argument:"+argument);
		if(this.inProcess()==true){
			flagRun=false;
			synchronized(shared){
				shared.notify();
			}
			try{
				this.threadMain.interrupt();
			}catch(SecurityException se){};
		}
		return EParserManagerCommands.RETURN_OK;
	}

	@Override
	public String execute(String value) {
		debug("получена команда от удаленного сервиса");
		EParserManagerCommands command=EParserManagerCommands.getCommandsFromString(value);
		return this.execute(command).getXmlString();
	}
	
	public static void main(String[] args){
		System.out.println("begin");
		new ParserManager("D:\\eclipse_workspace\\TempParser\\database\\SHOP_LIST_PARSE.GDB","d:\\temp\\shops\\",2010,3, ELoggerLevel.WARN);
		System.out.println("-end-");
	}

	@Override
	public void run() {
		// проверить состояния предыдущих ACTIONS, если были ACTIONS.ACTION_STATE==IN_PROCESS - перевести в STOPPED 
		DatabaseProxy database=new DatabaseProxy();
		/** полный список парсеров, которые прочитаны из каталога  */
		ArrayList<IManager> listOfParser=this.directoryClassLoader.getAllParsers();
		debug("определить, нужно ли продолжать парсинг, либо же заново сделать старт предыдущего");
		int actionsId=0;
		if(this.startArgument!=null){
			debug("нужно создать новый этап парсинга ( новую запись в таблице Action )");
			actionsId=database.getNewActionId();
		}else{
			debug("нужно продолжить парсинг на указанном элементе");
			try{
				actionsId=Integer.parseInt(this.startArgument);
				// debug("записать новую группу записей в таблицу CurrentAction"); - в таблице ACTIONS только те записи, сессии по которым уже отработаны 
				// database.writeNewCurrentAction(listOfParser);
			}catch(Exception ex){};
			debug("номер прочитан - проверить номер по базе");
			if((actionsId==0)||(database.isActionsIdExists(actionsId)==false)){
				database.getNewActionId();
				// debug("записать новую группу записей в таблицу CurrentAction"); - в таблице ACTIONS только те записи, сессии по которым уже отработаны 
				// database.writeNewCurrentAction(listOfParser);
			}else{
				// debug("проверить CURRENT_ACTION.ID_ACTION на появление новых парсеров в каталоге, которых нет в базе - дописать в базу");  
				// database.checkForNewParsers(actionsId, listOfParser);
				debug("проставить для парсеров, которые есть в базе данных и имеют состояния парсинга - значения этого парсинга");  
				database.setParserState(actionsId, listOfParser);
			}
		}
		/** */
		Composite[] set=new Composite[this.parserExecuted];
		for(int counter=0;counter<this.parserExecuted;counter++){
			set[counter]=new Composite();
			set[counter].setLogger(new DatabaseLogger(true));
			set[counter].getLogger().setLevel(this.loggerLevel);
		}
		IManager currentManager=this.getFirstReadyManager(listOfParser);
		if(currentManager!=null){
			set[0].setParser(currentManager);
			debug("предварительное наполнение парсерами текущей очереди"); 
			for(int counter=1;counter<this.parserExecuted;counter++){
				IManager nextManager=this.getNextReadyManager(listOfParser, currentManager);
				if(nextManager==null){
					debug("кол-во парсеров для запуска меньше чем есть"); 
					break;
				}else{
					set[counter].setParser(nextManager);
					currentManager=nextManager;
				}
			}
			
			debug("рабочее тело программы"); 
			mainCycle: while(flagRun){
				synchronized(shared){
					debug("запуск всех текущих менеджеров ");
					for(int counter=0;counter<set.length;counter++){
						if(set[counter].getParser()!=null){
							set[counter].getParser().setLogger(set[counter].getLogger());
							set[counter].getParser().setSaver(new DatabaseSaverConnection(set[counter].getLogger()));
							Integer sessionId=set[counter].getParser().start(this);
							database.writeToCurrentAction(actionsId, sessionId);
						}
					}
					
					sharedSynchronize: 
					while(true){
						try{
							debug("ожидание сигнала окончания парсинга, отпустить блокировку "); 
							shared.wait();
						}catch(Exception ex){
						}
						if(flagRun==false){
							break mainCycle;
						}
						debug("проверить очередь на окончание парсинга одного из парсеров");
						for(int counter=0;counter<set.length;counter++){
							if(set[counter].getParser()!=null){
								if(   (set[counter].getParser().getParseState().equals(EParseState.DONE_OK))
										||(set[counter].getParser().getParseState().equals(EParseState.DONE_ERROR))
										||(set[counter].getParser().getParseState().equals(EParseState.STOPPED))
										){
									debug("данный парсер закончил свою работу (PARSE_SESSION результат в ): "+set[counter].getParser().getShopUrlStartPage());
									set[counter].setParser(null);
									debug("найти очередной парсер ");
									IManager manager=this.getFirstReadyManager(listOfParser);
									if(manager!=null){
										debug("есть следующий - запустить следующий парсер:"+manager.getShopUrlStartPage());
										set[counter].setParser(manager);
										set[counter].getParser().setLogger(set[counter].getLogger());
										set[counter].getParser().setSaver(new DatabaseSaverConnection(set[counter].getLogger()));
										Integer sessionId=set[counter].getParser().start(this);
										database.writeToCurrentAction(actionsId, sessionId);
									}else{
										debug("нет следующего, есть ли парсеры, которые находятся в процессе? ");
										boolean returnValue=false;
										for(int index=0;index<set.length;index++){
											if(set[index].getParser()!=null){
												if(set[index].getParser().getParseState().equals(EParseState.PROCESS)){
													returnValue=true;
													break;
												}
											}
										}
										if(returnValue==false){
											debug("нет работающих парсеров - завершение ACTION");
											database.writeActionAs(actionsId,EActionState.DONE);
											break mainCycle;
										}else{
											debug("есть запущенные парсеры ");
										}
									}
								}else{
									// парсер находится в состоянии работы 
								}
							}else{
								// данная ячейка пуста в наборе Composite 
							}
						}
						if(flagRun==false){
							break mainCycle;
						}else{
							// перейти на ожидание сигнала от очередного закончившего свою работу парсера 
							continue sharedSynchronize;
						}
					}
				}
			}
			if(flagRun==false){
				debug("получена команда остановки парсинга "); 
				// записать данное действие как остановленное 
				database.writeActionAs(actionsId,EActionState.STOPPED);
				// остановить все рабочие в данный момент потоки
				for(int counter=0;counter<set.length;counter++){
					if(set[counter].getParser()!=null){
						set[counter].getParser().stop();
					}
				}
			}
		}else{
			debug("Нет парсеров для запуска ");
		}
	}
	/**  */
	private Object shared=new Object();
	
	/** получить из списка первый готовый менеджер  
	 * @param list - список доступных менеджеров\
	 * @return null - если таковые не найдены  
	 * */
	private IManager getFirstReadyManager(ArrayList<IManager> list){
		if(list!=null){
			for(int counter=0;counter<list.size();counter++){
				if(list.get(counter).getParseState().equals(EParseState.READY)){
					return list.get(counter);
				}
			}
			return null;
		}else{
			return null;
		}
	}
	
	/** 
	 * не используется: если остановить процесс, добавить новый парсер, который будет в начале списка, то он не будет запущен при перезапуске Action 
	 *  получить следующий готовый к выполнению парсер 
	 * @param list - список доступных менеджеров 
	 * @param manager - (nullable)текущий менеджер, после которого производить поиск 
	 */
	private IManager getNextReadyManager(ArrayList<IManager> list, IManager manager){
		if(manager==null){
			return getFirstReadyManager(list);
		}else{
			IManager returnValue=null;
			mainCycle: for(int counter=0;counter<list.size();counter++){
				if(list.get(counter).equals(manager)){
					// найден стартовый объект, после которого нужно проводить поиск 
					for(int index=counter+1;index<list.size();index++){
						if(list.get(index).getParseState().equals(EParseState.READY)){
							returnValue=list.get(index);
							break mainCycle;
						}
					}
				}
			}
			return returnValue;
		}
	}
	
	/** отладочное сообщение  */
	private void debug(Object message){
		System.out.println("DEBUG "+message);
	}

	/** отладочное сообщение  */
	private void error(Object message){
		System.out.println("ERROR "+message);
	}

	@Override
	public void endParsing(IManager manager, EParseState parseEndEvent) {
		debug("получен сигнал от одного из парсеров об окончании ");
		synchronized(shared){
			shared.notify(); 
		};
	}
}

/** класс, который содержит некоторые необходимые классы в виде одного блока кода  
 * <ul>
 * 	<li>IManager</li>
 * 	<li>ILogger</li>
 * 	<li>ISaver</li>
 * </ul>
 * */
class Composite{
	private IManager parser;
	private ILogger logger;

	public IManager getParser() {
		return parser;
	}
	public void setParser(IManager parser) {
		this.parser = parser;
	}
	public ILogger getLogger() {
		return logger;
	}
	public void setLogger(ILogger logger) {
		this.logger = logger;
	}
}
