package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class technohata_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 4;
	}

	@Override
	protected int getStartRow() {
		return 1;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[1];
			Float priceUsd=this.getFloatFromString(record[2]);
			String url=this.removeStartPage(record[3]);
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
		return new String[]{"http://tehnohata.ua/img/tehnohata.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://tehnohata.ua";
	}
	
}
