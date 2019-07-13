package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class xcomp_kiev_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 9;
	}

	@Override
	protected int getStartRow() {
		return 3;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(!isStringEqualsAny(record[8], "Есть"))return null;
			String name=record[4];
			Float priceUsd=this.getFloatFromString(record[5]);
			return new Record(name, null, null, null, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		return record[2];
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.xcomp.kiev.ua/prices/xcprice-full.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.xcomp.kiev.ua";
	}

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
}
