package shop_list.html.parser.engine;

/** результат парсинга  ( PARSE_RESULT )
 * <ul>
 * 	<li> <b>ok</b> - успешно завершен </li>
 * 	<li> <b>error</b> - ошибка выполнени€ </li>
 * </ul> 
 * */
public enum ESessionResult {
	/** остановлен */
	stopped(3),
	/** успешно окончен */
	ok(2),
	/** ошибка парсинга */
	error(1);

	private int kod;
	
	private ESessionResult(int databaseKod){
		this.kod=databaseKod;
	}
	/** код дл€ записи в таблицу базы данных  */
	public int getDatabaseKod(){
		return this.kod;
	}
}
