package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.RecursiveFinder;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class vipproduct_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://vipproduct.com.ua/";
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
		return new RecursiveFinder(
				this.parser
						.getNodeListFromUrl(
								"http://vipproduct.com.ua/",
								"utf-8",
								"/HTML/BODY/TABLE/TBODY/TR/TD[1]/DIV[@id=\"menu\"]/UL[@class=\"ul_menu\"]/LI[*]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return "http://vipproduct.com.ua"
								+ element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/TABLE/TBODY/TR/TD[2]/TABLE/TBODY/TR[*]/TD[*]/A[@class=\"blueu\"]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						url=url.trim();
						if(!url.startsWith("/")){
							return "http://vipproduct.com.ua/" + url;
						}else{
							return "http://vipproduct.com.ua" + url;
						}
						
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						String url=element.getAttribute("href").trim();
						if(!url.startsWith("/")){
							return new UniversalAnalisator(
									element.getTextContent(),
									"http://vipproduct.com.ua/"
											+ element.getAttribute("href"),
									getAnalisator());
						}else{
							return new UniversalAnalisator(
									element.getTextContent(),
									"http://vipproduct.com.ua"
											+ element.getAttribute("href"),
									getAnalisator());
						}
					}
				});
	};

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] {
				"http://vipproduct.com.ua/products/browse?cat=10134",
				"http://vipproduct.com.ua/products/browse?cat=10134&page=2&mode=asc",
				"http://vipproduct.com.ua/products/browse?cat=10134&page=3&mode=asc", };
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_REPEAT_LAST,
			ESectionEnd.NEXT_RECORDS_ZERO_SIZE };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/TABLE/TBODY/TR/TD[2]/DIV[@class=\"blackulb\"]/DIV[@id=\"catalogtwo\"]/TABLE/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[@class=\"cart_selector\"]";
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
		return "/TD[2]/A[@class=\"blacklink\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TD[2]/A[@class=\"blacklink\"]";
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
		return "/TD[3]/DIV[@class=\"product_position_right cart_attr\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		int indexBegin=priceText.indexOf('(');
		int indexEnd=priceText.indexOf(".)");
		return priceText.substring(indexBegin+1, indexEnd).replaceAll("[^0-9]", "");
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
	protected Record prepareRecordBeforeSave(Record record)
			throws EParseException {
		return record;
	}
}