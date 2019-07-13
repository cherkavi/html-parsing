package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class cost_com_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 6;
	}

	@Override
	protected int getStartRow() {
		return 12;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[2];
			String url=null; // record[2];
			Float price=this.getFloatFromString(record[5]);
			Float priceUsd=this.getFloatFromString(record[4]);
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
		return new String[]{"http://www.cost.com.ua/cost/download/Price_WWWCost.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.cost.com.ua";
	}

}
