package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class mirit_com_ua extends ExcelContinuous{

	@Override
	protected boolean isUncontinious() {
		return true;
	}
	
	@Override
	protected int getMaxEmptyRecord() {
		return 10;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 9;
	}

	@Override
	protected int getStartRow() {
		return 9;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(isStringEmpty(record[2])&&isStringEmpty(record[3])&&isStringEmpty(record[4]))throw new EParseExceptionEmptyRecord();
			if(!isStringEqualsAny(record[8], "+"))return null;
			String name=record[3];
			Float priceUsd=this.getFloatFromString(record[4]);
			return new Record(name, null, null, null, priceUsd, null);
		}catch(EParseException ep){
			throw ep;
		}
		catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[1])&&isStringEmpty(record[2])&&isStringEmpty(record[4])&&(!isStringEmpty(record[3]))){
			return record[3];
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.mirit.com.ua/price/PriceMirit.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.mirit.com.ua";
	}

}
