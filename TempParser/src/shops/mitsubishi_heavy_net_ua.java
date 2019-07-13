package shops;

import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous4;
import shop_list.html.parser.engine.record.Record;

public class mitsubishi_heavy_net_ua extends ExcelContinuous4{

	@Override
	protected int getStartRow() {
		return 0;
	}

	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[1];
			Float priceUsd=this.getFloatFromString(record[2].replaceAll("[$ ]",""));
			return new Record(name, null, null, null, priceUsd, null);
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
		return new String[]{"http://mitsubishi-heavy.net.ua/sections/praice"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://mitsubishi-heavy.net.ua";
	}

	@Override
	protected int getMaxColumnCount() {
		return 3;
	}

}
