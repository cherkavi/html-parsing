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

/** ���������� ������ �������� */
class ProcessorActions implements Runnable, IDetectEndOfParsing{
	private Logger logger=Logger.getLogger(this.getClass());
	private IActionComplete actionComplete;
	private boolean flagRun=false;
	/** �����, ������� �������/���������� ��� ��������� Action */
	private Thread thread;
	/** ������� ACTION, ������� �������� ��������� */
	private Integer actionId;
	/** ����������� � ������ ������ � ����������  */
	private LimitedArrayList<Integer> listOfCurrentActionInParse;
	/** ������, ������� ����� ���������� �� ������� Action */
	private ArrayList<Integer> listOfAvailableCurrentAction=new ArrayList<Integer>();
	/** ���������� �� ���������� �������  */
	private HashMap<IManager, Integer> managerList=new HashMap<IManager, Integer>();
	
	/** ���������� ������ �������� 
	 * @param complete - ������, ������� ������ ���� �������� �� ��������� ���������� ��������
	 * @param parseCount - ���-�� ���������� ������������-���������� �������� 
	 * */
	public ProcessorActions(IActionComplete complete, int parseCount){
		this.actionComplete=complete;
		this.listOfCurrentActionInParse=new LimitedArrayList<Integer>(parseCount);
	}
	
	/** ��������� �� ���������� ����� Action */
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

	/** ���������� ������� ��������  */
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
					logger.debug("������ �� �������� - ����� �������� ��� ���� ������� ��� ����������, ���� �� ����");  
					if(this.listOfAvailableCurrentAction.size()>0){
						logger.debug("���� ��������, ������� ����� ���� ��������� ("+this.listOfAvailableCurrentAction.size()+")"); 
						currentActionForExecute=this.listOfAvailableCurrentAction.remove(0);
						if(this.listOfCurrentActionInParse.add(currentActionForExecute)==false){
							currentActionForExecute=null;
						}
					}else{
						logger.debug("��� ��������� ��� ���������� � ������� �������� - ��������� �� ���������� ������"); 
						if(this.listOfCurrentActionInParse.isEmpty()==true){
							logger.debug("������ ��� ��������� ��� ���������� - ������ ��������");
							DatabaseProxy proxy=new DatabaseProxy();
							proxy.setActionState(actionId, EActionState.DONE);
							synchronized(this.actionComplete){
								this.actionComplete.actionComplete(actionId, EActionState.DONE);
							}
							break;// flagRun=false;
						}else{
							logger.debug("�������� ��� �����������");
							try{
								this.listOfCurrentActionInParse.wait();
							}catch(Exception ex){};
						}
					}
				}else{
					logger.debug("������ �������� - ������� ����������");
					try{
						this.listOfCurrentActionInParse.wait();
					}catch(Exception ex){};
				}
			}
			if(currentActionForExecute!=null){
				logger.debug("������� Action ������:"+currentActionForExecute);
				Integer sessionId=runCurrentAction(currentActionForExecute);
				logger.debug("�� �������� Action ������ SessionId:"+sessionId);
				synchronized(this.listOfCurrentActionInParse){
					if(sessionId==null){
						this.listOfCurrentActionInParse.remove((Object)currentActionForExecute);
					}
				}
			}
		}
	}
	
	/** ��������� ��������� ������ �� ���������� */
	private Integer runCurrentAction(Integer currentActionId){
		try{
			// FIXME ����� �������� ������� �������
			DatabaseProxy proxy=new DatabaseProxy();
			this.logger.debug("�������� ������ �� ���� ������ �� currentActionId:"+currentActionId);
			Current_action currentAction=proxy.getCurrentActionById(currentActionId);
			this.logger.debug("�������� ������ �� ���������� ��������");
			// �������� Manager �� ��������� ����� ������
			IManager manager=getManagerByClassName(currentAction.getParserClassName());
			this.logger.debug("������ �������:"+manager.getShopUrlStartPage());
			ILogger logger=new DatabaseLogger(ELoggerLevel.ERROR, true);
			// ILogger logger=new ConsoleLogger();
			manager.setLogger(logger);
			manager.setSaver(new DatabaseSaverConnection(logger));
			// manager.setSaver(new ConsoleSaver());
			synchronized (this.managerList) {
				this.managerList.put(manager,currentActionId);
			}
			this.logger.debug("����� ������� ");
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
	
	/** �������� ����������� ������ �� ��������� ������������� �������� */
	@SuppressWarnings("unused")
	private IManager getManagerByShop(Shop_list shop){
		logger.debug("�������� ��� �������");
		ArrayList<IManager> list=DirectoryClassLoader.getAvailableShopListParser(Processor.parserClassesDirectory);
		IManager returnValue=null;
		logger.debug("�������� ������ �� ��������� ��������� ��������");
		for(IManager manager:list){
			if(manager.getShopUrlStartPage().equalsIgnoreCase(shop.getStart_page())){
				returnValue=manager;
				break;
			}
		}
		return returnValue;
	}
	
	/** �������� ����������� ������ �� ��������� ����� ������  */
	private IManager getManagerByClassName(String className){
		logger.debug("�������� ��� �������");
		ArrayList<IManager> list=DirectoryClassLoader.getAvailableShopListParser(Processor.parserClassesDirectory);
		IManager returnValue=null;
		logger.debug("�������� ������ �� ��������� ��������� ��������");
		for(IManager manager:list){
			if(manager.getClass().getName().equals(className)){
				returnValue=manager;
				break;
			}
		}
		return returnValue;
	}
	
	
	
	/** ���������� � ��������������� ������  */
	private void startUp(){
		logger.debug("�������� ��� ������, ������� ����� ����������"); 
		this.listOfAvailableCurrentAction.clear();
		DatabaseProxy proxy=new DatabaseProxy();
		List<Integer> list=proxy.getAllCurrentActionsByAction(this.actionId);
		if(list!=null){
			this.listOfAvailableCurrentAction.addAll(list);
		}
	}

	/** �������� �� ������ ������ "� ������" ���� �� ������� ������� ����������� */
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
