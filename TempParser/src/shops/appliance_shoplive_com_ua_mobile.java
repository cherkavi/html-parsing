package shops;

import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class appliance_shoplive_com_ua_mobile extends ExcelContinuous{

	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[0];
			Float priceUSD=this.getFloatFromString(record[1]);
			if((priceUSD==null)||(priceUSD.floatValue()==0))return null;
			if(isStringEmpty(record[2]))return null;
			return new Record(name, null, null, null,priceUSD,null);
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
		return new String[]{"http://www.shoplive.com.ua/price_phones.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://appliance.shoplive.com.ua/";
	}

	@Override
	protected int getMaxColumnCount() {
		return 4;
	}

	@Override
	protected int getStartRow() {
		return 6;
	}

}
