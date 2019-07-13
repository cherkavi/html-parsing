package shops;

import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class atlant_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 9;
	}

	@Override
	protected int getStartRow() {
		return 1;
	}

	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[1]+" "+record[2];
			Float price=this.getFloatFromString(record[4]);
			if((price==null)||(price.floatValue()==0))return null;
			if(!isStringEqualsAny(record[6], "есть на складе"))return null;
			String url=this.removeStartPage(record[8]);
			return  new Record(name,null,url, price,null,null);
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
		return new String[]{"http://atlant.ua/price/price1.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://atlant.ua";
	}

}
