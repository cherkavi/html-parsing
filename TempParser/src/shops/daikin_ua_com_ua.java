package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous4;
import shop_list.html.parser.engine.record.Record;

public class daikin_ua_com_ua extends ExcelContinuous4{

	@Override
	protected int getColumnControlNumber() {
		return 1;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 3;
	}

	@Override
	protected int getStartRow() {
		return 0;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[1];
			Float priceUsd=this.getFloatFromString(record[2].replaceAll("[^0-9]", ""));
			if(priceUsd.floatValue()==0)return null;
			return new Record(name,null, null, null, priceUsd, null);
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
		return new String[]{"http://www.daikin-ua.com.ua/sections/praice"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.daikin-ua.com.ua";
	}

}
