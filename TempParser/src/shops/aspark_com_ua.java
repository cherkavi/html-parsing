package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class aspark_com_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 8;
	}

	@Override
	protected int getStartRow() {
		return 4;
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
			if(isStringEmpty(record[0])&&isStringEmpty(record[2])&&isStringEmpty(record[3])&&isStringEmpty(record[4]))throw new EParseExceptionEmptyRecord();
			// if(!isStringEqualsAny(record[7], "Есть"))return null;
			String name=record[2];
			String url=this.removeStartPage(record[7]);
			if(url.indexOf("http")==0){
				url=null;
			}
			Float price=this.getFloatFromString(record[4]);
			Float priceUsd=this.getFloatFromString(record[5]);
			return new Record(name, null, url, price, priceUsd, price);
		}catch(EParseException ep){
			throw ep;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[8])&&isStringEmpty(record[9])&&isStringEmpty(record[7])){
			if(isStringEmpty(record[0])){
				return null;
			}
			return record[0];
		}else{
			return null;
		}
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.aspark.com.ua/pricexls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.aspark.com.ua";
	}
	
}
