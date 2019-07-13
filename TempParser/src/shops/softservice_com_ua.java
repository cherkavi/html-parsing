package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class softservice_com_ua extends ExcelContinuous{

	
	
	@Override
	protected int getMaxColumnCount() {
		return 13;
	}

	@Override
	protected int getStartRow() {
		return 9;
	}
	
	
	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(isStringEmpty(record[0])&&isStringEmpty(record[1])&&isStringEmpty(record[2])&&isStringEmpty(record[3])&&isStringEmpty(record[4])){
				throw new EParseExceptionEmptyRecord();
			}
			String name=record[2];
			String url=this.removeStartPage(record[12]);
			if(url.indexOf("http")==0){
				url=null;
			}
			Float priceUsd=this.getFloatFromString(record[3]);
			return new Record(name, null, url, null, priceUsd, null);
		}catch(EParseException ep){
			throw ep;
		}catch(Exception ex){
			return null;
		}
	}
	
	@Override
	protected boolean isUncontinious() {
		return true;
	}

	@Override
	protected int getMaxEmptyRecord() {
		return 7;
	}
	
	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(!isStringEmpty(record[0])&&isStringEmpty(record[1])&&isStringEmpty(record[2])){
			int indexOfDot=record[0].indexOf('.');
			if(indexOfDot>0){
				return record[0].substring(indexOfDot+1);
			}
			return record[0];
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.softservice.com.ua/files/softserv.zip"};
	}

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
	
	@Override
	public String getShopUrlStartPage() {
		return "http://www.softservice.com.ua";
	}

}
