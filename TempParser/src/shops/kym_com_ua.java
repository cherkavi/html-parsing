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


public class kym_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.kym.com.ua";
	}

	@Override
	protected String getCharset() {
		return "utf-8";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new DirectFinder(
				this.parser
						.getNodeListFromUrl(
								"http://www.kym.com.ua",
								"utf-8",
								"/HTML/BODY/DIV[@class=\"helper-container\"]/DIV[@id=\"container\"]/DIV[@id=\"content\"]/DIV[@class=\"content-helper clear\"]/DIV[@class=\"central-column\"]/DIV[@class=\"central-content\"]/TABLE/TBODY/TR[*]/TD[@class=\"h_subcat_main\"]/A"),
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(),
								"http://www.kym.com.ua/"
										+ element.getAttribute("href"),
								getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] { "http://www.kym.com.ua/refrigerators/",
							  "http://www.kym.com.ua/refrigerators/page-2/",
							  "http://www.kym.com.ua/refrigerators/page-3/", };
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
		return "/HTML/BODY/DIV[@class=\"helper-container\"]/DIV[@id=\"container\"]/DIV[@id=\"content\"]/DIV[@class=\"content-helper clear\"]/DIV[@class=\"central-column\"]/DIV[@class=\"central-content\"]/DIV[@class=\"mainbox-container\"]/DIV[@class=\"mainbox-body\"]/DIV[@id=\"pagination_contents\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[@class=\"product-container clear\"]/form/div[2]/div/";
	}

	@Override
	protected String recordFromNodeIsPresent() {
		return "/p[1]";
	}

	@Override
	protected String recordFromNodeIsPresentText() {
		return "Есть";
	}

	@Override
	protected String recordFromNodeInRecordToName() {
		return "/h3/a";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/h3/a";
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
		return "/div[@class=\"prices-container clear\"]/div/div/span/span";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		return null;
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