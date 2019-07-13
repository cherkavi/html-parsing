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

public class shopping_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://shopping.com.ua";
	}

	@Override
	protected String getCharset() {
		return "KOI8-U";
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
								"http://shopping.com.ua/directory.html",
								"KOI8-U",
								"/HTML/BODY/DIV[@id=\"min-width\"]/DIV[@id=\"conteiner\"]/DIV[@id=\"center_block\"]/DIV[@id=\"main_content\"]/DIV[@id=\"catalog\"]/LI[*]/UL[*]/LI[*]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/DIV[@id=\"min-width\"]/DIV[@id=\"conteiner\"]/DIV[@id=\"center_block\"]/DIV[@id=\"main_content\"]/DIV[@id=\"catalog\"]/DIV[@id=\"goods\"]/DIV[@class=\"cat\"]/TABLE/TBODY/TR[*]/TD[*]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						if(url.indexOf("http://")<0){
							return "http://shopping.com.ua" + url;
						}else{
							return url;
						}
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						String url=element.getAttribute("href");
						if(url.indexOf("http://")<0){
							url="http://shopping.com.ua" + url;
						}
						return new UniversalAnalisator(
								element.getTextContent(),
								url,
								getAnalisator());
					}
				});
	};

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] { "http://shopping.com.ua/shop/102/461/",
				"http://shopping.com.ua/shop/102/461/pg2/",
				"http://shopping.com.ua/shop/102/461/pg3/", };
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
		return "/HTML/BODY/DIV[@id=\"min-width\"]/DIV[@id=\"conteiner\"]/DIV[@id=\"center_block\"]/DIV[@id=\"main_content\"]/DIV[@id=\"catalog\"]/DIV[@id=\"goods\"]/TABLE/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[4]/TD[1]";
	}

	@Override
	protected String recordFromNodeIsPresent() {
		return "/DIV[@class=\"status\"]";
	}

	@Override
	protected String recordFromNodeIsPresentText() {
		return "Есть";
	}

	@Override
	protected String recordFromNodeInRecordToName() {
		return "/A[2]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/A[2]";
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
		return "/DIV[@class=\"price\"]";
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