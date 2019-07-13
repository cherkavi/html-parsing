package html_parser;

import html_parser.record.Record;

/** абстрактный класс, который служит для сохранения записей */
public abstract class Saver {
	/** очистить все записи для начала записи */
	public abstract boolean resetAllRecord();
	/** сохранить текущую запись */
	public abstract boolean save(String sectionName, Record record);
	/** закончить запись постамбула */
	public abstract boolean finish();
	/** начало записи данных - преамбула */
	public abstract boolean begin();
}
