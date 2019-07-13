package shops;

import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.exception.EParseException;
import shop_list.html.parser.engine.file_parser.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous4;
import shop_list.html.parser.engine.record.Record;

public class ankor_biz extends ExcelContinuous4{

	@Override
	protected int getMaxColumnCount() {
		return 3;
	}

	@Override
	protected int getStartRow() {
		return 9;
	}

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(isStringEmpty(record[0])&&isStringEmpty(record[1])&&isStringEmpty(record[2])){
				throw new EParseExceptionEmptyRecord();
			}
			String name=record[0];
			Float price=this.getFloatFromString(record[1]);
			Float priceUsd=this.getFloatFromString(record[2]);
			if((price.floatValue()==0)&&(priceUsd.floatValue()==0))return null;
			return new Record(name, null, null, price, priceUsd, null);
		}catch(EParseExceptionEmptyRecord ep){
			throw ep;
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
		return new String[]{"http://www.ankor.biz/price/price.rar"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.ankor.biz";
	}

}
