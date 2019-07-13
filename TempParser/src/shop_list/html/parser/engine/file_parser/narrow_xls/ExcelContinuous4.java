package shop_list.html.parser.engine.file_parser.narrow_xls;

import java.io.IOException;
import java.nio.charset.Charset;

import jp.ne.so_net.ga2.no_ji.jcom.ReleaseManager;
import jp.ne.so_net.ga2.no_ji.jcom.excel8.ExcelApplication;
import jp.ne.so_net.ga2.no_ji.jcom.excel8.ExcelRange;
import jp.ne.so_net.ga2.no_ji.jcom.excel8.ExcelWorkbook;
import jp.ne.so_net.ga2.no_ji.jcom.excel8.ExcelWorksheet;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.MultiFileParseManager;


/** ����������� Excel ����, ������� ������  */
public abstract class ExcelContinuous4 extends MultiFileParseManager<ExcelWorksheet, String[]>{

	/** ����� �� ����������� ����� �� "�����", �������� �� ������ ������ ������ ������ ��������������� ��������� */
	protected boolean isUncontinious(){
		return false;
	}

	protected int rowCounter=(-2);
	
	/** �������� ��������� ������� ������ � ����� <br>
	 * <b>Important: ������ ������������ � ����!!!</b>
	 * */
	protected abstract int getStartRow();
	
	/** �������-�������� ��� ������ �� ��������, <br /> ������� ������� ��� ������ �������� ������� ( �� ��������� - 0 )  
	 * <br>
	 * <b>�����: ��� ��������� ��������, ����� ������� �� �������, ������� ���������� � getRecord() </b>
	 * */
	protected int getColumnFirst(){
		return 0;
	}
	
	/** ����������� ������� ��� ������, ������� ����� ����� � ������ {@link #isUncontinious()}==false (default) � ����������� ������ �� ������� ������-������ �������� 
	 * �� ��������� ���������� {@link ExcelContinuous4#getColumnFirst()}
	 * */
	protected int getColumnControlNumber(){
		return this.getColumnFirst();
	}
	
	@Override
	protected boolean fileHasRecord(ExcelWorksheet sheet) {
		if(rowCounter==(-2)){
			rowCounter=getStartRow()-1;
		}
		rowCounter++;
		try{
			// ����������� �������� ������, ����� ���-�� ��������� ����������� � ����� "��������" � Exception
			ExcelRange cell=sheet.Cells().Item(rowCounter+1, this.getColumnControlNumber()+1);
			if(this.isUncontinious()==false){
				String value=cell.Text();
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

	/** �������� ������������ ���������� ������� � ����� (��� ������ ����� ������) */
	protected abstract int getMaxColumnCount();
	
	@Override
	protected String[] getNextRecord(ExcelWorksheet sheet) {
		String[] returnValue=new String[this.getMaxColumnCount()];
		for(int counter=0;counter<this.getMaxColumnCount();counter++){
			returnValue[counter]=getCellValue(sheet,this.getColumnFirst()+counter);
		}
		return returnValue;
	}

	/** ���������� ����������� �� Cell ���� �� null, ���� ���������� �������� ����������� �� ������� Cell , ������������ � {@link ExcelContinuous4#getCellValue(Sheet, int)}*/
	protected String getHLinkFromCell(ExcelRange cell){
		try{
			// formula:HYPERLINK("http://itcity.com.ua/CHIEFTEC-GPS-350EB-101A-350W-p72972.html","http://itcity.com.ua/CHIEFTEC-GPS-350EB-101A-350W-p72972.html")
			String formula=cell.Formula();
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
		
		/*
		if(cell instanceof StringFormulaCell){
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

	/** �������� �� ������ ������������ ������������ � {@link #getCellValue}*/
	protected boolean isHLink(ExcelRange cell){
		try{
			String formula=cell.Formula();
			return (formula.indexOf("HYPERLINK")>=0);
		}catch(Exception ex){
			return false;
		}
		
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

	/** ���������� ����������� �� Cell ���� �� null, ���� ���������� �������� ����������� �� ������� Cell, ������������ � {@link ExcelContinuous4#getCellValue(Sheet, int)}*/
	protected String getHLinkCaption(ExcelRange cell){
		try{
			// formula:HYPERLINK("http://itcity.com.ua/CHIEFTEC-GPS-350EB-101A-350W-p72972.html","http://itcity.com.ua/CHIEFTEC-GPS-350EB-101A-350W-p72972.html")
			String formula=cell.Formula();
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
	}
	
	/**
	 * @param sheet - ���� 
	 * @param row - ����� ������
	 * @param column - ����� �������
	 * @return
	 */
	protected ExcelRange getCell(ExcelWorksheet sheet, int row, int column){
		try{
			return sheet.Cells().Item(row+1, column+1);
		}catch(Exception ex){
			return null;
		}
	}
	
	/** �������� �������� ������ �� ������ ������� */
	protected String getCellValue(ExcelWorksheet sheet, int columnNumber){
		try{
			// Cell cell=sheet.getCell(columnNumber,rowCounter);
			// System.out.println(">>> "+cell.getContents());
			// if(cell instanceof FormulaData){
			//	System.out.println("formula:"+((StringFormulaCell)cell).getFormula());
			// 	formula:HYPERLINK("http://itcity.com.ua/CHIEFTEC-GPS-350EB-101A-350W-p72972.html","http://itcity.com.ua/CHIEFTEC-GPS-350EB-101A-350W-p72972.html")
			// }
			return sheet.Cells().Item(rowCounter+1, columnNumber+1).Text();
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
	
	private ReleaseManager releaseManager=new ReleaseManager();
	private ExcelApplication application;
	@Override
	protected ExcelWorksheet openFile(String string) throws IOException {
		try{
			application=new ExcelApplication(releaseManager);
			application.Visible(false);
			ExcelWorkbook workbook=application.Workbooks().Open(string);
			return workbook.Worksheets().Item(this.getSheetNumber()+1);
		} catch (Exception e) {
			logger.error(this, "Excel file get Sheet Exception:"+e.getMessage());
			return null;
		}
	}

	/** get Sheet number for parse (default is 0) */
	protected int getSheetNumber(){
		return 0;
	}

	@Override
	protected boolean closeFile(ExcelWorksheet object) throws Exception {
		try{
			application.Quit();
			this.releaseManager.release();
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
