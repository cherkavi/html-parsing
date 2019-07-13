package shops;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.file_parser.html_analisator.DynamicLinkAnalisator;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class c_tech_com_ua extends ExcelContinuous{

	@Override
	protected boolean isUncontinious() {
		return true;
	}
	
	@Override
	protected int getMaxEmptyRecord() {
		return 5;
	}
	
	@Override
	protected int getMaxColumnCount() {
		return 6;
	}

	@Override
	protected int getStartRow() {
		return 3;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(isStringEmpty(record[0])&&isStringEmpty(record[1])&&isStringEmpty(record[2]))throw new EParseExceptionEmptyRecord();
			if(record[5].trim().indexOf("склад")!=0)return null;
			String name=record[1];
			Float priceUsd=this.getFloatFromString(record[3]);
			return new Record(name, null, null, null, priceUsd, null);
		}catch(EParseException ex){
			throw ex;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[1])&&isStringEmpty(record[2])&&isStringEmpty(record[3])){
			if(!isStringEmpty(record[0]))return record[0];
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		DynamicLinkAnalisator analisator=new DynamicLinkAnalisator();
		return new String[]{"http://www.c-tech.com.ua/"+analisator.getUrlFromPageFromElement("http://www.c-tech.com.ua/price", ECharset.WIN_1251.getName(), "/html/body/table/tbody/tr[2]/td/table/tbody/tr/td[2]/div[2]/div[2]/a")};
		
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.c-tech.com.ua";
	}

}
