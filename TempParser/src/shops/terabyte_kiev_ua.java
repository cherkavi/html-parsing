package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class terabyte_kiev_ua extends ExcelContinuous{

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
		return 5;
	}

	@Override
	protected int getStartRow() {
		return 5;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(isStringEmpty(record[0])&&isStringEmpty(record[1])&&isStringEmpty(record[2])&&isStringEmpty(record[3])){
				throw new EParseExceptionEmptyRecord();
			}
			String name=record[1]+" "+record[2]+" "+record[3];
			Float price=this.getFloatFromString(record[4]);
			return new Record(name, null, null, price, null, null);
		}catch(EParseExceptionEmptyRecord pe){
			throw pe;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(!isStringEmpty(record[0])){
			int indexCurve=record[0].indexOf('(');
			if(indexCurve>0){
				return record[0].substring(0,indexCurve);
			}else{
				return record[0];
			}
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://terabyte.kiev.ua/price/price.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://terabyte.kiev.ua";
	}

}
