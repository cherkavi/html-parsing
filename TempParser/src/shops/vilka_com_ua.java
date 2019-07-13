package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class vilka_com_ua extends ExcelContinuous{

	
	
	@Override
	protected int getMaxColumnCount() {
		return 6;
	}

	@Override
	protected int getStartRow() {
		return 5;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(isStringEmpty(record[2]))return null;
			String name=record[1];
			Float priceUsd=this.getFloatFromString(record[4]);
			Float price=this.getFloatFromString(record[5]);
			return new Record(name, null, null, price, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(!isStringEmpty(record[0])&&isStringEmpty(record[1])&&isStringEmpty(record[2])){
			return record[0];
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.vilka.com.ua/price.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.vilka.com.ua";
	}

}
