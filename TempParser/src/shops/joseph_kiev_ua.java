package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class joseph_kiev_ua extends ExcelContinuous{

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 6;
	}

	@Override
	protected int getStartRow() {
		return 6;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[3];
			Float priceUsd=this.getFloatFromString(record[4]);
			return new Record(name, null, null, null, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected int getColumnControlNumber() {
		return 2;
	}
	
	@Override
	protected String getSectionNameFromRecord(String[] record) {
		return record[1];
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.joseph.kiev.ua/Uploaded/Joseph-Price.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.joseph.kiev.ua";
	}

}
