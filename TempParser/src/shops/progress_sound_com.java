package shops;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous4;
import shop_list.html.parser.engine.record.Record;

public class progress_sound_com extends ExcelContinuous4{

	@Override
	protected boolean isUncontinious() {
		return true;
	}
	
	@Override
	protected int getMaxEmptyRecord() {
		return 10;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 6;
	}

	@Override
	protected int getStartRow() {
		return 14;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(isStringEmpty(record[0])&&isStringEmpty(record[1])&&isStringEmpty(record[2])&&isStringEmpty(record[3])){
				throw new EParseExceptionEmptyRecord();
			}
			String name=record[1];
			String url=this.removeStartPage(record[5]);
			Float price=this.getFloatFromString(record[2].replaceAll("[^0-9^,]", ""));
			Float priceUsd=this.getFloatFromString(record[3].replaceAll("[^0-9^,]", ""));
			if((price.floatValue()==0)&&(priceUsd.floatValue()==0))return null;
			return new Record(name, null, url, price, priceUsd, null);
		}catch(EParseExceptionEmptyRecord ep){
			throw ep;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(!isStringEmpty(record[0]))return record[0];
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.progress-sound.com/ext/export_excel/export_excel.php"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.progress-sound.com";
	}

}
