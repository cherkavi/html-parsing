package shop_list.html.parser.engine.file_parser.narrow_xls;

import java.io.File;

import java.io.IOException;
import java.nio.charset.Charset;
import jxl.Cell;
import jxl.FormulaCell;
import jxl.LabelCell;
import jxl.Sheet;
import jxl.StringFormulaCell;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.MultiFileParseManager;


/** непрерывный Excel файл, который служит  */
public abstract class ExcelContinuous extends MultiFileParseManager<Sheet, String[]>{

	/** удалить из строки после указанного символа все элементы, включая его же */
	protected String removeAfterSymbolIncludeIt(String url, char c) {
		int index=url.indexOf(c);
		if(index>0){
			return url.substring(0,index); 
		}else{
			return url;
		}
	}

	
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
	 * по умолчанию возвращает {@link ExcelContinuous#getColumnFirst()}
	 * */
	protected int getColumnControlNumber(){
		return this.getColumnFirst();
	}
	
	@Override
	protected boolean fileHasRecord(Sheet sheet) {
		if(rowCounter==(-2)){
			rowCounter=getStartRow()-1;
		}
		rowCounter++;
		try{
			// обязательно получить запись, иначе кол-во элементов закончилось и тогда "свалится" в Exception
			Cell cell=sheet.getCell(this.getColumnControlNumber(),rowCounter);
			if(this.isUncontinious()==false){
				return (cell!=null)&&(cell.getContents()!=null)&&(!cell.getContents().equals(""));
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
	protected String[] getNextRecord(Sheet sheet) {
		String[] returnValue=new String[this.getMaxColumnCount()];
		for(int counter=0;counter<this.getMaxColumnCount();counter++){
			returnValue[counter]=getCellValue(sheet,this.getColumnFirst()+counter);
		}
		return returnValue;
	}

	/** возвращает гиперссылку из Cell либо же null, если невозможно получить гиперссылку из данного Cell , используется в {@link ExcelContinuous#getCellValue(Sheet, int)}*/
	protected String getHLinkFromCell(Cell cell){
		if(cell instanceof FormulaCell){
			try{
				// formula:HYPERLINK("http://itcity.com.ua/CHIEFTEC-GPS-350EB-101A-350W-p72972.html","http://itcity.com.ua/CHIEFTEC-GPS-350EB-101A-350W-p72972.html")
				String formula=((FormulaCell)cell).getFormula();
				String returnValue=null;
				int brace=formula.indexOf('"');
				int brace2=formula.indexOf('"', brace+1);
				if((brace>0)&&(brace2>0)){
					returnValue=formula.substring(brace+1,brace2);
				}
				return (returnValue==null)?null:returnValue;
			}catch(Exception ex){
				return null;
			}
		}else{
			if(cell instanceof LabelCell){
				LabelCell value=(LabelCell)cell;
				return value.getContents();
			}
			return null;
		}
	}

	/** является ли ячейка Гиперссылкой используется в {@link #getCellValue}*/
	protected boolean isHLink(Cell cell){
		try{
			if(cell instanceof FormulaCell){
				String formula=((FormulaCell)cell).getFormula();
				return (formula.indexOf("HYPERLINK")>=0);
			}else{
				return false;
			}
		}catch(Exception ex){
			return false;
		}
	}

	/** возвращает гиперссылку из Cell либо же null, если невозможно получить гиперссылку из данного Cell, используется в {@link ExcelContinuous#getCellValue(Sheet, int)}*/
	protected String getHLinkCaption(Cell cell){
		if(cell instanceof StringFormulaCell){
			try{
				// formula:HYPERLINK("http://itcity.com.ua/CHIEFTEC-GPS-350EB-101A-350W-p72972.html","http://itcity.com.ua/CHIEFTEC-GPS-350EB-101A-350W-p72972.html")
				String formula=((FormulaCell)cell).getFormula();
				String returnValue=null;
				int comma=formula.indexOf(',');
				if(comma<0)comma=0;
				int brace=formula.indexOf('"',comma);
				int brace2=formula.indexOf('"', comma+brace+1);
				if((brace>0)&&(brace2>0)){
					returnValue=formula.substring(brace+1,brace2);
				}
				return (returnValue==null)?null:returnValue;
			}catch(Exception ex){
				return null;
			}
		}else{
			return null;
		}
	}
	
	
	/** получить значение ячейки по номеру колонки */
	protected String getCellValue(Sheet sheet, int columnNumber){
		try{
			// Cell cell=sheet.getCell(columnNumber,rowCounter);
			// System.out.println(">>> "+cell.getContents());
			// if(cell instanceof FormulaData){
			//	System.out.println("formula:"+((StringFormulaCell)cell).getFormula());
			// 	formula:HYPERLINK("http://itcity.com.ua/CHIEFTEC-GPS-350EB-101A-350W-p72972.html","http://itcity.com.ua/CHIEFTEC-GPS-350EB-101A-350W-p72972.html")
			// }
			return sheet.getCell(columnNumber, rowCounter).getContents();
		}catch(Exception ex){
			return null;
		}
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
	protected Sheet openFile(String string) throws IOException {
		try{
			WorkbookSettings settings=new WorkbookSettings();
			settings.setEncoding(this.getFileCharset().name());
			// settings.setDrawingsDisabled(false);
			settings.setGCDisabled(true);
			this.workbook=Workbook.getWorkbook(new File(string));
			
			try{
				return this.workbook.getSheet(this.getSheetNumber());
			}catch(NullPointerException npe){
				if(this.workbook.getNumberOfSheets()>0){
					String[] names=this.workbook.getSheetNames();
					return this.workbook.getSheet(names[this.getSheetNumber()]);
				}else{
					logger.warn(this, "Excel file not consists Sheets");
					return null;
				}
			}
			
		} catch (BiffException e) {
			logger.error(this, "Excel file get Sheet Exception:"+e.getMessage());
			return null;
		}
	}

	/** get Sheet number for parse (default is 0) */
	protected int getSheetNumber(){
		return 0;
	}

	private Workbook workbook;
	
	@Override
	protected boolean closeFile(Sheet object) throws Exception {
		try{
			this.workbook.close();
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
