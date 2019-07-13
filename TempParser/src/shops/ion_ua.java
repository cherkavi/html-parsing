package shops;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.file_parser.html_analisator.DynamicLinkAnalisator;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class ion_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 6;
	}

	@Override
	protected int getStartRow() {
		return 5;
	}

	@Override
	protected boolean isUncontinious() {
		return true;
	}
	
	@Override
	protected int getMaxEmptyRecord() {
		return 5;
	}
	
	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(isStringEmpty(record[1])&&isStringEmpty(record[2])&&isStringEmpty(record[3])){
				throw new EParseExceptionEmptyRecord();
			}
			String name=record[3];
			String url=null;
			Float price=this.getFloatFromString(record[5].replaceAll("[^0-9^.^,]", ""));
			Float priceUsd=this.getFloatFromString(record[4].replaceAll("[^0-9^.^,]", ""));
			return new Record(name, null, url, price, priceUsd, null);
		}catch (EParseException ep){
			throw ep;
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
		DynamicLinkAnalisator analisator=new DynamicLinkAnalisator();
		return new String[]{analisator.getUrlFromPageFromElement("http://ion.ua", ECharset.WIN_1251.getName(), "/html/body/table/tbody/tr/td[2]/div[2]/div[2]/a")};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://ion.ua";
	}

}
