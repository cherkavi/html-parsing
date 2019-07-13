package shops;

import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class btplus_com_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 3;
	}

	@Override
	protected int getStartRow() {
		return 3;
	}

	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[1];
			Float priceUsd=this.getFloatFromString(record[2]);
			if((priceUsd==null)||(priceUsd.floatValue()==0))return null;
			return new Record(name, null, null, null, priceUsd, null);
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
		return new String[]{"http://btplus.com.ua/Price_BtPlus.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://btplus.com.ua";
	}


	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
}
