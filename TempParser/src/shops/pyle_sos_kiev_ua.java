package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;

import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous4;
import shop_list.html.parser.engine.record.Record;

public class pyle_sos_kiev_ua extends ExcelContinuous4{

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
		return 5;
	}

	@Override
	protected int getStartRow() {
		return 1;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(isStringEmpty(record[0])&&isStringEmpty(record[1])&&isStringEmpty(record[2])&&isStringEmpty(record[4])){
				throw new EParseExceptionEmptyRecord();
			}
			String name=record[2];
			Float price=this.getFloatFromString(record[4].replaceAll("[à-ÿÀ-ß]", ""));
			if(price.floatValue()==0)return null;
			return new Record(name, null,null, price, null,null);
		}catch(EParseExceptionEmptyRecord ep){
			throw ep;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(!isStringEmpty(record[0])){
			return record[0];
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://pyle-sos.kiev.ua/pricexls.php"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://pyle-sos.kiev.ua";
	}

}
