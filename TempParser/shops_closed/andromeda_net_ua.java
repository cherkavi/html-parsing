package shops;

import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class andromeda_net_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 5;
	}

	@Override
	protected int getStartRow() {
		return 1;
	}

	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[2];
			Float price=this.getFloatFromString(record[4].toUpperCase().replaceAll("[\\.À-ß]",""));
			if((price==null)||(price.floatValue()==0))return null;
			return new Record(name,null,null,price,null,null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if (isStringEmpty(record[0])&&(isStringEmpty(record[2]))){
			return record[1];
		}else{
			return null;
		}
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://andromeda.net.ua/pricexls.php"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://andromeda.net.ua";
	}

}
