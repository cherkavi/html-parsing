package shops;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;


public class ton_kiev_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 8;
	}

	@Override
	protected int getStartRow() {
		return 7;
	}

	@Override
	protected int getColumnControlNumber() {
		return 1;
	}
	
	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(!isStringEqualsAny(record[7], "есть"))return null;
			String name=record[1];
			Float priceUsd=this.getFloatFromString(record[4]);
			return new Record(name, null, null, null, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(!isStringEmpty(record[1])&&isStringEmpty(record[0])&&isStringEmpty(record[2]))return record[1];
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.ton.kiev.ua/price.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.ton.kiev.ua";
	}

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
}
