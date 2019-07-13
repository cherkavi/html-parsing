package database.wrap_mysql;

/** результат выполнения действия по парсингу группы объектов  */
public enum EActionState {
	/** успешно завершен */
	DONE(1),
	/** завершен с ошибками  */
	ERROR(2),
	/** в процессе выполнения  */
	IN_PROCESS(3),
	/** новый  */
	NEW(4),
	/** остановленный */
	STOPPED(5);
	
	private int kod=0;
	
	/** результат выполнения действия */
	private EActionState( int kod){
		this.kod=kod;
	}
	
	/** Получить значение для базы данных  */
	public int getKod(){
		return this.kod;
	}
}
