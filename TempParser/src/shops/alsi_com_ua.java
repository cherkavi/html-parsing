package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.file_parser.EFileTypes;

import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous4;
import shop_list.html.parser.engine.record.Record;

public class alsi_com_ua extends ExcelContinuous4{

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 8;
	}

	@Override
	protected int getStartRow() {
		return 11;
	}

	@Override
	protected boolean isUncontinious() {
		return true;
	}
	
	@Override
	protected int getMaxEmptyRecord() {
		return 5;
	}
	
	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(isStringEmpty(record[0])&&isStringEmpty(record[1])&&isStringEmpty(record[2]))throw new EParseExceptionEmptyRecord();
			if(!isStringEqualsAny(record[5], "на складе", "доступно"))return null;
			return new Record(record[3], null, null, null, this.getFloatFromString(record[7]), null);
		}catch(EParseExceptionEmptyRecord ep){
			throw ep;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[1])&&isStringEmpty(record[2])){
			return record[0];
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.alsi.com.ua/PRICE.ZIP"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.alsi.com.ua";
	}

}
