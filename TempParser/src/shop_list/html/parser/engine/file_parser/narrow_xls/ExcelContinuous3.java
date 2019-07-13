package shop_list.html.parser.engine.file_parser.narrow_xls;

import java.io.File;
import java.io.FileInputStream;



import java.io.IOException;
import java.nio.charset.Charset;


import com.extentech.ExtenXLS.*;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.MultiFileParseManager;


/** непрерывный Excel файл, который служит  */
public abstract class ExcelContinuous3 extends MultiFileParseManager<WorkSheetHandle, String[]>{

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
	 * по умолчанию возвращает {@link ExcelContinuous3#getColumnFirst()}
	 * */
	protected int getColumnControlNumber(){
		return this.getColumnFirst();
	}
	
	@Override
	protected boolean fileHasRecord(WorkSheetHandle sheet) {
		if(rowCounter==(-2)){
			rowCounter=getStartRow()-1;
		}
		rowCounter++;
		try{
			// обязательно получить запись, иначе кол-во элементов закончилось и тогда "свалится" в Exception
			CellHandle cell=sheet.getCell(this.getColumnControlNumber(),rowCounter);
			if(this.isUncontinious()==false){
				String value=cell.getStringVal();
				return (cell!=null)&&(value!=null)&&(!value.equals(""));
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
	protected String[] getNextRecord(WorkSheetHandle sheet) {
		String[] returnValue=new String[this.getMaxColumnCount()];
		for(int counter=0;counter<this.getMaxColumnCount();counter++){
			returnValue[counter]=getCellValue(sheet,this.getColumnFirst()+counter);
		}
		return returnValue;
	}

	/** возвращает гиперссылку из Cell либо же null, если невозможно получить гиперссылку из данного Cell , используется в {@link ExcelContinuous3#getCellValue(Sheet, int)}*/
	protected String getHLinkFromCell(CellHandle cell){
		return cell.getURL();
/*		if(cell instanceof StringFormulaCell){
			try{
				// formula:HYPERLINK("http://itcity.com.ua/CHIEFTEC-GPS-350EB-101A-350W-p72972.html","http://itcity.com.ua/CHIEFTEC-GPS-350EB-101A-350W-p72972.html")
				String formula=((StringFormulaCell)cell).getFormula();
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
*/		
	}

	/** является ли ячейка Гиперссылкой используется в {@link #getCellValue}*/
	protected boolean isHLink(CellHandle cell){
		return cell.getURL()!=null;
		/*try{
			if(cell instanceof StringFormulaCell){
				String formula=((StringFormulaCell)cell).getFormula();
				return (formula.indexOf("HYPERLINK")>=0);
			}else{
				return false;
			}
		}catch(Exception ex){
			return false;
		}*/
	}

	/** возвращает гиперссылку из Cell либо же null, если невозможно получить гиперссылку из данного Cell, используется в {@link ExcelContinuous3#getCellValue(Sheet, int)}*/
	protected String getHLinkCaption(CellHandle cell){
		return cell.getStringVal();
		/* 
		if(cell instanceof StringFormulaCell){
			try{
				// formula:HYPERLINK("http://itcity.com.ua/CHIEFTEC-GPS-350EB-101A-350W-p72972.html","http://itcity.com.ua/CHIEFTEC-GPS-350EB-101A-350W-p72972.html")
				String formula=((StringFormulaCell)cell).getFormula();
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
		}*/
	}
	
	
	/** получить значение ячейки по номеру колонки */
	protected String getCellValue(WorkSheetHandle sheet, int columnNumber){
		try{
			// Cell cell=sheet.getCell(columnNumber,rowCounter);
			// System.out.println(">>> "+cell.getContents());
			// if(cell instanceof FormulaData){
			//	System.out.println("formula:"+((StringFormulaCell)cell).getFormula());
			// 	formula:HYPERLINK("http://itcity.com.ua/CHIEFTEC-GPS-350EB-101A-350W-p72972.html","http://itcity.com.ua/CHIEFTEC-GPS-350EB-101A-350W-p72972.html")
			// }
			return sheet.getCell(this.rowCounter, columnNumber).getStringVal();
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
	protected WorkSheetHandle openFile(String string) throws IOException {
		try{
			this.workbook=new WorkBookHandle(new FileInputStream(new File(string)));
			return this.workbook.getWorkSheet(this.getSheetNumber());
		} catch (Exception e) {
			logger.error(this, "Excel file get Sheet Exception:"+e.getMessage());
			return null;
		}
	}

	/** get Sheet number for parse (default is 0) */
	protected int getSheetNumber(){
		return 0;
	}

	private WorkBookHandle workbook;
	
	@Override
	protected boolean closeFile(WorkSheetHandle object) throws Exception {
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
