package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class notebookshop_com_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 7;
	}

	@Override
	protected int getStartRow() {
		return 1;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(!this.isStringEqualsAny(record[4], "склад"))return null;
			String name=record[2];
			Float priceUsd=this.getFloatFromString(record[3].replaceAll("[^0-9]", ""));
			String url=this.removeStartPage(record[6]);
			return new Record(name, null, url, null, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[1])&&isStringEmpty(record[3])&&(!isStringEmpty(record[2]))){
			return record[2];
		}
		return null;
	}

	@Override
	protected int getColumnControlNumber() {
		return 2;
	}
	
	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.notebookshop.com.ua/price.zip"};
	}
	
	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.notebookshop.com.ua";
	}

}
