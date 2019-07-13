package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class gelezka_kiev_ua extends ExcelContinuous{

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 5;
	}

	@Override
	protected int getStartRow() {
		return 5;
	}

	@Override
	protected int getColumnControlNumber() {
		return 1;
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
		if( !isStringEmpty(record[1])&&isStringEmpty(record[2])&&isStringEmpty(record[3]) ){
			return record[1];
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.gelezka.kiev.ua/uploads/files/price_Gelezka.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.gelezka.kiev.ua";
	}

}
