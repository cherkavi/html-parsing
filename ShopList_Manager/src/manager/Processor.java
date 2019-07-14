package manager;

import java.util.List;

import manager.utility.UniqueIntegerArrayList;

import org.apache.log4j.Logger;

import database.functions.DatabaseProxy;
import database.wrap_mysql.Actions;
import database.wrap_mysql.EActionState;

/** объект-фасад для процесса обработки данных ( обработка Actions из базы данных ) */
public class Processor implements Runnable, IActionComplete{
	private Logger logger=Logger.getLogger(this.getClass());
	private static Processor instance;
	/** обработчик для указанных процессов */
	private ProcessorActions processorActions;
	public static String parserClassesDirectory=null;
	public static int parserThreadCount=10;
	
	/** объект-фасад для процесса обработки данных ( обработка Actions из базы данных ) */
	public static Processor getInstance(){
		if(instance==null)instance=new Processor(parserClassesDirectory, parserThreadCount);
		return instance;
	}
	
	/** объект-фасад для процесса обработки данных ( обработка Actions из базы данных )
	 * @param pathToParsersDirectory - полный путь к каталогу с парсерами  
	 * @param parserCount - кол-во одновременно допустимых запущенных рабочих потоков
	 * */
	private Processor(String pathToParsersDirectory, int parserCount){
		processorActions=new ProcessorActions(this, parserCount);
		thread=new Thread(this);
		thread.start();
	}

	/** флаг того, что данный поток в состоянии RUN */
	private volatile boolean flagRun=true;
	/** поток, который запущен */
	private Thread thread;
	/**  список всех Action, которые должны быть запущены*/
	private UniqueIntegerArrayList listOfAction=new UniqueIntegerArrayList();
	
	
	/** остановка потока флагом окончания для основного цикла */
	public void stopThread(){
		this.stopAll();
		flagRun=false;
		thread.interrupt();
	}
	
	/** объект, который нужно захватывать для потокобезопастности других объектов (listOfAction, processorAction) */
	private Object sharedResource=new Object();
	
	@Override
	public void run() {
		logger.debug("загрузить из базы данных возможные первоначальные данные");
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
	
	/** остановить все процессы */
	private void stopAll(){
		synchronized(this.sharedResource){
			this.listOfAction.clear();
		}
		this.processorActions.stopThread();
	}
	
	/** переданный Action должен попасть в StartUp List - который будет обрабатываться */
	public void addActions(Integer actionId){
		synchronized(this.listOfAction){
			this.listOfAction.add(actionId);
		}
		DatabaseProxy proxy=new DatabaseProxy();
		proxy.setActionState(actionId, EActionState.IN_PROCESS);
		this.notifyAboutListChanged();
	}

	/** переданный Action должен быть удален из StartUp List - который будет обрабатываться */
	public void removeActions(Integer actionId){
		synchronized(this.listOfAction){
			this.listOfAction.remove(actionId);
		}
		DatabaseProxy proxy=new DatabaseProxy();
		proxy.setActionState(actionId, EActionState.STOPPED);
	}
	
	/** первоначальная загрузка данных  */
	private void startupLoad(){
		logger.debug("загрузить из базы данных все элементы IN_PROCESS");
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

	/** оповестить основной поток о необходимости запуска нового обработчика для Action */
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
