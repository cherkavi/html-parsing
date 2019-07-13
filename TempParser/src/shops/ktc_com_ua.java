package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class ktc_com_ua extends ExcelContinuous{

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 9;
	}

	@Override
	protected int getStartRow() {
		return 22;
	}
	
	@Override
	protected int getColumnControlNumber() {
		return 1;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			// if(!this.isStringEqualsAny(record[3], "ü"))return null;
			String name=record[1];
			String url=this.removeStartPage(record[8].replaceAll("[^0-9^,]", ""));
			Float price=this.getFloatFromString(record[7].replaceAll("[^0-9^,]", ""));
			Float priceUsd=this.getFloatFromString(record[6]);
			return new Record(name, null, url, price, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if( isStringEmpty(record[0])&&(!isStringEmpty(record[1]))&&isStringEmpty(record[2])){
			return record[1];
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.ktc.com.ua/prices/rozn.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.ktc.com.ua";
	}

}
