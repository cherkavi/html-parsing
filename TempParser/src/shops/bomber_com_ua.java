package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.RecursiveFinder;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class bomber_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.bomber.com.ua";
	}

	@Override
	protected String getCharset() {
		return "utf-8";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		// return new TestStubFinder(new
		// UniversalAnalisator("temp","http://",getAnalisator()));
		// import org.w3c.dom.Node;
		// import
		// shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
		// ArrayList<Node> listOfNode=(new NodeListFinderByUrl(parser,
		// getCharset(),
		// "/html/body/div[4]/div/div[2]/div[3]/table/tbody/tr[*]/td[@width="25%"]/div/div/ul/li[*]/a")).getNodeListByUrl(this.getShopUrlStartPage());
		// removeNodeFromListByTextContent(listOfNode, new
		// String[]{"Банковское оборудование", "Автомобильные товары"});
		// removeNodeWithRepeatAttributes(list, "href");
		return new RecursiveFinder(
				this.parser
						.getNodeListFromUrl(
								"http://www.bomber.com.ua",
								"utf-8",
								"/HTML/BODY[@id=\"bd\"]/DIV[@id=\"ja-wrapper\"]/DIV[@id=\"ja-wrapper-inner\"]/DIV[@id=\"ja-containerwrap\"]/DIV[@id=\"ja-container\"]/DIV[@id=\"ja-col\"]/DIV[@class=\"moduletable_menuhilite\"]/A[@class=\"mainlevel\"]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return addHttpPreambula("http://www.bomber.com.ua",
								element.getAttribute("href"));
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY[@id=\"bd\"]/DIV[@id=\"ja-wrapper\"]/DIV[@id=\"ja-wrapper-inner\"]/DIV[@id=\"ja-containerwrap\"]/DIV[@id=\"ja-container\"]/DIV[@id=\"ja-mainbody\"]/DIV[@id=\"ja-content\"]/DIV[@id=\"vmMainPage\"]/DIV[2]/TABLE/TBODY/TR[*]/TD[*]/A[@class=\"category\"]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return addHttpPreambula("http://www.bomber.com.ua",
								element.getAttribute("href"));
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(), addHttpPreambula(
										"http://www.bomber.com.ua", element
												.getAttribute("href")),
								getAnalisator());
					}
				});
	};

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] {
				"http://www.bomber.com.ua/TSifrovoe-foto/Svetofiltryi/Hoya/polyarizatsionnyie.html",
				"http://www.bomber.com.ua/TSifrovoe-foto/Svetofiltryi/Hoya/polyarizatsionnyie/Page-2-15.html",
				"http://www.bomber.com.ua/TSifrovoe-foto/Svetofiltryi/Hoya/polyarizatsionnyie/Page-3-15.html", };
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_REPEAT_LAST,
			ESectionEnd.NEXT_RECORDS_LOAD_ERROR,
			ESectionEnd.NEXT_RECORDS_ZERO_SIZE };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY[@id=\"bd\"]/DIV[@id=\"ja-wrapper\"]/DIV[@id=\"ja-wrapper-inner\"]/DIV[@id=\"ja-containerwrap\"]/DIV[@id=\"ja-container\"]/DIV[@id=\"ja-mainbody\"]/DIV[@id=\"ja-content\"]/DIV[@id=\"vmMainPage\"]/DIV[@id=\"product_list\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[*]";
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
		return "/DIV[@class=\"browseProductContainer\"]/H3[@class=\"browseProductTitle\"]/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/DIV[@class=\"browseProductContainer\"]/H3[@class=\"browseProductTitle\"]/A";
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
		return "/DIV[@class=\"browseProductContainer\"]/DIV[@class=\"browsePriceContainer\"]/SPAN[@class=\"productPrice\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		return priceText.replaceAll("[^0-9^.]", "");
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
	protected Record prepareRecordBeforeSave(Record record)
			throws EParseException {
		return record;
	}
}