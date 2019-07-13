package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class kpiservice_com_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 10;
	}

	@Override
	protected int getStartRow() {
		return 20;
	}

	
	@Override
	protected boolean isUncontinious() {
		return true;
	}
	
	@Override
	protected int getMaxEmptyRecord() {
		return 10;
	}
	
	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(!isStringEqualsAny(record[9], "+"))return null;
			if(isStringEmpty(record[1])&&isStringEmpty(record[2])&&isStringEmpty(record[3]))throw new EParseExceptionEmptyRecord();
			String name=record[3];
			Float priceUsd=this.getFloatFromString(record[6]);
			return new Record(name, null, null, null, priceUsd, null);
		}catch(EParseException ep){
			throw ep;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(!isStringEmpty(record[1])&&isStringEmpty(record[8])&&isStringEmpty(record[9]))return record[1];
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.kpiservice.com.ua/upload/price1.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.kpiservice.com.ua";
	}

}
