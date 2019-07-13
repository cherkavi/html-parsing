package shops;

import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class electrovoz_com_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 8;
	}

	@Override
	protected int getStartRow() {
		return 10;
	}

	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[2];
			Float price=this.getFloatFromString(record[4]);
			String url=this.removeStartPage(record[6]);
			if((price==null)||(price.floatValue()==0))return null;
			Float priceUsd=this.getFloatFromString(record[7]);
			return new Record(name, null, url, price, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		return record[0];
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://electrovoz.com.ua/electrovoz.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://electrovoz.com.ua";
	}
	
}
