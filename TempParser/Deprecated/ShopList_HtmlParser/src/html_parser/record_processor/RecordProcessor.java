package html_parser.record_processor;

import html_parser.record.Record;

import java.util.ArrayList;

/** объект, который служит для дополнительной/пост обработки полученного блока записей*/
public abstract class RecordProcessor {
	/** метод, которому передаются полученные со страницы записи <b>перед</b> сохранением */
	public abstract void beforeSave(ArrayList<Record> list);

	/** метод, которому передаются полученные со страницы записи <b>после</b> сохранением */
	public abstract void afterSave(ArrayList<Record> block);
}
