package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class soloshop_com_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 10;
	}

	@Override
	protected int getStartRow() {
		return 1;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(!isStringEqualsAny(record[7], "есть на складе"))return null;
			Float price=this.getFloatFromString(this.removeCharFromString(record[4],160));
			Float priceUsd=this.getFloatFromString(this.removeCharFromString(record[5],160));
			if((price.floatValue()==0)&&(priceUsd.floatValue()==0))return null;
			String name=record[2];
			String url=this.removeStartPage(record[9]);
			return new Record(name, null, url, price, priceUsd, null);
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
		return new String[]{"http://soloshop.com.ua/userfiles/prices/solo-price.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://soloshop.com.ua";
	}

}
