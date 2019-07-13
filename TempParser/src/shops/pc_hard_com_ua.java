package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class pc_hard_com_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 10;
	}

	@Override
	protected boolean isUncontinious() {
		return true;
	}
	
	@Override
	protected int getMaxEmptyRecord() {
		return 10;
	}
	
	@Override
	protected int getStartRow() {
		return 2;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(isStringEmpty(record[0])&&isStringEmpty(record[1])&&isStringEmpty(record[2])&&isStringEmpty(record[3])&&isStringEmpty(record[4])){
				throw new EParseExceptionEmptyRecord();
			}
			String name=record[3];
			Float priceUsd=this.getFloatFromString(record[8]);
			Float price=this.getFloatFromString(record[9].replaceAll("[ ]","").replaceAll("$.","").replaceAll("[^0-9^.]",""));
			return new Record(name, null, null, price, priceUsd, null);
		}catch(EParseException ep){
			throw ep;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(!isStringEmpty(record[0])&&isStringEmpty(record[1])&&isStringEmpty(record[2])&&isStringEmpty(record[3])){
			return record[0];
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.pc-hard.com.ua/e_price-Petruk.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.pc-hard.com.ua";
	}

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
}
