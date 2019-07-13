package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class pro100r_com_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 6;
	}

	@Override
	protected int getStartRow() {
		return 2;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[1]+" "+record[2];
			Float price=this.getFloatFromString(record[4]);
			Float priceUsd=this.getFloatFromString(record[3]);
			return new Record(name, null, null, price, priceUsd, null);
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
		return new String[]{"http://www.pro100r.com.ua/downloads/price.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.pro100r.com.ua";
	}

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
}
