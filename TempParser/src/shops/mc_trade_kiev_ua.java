package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class mc_trade_kiev_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 7;
	}

	@Override
	protected int getStartRow() {
		return 2;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(!isStringEqualsAny(record[6], "склад"))return null;
			String name=record[1]+" "+record[2];
			Float priceUsd=this.getFloatFromString(record[4]);
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
		return new String[]{"http://www.mc-trade.kiev.ua/mctrade_price.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.mc-trade.kiev.ua";
	}

}
