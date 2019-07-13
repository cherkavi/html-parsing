package shops;

import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class forsale_com_ua extends ExcelContinuous{

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
		return 8;
	}

	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[0];
			String url=this.removeStartPage(record[6]);
			Float price=this.getFloatFromString(record[2]);
			Float priceUsd=this.getFloatFromString(record[3]);
			if( ((price==null)||(price.floatValue()==0))&&( (priceUsd==null)||(priceUsd.floatValue()==0) ) ){
				return null;
			}
			if(!isStringEqualsAny(record[5], "+")){
				return null;
			}
			return new Record(name, null, url, price, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[1])&&isStringEmpty(record[2])){
			return record[0]; 
		}else{
			return null;
		}
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://forsale.com.ua/price.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://forsale.com.ua";
	}

}
