package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class bm_com_ua extends ExcelContinuous{
	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 5;
	}

	@Override
	protected int getStartRow() {
		return 9;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[1];
			Float price=this.getFloatFromString(this.removeCharFromString(record[3],160));
			Float priceUsd=this.getFloatFromString(this.removeCharFromString(record[4],160));
			if((price.floatValue()==0)&&(priceUsd.floatValue()==0)){
				return null;
			}
			if(!isStringEqualsAny(record[2], "+"))return null;
			return new Record(name, null, null, price, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if( !isStringEmpty(record[0])&&isStringEmpty(record[1])&&isStringEmpty(record[2])){
			return record[0];
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.bm.com.ua/files/bm_all.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.bm.com.ua";
	}

}
