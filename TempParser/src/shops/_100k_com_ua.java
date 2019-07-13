package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class _100k_com_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 4;
	}

	@Override
	protected int getStartRow() {
		return 2;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[1]+" "+record[2];
			Float price=this.getFloatFromString(record[3]);
			if(price.floatValue()==0)return null;
			return new Record(name, null, null, price, null, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
	
	@Override
	protected String getSectionNameFromRecord(String[] record) {
		return record[0];
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.100k.com.ua/price.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.100k.com.ua";
	}

}
