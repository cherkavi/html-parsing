package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.exception.EParseExceptionNotInStore;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class mkp_kiev_ua extends ExcelContinuous{
	@Override
	protected int getMaxEmptyRecord() {
		return 5;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 10;
	}

	@Override
	protected int getStartRow() {
		return 14;
	}
	
	@Override
	protected boolean isUncontinious() {
		return true;
	}
	
	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[3];
			Float price=this.getFloatFromString(record[4]);
			if((price==null)&&(isStringEmpty(record[2]))&&(isStringEmpty(record[3])) )throw new EParseExceptionEmptyRecord();
			if(price.floatValue()==0)return null;
			if(!isStringEqualsAny(record[9], "+","резерв"))throw new EParseExceptionNotInStore();
			return new Record(name, null, null, price, null, null);
		}catch(EParseException ep){
			throw ep;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[1])&&isStringEmpty(record[3])&&isStringEmpty(record[4])){
			return record[2];
		}else{
			return null;
		}
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://mkp.kiev.ua/price/price.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://mkp.kiev.ua";
	}

}
