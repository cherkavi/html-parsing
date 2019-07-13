package shops;

import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class dyson_shop_com_ua extends ExcelContinuous{

	@Override
	protected int getColumnFirst() {
		return 1;
	}
	
	protected int getColumnControlNumber() {
		return 2;
	};
	@Override
	protected int getMaxColumnCount() {
		return 11;
	}

	@Override
	protected int getStartRow() {
		return 4;
	}

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
	
	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[1];
			Float price=this.getFloatFromString(record[2]);
			if((price==null)||(price.floatValue()==0))return null;
			String url=this.removeStartPage(record[10]);
			return new Record(name, null, url, price, null, null);
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
		return new String[]{"http://dyson-shop.com/files/price.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://dyson-shop.com";
	}

}
