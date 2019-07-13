package shops;

import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class evrotel_com_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 7;
	}

	@Override
	protected int getStartRow() {
		return 7;
	}

	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[0];
			Float price=this.getFloatFromString(record[4]);
			Float priceUsd=this.getFloatFromString(record[5]);
			if((price.floatValue()==0)||(priceUsd.floatValue()==0)){
				return null;
			}
			return new Record(name, null, null, price, priceUsd,null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		try{
			if(record[0].equals(record[1].equals(record[2]))){
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
		return new String[]{"http://evrotel.com.ua/evrotel.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://evrotel.com.ua";
	}

}
