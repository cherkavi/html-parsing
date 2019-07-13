package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class acs_net_ua extends ExcelContinuous{
	@Override
	protected int getColumnControlNumber() {
		return 1;
	}
	
	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 7;
	}

	@Override
	protected int getStartRow() {
		return 4;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[1];
			// String url=record[6];
			Float priceUsd=this.getFloatFromString(record[2]);
			return new Record(name, null, null, null, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		return record[1];
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.acs.net.ua/price/price.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.acs.net.ua";
	}

}
