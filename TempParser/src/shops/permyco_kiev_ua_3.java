package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class permyco_kiev_ua_3 extends ExcelContinuous{

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
		return 5;
	}

	@Override
	protected int getStartRow() {
		return 7;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(isStringEmpty(record[1])&&isStringEmpty(record[2]))throw new EParseExceptionEmptyRecord();
			String name=record[2];
			Float priceUsd=this.getFloatFromString(record[4]);
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
		return "Плазменные телевизоры";
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.permyco.kiev.ua/images/Plasma_Ukraine_Stock.zip"};
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
