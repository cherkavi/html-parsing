package shops;

import jxl.Cell;
import jxl.Sheet;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class creation_kiev_ua extends ExcelContinuous{

	@Override
	protected int getColumnControlNumber() {
		return 1;
	}
	
	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 6;
	}

	@Override
	protected int getStartRow() {
		return 4;
	}

	/** получить значение €чейки по номеру колонки */
	protected String getCellValue(Sheet sheet, int columnNumber){
		if(columnNumber!=5){
			return super.getCellValue(sheet, columnNumber);
		}else{
			Cell cell=sheet.getCell(columnNumber,rowCounter);
			String returnValue=this.getHLinkFromCell(cell);
			return (returnValue==null)?cell.getContents():returnValue;
		}
	}
	
	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[2];
			String url=null; // this.removeStartPage(record[5]);
			Float price=this.getFloatFromString(record[3].replaceAll("[а-€ ]", ""));
			Float priceUsd=this.getFloatFromString(record[4].replaceAll("[$ ]", ""));
			if( (price.floatValue()==0)&&(priceUsd.floatValue()==0) ){
				return null;
			}
			return new Record(name, null, url, price, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[0])&&(!isStringEmpty(record[1]))&&isStringEmpty(record[2])){
			return record[1];
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.creation.kiev.ua/Price/Creation_pricelist.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.creation.kiev.ua";
	}

}
