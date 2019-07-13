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

public class tehnomix_kiev_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.tehnomix.kiev.ua";
	}

	@Override
	protected String getCharset() {
		return "windows-1251";
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
		return new RecursiveFinder(
				this.parser
						.getNodeListFromUrl(
								"http://www.tehnomix.kiev.ua",
								"windows-1251",
								"/HTML/BODY/TABLE[5]/TBODY/TR/TD[3]/TABLE/TBODY/TR/TD/TABLE[1]/TBODY/TR[2]/TD/TABLE/TBODY/TR[*]/TD[*]/TABLE/TBODY/TR[1]/TD/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return addHttpPreambula("http://www.tehnomix.kiev.ua",
								element.getAttribute("href"));
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/TABLE[5]/TBODY/TR/TD[3]/TABLE/TBODY/TR/TD/DIV/TABLE/TBODY/TR/TD[*]/TABLE/TBODY/TR[*]/TD/A[@class=\"link\"]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return addHttpPreambula("http://www.tehnomix.kiev.ua",
								element.getAttribute("href"));
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(), addHttpPreambula(
										"http://www.tehnomix.kiev.ua", element
												.getAttribute("href")),
								getAnalisator());
					}
				});
	};

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] { "http://www.tehnomix.kiev.ua/cid_1578.html",
				"http://www.tehnomix.kiev.ua/cid_1578_offset=16.html",
				"http://www.tehnomix.kiev.ua/cid_1578_offset=32.html", };
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_REPEAT_LAST,
			ESectionEnd.NEXT_RECORDS_SHOW_FIRST,
			ESectionEnd.NEXT_RECORDS_LOAD_ERROR };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/TABLE[5]/TBODY/TR/TD[3]/TABLE/TBODY/TR/TD/FONT[1$]/TABLE/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD[*]/TABLE/TBODY/TR/TD[2]";
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
		return "/TABLE/TBODY/TR[1]/TD/TABLE/TBODY/TR/TD/A[@class=\"light\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TABLE/TBODY/TR[1]/TD/TABLE/TBODY/TR/TD/A[@class=\"light\"]";
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
		return "/TABLE/TBODY/TR[2]/TD[@class=\"small\"]/B/FONT[@class=\"price2\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/TABLE/TBODY/TR[2]/TD[@class=\"small\"]/FONT[@class=\"lightstandard\"]";
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