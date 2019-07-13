package shop_list.html.parser.engine.file_parser.narrow_xls;

import java.io.File;
import java.io.FileInputStream;



import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.MultiFileParseManager;


/** непрерывный Excel файл, который служит  */
public abstract class ExcelContinuous2 extends MultiFileParseManager<HSSFSheet, String[]>{

	/** нужно ли исследовать прайс до "упора", невзирая на пустые строки внутри самого сформированного документа */
	protected boolean isUncontinious(){
		return false;
	}

	protected int rowCounter=(-2);
	
	/** получить стартовую позицию строки в файле <br>
	 * <b>Important: отсчет производится с нуля!!!</b>
	 * */
	protected abstract int getStartRow();
	
	/** колонка-смещение для данных на странице, <br /> другими словами это первая значимая колонка ( по умолчанию - 0 )  
	 * <br>
	 * <b>Важно: при установке значения, нужно следать за данными, которые передаются в getRecord() </b>
	 * */
	protected int getColumnFirst(){
		return 0;
	}
	
	/** контрольная колонка для данных, которая имеет смысл в случае {@link #isUncontinious()}==false (default) и проверяется строка на наличие какого-нибудь значения 
	 * по умолчанию возвращает {@link ExcelContinuous2#getColumnFirst()}
	 * */
	protected int getColumnControlNumber(){
		return this.getColumnFirst();
	}
	
	@Override
	protected boolean fileHasRecord(HSSFSheet sheet) {
		if(rowCounter==(-2)){
			rowCounter=getStartRow()-1;
		}
		rowCounter++;
		try{
			// обязательно получить запись, иначе кол-во элементов закончилось и тогда "свалится" в Exception
			HSSFCell cell=sheet.getRow(rowCounter).getCell(this.getColumnControlNumber());
			if(this.isUncontinious()==false){
				String cellValue=cell.getStringCellValue();
				return (cell!=null)&&(cellValue!=null)&&(!cellValue.equals(""));
			}else{
				return true;
			}
		}catch(Exception ex){
			return false;
		}
	}
	
	@Override
	protected Charset getFileCharset() {
		return Charset.forName("windows-1251");
	}

	/** получить максимальное количество колонок в файле (для чтения одной записи) */
	protected abstract int getMaxColumnCount();
	
	@Override
	protected String[] getNextRecord(HSSFSheet sheet) {
		String[] returnValue=new String[this.getMaxColumnCount()];
		for(int counter=0;counter<this.getMaxColumnCount();counter++){
			returnValue[counter]=getCellValue(sheet,this.getColumnFirst()+counter);
		}
		return returnValue;
	}

	/** возвращает гиперссылку из Cell либо же null, если невозможно получить гиперссылку из данного Cell , используется в {@link ExcelContinuous2#getCellValue(Sheet, int)}*/
	protected String getHLinkFromCell(HSSFCell cell){
		HSSFHyperlink link=cell.getHyperlink();
		return link.getAddress();
	}

	/** является ли ячейка Гиперссылкой используется в {@link #getCellValue}*/
	protected boolean isHLink(HSSFCell cell){
		return cell.getHyperlink()!=null;
	}

	/** возвращает гиперссылку из Cell либо же null, если невозможно получить гиперссылку из данного Cell, используется в {@link ExcelContinuous2#getCellValue(Sheet, int)}*/
	protected String getHLinkCaption(HSSFCell cell){
		HSSFHyperlink link=cell.getHyperlink();
		return link.getLabel();
	}
	
	
	/** получить значение ячейки по номеру колонки */
	protected String getCellValue(HSSFSheet sheet, int columnNumber){
		return sheet.getRow(this.rowCounter).getCell(columnNumber).getStringCellValue();
	}
	
	/*
	 * 
	private void printParents(Object object){
		Class<?>[] interfaces=object.getClass().getInterfaces();
		System.out.println("====Interfaces====");
		for(int counter=0;counter<interfaces.length;counter++){
			System.out.println(counter+"  "+interfaces[counter]);
		}
		System.out.println("====Classes====: "+object.getClass().getName());
		System.out.println("========");
	}*/
	

	@Override
	protected HSSFSheet openFile(String string) throws IOException {
		try{
			this.workbookInputStream=new FileInputStream(new File(string));
			this.workbook=new HSSFWorkbook(this.workbookInputStream);
			return this.workbook.getSheetAt(this.getSheetNumber());
		} catch (Exception e) {
			logger.error(this, "Excel file get Sheet Exception:"+e.getMessage());
			return null;
		}
	}

	/** get Sheet number for parse (default is 0) */
	protected int getSheetNumber(){
		return 0;
	}

	private HSSFWorkbook workbook;
	private FileInputStream workbookInputStream=null; 
	@Override
	protected boolean closeFile(HSSFSheet object) throws Exception {
		try{
			this.workbookInputStream.close();
			return true;
		}catch(Exception ex){
			return false;
		}
	}
	
	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.xls;
	}
}
