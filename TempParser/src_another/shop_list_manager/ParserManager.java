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
 * ����������� ��������� �������� 
 * */
public class ParserManager implements ICommand, Runnable, IDetectEndOfParsing{
	/** ������, ������� ������������ ������ � �������� ��������  */
	private DirectoryClassLoader directoryClassLoader;
	/** �������� �����, ������� ���������� �������� �������� � �������� ���������� �������� */
	private Thread threadMain=null;
	/** ����, ������� "�������" � ��� ��� ����� ���������� ���� ������  */
	private volatile boolean flagRun=false;
	/** ���-�� ������������ ����������� ��������  */
	private int parserExecuted=1;
	/** ��������, ������� ��� ������� � �������� START */
	private String startArgument=null;
	/** ������� ������������ ��-��������� */
	private ELoggerLevel loggerLevel;
	/**   
	 * FIXME start point
	 * ����������� ��������� ��������  
	 * @param pathToDatabase - ������ ���� � ���� ������ 
	 * @param pathToDirectory - ������ ���� � �������� 
	 * @param portInput - ����, ������� ����� ������� ��� ��������� �������
	 * @param parserExecuted - ���-�� ������������ ����������� ��������
	 * @param level - ������� ������������   
	 * */
	public ParserManager(String pathToDatabase, String pathToDirectory, int portInput, int parserExecuted, ELoggerLevel level){
		ConnectorSingleton.pathToDatabase=pathToDatabase;
		loggerLevel=level;
		this.directoryClassLoader=new DirectoryClassLoader(pathToDirectory);
		this.parserExecuted=parserExecuted;
		try{
			new Server(portInput, this);
		}catch(Exception ex){
			debug("�������� ��� �������� ���� ������ ���������"); 
			error("ParserManager run Exception: "+ex.getMessage());
		}
	}
	
