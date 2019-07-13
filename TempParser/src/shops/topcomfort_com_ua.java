package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous4;
import shop_list.html.parser.engine.record.Record;

public class topcomfort_com_ua extends ExcelContinuous4{

	@Override
	protected int getColumnControlNumber() {
		return 1;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 5;
	}

	@Override
	protected int getStartRow() {
		return 1;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[1]+" "+record[2];
			String url=this.removeStartPage(record[4]);
			Float priceUsd=this.getFloatFromString(record[3]);
			if(priceUsd.floatValue()==0)return null;
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
		return new String[]{"http://topcomfort.com.ua/?price=1"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://topcomfort.com.ua";
	}

}
