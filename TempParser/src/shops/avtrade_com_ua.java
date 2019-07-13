package shops;

import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class avtrade_com_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 4;
	}

	@Override
	protected int getStartRow() {
		return 14;
	}

	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[0];
			Float priceUsd=this.getFloatFromString(record[1]);
			if((priceUsd==null)||(priceUsd.floatValue()==0)) return null;
			if(!isStringEqualsAny(record[3], "+","резерв"))return null;
			return new Record(name, null, null, null, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		try{
			if(record[0].equals(record[1])){
				return record[0];
			}else{
				return null;
			}
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://avtrade.com.ua/prices/comp.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://avtrade.com.ua";
	}

}
