package shops;

import shop_list.html.parser.engine.file_parser.exception.EParseException;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class tehnodom_kiev_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 6;
	}

	@Override
	protected int getStartRow() {
		return 1;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[3]+" "+record[4];
			Float priceUsd=this.getFloatFromString(record[5]);
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
		return new String[]{"http://www.technodom.kiev.ua/price/price.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.technodom.kiev.ua";
	}

}
