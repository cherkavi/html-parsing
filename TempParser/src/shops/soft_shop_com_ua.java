package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class soft_shop_com_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 7;
	}

	@Override
	protected int getStartRow() {
		return 10;
	}

	@Override
	protected int getColumnControlNumber() {
		return 1;
	}
	
	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[1];
			Float priceUsd=this.getFloatFromString(record[5].replaceAll("[^0-9^,]", ""));
			return new Record(name, null, null, null, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(!isStringEmpty(record[1])&&isStringEmpty(record[5])&&isStringEmpty(record[6])){
			return record[1];
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.soft-shop.com.ua/price.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.soft-shop.com.ua";
	}

}
