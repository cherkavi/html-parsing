package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous4;
import shop_list.html.parser.engine.record.Record;

public class technounity_com_ua_parts extends ExcelContinuous4{

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
	protected int getMaxColumnCount() {
		return 7;
	}

	@Override
	protected int getStartRow() {
		return 10;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(isStringEmpty(record[1])&&isStringEmpty(record[2])&&isStringEmpty(record[3])&&isStringEmpty(record[4])){
				throw new EParseExceptionEmptyRecord();
			}
			String name=record[3];
			Float price=this.getFloatFromString(record[5]);
			Float priceUsd=this.getFloatFromString(record[6]);
			if((price.floatValue()==0)&&(priceUsd.floatValue()==0))return null;
			return new Record(name, null, null, price, priceUsd, null);
		}catch(EParseExceptionEmptyRecord ep){
			throw ep;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		try{
			if(isStringEmpty(record[3])&&isStringEmpty(record[4])&&isStringEmpty(record[5])){
				if(isStringEmpty(record[2])){
					return record[3];
				}else{
					return record[2];
				}
			}else{
				return null;
			}
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://technounity.com.ua/TUnity_price.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://technounity.com.ua";
	}

}