	/** ��������� �������  
	 * @param command ( {@link EParserManagerCommands} )
	 * @param argument - �������� ��� �������
	 * */
	private EParserManagerCommands execute(EParserManagerCommands command){
		// IMPORTANT ���������� �� ������� ������ - ����� ������������������;
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
	
	/** ����� ��������  
	 * @return ��������� ���������� ������� 
	 * */
	private EParserManagerCommands startParser(String argument){
		// ���������� �� ������� ������ - ����� ������������������ 
		debug("Execute start Parser Argument:"+argument);
		if(this.inProcess()==false){
			flagRun=true;
			threadMain=new Thread(this);
			this.startArgument=argument;
			threadMain.start();
		}
		return EParserManagerCommands.RETURN_OK;
	}
	
	/** ��������� �� ������ ����� � ��������� �������� */
	private boolean inProcess(){
		if(this.threadMain==null){
			return false;
		}else{
			return this.threadMain.isAlive();
		}
	}
	
	/** ��������� ��������  
	 * @return ��������� ���������� ������� 
	 * */
	private EParserManagerCommands stopParser(String argument){
		// ���������� �� ������� ������ - ����� ������������������ 
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
		debug("�������� ������� �� ���������� �������");
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
		// ��������� ��������� ���������� ACTIONS, ���� ���� ACTIONS.ACTION_STATE==IN_PROCESS - ��������� � STOPPED 
		DatabaseProxy database=new DatabaseProxy();
		/** ������ ������ ��������, ������� ��������� �� ��������  */
		ArrayList<IManager> listOfParser=this.directoryClassLoader.getAllParsers();
		debug("����������, ����� �� ���������� �������, ���� �� ������ ������� ����� �����������");
		int actionsId=0;
		if(this.startArgument!=null){
			debug("����� ������� ����� ���� �������� ( ����� ������ � ������� Action )");
			actionsId=database.getNewActionId();
		}else{
			debug("����� ���������� ������� �� ��������� ��������");
			try{
				actionsId=Integer.parseInt(this.startArgument);
				// debug("�������� ����� ������ ������� � ������� CurrentAction"); - � ������� ACTIONS ������ �� ������, ������ �� ������� ��� ���������� 
				// database.writeNewCurrentAction(listOfParser);
			}catch(Exception ex){};
			debug("����� �������� - ��������� ����� �� ����");
			if((actionsId==0)||(database.isActionsIdExists(actionsId)==false)){
				database.getNewActionId();
				// debug("�������� ����� ������ ������� � ������� CurrentAction"); - � ������� ACTIONS ������ �� ������, ������ �� ������� ��� ���������� 
				// database.writeNewCurrentAction(listOfParser);
			}else{
				// debug("��������� CURRENT_ACTION.ID_ACTION �� ��������� ����� �������� � ��������, ������� ��� � ���� - �������� � ����");  
				// database.checkForNewParsers(actionsId, listOfParser);
				debug("���������� ��� ��������, ������� ���� � ���� ������ � ����� ��������� �������� - �������� ����� ��������");  
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
			debug("��������������� ���������� ��������� ������� �������"); 
			for(int counter=1;counter<this.parserExecuted;counter++){
				IManager nextManager=this.getNextReadyManager(listOfParser, currentManager);
				if(nextManager==null){
					debug("���-�� �������� ��� ������� ������ ��� ����"); 
					break;
				}else{
					set[counter].setParser(nextManager);
					currentManager=nextManager;
				}
			}
			
			debug("������� ���� ���������"); 
			mainCycle: while(flagRun){
				synchronized(shared){
					debug("������ ���� ������� ���������� ");
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
							debug("�������� ������� ��������� ��������, ��������� ���������� "); 
							shared.wait();
						}catch(Exception ex){
						}
						if(flagRun==false){
							break mainCycle;
						}
						debug("��������� ������� �� ��������� �������� ������ �� ��������");
						for(int counter=0;counter<set.length;counter++){
							if(set[counter].getParser()!=null){
								if(   (set[counter].getParser().getParseState().equals(EParseState.DONE_OK))
										||(set[counter].getParser().getParseState().equals(EParseState.DONE_ERROR))
										||(set[counter].getParser().getParseState().equals(EParseState.STOPPED))
										){
									debug("������ ������ �������� ���� ������ (PARSE_SESSION ��������� � ): "+set[counter].getParser().getShopUrlStartPage());
									set[counter].setParser(null);
									debug("����� ��������� ������ ");
									IManager manager=this.getFirstReadyManager(listOfParser);
									if(manager!=null){
										debug("���� ��������� - ��������� ��������� ������:"+manager.getShopUrlStartPage());
										set[counter].setParser(manager);
										set[counter].getParser().setLogger(set[counter].getLogger());
										set[counter].getParser().setSaver(new DatabaseSaverConnection(set[counter].getLogger()));
										Integer sessionId=set[counter].getParser().start(this);
										database.writeToCurrentAction(actionsId, sessionId);
									}else{
										debug("��� ����������, ���� �� �������, ������� ��������� � ��������? ");
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
											debug("��� ���������� �������� - ���������� ACTION");
											database.writeActionAs(actionsId,EActionState.DONE);
											break mainCycle;
										}else{
											debug("���� ���������� ������� ");
										}
									}
								}else{
									// ������ ��������� � ��������� ������ 
								}
							}else{
								// ������ ������ ����� � ������ Composite 
							}
						}
						if(flagRun==false){
							break mainCycle;
						}else{
							// ������� �� �������� ������� �� ���������� ������������ ���� ������ ������� 
							continue sharedSynchronize;
						}
					}
				}
			}
			if(flagRun==false){
				debug("�������� ������� ��������� �������� "); 
				// �������� ������ �������� ��� ������������� 
				database.writeActionAs(actionsId,EActionState.STOPPED);
				// ���������� ��� ������� � ������ ������ ������
				for(int counter=0;counter<set.length;counter++){
					if(set[counter].getParser()!=null){
						set[counter].getParser().stop();
					}
				}
			}
		}else{
			debug("��� �������� ��� ������� ");
		}
	}
	/**  */
	private Object shared=new Object();
	
	/** �������� �� ������ ������ ������� ��������  
	 * @param list - ������ ��������� ����������\
	 * @return null - ���� ������� �� �������  
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
	 * �� ������������: ���� ���������� �������, �������� ����� ������, ������� ����� � ������ ������, �� �� �� ����� ������� ��� ����������� Action 
	 *  �������� ��������� ������� � ���������� ������ 
	 * @param list - ������ ��������� ���������� 
	 * @param manager - (nullable)������� ��������, ����� �������� ����������� ����� 
	 */
	private IManager getNextReadyManager(ArrayList<IManager> list, IManager manager){
		if(manager==null){
			return getFirstReadyManager(list);
		}else{
			IManager returnValue=null;
			mainCycle: for(int counter=0;counter<list.size();counter++){
				if(list.get(counter).equals(manager)){
					// ������ ��������� ������, ����� �������� ����� ��������� ����� 
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
	
	/** ���������� ���������  */
	private void debug(Object message){
		System.out.println("DEBUG "+message);
	}

	/** ���������� ���������  */
	private void error(Object message){
		System.out.println("ERROR "+message);
	}

	@Override
	public void endParsing(IManager manager, EParseState parseEndEvent) {
		debug("������� ������ �� ������ �� �������� �� ��������� ");
		synchronized(shared){
			shared.notify(); 
		};
	}
}

/** �����, ������� �������� ��������� ����������� ������ � ���� ������ ����� ����  
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
