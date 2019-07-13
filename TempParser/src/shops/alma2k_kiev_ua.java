package shops;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import jxl.*;
import jxl.read.biff.BiffException;

import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.MultiFileParseManager;
import shop_list.html.parser.engine.record.Record;


public class alma2k_kiev_ua extends MultiFileParseManager<Sheet, String[]>{

	@Override
	protected Charset getFileCharset() {
		return Charset.forName("windows-1251");
	}


	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[0];
			Float price=this.getFloatFromString(record[1]);
			Float priceUsd=this.getFloatFromString(record[2]);
			if((price==null)&&(priceUsd==null))return null;
			String url=this.removeStartPage(record[3]);
			return new Record(name, null, url, price, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		try{
			if (   ((record[1]==null)||(record[1].equals("")))
				&&((record[2]==null)||(record[2].equals("")))){
				return record[0];
			}else{
				return null;
			}
		}catch(Exception ex){
			return null;
		}
	}

	
	@Override
	protected boolean closeFile(Sheet object) throws Exception {
		if(workbook!=null){
			this.workbook.close();
		}
		return true;
	}
	
	private Workbook workbook=null;;
	
	@Override
	protected Sheet openFile(String string) throws IOException {
		try{
			WorkbookSettings settings=new WorkbookSettings();
			settings.setEncoding(this.getFileCharset().name());
			this.workbook=Workbook.getWorkbook(new File(string));
			return this.workbook.getSheet(0);
		} catch (BiffException e) {
			return null;
		}
	}

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://alma2k.kiev.ua/alma2000.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://alma2k.kiev.ua";
	}

	private int rowCounter=(-1);
	@Override
	protected boolean fileHasRecord(Sheet sheet) {
		rowCounter++;
		try{
			Cell cell=sheet.getCell(0,rowCounter);
			return (cell!=null)&&(cell.getContents()!=null)&&(!cell.getContents().equals(""));
		}catch(Exception ex){
			return false;
		}
	}


	@Override
	protected String[] getNextRecord(Sheet sheet) {
		return new String[]{getCellValue(sheet,0), 
							getCellValue(sheet,1), 
							getCellValue(sheet,2), 
							getCellValue(sheet,3) 
							};
	}

	
	/** получить значение €чейки по номеру колонки */
	private String getCellValue(Sheet sheet, int columnNumber){
		try{
			return sheet.getCell(columnNumber, rowCounter).getContents();
		}catch(Exception ex){
			return null;
		}
	}
}
