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

public class mirka_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://mirka.com.ua";
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
								"http://mirka.com.ua",
								"windows-1251",
								"/HTML/BODY/DIV/TABLE/TBODY/TR/TD/TABLE[4]/TBODY/TR/TD[1]/TABLE/TBODY/TR[1]/TD/TABLE/TBODY/TR[3]/TD/TABLE[@class=\"infoBox1\"]/TBODY/TR/TD/TABLE[@class=\"infoBoxContents1\"]/TBODY/TR[*]/TD[@class=\"categories_nav\"]/A[@class=\"navBlueCateg\"]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/DIV/TABLE/TBODY/TR/TD/TABLE[4]/TBODY/TR/TD[2]/TABLE/TBODY/TR[3]/TD/TABLE/TBODY/TR[1]/TD/TABLE/TBODY/TR[*]/TD[@class=\"smallText_sub_cat\"]/A"),
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
				"http://mirka.com.ua/%C0%E2%F2%EE+-+%F2%E5%F5%ED%E8%EA%E0/%C0%E2%F2%EE%EC%E0%E3%ED%E8%F2%EE%EB%FB.htm",
				"http://mirka.com.ua/%C0%E2%F2%EE+-+%F2%E5%F5%ED%E8%EA%E0/%C0%E2%F2%EE%EC%E0%E3%ED%E8%F2%EE%EB%FB.htm?page=2",
				"http://mirka.com.ua/%C0%E2%F2%EE+-+%F2%E5%F5%ED%E8%EA%E0/%C0%E2%F2%EE%EC%E0%E3%ED%E8%F2%EE%EB%FB.htm?page=3", };
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
		return "/HTML/BODY/DIV/TABLE/TBODY/TR/TD/TABLE[4]/TBODY/TR/TD[2]/TABLE/TBODY/TR[3]/TD/TABLE[@class=\"productListing\"]/TBODY";
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
		return "/TR[4]/TD[@class=\"smallTextMan\"]/A[@class=\"navBlueCenter1\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TR[4]/TD[@class=\"smallTextMan\"]/A[@class=\"navBlueCenter1\"]";
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
		return "/TR[2]/TD/SPAN";
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