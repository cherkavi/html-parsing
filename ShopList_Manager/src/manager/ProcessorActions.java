package manager;

import java.util.ArrayList;


import java.util.HashMap;
import java.util.List;

import manager.utility.DirectoryClassLoader;
import manager.utility.LimitedArrayList;

import org.apache.log4j.Logger;

import shop_list.html.parser.engine.EParseState;
import shop_list.html.parser.engine.IDetectEndOfParsing;
import shop_list.html.parser.engine.IManager;
import shop_list.html.parser.engine.logger.ELoggerLevel;
import shop_list.html.parser.engine.logger.ILogger;

import database.functions.DatabaseLogger;
import database.functions.DatabaseProxy;
import database.functions.DatabaseSaverConnection;
import database.wrap_mysql.Current_action;
import database.wrap_mysql.EActionState;
import database.wrap_mysql.Shop_list;

/** обработчик одного действия */
class ProcessorActions implements Runnable, IDetectEndOfParsing{
	private Logger logger=Logger.getLogger(this.getClass());
	private IActionComplete actionComplete;
	private boolean flagRun=false;
	/** поток, который запущен/остановлен для обработки Action */
	private Thread thread;
	/** текущий ACTION, который подлежит обработке */
	private Integer actionId;
	/** пребывающие в данный момент в разработке  */
	private LimitedArrayList<Integer> listOfCurrentActionInParse;
	/** сессии, которые нужно обработать по данному Action */
	private ArrayList<Integer> listOfAvailableCurrentAction=new ArrayList<Integer>();
	/** запущенные на выполнение парсеры  */
	private HashMap<IManager, Integer> managerList=new HashMap<IManager, Integer>();
	
	/** обработчик одного действия 
	 * @param complete - объект, который должен быть оповещен об окончании выполнения действия
	 * @param parseCount - кол-во допустимых одновременно-запущенных парсеров 
	 * */
	public ProcessorActions(IActionComplete complete, int parseCount){
		this.actionComplete=complete;
		this.listOfCurrentActionInParse=new LimitedArrayList<Integer>(parseCount);
	}
	
	/** запустить на выполнение новый Action */
	public boolean startNewAction(Integer actionId){
		if(this.isBusy()==false){
			this.actionId=actionId;
			this.thread=new Thread(this);
			this.thread.start();
			return true;
		}else{
			// in process
			return false;
		}
	}

	/** остановить процесс парсинга  */
	public void stopThread(){
		try{
			DatabaseProxy proxy=new DatabaseProxy();
			proxy.setActionState(actionId, EActionState.STOPPED);
			this.flagRun=false;
			this.thread.interrupt();
			synchronized(this.actionComplete){
				this.actionComplete.actionComplete(actionId, EActionState.STOPPED);
			}
		}catch(Exception ex){};
	}
	
	@Override
	public void run() {
		this.startUp();
		this.flagRun=true;
		Integer currentActionForExecute=null;
		while(flagRun==true){
			currentActionForExecute=null;
			synchronized(this.listOfCurrentActionInParse){
				if(this.listOfCurrentActionInParse.isFull()==false){
					logger.debug("объект не заполнен - можно добавить еще один элемент для выполнения, если он есть");  
					if(this.listOfAvailableCurrentAction.size()>0){
						logger.debug("есть элементы, которые могут быть добавлены ("+this.listOfAvailableCurrentAction.size()+")"); 
						currentActionForExecute=this.listOfAvailableCurrentAction.remove(0);
						if(this.listOfCurrentActionInParse.add(currentActionForExecute)==false){
							currentActionForExecute=null;
						}
					}else{
						logger.debug("нет элементов для добавления в текущий механизм - проверить на завершение работы"); 
						if(this.listOfCurrentActionInParse.isEmpty()==true){
							logger.debug("больше нет элементов для выполнения - список выполнен");
							DatabaseProxy proxy=new DatabaseProxy();
							proxy.setActionState(actionId, EActionState.DONE);
							synchronized(this.actionComplete){
								this.actionComplete.actionComplete(actionId, EActionState.DONE);
							}
							break;// flagRun=false;
						}else{
							logger.debug("элементы еще выполняются");
							try{
								this.listOfCurrentActionInParse.wait();
							}catch(Exception ex){};
						}
					}
				}else{
					logger.debug("объект заполнен - ожидать выполнения");
					try{
						this.listOfCurrentActionInParse.wait();
					}catch(Exception ex){};
				}
			}
			if(currentActionForExecute!=null){
				logger.debug("текущий Action найден:"+currentActionForExecute);
				Integer sessionId=runCurrentAction(currentActionForExecute);
				logger.debug("по текущему Action создан SessionId:"+sessionId);
				synchronized(this.listOfCurrentActionInParse){
					if(sessionId==null){
						this.listOfCurrentActionInParse.remove((Object)currentActionForExecute);
					}
				}
			}
		}
	}
	
