package shops;

import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class homestore_com_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 10;
	}

	@Override
	protected int getStartRow() {
		return 8;
	}

	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[3];
			String description=record[4];
			String url=this.removeStartPage(record[9]);
			Float priceUsd=this.getFloatFromString(record[6]);
			if((priceUsd==null)||(priceUsd.floatValue()==0))return null;
			return new Record(name, description, url, null, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
		
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		return record[1];
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://homestore.com.ua/cache/export_products.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://homestore.com.ua";
	}

}
