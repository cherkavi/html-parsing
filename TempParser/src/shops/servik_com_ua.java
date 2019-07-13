package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous4;
import shop_list.html.parser.engine.record.Record;

public class servik_com_ua extends ExcelContinuous4{

	@Override
	protected int getMaxColumnCount() {
		return 5;
	}

	@Override
	protected int getStartRow() {
		return 1;
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
			if(this.isStringEmpty(record[0])&&this.isStringEmpty(record[1])&&this.isStringEmpty(record[2])&&this.isStringEmpty(record[3])){
				throw new EParseExceptionEmptyRecord();
			}
			String name=record[2];
			Float priceUsd=this.getFloatFromString(record[4].replaceAll("[à-ÿÀ-ßa-zA-Z.]", ""));
			if(priceUsd.floatValue()==0)return null;
			return new Record(name, null, null, null, priceUsd, null);
		}catch(EParseExceptionEmptyRecord pe){
			throw pe;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[0])&&isStringEmpty(record[2])&&isStringEmpty(record[3])&&(!isStringEmpty(record[1]))){
			return record[1];
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://servik.com.ua/pricexls.php"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://servik.com.ua";
	}

}