	/** запустить указанную сессию на выполнение */
	private Integer runCurrentAction(Integer currentActionId){
		try{
			// FIXME старт отдельно взятого парсера
			DatabaseProxy proxy=new DatabaseProxy();
			this.logger.debug("получить данные из базы данных по currentActionId:"+currentActionId);
			Current_action currentAction=proxy.getCurrentActionById(currentActionId);
			this.logger.debug("получить парсер из указанного каталога");
			// получать Manager на основании имени класса
			IManager manager=getManagerByClassName(currentAction.getParserClassName());
			this.logger.debug("парсер получен:"+manager.getShopUrlStartPage());
			ILogger logger=new DatabaseLogger(ELoggerLevel.ERROR, true);
			// ILogger logger=new ConsoleLogger();
			manager.setLogger(logger);
			manager.setSaver(new DatabaseSaverConnection(logger));
			// manager.setSaver(new ConsoleSaver());
			synchronized (this.managerList) {
				this.managerList.put(manager,currentActionId);
			}
			this.logger.debug("старт парсера ");
			Integer returnValue=manager.start(this);
			if(returnValue!=null){
				proxy.setCurrentActionSessionId(currentActionId, returnValue);
			}
			return returnValue;
		}catch(Exception ex){
			logger.error("runSession Exception: "+ex.getMessage());
			return null;
		}
	}
	
	/** получить управляющий парсер на основании компьютерного магазина */
	@SuppressWarnings("unused")
	private IManager getManagerByShop(Shop_list shop){
		logger.debug("получить все парсеры");
		ArrayList<IManager> list=DirectoryClassLoader.getAvailableShopListParser(Processor.parserClassesDirectory);
		IManager returnValue=null;
		logger.debug("получить парсер на основании стартовой страницы");
		for(IManager manager:list){
			if(manager.getShopUrlStartPage().equalsIgnoreCase(shop.getStart_page())){
				returnValue=manager;
				break;
			}
		}
		return returnValue;
	}
	
	/** получить управляющий парсер на основании имени класса  */
	private IManager getManagerByClassName(String className){
		logger.debug("получить все парсеры");
		ArrayList<IManager> list=DirectoryClassLoader.getAvailableShopListParser(Processor.parserClassesDirectory);
		IManager returnValue=null;
		logger.debug("получить парсер на основании стартовой страницы");
		for(IManager manager:list){
			if(manager.getClass().getName().equals(className)){
				returnValue=manager;
				break;
			}
		}
		return returnValue;
	}
	
	
	
	/** подготовка к первоначальному старту  */
	private void startUp(){
		logger.debug("получить все сессии, которые нужно обработать"); 
		this.listOfAvailableCurrentAction.clear();
		DatabaseProxy proxy=new DatabaseProxy();
		List<Integer> list=proxy.getAllCurrentActionsByAction(this.actionId);
		if(list!=null){
			this.listOfAvailableCurrentAction.addAll(list);
		}
	}

	/** является ли данный объект "в работе" либо же ожидает внешних воздействий */
	public boolean isBusy() {
		try{
			return this.thread.isAlive();
		}catch(NullPointerException npe){
			return false;
		}
	}

	@Override
	public void endParsing(IManager manager, 
						   EParseState parseEndEvent) {
		Integer sessionId=this.managerList.get(manager);
		synchronized (this.managerList) {
			this.managerList.remove(manager);
		}
		synchronized(this.listOfCurrentActionInParse){
			this.listOfCurrentActionInParse.remove((Object)sessionId);
			this.listOfCurrentActionInParse.notify();
		}
		
	}
}
