package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.RecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.RecursiveFinder;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class tehnodomik_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://tehnodomik.com.ua";
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
								"http://tehnodomik.com.ua",
								"utf-8",
								"/HTML/BODY[@id=\"bd\"]/DIV[@id=\"bg-t\"]/DIV[@id=\"ja-wrapper\"]/DIV[@id=\"ja-wrapper-inner\"]/DIV[@id=\"ja-containerwrap\"]/DIV[@id=\"ja-container\"]/DIV[@id=\"ja-col\"]/DIV[@class=\"sidebar\"]/DIV[@class=\"module-bg png\"]/A[@class=\"mainlevel\"]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return addHttpPreambula("http://tehnodomik.com.ua",
								element.getAttribute("href"));
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY[@id=\"bd\"]/DIV[@id=\"bg-t\"]/DIV[@id=\"ja-wrapper\"]/DIV[@id=\"ja-wrapper-inner\"]/DIV[@id=\"ja-containerwrap\"]/DIV[@id=\"ja-container\"]/DIV[@id=\"ja-mainbody\"]/DIV[@id=\"ja-content\"]/DIV[@id=\"vmMainPage\"]/DIV[2]/TABLE/TBODY/TR[*]/TD[*]/A[@class=\"category\"]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return addHttpPreambula("http://tehnodomik.com.ua",
								element.getAttribute("href"));
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								addHttpPreambula("http://tehnodomik.com.ua",
										element.getAttribute("href")));
					}
				});
	};

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;

		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// http://tehnodomik.com.ua/index.php?option=com_virtuemart&page=shop.browse&category_id=111&Itemid=5
				// http://tehnodomik.com.ua/index.php?option=com_virtuemart&page=shop.browse&category_id=111&Itemid=5&limit=20&limitstart=20
				// http://tehnodomik.com.ua/index.php?option=com_virtuemart&Itemid=5&category_id=111&page=shop.browse&limit=20&limitstart=20
				return this.getUrl()+"&limit=20&limitstart="+(pageCounter-1)*20;
			}
			return this.getUrl();
		}
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
		return "/HTML/BODY[@id=\"bd\"]/DIV[@id=\"bg-t\"]/DIV[@id=\"ja-wrapper\"]/DIV[@id=\"ja-wrapper-inner\"]/DIV[@id=\"ja-containerwrap\"]/DIV[@id=\"ja-container\"]/DIV[@id=\"ja-mainbody\"]/DIV[@id=\"ja-content\"]/DIV[@id=\"vmMainPage\"]/DIV[@id=\"product_list\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[*]/DIV[@class=\"browseProductContainer\"]";
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
		return "/H3[@class=\"browseProductTitle\"]/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/H3[@class=\"browseProductTitle\"]/A";
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
		return "/DIV[@class=\"browsePriceContainer\"]/SPAN[@class=\"productPrice\"]";
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