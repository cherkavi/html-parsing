package database.wrap_mysql;

/** состояние парсинга отдельно взятой сессии  */
public enum EParseResult {
	ERROR(1), OK(2), STOPPED(3), NEW(4);
	
	private int id;
	
	private EParseResult(int value) {
		this.id=value;
	}
	
	/** получить уникальный код на основании базы данных */
	public int getId(){
		return this.id;
	}
}
