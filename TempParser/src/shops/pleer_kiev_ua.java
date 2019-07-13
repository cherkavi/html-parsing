package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class pleer_kiev_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 7;
	}

	@Override
	protected int getStartRow() {
		return 1;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[2]+" "+record[3];
			Float price=this.getFloatFromString(record[4]);
			String url=this.removeStartPage(record[6]);
			return new Record(name, null, url, price, null, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		return record[1];
	}

	@Override
	protected int getColumnControlNumber() {
		return 1;
	}
	
	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.pleer.kiev.ua/uploads/data/price.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.pleer.kiev.ua";
	}

}
