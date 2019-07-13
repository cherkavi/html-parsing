package shops;

import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class dom_market_com_ua extends ExcelContinuous{

	@Override
	protected boolean isUncontinious() {
		return true;
	}
	
	@Override
	protected int getSheetNumber() {
		return 2;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 6;
	}

	@Override
	protected int getStartRow() {
		return 8;
	}

	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[1]+" "+record[2];
			Float price=this.getFloatFromString(record[3]);
			Float priceUsd=this.getFloatFromString(record[4]);
			String url=this.removeStartPage(record[5]);
			if(   ((price==null)||(price.floatValue()==0))
				&&((priceUsd==null)||(priceUsd.floatValue()==0))){
				return null;
			}
			return new Record(name, null, url, price, priceUsd,null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[1])&&isStringEmpty(record[3])&&isStringEmpty(record[4])){
			return record[0];
		}else{
			return null;
		}
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://dom-market.com.ua/price.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://dom-market.com.ua";
	}

}
