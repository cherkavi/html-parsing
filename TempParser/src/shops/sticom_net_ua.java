package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class sticom_net_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 6;
	}

	@Override
	protected int getStartRow() {
		return 7;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(isStringEmpty(record[0])&&isStringEmpty(record[1])&&isStringEmpty(record[2])&&isStringEmpty(record[3]))throw new EParseExceptionEmptyRecord();
			if(!isStringEqualsAny(record[5], "+"))return null;
			String name=record[0];
			Float price=this.getFloatFromString(record[2]);
			Float priceUsd=this.getFloatFromString(record[3]);
			return new Record(name, null, null, price, priceUsd, null);
		}catch(EParseException ep){
			throw ep;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(!isStringEmpty(record[1])&&(!isStringEmpty(record[2]))){
			if( (record[1].trim().equalsIgnoreCase("√ар"))&& (record[2].trim().equalsIgnoreCase("грн"))){
				return record[0];
			}
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.sticom.net.ua/data/price/price.zip"};
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
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
	
	@Override
	public String getShopUrlStartPage() {
		return "http://www.sticom.net.ua";
	}

}
