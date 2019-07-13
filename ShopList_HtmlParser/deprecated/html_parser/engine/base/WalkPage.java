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
	/** ���������� ����������� ������ �������� �� ��������� ����� ������ �������� */
	public abstract void first();
	
	/** �������� ��������� �������� � �������
	 * @throws WalkPageExceptionNoPage - ���� �������� ������
	 * @return - �������� Html ��� null, ���� ������� ���
	 *  
	 * */
	public abstract HtmlPage getNextPage();
	
	/** �������� ������� ������, �� �������� ���������� ������ ������ */
	public abstract String getCurrentSection();
	
	
	/** �������� �� CSV ����� ������ �����
	 * @param pathToCsv - ���� � CSV �����
	 * @param delimeter - ����������� ��� CSV �����
	 * @param column - ����� �������, �� ������� ����� ����� ������ ��� �������  
	 * @param defaultValue - �������� �� ���������, ���� ��� ������ � �������
	 * @param converter - ��������� ��� ������ ��� �������������� ������ 
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
	

	/** �������� �� CSV ����� ������ ����-�������� ���
	 * @param pathToCsv - ���� � CSV �����
	 * @param delimeter - ����������� ��� CSV �����  
	 * @param defaultValue - �������� �� ��������� ��� Value ( ���� � ������ CSV ������ ���� ��������)
	 * @param columnKey - ����� ������� ��� �����
	 * @param converterKey - (nullable)  ��������� ��� �����
	 * @param columnValue - ����� ������� ��� ��������
	 * @param converterValue - (nullable) ��������� ��� ��������
	 * @throws FileNotFoundException - ���� �� ������
	 * @throws IOException - ������ ������ ������
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
				// ���� ���� � �������� 
				returnValue.put((converterKey==null)?reader.get(columnKey):converterKey.convertString(reader.get(columnKey)), 
								(converterValue==null)?reader.get(columnValue):converterValue.convertString(reader.get(columnValue))
							    );
			}else{
				// ��� ���� �����, ���� �������� 
				if(reader.getColumnCount()>=columnKey){
					// ���� ���� - �������� ���
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
