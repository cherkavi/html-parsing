package shops;

import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.file_parser.EFileTypes;

import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous4;
import shop_list.html.parser.engine.record.Record;

public class microstore_com_ua extends ExcelContinuous4{
	
	@Override
	protected boolean isUncontinious() {
		return true;
	}
	
	@Override
	protected int getMaxEmptyRecord() {
		return 5;
	}
	
	@Override
	protected int getStartRow() {
		return 9;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseExceptionEmptyRecord{
		try{
			if(isStringEmpty(record[0])&&isStringEmpty(record[1])&&isStringEmpty(record[2])&&isStringEmpty(record[3])){
				throw new EParseExceptionEmptyRecord();
			}
			String name=record[2];
			Float priceUsd=this.getFloatFromString(record[3]);
			if(priceUsd.floatValue()==0)return null;
			if(!isStringEqualsAny(record[6], "+","+-"))return null;
			return new Record(name, null, null, null, priceUsd,null);
		}catch(EParseExceptionEmptyRecord ep){
			throw ep;
		}
		catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if( isStringEmpty(record[1])&&isStringEmpty(record[2])&&isStringEmpty(record[3])  ){
			return record[0];
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://microstore.com.ua/price/microstore.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://microstore.com.ua";
	}

	@Override
	protected int getMaxColumnCount() {
		return 7;
	}

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
}
