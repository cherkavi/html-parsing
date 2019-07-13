package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class iline_in_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 4;
	}

	@Override
	protected int getColumnControlNumber() {
		return 1;
	}
	
	@Override
	protected int getStartRow() {
		return 1;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[1];
			Float priceUsd=this.getFloatFromString(record[3]);
			if(priceUsd.floatValue()==0)return null;
			return new Record(name, null, null, null, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[0])&&(!isStringEmpty(record[1]))&&isStringEmpty(record[2]) ){
			return record[1];
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.iline.in.ua/price/price_apple.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.iline.in.ua";
	}

}
