package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous4;
import shop_list.html.parser.engine.record.Record;

public class technograd_com_ua extends ExcelContinuous4{
	@Override
	protected int getColumnControlNumber() {
		return 1;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 4;
	}

	@Override
	protected int getStartRow() {
		return 1;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			Float priceUsd=this.getFloatFromString(record[3].replaceAll("[$ ]", ""));
			if(priceUsd.floatValue()==0)return null;
			String name=record[0]+" "+record[1];
			return new Record(name, null, null, null, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[0])&&isStringEmpty(record[2])&&(!isStringEmpty(record[1]))){
			return record[1];
		}else{
			return null;
		}
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://technogrand.com.ua/price.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://technogrand.com.ua";
	}

}
