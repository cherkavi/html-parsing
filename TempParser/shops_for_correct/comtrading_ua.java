package shops;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.html_analisator.DynamicLinkAnalisator;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class comtrading_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 8;
	}

	@Override
	protected int getStartRow() {
		return 3;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(record[6].indexOf("Да")<0)return null;
			String name=record[2];
			String url=this.removeStartPage(record[7]);
			Float price=this.getFloatFromString(record[5]);
			return new Record(name, null, url, price, null, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if(isStringEmpty(record[2])&&isStringEmpty(record[3])){
			if(!isStringEmpty(record[1]))return record[1];
		}
		return null;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		DynamicLinkAnalisator analisator=new DynamicLinkAnalisator();
		return new String[]{this.getShopUrlStartPage()+"/"+analisator.getUrlFromPageFromElement("http://www.comtrading.ua", ECharset.WIN_1251.getName(), "/html/body/table[2]/tbody/tr/td[2]/table/tbody/tr/td/a")};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.comtrading.ua";
	}

}
