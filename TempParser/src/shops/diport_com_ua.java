package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class diport_com_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 9;
	}

	@Override
	protected int getStartRow() {
		return 4;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[1]+" "+record[2];
			String url=this.removeStartPage(record[8]);
			Float priceUsd=this.getFloatFromString(record[4]);
			if(!isStringEqualsAny(record[5],"есть на складе"))return null;
			return new Record(name, null, url, null, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		return record[1];
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.diport.com.ua/export/price.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.diport.com.ua";
	}

}
