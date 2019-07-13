package shops;

import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class e34_com_ua extends ExcelContinuous{

	@Override
	protected boolean isUncontinious() {
		return true;
	}
	
	@Override
	protected int getMaxCellsCount() {
		return 5;
	}

	@Override
	protected int getStartRow() {
		return 4;
	}

	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[0];
			Float priceUsd=this.getFloatFromString(record[4].replaceAll("\\$", "").trim());
			if((priceUsd==null)||(priceUsd.floatValue()==0))return null;
			return new Record(name, null, null, null, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		return (isStringEmpty(record[0])?null:record[0]);
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://e34.com.ua/pricexls.php"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://e34.com.ua";
	}

}
