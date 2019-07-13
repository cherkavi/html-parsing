package shops;

import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous4;
import shop_list.html.parser.engine.record.Record;

public class lg_ua_net extends ExcelContinuous4{

	@Override
	protected int getStartRow() {
		return 0;
	}

	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[1];
			Float price=this.getFloatFromString(record[2].replaceAll("[$ ]",""));
			if(price.floatValue()==0)return null;
			return new Record(name, null, null, price, price, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://lg-ua.net/sections/price"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://lg-ua.net";
	}

	@Override
	protected int getMaxColumnCount() {
		return 3;
	}

	@Override
	protected int getColumnControlNumber() {
		return 1;
	}
}
