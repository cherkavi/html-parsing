package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class sis_tema_com_ua extends ExcelContinuous{

	@Override
	protected boolean isUncontinious() {
		return true;
	}
	
	@Override
	protected int getMaxEmptyRecord() {
		return 5;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 7;
	}

	@Override
	protected int getStartRow() {
		return 5;
	}

	
	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(isStringEmpty(record[1])&&(isStringEmpty(record[3]))&&(isStringEmpty(record[4]))&&(isStringEmpty(record[5])))throw new EParseExceptionEmptyRecord();
			if(record[6].indexOf('+')<0)return null;
			String url=null;
			String name=record[1];
			Float price=this.getFloatFromString(record[3].replaceAll("[^0-9^,]", ""));
			return new Record(name, null, url, price, null, null );
		}catch(EParseExceptionEmptyRecord ep){
			throw ep;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[0])&&isStringEmpty(record[2])&&isStringEmpty(record[3])&&isStringEmpty(record[4])){
			if(!isStringEmpty(record[1])){
				int dotPosition=record[1].indexOf('.');
				if(dotPosition>=0){
					return record[1].substring(dotPosition+1);
				}
			}
		}
		return null;
	}

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
	
	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.sis-tema.com.ua/downloads/prices/price.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.sis-tema.com.ua";
	}

}
