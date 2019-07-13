package shops;

import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class jera_ua_com extends ExcelContinuous{

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
			Float price=this.getFloatFromString(record[3]);
			if(price.floatValue()==0)return null;
			if(!isStringEqualsAny(record[4], "+"))return null;
			return new Record(name, null, null, price, null, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[2])&&isStringEmpty(record[3])&&isStringEmpty(record[4])){
			return record[1];
		}else{
			return null;
		}
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://jera-ua.com/goods/price.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://jera-ua.com";
	}

}
