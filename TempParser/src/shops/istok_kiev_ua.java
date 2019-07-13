package shops;

import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class istok_kiev_ua extends ExcelContinuous{

	protected shop_list.html.parser.engine.file_parser.EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	};
	
	@Override
	protected int getMaxColumnCount() {
		return 10;
	}

	@Override
	protected int getStartRow() {
		return 5;
	}

	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[2];
			String description=record[3];
			Float price=this.getFloatFromString(record[4]);
			Float priceUsd=this.getFloatFromString(record[5]);
			if((price.floatValue()==0)&&(priceUsd.floatValue()==0))return null;
			String url=this.removeStartPage(record[9]);
			if(!isStringEqualsAny(record[8], "есть","склад")){
				return null;
			}
			return new Record(name, description ,url, price, priceUsd, null );
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		return record[0];
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://istok.kiev.ua/price_istok.kiev.ua.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://istok.kiev.ua";
	}

}
