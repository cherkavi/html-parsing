package shops;

import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class ateks_com_ua extends ExcelContinuous{

	@Override
	protected boolean isUncontinious() {
		return true;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 5;
	}

	@Override
	protected int getStartRow() {
		return 7;
	}

	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[1];
			Float priceUsd=this.getFloatFromString(record[3]);
			if((priceUsd==null)||(priceUsd.floatValue()==0))return null;
			if(!isStringEqualsAny(record[4],"+","заказ")){
				return null; 
			}
			return new Record(name, null, null, null, priceUsd,null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[0])&&isStringEmpty(record[2])&&isStringEmpty(record[3])){
			return record[2];
		}else{
			return null;
		}
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://ateks.com.ua/files/price.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://ateks.com.ua";
	}

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
}
