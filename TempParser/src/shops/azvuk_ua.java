package shops;

import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous4;
import shop_list.html.parser.engine.record.Record;

public class azvuk_ua extends ExcelContinuous4{

	@Override
	protected int getStartRow() {
		return 0;
	}

	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[1];
			Float priceUsd=this.getFloatFromString(record[3]);
			Float price=this.getFloatFromString(record[4]);
			if((priceUsd==null)||(priceUsd.floatValue()==0)) return null;
			return new Record(name, null, null, price, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://azvuk.ua/pricedownload.html"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://azvuk.ua";
	}

	@Override
	protected int getMaxColumnCount() {
		return 6;
	}

}
