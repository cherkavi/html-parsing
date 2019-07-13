package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.*;


public class mobi_lux_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://mobi-lux.com.ua";
	}

	@Override
	protected String getCharset() {
		return "windows-1251";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new DirectFinder(
				this.parser
						.getNodeListFromUrl(
								"http://mobi-lux.com.ua",
								"windows-1251",
								"/HTML/BODY/DIV[@id=\"container\"]/DIV[@id=\"wrapper\"]/DIV[@id=\"content\"]/DIV[@id=\"incontent\"]/TABLE/TBODY/TR[*]/TD[@width=\"49%\"]/DIV[@class=\"tovar-block\"]/DIV[@class=\"intovar-block\"]/DIV[@class=\"intovar-block2\"]/A[@class=\"subcat\"]"),
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(),
								"http://mobi-lux.com.ua/"
										+ element.getAttribute("href"),
								getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] {
				"http://mobi-lux.com.ua/index.php?categoryID=114",
				"http://mobi-lux.com.ua/index.php?categoryID=114&sort=in_stock&direction=DESC&offset=8",
				"http://mobi-lux.com.ua/index.php?categoryID=114&sort=in_stock&direction=DESC&offset=16", };
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_LOAD_ERROR,
			ESectionEnd.NEXT_RECORDS_ZERO_SIZE };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/DIV[@id=\"container\"]/DIV[@id=\"wrapper\"]/DIV[@id=\"content\"]/DIV[@id=\"incontent\"]/TABLE/TBODY/TR[3]/TD[@width=\"75%\"]/TABLE[@width=\"100%\"]/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/tr[*]/td[@width=\"50%\"]/table[@width=\"100%\"]/tbody/tr/td[@width=\"99%\"]/table/tbody";
	}

	@Override
	protected String recordFromNodeIsPresent() {
		return null;
	}

	@Override
	protected String recordFromNodeIsPresentText() {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToName() {
		return "/tr/td[@valign=\"top\"]/table/tbody/tr/td/a[@class=\"cat\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/tr/td[@valign=\"top\"]/table/tbody/tr/td/a[@class=\"cat\"]";
	}

	@Override
	protected String getAttributeForExtractRecordUrl() {
		return "href";
	}

	@Override
	protected boolean recordIsRemoveStartPageFromUrl() {
		return false;
	}

	@Override
	protected String recordFromNodeInRecordToPrice() {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/tr[2]/td[@colspan=\"2\"]/b/font[@id=\"currentPrice\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		return priceText.replaceAll("[^0-9^,]", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceEuro() {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToPriceEuroBeforeConvert(
			String priceText) {
		return null;
	}

	@Override
	protected Record prepareRecordBeforeSave(Record record) throws EParseException {
		return record;
	}
}