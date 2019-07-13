package shops;

import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class boom_net_ua extends ExcelContinuous{
	
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
		return 6;
	}

	@Override
	protected int getStartRow() {
		return 12;
	}

	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[1];
			Float price=this.getFloatFromString(record[4]);
			Float priceUsd=this.getFloatFromString(record[5]);
			if (((price==null)||(price.floatValue()==0))&&((priceUsd==null)||(priceUsd.floatValue()==0))) return null;
			if(!this.isStringEqualsAny(record[3], "+"))return null;
			return new Record(name, null, null, price, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		try{
			if(isStringEmpty(record[0])&&isStringEmpty(record[2])){
				return record[1];
			}else{
				return null;
			}
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://boom.net.ua/prices/price.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://boom.net.ua";
	}

}
