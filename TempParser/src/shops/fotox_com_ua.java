package shops;

import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous4;
import shop_list.html.parser.engine.record.Record;

public class fotox_com_ua extends ExcelContinuous4{

	@Override
	protected int getStartRow() {
		return 10;
	}

	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[1]+" "+record[2];
			Float priceUsd=this.getFloatFromString(record[4].replaceAll("[ $]", ""));
			if((priceUsd==null)||(priceUsd.floatValue()==0))return null;
			if(!isStringEqualsAny(record[6], "Есть"))return null;
			return new Record(name, null, null, null, priceUsd, null);
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
		return new String[]{"http://fotox.com.ua/price.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://fotox.com.ua";
	}

	@Override
	protected int getMaxColumnCount() {
		return 7;
	}

}
