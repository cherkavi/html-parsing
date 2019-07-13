package shops;

import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous4;
import shop_list.html.parser.engine.record.Record;

public class eps_kiev_ua extends ExcelContinuous4{

	@Override
	protected int getStartRow() {
		return 1;
	}

	@Override
	protected int getColumnControlNumber() {
		return 2;
	}

	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[2];
			Float priceUsd=this.getFloatFromString(record[3]);
			if((priceUsd==null)||(priceUsd.floatValue()==0))return null;
			if(this.isStringEqualsAny(record[5], "есть")==false)return null;
			return new Record(name, null, null, null, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(this.isStringEmpty(record[0])==false){
			return record[0];
		}else{
			return null;
		}
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://eps.kiev.ua/default.aspx?do=export_excel"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://eps.kiev.ua";
	}

	@Override
	protected int getMaxColumnCount() {
		return 6;
	}

}
