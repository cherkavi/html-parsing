package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class dmarket_com_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 10;
	}

	@Override
	protected int getStartRow() {
		return 5;
	}

	@Override
	protected int getColumnControlNumber() {
		return 2;
	}
	
	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[3]+" "+record[4];
			Float price=this.getFloatFromString(record[6]);
			Float priceUsd=this.getFloatFromString(record[7]);
			String url=this.removeStartPage(record[9]);
			return new Record(name, null, url, price, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		return record[2];
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.dmarket.com.ua/price.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.dmarket.com.ua";
	}

}
