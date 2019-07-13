package database;

/** результат выполнения действия по парсингу группы объектов  */
public enum EActionState {
	/** в процессе выполнения  */
	IN_PROCESS(0),
	/** успешно завершен */
	DONE(1),
	/** завершен с ошибками  */
	ERROR(2),
	/** остановлен  */
	STOPPED(3);
	
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
