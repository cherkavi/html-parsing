package shops;

import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class linecom_kiev_ua extends ExcelContinuous{

	protected shop_list.html.parser.engine.file_parser.EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	};
	
	@Override
	protected int getMaxColumnCount() {
		return 6;
	}

	@Override
	protected int getStartRow() {
		return 7;
	}

	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[2];
			Float price=this.getFloatFromString(record[4]);
			Float priceUsd=this.getFloatFromString(record[3]);
			if  ((price==null)||(price.floatValue()==0)&&((priceUsd==null)||(price.floatValue()==0))){
				return null;
			}
			return new Record(name, null, null, price, priceUsd, null);
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
		return new String[]{"http://linecom.kiev.ua/file.php?id=49"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://linecom.kiev.ua";
	}

}
