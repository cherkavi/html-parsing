package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class permyco_kiev_ua_2 extends ExcelContinuous{

	@Override
	protected boolean isUncontinious() {
		return true;
	}
	
	@Override
	protected int getMaxEmptyRecord() {
		return 10;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 3;
	}

	@Override
	protected int getStartRow() {
		return 9;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(isStringEmpty(record[0])&&isStringEmpty(record[1]))throw new EParseExceptionEmptyRecord();
			String name=record[0];
			Float priceUsd=this.getFloatFromString(record[2]);
			if(priceUsd.floatValue()==0)return null;
			return new Record(name, null, null, null, priceUsd, null);
		}catch(EParseException ep){
			throw ep;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		return "LCD телевизоры";
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.permyco.kiev.ua/images/LCD-TV_Ukraine_Stock.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.permyco.kiev.ua";
	}

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
}
