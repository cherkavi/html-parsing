package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class te_com_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 9;
	}

	@Override
	protected int getStartRow() {
		return 2;
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
			if(isStringEmpty(record[0])&&isStringEmpty(record[1])&&isStringEmpty(record[2])&&isStringEmpty(record[3]))throw new EParseExceptionEmptyRecord();
			String name=record[1]+" "+record[2];
			Float price=this.getFloatFromString(record[4]);
			Float priceUsd=this.getFloatFromString(record[3]);
			String url=this.removeStartPage(record[8]);
			return new Record(name, null, url, price, priceUsd, null);
		}catch(EParseException ep){
			throw ep;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(!isStringEmpty(record[0])&&isStringEmpty(record[1])&&isStringEmpty(record[2])){
			return record[0];
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.te.com.ua/prices/topaz.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.te.com.ua";
	}

}
