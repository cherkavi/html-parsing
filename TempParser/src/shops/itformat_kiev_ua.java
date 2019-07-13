package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class itformat_kiev_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 5;
	}

	@Override
	protected int getStartRow() {
		return 3;
	}

	@Override
	protected int getColumnControlNumber() {
		return 1;
	}
	
	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[2];
			Float price=this.getFloatFromString(record[4]);
			Float priceUsd=this.getFloatFromString(record[3]);
			return new Record(name, null, null, price, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[3])&&isStringEmpty(record[4])&&(!isStringEmpty(record[2]))){
			return record[2].trim();
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.itformat.kiev.ua/export/price.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.itformat.kiev.ua";
	}

}
