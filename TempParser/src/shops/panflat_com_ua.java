package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class panflat_com_ua extends ExcelContinuous{

	@Override
	protected boolean isUncontinious() {
		return true;
	}

	@Override
	protected int getMaxEmptyRecord() {
		return 5;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 3;
	}

	@Override
	protected int getStartRow() {
		return 1;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[0]+" "+record[1];
			Float priceUsd=this.getFloatFromString(record[2]);
			if(isStringEmpty(record[0])&&isStringEmpty(record[1])&&isStringEmpty(record[2])){
				throw new EParseExceptionEmptyRecord();
			}
			if(priceUsd.floatValue()==0)return null;
			return new Record(name, null, null, null, priceUsd, null);
		}catch(EParseExceptionEmptyRecord empty){
			throw empty;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[1])&&(isStringEmpty(record[2]))&&(!isStringEmpty(record[0]))){
			return record[0];
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://panflat.com.ua/panflat_price.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://panflat.com.ua";
	}

}
