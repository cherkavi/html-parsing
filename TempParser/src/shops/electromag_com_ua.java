package shops;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.html_analisator.DynamicLinkAnalisator;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class electromag_com_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 3;
	}

	@Override
	protected int getStartRow() {
		return 1;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[0];
			String url=this.removeStartPage(record[2]);
			Float priceUsd=this.getFloatFromString(record[1]);
			return new Record(name, null, url, null, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[1])&&isStringEmpty(record[2])){
			if(!isStringEmpty(record[0])){
				return record[0];
			}
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		DynamicLinkAnalisator analisator=new DynamicLinkAnalisator();
		String url=analisator.getUrlFromPageFromElement("http://www.electromag.com.ua/price/", ECharset.WIN_1251.getName(), "/html/body/table[3]/tbody/tr/td[2]/table[2]/tbody/tr/td/ul/li/a");
		return new String[]{url};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.electromag.com.ua";
	}

}
