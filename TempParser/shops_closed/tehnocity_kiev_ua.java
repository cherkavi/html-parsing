package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class tehnocity_kiev_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 4;
	}

	@Override
	protected int getStartRow() {
		return 6;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[2];
			Float priceUsd=this.getFloatFromString(record[3]);
			return new Record(name, null, null, null, priceUsd, null);
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
		return new String[]{"http://tehnocity.kiev.ua/price/tehnocite_price.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://tehnocity.kiev.ua";
	}
	
}
