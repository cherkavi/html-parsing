package shops;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.html_analisator.DynamicLinkAnalisator;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class novostar_net extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 10;
	}

	@Override
	protected int getStartRow() {
		return 11;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			if(record[9].indexOf('+')<0)return null;
			String name=record[4];
			Float price=this.getFloatFromString(record[6]);
			Float priceUsd=this.getFloatFromString(record[7]);
			return new Record(name, null, null, price, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		return record[2];
	}

	@Override
	protected int getColumnControlNumber() {
		return 2;
	}
	@Override
	protected String[] getShopPriceFileUrl() {
		DynamicLinkAnalisator analisator=new DynamicLinkAnalisator();
		String url=analisator.getUrlFromPageFromElement("http://novostar.net/prices/", ECharset.WIN_1251.getName(), "/html/body/table/tbody/tr[2]/td/table/tbody/tr/td[2]/table/tbody/tr[3]/td/table/tbody/tr/td/a");
		return new String[]{url};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://novostar.net";
	}

}
