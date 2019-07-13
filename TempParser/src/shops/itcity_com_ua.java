package shops;

import jxl.Cell;
import jxl.Sheet;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class itcity_com_ua extends ExcelContinuous{
	
	@Override
	protected boolean isUncontinious() {
		return true;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 8;
	}

	@Override
	protected int getStartRow() {
		return 10;
	}

	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[1];
			String url=this.removeStartPage(record[7]);
			Float price=this.getFloatFromString(record[3]);
			Float priceUsd=this.getFloatFromString(record[2]);
			if (((price==null)||(price.floatValue()==0))&&((priceUsd==null)||(priceUsd.floatValue()==0)))return null;
			if(!isStringEqualsAny(record[6], "+"))return null;
			return new Record(name, null, url, price, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	/** получить значение €чейки по номеру колонки */
	protected String getCellValue(Sheet sheet, int columnNumber){
		if(columnNumber!=7){
			return super.getCellValue(sheet, columnNumber);
		}else{
			Cell cell=sheet.getCell(columnNumber,rowCounter);
			String returnValue=this.getHLinkFromCell(cell);
			return (returnValue==null)?cell.getContents():returnValue;
		}
	}
	
	
	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[0])&&isStringEmpty(record[2])&&isStringEmpty(record[3])){
			return record[1];
		}else{
			return null;
		}
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://itcity.com.ua/price/ITCity_price.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://itcity.com.ua";
	}

}
