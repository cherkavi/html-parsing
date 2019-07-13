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

public class abuys_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://abuys.ua";
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
		return new RecursiveFinder(
				this.parser
						.getNodeListFromUrl(
								"http://abuys.ua",
								"windows-1251",
								"/HTML/BODY/DIV[@id=\"main_top\"]/DIV[@id=\"main\"]/DIV[@id=\"left\"]/TABLE/TBODY/TR[1]/TD/TABLE/TBODY/TR/TD[3]/TABLE[@class=\"infoBox1\"]/TBODY/TR/TD/TABLE[@class=\"infoBoxContents1\"]/TBODY/TR[*]/TD[@class=\"categories_nav\"]/A[@class=\"navBlueCateg\"]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/DIV[@id=\"main_top\"]/DIV[@id=\"main\"]/DIV[@id=\"center\"]/TABLE/TBODY/TR/TD[1]/TABLE/TBODY/TR[3]/TD/TABLE/TBODY/TR[1]/TD/TABLE/TBODY/TR[*]/TD[@class=\"smallText\"]/TABLE/TBODY/TR[1]/TD[1]/A[@class=\"navBlueCenter1\"]"),
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
		return new String[] {
				"http://abuys.ua/%C1%FB%F2%EE%E2%E0%FF+%F2%E5%F5%ED%E8%EA%E0/%CF%EB%E8%F2%FB+%EA%EE%EC%E1%E8%ED%E8%F0%EE%E2%E0%ED%ED%FB%E5.htm",
				"http://abuys.ua/%C1%FB%F2%EE%E2%E0%FF+%F2%E5%F5%ED%E8%EA%E0/%CF%EB%E8%F2%FB+%EA%EE%EC%E1%E8%ED%E8%F0%EE%E2%E0%ED%ED%FB%E5.htm?page=2",
				"http://abuys.ua/%C1%FB%F2%EE%E2%E0%FF+%F2%E5%F5%ED%E8%EA%E0/%CF%EB%E8%F2%FB+%EA%EE%EC%E1%E8%ED%E8%F0%EE%E2%E0%ED%ED%FB%E5.htm?page=3", };
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_REPEAT_LAST,
			ESectionEnd.NEXT_RECORDS_LOAD_ERROR };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/DIV[@id=\"main_top\"]/DIV[@id=\"main\"]/DIV[@id=\"center\"]/TABLE/TBODY/TR/TD[1]/TABLE/TBODY/TR[3]/TD/TABLE[@class=\"productListing\"]/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD[@class=\"productListing-data\"]/TABLE/TBODY";
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
		return "/TR/TD[1]/A[@class=\"navBlueCenter1\"]/H3";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TR/TD[1]/A[@class=\"navBlueCenter1\"]";
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
		return "/TR/TD[1]/SPAN";
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
	protected Record prepareRecordBeforeSave(Record record)
			throws EParseException {
		return record;
	}
}