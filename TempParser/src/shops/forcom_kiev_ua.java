package shops;

import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class forcom_kiev_ua extends ExcelContinuous{

	@Override
	protected boolean isUncontinious() {
		return true;
	}
	
	@Override
	protected int getColumnFirst() {
		return 1;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 8;
	}

	@Override
	protected int getStartRow() {
		return 9;
	}

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
	
	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[2];
			Float price=this.getFloatFromString(record[5]);
			Float priceUsd=this.getFloatFromString(record[6]);
			if(((price==null)||(price.floatValue()==0))&&((priceUsd==null)||(priceUsd.floatValue()==0))){
				return null;
			}
			if(!isStringEqualsAny(record[4], "+", "резерв")){
				return null;
			}
			return new Record(name, null, null, price, priceUsd, null );
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[0])&&isStringEmpty(record[3])){
			return record[1];
		}else{
			return null;
		}
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://forcom.kiev.ua/download/FORCOM.ZIP"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://forcom.kiev.ua";
	}

}
