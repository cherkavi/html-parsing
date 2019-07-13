package html_parser.engine.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.csvreader.CsvReader;

import html_parser.element.base.HtmlPage;
import html_parser.engine.utility.StringConverter;

public abstract class WalkPage {
	/** установить виртуальный курсор движения по страницам перед первой позицией */
	public abstract void first();
	
	/** получить следующую страницу с данными
	 * @throws WalkPageExceptionNoPage - если закончен список
	 * @return - страницу Html или null, если таковой нет
	 *  
	 * */
	public abstract HtmlPage getNextPage();
	
	/** получить текущий раздел, по которому происходит чтение данных */
	public abstract String getCurrentSection();
	
	
	/** получить из CSV файла список полей
	 * @param pathToCsv - путь к CSV файлу
	 * @param delimeter - разделитель для CSV файла
	 * @param column - номер колонки, из которой нужно брать данные для выборки  
	 * @param defaultValue - значение по умолчанию, если нет данных в колонке
	 * @param converter - конвертер для строки для преобразования данных 
	 * @throws IOException 
	 * */
	protected ArrayList<String> getElementsFromCsvToList(String pathToCsv, 
													     char delimeter, 
														 int column,
														 String defaultValue,
														 StringConverter converter) throws FileNotFoundException, IOException {
		File file=new File(pathToCsv);
		CsvReader reader=new CsvReader(new InputStreamReader(new FileInputStream(file)));
		reader.setDelimiter(delimeter);
		String newDefaultValue=null;
		if(converter!=null){
			newDefaultValue=converter.convertString(defaultValue);
		}else{
			newDefaultValue=defaultValue;
		}
		ArrayList<String> returnValue=new ArrayList<String>();
		while(reader.readRecord()){
			if(reader.getColumnCount()>=column){
				if(converter!=null){
					returnValue.add(converter.convertString(reader.get(column-1)));
				}else{
					returnValue.add(reader.get(column-1));
				}
			}else{
				returnValue.add(newDefaultValue);
			}
		}
		reader.close();
		return returnValue;
	}
	

	/** получить из CSV файла список ключ-значение для
	 * @param pathToCsv - путь к CSV файлу
	 * @param delimeter - разделитель для CSV файла  
	 * @param defaultValue - значение по умолчанию для Value ( если в строке CSV только одно значение)
	 * @param columnKey - номер столбца для ключа
	 * @param converterKey - (nullable)  конвертер для ключа
	 * @param columnValue - номер столбца для значения
	 * @param converterValue - (nullable) конвертер для значения
	 * @throws FileNotFoundException - файл не найден
	 * @throws IOException - ошибка чтения данных
	 * @return {@literal HashMap<String key,String value> }  
	 */
	protected HashMap<String,String> getElementsFromCsvToHashMap(String pathToCsv, 
																 char delimeter, 
																 String defaultValue,
																 int columnKey,
																 StringConverter converterKey,
																 int columnValue,
																 StringConverter converterValue) throws FileNotFoundException, IOException {
		File file=new File(pathToCsv);
		CsvReader reader=new CsvReader(new InputStreamReader(new FileInputStream(file)));
		reader.setDelimiter(delimeter);
		HashMap<String,String> returnValue=new HashMap<String,String>();
		
		int maxColumnValue=Math.max(columnKey, columnValue);
		String newDefaultValue=null;
		if(converterValue!=null){
			newDefaultValue=converterValue.convertString(defaultValue);
		}else{
			newDefaultValue=defaultValue;
		}
		while(reader.readRecord()){
			if(reader.getColumnCount()>=maxColumnValue){
				// есть ключ и значение 
				returnValue.put((converterKey==null)?reader.get(columnKey):converterKey.convertString(reader.get(columnKey)), 
								(converterValue==null)?reader.get(columnValue):converterValue.convertString(reader.get(columnValue))
							    );
			}else{
				// нет либо ключа, либо значения 
				if(reader.getColumnCount()>=columnKey){
					// ключ есть - значения нет
					returnValue.put((converterKey==null)?reader.get(columnKey):converterKey.convertString(reader.get(columnKey)), 
									newDefaultValue);
				}
			}
		}
		reader.close();
		return returnValue;
	}
	
	protected void err(Object information){
		System.out.print(this.getClass().getName());
		System.out.print(" ERROR ");
		System.out.println(information);
	}
	protected void out(Object information){
		System.out.print(this.getClass().getName());
		System.out.print(" DEBUG ");
		System.out.println(information);
	}
}
