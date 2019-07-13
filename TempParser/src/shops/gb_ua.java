package shops;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.html_analisator.DynamicLinkAnalisator;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class gb_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 6;
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
	protected int getStartRow() {
		return 11;
	}

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
	
	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(isStringEmpty(record[0])&&isStringEmpty(record[1])&&isStringEmpty(record[2])){
				throw new EParseExceptionEmptyRecord();
			}
			if(this.isStringEqualsAny(record[5], "+")){
				String name=record[1];
				Float price=this.getFloatFromString(record[2]);
				if((price==null)||(price.floatValue()==0)){
					return null;
				}
				return new Record(name, null, null, price, null, null);
			}else{
				return null;
			}
		}catch(EParseException ep){
			throw ep;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(!isStringEmpty(record[0])&&isStringEmpty(record[1])&&isStringEmpty(record[2])){
		// if(record[0].equals(record[1])&&record[1].equals(record[2])){
			return record[0];
		}else{
			return null;
		}
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		DynamicLinkAnalisator analisator=new DynamicLinkAnalisator();
		String returnValue=analisator.getUrlFromPageFromElement("http://www.gb.ua/price/price.asp", 
																ECharset.WIN_1251.getName(), 
																"/html/body/table/tbody/tr[2]/td[2]/table[3]/tbody/tr/td/table/tbody/tr/td/p/a");
		return new String[]{returnValue}; 
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.gb.ua";
	}

}
