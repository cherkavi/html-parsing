package shops;

import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class eplus_kiev_ua extends ExcelContinuous{

	@Override
	protected boolean isUncontinious() {
		return true;
	}
	
	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 9;
	}

	@Override
	protected int getStartRow() {
		return 5;
	}

	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[2];
			Float price=this.getFloatFromString(record[4]);
			Float priceUsd=this.getFloatFromString(record[5]);
			if (((price==null)||(price.floatValue()==0)) && ((priceUsd==null)||(priceUsd.floatValue()==0))){
				return null;
			}
			return new Record(name, null, null, price, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if((isStringEmpty(record[2])==true)&&(isStringEmpty(record[3]))){
			return record[1];
		}else{
			return null;
		}
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://eplus.kiev.ua/euro.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://eplus.kiev.ua";
	}

}
