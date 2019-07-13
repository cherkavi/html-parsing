package shops;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.html_analisator.DynamicLinkAnalisator;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous4;
import shop_list.html.parser.engine.record.Record;

public class mvclimate_com_ua extends ExcelContinuous4{

	@Override
	protected int getMaxColumnCount() {
		return 5;
	}

	@Override
	protected int getStartRow() {
		return 6;
	}

	private final String alias="http://www.microclimat.com.ua";
	
	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[1];
			String url=this.removeStartPage(record[4]);
			int dotPosition=url.indexOf(alias);
			if(dotPosition>=0){
				url=url.substring(dotPosition+alias.length());
			}
			Float priceUsd=this.getFloatFromString(record[2]);
			return new Record(name, null, url, null, priceUsd, null);
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
		DynamicLinkAnalisator analisator=new DynamicLinkAnalisator();
		return new String[]{analisator.getUrlFromPageFromElement("http://www.mvclimate.com.ua/price/", ECharset.WIN_1251.getName(), "/html/body/table[3]/tbody/tr/td[2]/table/tbody/tr[3]/td/ul/li/a")};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.mvclimate.com.ua";
	}

}
