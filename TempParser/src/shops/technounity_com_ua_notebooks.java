package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class technounity_com_ua_notebooks extends ExcelContinuous{
	@Override
	protected int getColumnControlNumber() {
		return 1;
	}

	@Override
	protected int getMaxColumnCount() {
		return 5;
	}

	@Override
	protected int getStartRow() {
		return 9;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[1];
			Float price=this.getFloatFromString(record[2]);
			Float priceUsd=this.getFloatFromString(record[3]);
			if((price.floatValue()==0)&&(priceUsd.floatValue()==0))return null;
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
		return new String[]{"http://technounity.com.ua/TUnity_nbpc.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://technounity.com.ua";
	}

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
}
