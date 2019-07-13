package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous4;
import shop_list.html.parser.engine.record.Record;

public class dtrade_com_ua extends ExcelContinuous4 {

	@Override
	protected int getMaxColumnCount() {
		return 6;
	}

	@Override
	protected int getStartRow() {
		return 0;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[1]+" "+record[2];
			String url=this.removeStartPage(record[3]);
			Float priceUsd=this.getFloatFromString(record[5]);
			return new Record(name, null, url, null, priceUsd, null);
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
		return new String[]{"http://www.dtrade.com.ua/index.php?section=price"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.dtrade.com.ua";
	}

}
