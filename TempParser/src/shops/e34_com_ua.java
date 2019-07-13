package shops;

import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;

import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous4;
import shop_list.html.parser.engine.record.Record;

public class e34_com_ua extends ExcelContinuous4{

	@Override
	protected boolean isUncontinious() {
		return true;
	}
	
	@Override
	protected int getStartRow() {
		return 4;
	}

	@Override
	protected int getMaxEmptyRecord() {
		return 20;
	}
	
	@Override
	protected Record getSaveRecord(String[] record) throws EParseExceptionEmptyRecord{
		try{
			if(isStringEmpty(record[0])&& isStringEmpty(record[1]) && isStringEmpty(record[2]) && isStringEmpty(record[3])){
				throw new EParseExceptionEmptyRecord();
			}
			String name=record[2];
			Float priceUsd=this.getFloatFromString(record[4].replaceAll("\\$", "").trim());
			if((priceUsd==null)||(priceUsd.floatValue()==0))return null;
			return new Record(name, null, null, null, priceUsd, null);
		}catch(EParseExceptionEmptyRecord ex){
			throw ex;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		return ((!isStringEmpty(record[2])&&isStringEmpty(record[1])&&isStringEmpty(record[3])&&isStringEmpty(record[4]))?record[2]:null);
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://e34.com.ua/pricexls.php"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://e34.com.ua";
	}

	@Override
	protected int getMaxColumnCount() {
		return 5;
	}

}
