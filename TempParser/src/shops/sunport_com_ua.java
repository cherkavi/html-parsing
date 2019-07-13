package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class sunport_com_ua extends ExcelContinuous{

	@Override
	protected int getColumnControlNumber() {
		return 2;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 4;
	}

	@Override
	protected int getStartRow() {
		return 3;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[2];
			Float priceUsd=this.getFloatFromString(record[3]);
			if(priceUsd.floatValue()==0)return null;
			return new Record(name, null, null,null, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[0])&&isStringEmpty(record[1])&&isStringEmpty(record[3])){
			return record[2];
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://sunport.com.ua/userfiles/price/sunport_price.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://sunport.com.ua";
	}

}
