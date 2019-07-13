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

public class megabyte_kiev_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://megabyte.kiev.ua";
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
		return new RecursiveFinder(
				this.parser
						.getNodeListFromUrl(
								"http://megabyte.kiev.ua",
								"utf-8",
								"/HTML/BODY/CENTER/DIV[@id=\"bottomBG\"]/DIV[@id=\"topBG\"]/TABLE[3]/TBODY/TR/TD[1]/DIV[@id=\"categories\"]/DIV[@class=\"inn1\"]/DIV[@class=\"inn2\"]/DIV[@class=\"inn3\"]/DIV[@class=\"inn4\"]/DIV[@class=\"inn5\"]/DIV[@class=\"inn6\"]/DIV[@class=\"inn7\"]/DIV[@class=\"inn8\"]/DIV[@class=\"box1_body\"]/DIV[@id=\"categoriesContent\"]/UL/LI[*]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/CENTER/DIV[@id=\"bottomBG\"]/DIV[@id=\"topBG\"]/TABLE[3]/TBODY/TR/TD[@id=\"content\"]/DIV[@class=\"box1\"]/DIV[@class=\"inn1\"]/DIV[@class=\"inn2\"]/DIV[@class=\"inn3\"]/DIV[@class=\"inn4\"]/DIV[@class=\"inn5\"]/DIV[@class=\"inn6\"]/DIV[@class=\"inn7\"]/DIV[@class=\"inn88\"]/DIV[@class=\"page\"]/DIV[@class=\"prod_box\"]/DIV[@class=\"inn1\"]/DIV[@class=\"inn2\"]/DIV[@class=\"inn3\"]/DIV[@class=\"inn4\"]/DIV[@class=\"inn5\"]/DIV[@class=\"inn6\"]/DIV[@class=\"inn7\"]/DIV[@class=\"inn8\"]/TABLE/TBODY/TR[*]/TD[@class=\"main\"]/CENTER/STRONG/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return url;
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(), element
										.getAttribute("href"), getAnalisator());
					}
				});
	};

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] { "http://megabyte.kiev.ua/index.php?cat=113",
				"http://megabyte.kiev.ua/index.php?cat=113&page=2",
				"http://megabyte.kiev.ua/index.php?cat=113&page=3", };
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
		return "/HTML/BODY/CENTER/DIV[@id=\"bottomBG\"]/DIV[@id=\"topBG\"]/TABLE[3]/TBODY/TR/TD[@id=\"content\"]/DIV[@class=\"box1\"]/DIV[@class=\"inn1\"]/DIV[@class=\"inn2\"]/DIV[@class=\"inn3\"]/DIV[@class=\"inn4\"]/DIV[@class=\"inn5\"]/DIV[@class=\"inn6\"]/DIV[@class=\"inn7\"]/DIV[@class=\"inn88\"]/FORM/TABLE/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD/DIV[@class=\"prod_box\"]/DIV[@class=\"inn1\"]/DIV[@class=\"inn2\"]/DIV[@class=\"inn3\"]/DIV[@class=\"inn4\"]/DIV[@class=\"inn5\"]/DIV[@class=\"inn6\"]/DIV[@class=\"inn7\"]/DIV[@class=\"inn8\"]";
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
		return "/DIV[@class=\"name\"]/A[@class=\"prodName\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/DIV[@class=\"name\"]/A[@class=\"prodName\"]";
	}

	@Override
	protected String getAttributeForExtractRecordUrl() {
		return "href";
	}

	@Override
	protected boolean recordIsRemoveStartPageFromUrl() {
		return true;
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
		return "/TABLE/TBODY/TR/TD[2]/DIV[@class=\"price\"]/DIV[@class=\"in1\"]/DIV[@class=\"in2\"]/DIV[@class=\"in3\"]/DIV[@class=\"in4\"]/DIV[@class=\"in5\"]/DIV[@class=\"col1\"]/STRONG";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		return priceText.replaceAll("[^0-9]", "");
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