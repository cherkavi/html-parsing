package shops;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.html_analisator.DynamicLinkAnalisator;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class ucrf_com_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 7;
	}

	@Override
	protected int getStartRow() {
		return 5;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(record[4].indexOf("Есть")<0)return null;
			String name=record[0];
			String url=this.removeStartPage(record[6]);
			Float price=this.getFloatFromString(this.removeAfterSymbolIncludeIt(record[1], ',').replaceAll("[ а-я.]",""));
			Float priceUsd=this.getFloatFromString(record[2].replaceAll("[ $]", ""));
			return new Record(name, null, url, price, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(   isStringEmpty(record[1])
			&&isStringEmpty(record[2])
			&&isStringEmpty(record[3])
			&&isStringEmpty(record[4])
			&&(!isStringEmpty(record[0]))){
			return record[0];
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		DynamicLinkAnalisator analisator=new DynamicLinkAnalisator();
		return new String[]{analisator.getUrlFromPageFromElement("http://www.ucrf.com.ua/prajs-list.html", ECharset.WIN_1251.getName(), "/html/body/table/tbody/tr/td[2]/div/span[2]/table[2]/tbody/tr/td/p[2]/a")};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.ucrf.com.ua";
	}

}
