package shops;

import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class akviss_com_ua extends ExcelContinuous{


	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[0];
			Float price=this.getFloatFromString(record[2]);
			if((price==null)||(price.floatValue()==0))return null;
			return new Record(name, null, null, price,null,null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[1])&&isStringEmpty(record[2])){
			return record[0];
		}else{
			return null;
		}
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://akviss.com.ua/price_akv.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://akviss.com.ua";
	}

	@Override
	protected int getMaxColumnCount() {
		return 3;
	}

	@Override
	protected int getStartRow() {
		return 11;
	}

}
