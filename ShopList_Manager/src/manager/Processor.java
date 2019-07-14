package manager;

import java.util.List;

import manager.utility.UniqueIntegerArrayList;

import org.apache.log4j.Logger;

import database.functions.DatabaseProxy;
import database.wrap_mysql.Actions;
import database.wrap_mysql.EActionState;

/** ������-����� ��� �������� ��������� ������ ( ��������� Actions �� ���� ������ ) */
public class Processor implements Runnable, IActionComplete{
	private Logger logger=Logger.getLogger(this.getClass());
	private static Processor instance;
	/** ���������� ��� ��������� ��������� */
	private ProcessorActions processorActions;
	public static String parserClassesDirectory=null;
	public static int parserThreadCount=10;
	
	/** ������-����� ��� �������� ��������� ������ ( ��������� Actions �� ���� ������ ) */
	public static Processor getInstance(){
		if(instance==null)instance=new Processor(parserClassesDirectory, parserThreadCount);
		return instance;
	}
	
	/** ������-����� ��� �������� ��������� ������ ( ��������� Actions �� ���� ������ )
	 * @param pathToParsersDirectory - ������ ���� � �������� � ���������  
	 * @param parserCount - ���-�� ������������ ���������� ���������� ������� �������
	 * */
	private Processor(String pathToParsersDirectory, int parserCount){
		processorActions=new ProcessorActions(this, parserCount);
		thread=new Thread(this);
		thread.start();
	}

	/** ���� ����, ��� ������ ����� � ��������� RUN */
	private volatile boolean flagRun=true;
	/** �����, ������� ������� */
	private Thread thread;
	/**  ������ ���� Action, ������� ������ ���� ��������*/
	private UniqueIntegerArrayList listOfAction=new UniqueIntegerArrayList();
	
	
	/** ��������� ������ ������ ��������� ��� ��������� ����� */
	public void stopThread(){
		this.stopAll();
		flagRun=false;
		thread.interrupt();
	}
	
	/** ������, ������� ����� ����������� ��� ������������������� ������ �������� (listOfAction, processorAction) */
	private Object sharedResource=new Object();
	
	@Override
	public void run() {
		logger.debug("��������� �� ���� ������ ��������� �������������� ������");
		this.startupLoad();
		Integer currentAction=null;
		while(this.flagRun){
			currentAction=null;
			synchronized(this.sharedResource){
				if(this.processorActions.isBusy()==false){
					if(this.listOfAction.size()>0){
						currentAction=this.listOfAction.remove(0);
					}else{
						try{
							this.sharedResource.wait();
						}catch(Exception ex){};
					}
				}else {
					logger.debug("processorActions is busy");
					try{
						this.sharedResource.wait();
					}catch(Exception ex){};
				}
			}
			if(currentAction!=null){
				this.processorActions.startNewAction(currentAction);
			}
		}
	}
	
	/** ���������� ��� �������� */
	private void stopAll(){
		synchronized(this.sharedResource){
			this.listOfAction.clear();
		}
		this.processorActions.stopThread();
	}
	
	/** ���������� Action ������ ������� � StartUp List - ������� ����� �������������� */
	public void addActions(Integer actionId){
		synchronized(this.listOfAction){
			this.listOfAction.add(actionId);
		}
		DatabaseProxy proxy=new DatabaseProxy();
		proxy.setActionState(actionId, EActionState.IN_PROCESS);
		this.notifyAboutListChanged();
	}

	/** ���������� Action ������ ���� ������ �� StartUp List - ������� ����� �������������� */
	public void removeActions(Integer actionId){
		synchronized(this.listOfAction){
			this.listOfAction.remove(actionId);
		}
		DatabaseProxy proxy=new DatabaseProxy();
		proxy.setActionState(actionId, EActionState.STOPPED);
	}
	
	/** �������������� �������� ������  */
	private void startupLoad(){
		logger.debug("��������� �� ���� ������ ��� �������� IN_PROCESS");
		DatabaseProxy proxy=new DatabaseProxy();
		List<Actions> list=proxy.getActionsInProcess();
		for(int counter=0;counter<list.size();counter++){
			proxy.removeNotCompleteSessionByAction(list.get(counter).getId());
		}
		synchronized(this.listOfAction){
			for(Actions currentActions:list){
				this.listOfAction.add(currentActions.getId());
			}
		}
		this.notifyAboutListChanged();
	}

	/** ���������� �������� ����� � ������������� ������� ������ ����������� ��� Action */
	private void notifyAboutListChanged(){
		synchronized(this.sharedResource){
			this.sharedResource.notify();
		}
	}

	@Override
	public void actionComplete(Integer actionId, EActionState state) {
		synchronized(this.listOfAction){
			this.listOfAction.remove((Object)actionId);
		}
		DatabaseProxy proxy=new DatabaseProxy();
		proxy.setActionState(actionId, state);
		this.notifyAboutListChanged();
	}
}
