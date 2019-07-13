package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class spark_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 5;
	}

	@Override
	protected int getStartRow() {
		return 4;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[2];
			Float price=this.getFloatFromString(record[3]);
			return new Record(name, null, null, price, null, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(!isStringEmpty(record[0])&&isStringEmpty(record[1])&&isStringEmpty(record[2])){
			return record[0];
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.spark.ua/pricelist/full.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.spark.ua";
	}

}
