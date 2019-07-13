package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class ggshop_com_ua extends ExcelContinuous{

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 11;
	}

	@Override
	protected int getStartRow() {
		return 14;
	}
	
	@Override
	protected int getColumnControlNumber() {
		return 1;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(!isStringEqualsAny(record[9], "+"))return null;
			String name=record[3]+" "+record[4];
			String url=this.removeStartPage(record[10]);
			Float price=this.getFloatFromString(record[8]);
			Float priceUsd=this.getFloatFromString(record[7]);
			return new Record(name, null, url, price, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
		
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(!isStringEmpty(record[1])&&isStringEmpty(record[2])&&isStringEmpty(record[3])){
			return record[1];
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.ggshop.com.ua/UserFiles/ggshop_fullprice.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.ggshop.com.ua";
	}

}
