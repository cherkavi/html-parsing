package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class challenge_kiev_ua_comp extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 3;
	}

	@Override
	protected int getStartRow() {
		return 3;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[1];
			Float price=this.getFloatFromString(record[2].replaceAll("[^0-9^,]", ""));
			if(price.floatValue()==0)return null;
			return new Record(name, null, null, price, null, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		return "Компьютеры";
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.challenge.kiev.ua/images/stories/fruit/prices/comp.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.challenge.kiev.ua";
	}

}
