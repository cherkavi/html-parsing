package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;

import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous4;
import shop_list.html.parser.engine.record.Record;

public class newworld_co_ua extends ExcelContinuous4{

	@Override
	protected int getMaxColumnCount() {
		return 5;
	}

	@Override
	protected int getStartRow() {
		return 10;
	}

	@Override
	protected boolean isUncontinious() {
		return true;
	}

	@Override
	protected int getMaxEmptyRecord() {
		return 5;
	}
	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(isStringEmpty(record[1])&&isStringEmpty(record[2])&&isStringEmpty(record[3])){
				throw new EParseExceptionEmptyRecord();
			}
			String name=record[2];
			Float priceUsd=this.getFloatFromString(record[4]);
			if(priceUsd.floatValue()==0)return null;
			return new Record(name, null, null, null, priceUsd, null);
		}catch(EParseExceptionEmptyRecord ep){
			throw ep;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[1])&&isStringEmpty(record[3])&&isStringEmpty(record[4])){
			return record[2];
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://newworld.co.ua/misc/getprice.php"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://newworld.co.ua";
	}

}
