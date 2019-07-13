package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous4;
import shop_list.html.parser.engine.record.Record;

public class aksmarket_com_ua extends ExcelContinuous4{

	@Override
	protected int getMaxColumnCount() {
		return 2;
	}

	@Override
	protected int getStartRow() {
		return 2;
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
			if(isStringEmpty(record[0])&&isStringEmpty(record[1])){
				System.out.println("empty");
				throw new EParseExceptionEmptyRecord();
			}
			String name=record[0];
			Float price=this.getFloatFromString(record[1]);
			if(price.floatValue()==0)return null;
			return new Record(name, null, null, price, null, null);
		}catch(EParseExceptionEmptyRecord ep){
			throw ep;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		try{
			if(record[1].indexOf("Цена")>=0){
				return record[0];
			}else{
				return null;
			}
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.aksmarket.com.ua/price.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.aksmarket.com.ua";
	}

}
