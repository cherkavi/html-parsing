package shops;

import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous4;
import shop_list.html.parser.engine.record.Record;

public class comline_com_ua extends ExcelContinuous4{

	@Override
	protected boolean isUncontinious() {
		return true;
	}

	@Override
	protected int getMaxEmptyRecord() {
		return 10;
	}
	
	@Override
	protected Record getSaveRecord(String[] record) throws EParseExceptionEmptyRecord{
		try{
			String name=record[2];
			Float priceUsd=this.getFloatFromString(record[4].replaceAll("[.$]", ""));
			if(priceUsd.floatValue()==0)return null;
			if(isStringEmpty(record[1])&&isStringEmpty(record[2])&&isStringEmpty(record[4])){
				throw new EParseExceptionEmptyRecord();
			}
			return new Record(name, null, null, null, priceUsd, null);
		}catch(EParseExceptionEmptyRecord ex){
			throw ex;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[0])&&(isStringEmpty(record[2]))){
			return record[1];
		}else{
			return null;
		}
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://comline.com.ua/pricexls.php"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://comline.com.ua";
	}

	@Override
	protected int getMaxColumnCount() {
		return 5;
	}

	@Override
	protected int getStartRow() {
		return 0;
	}

}
