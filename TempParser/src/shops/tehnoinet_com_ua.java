package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.RecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.RecursiveFinder;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class tehnoinet_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.tehnoinet.com.ua";
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
								"http://www.tehnoinet.com.ua",
								"utf-8",
								"/HTML/BODY/DIV[2]/TABLE[2]/TBODY/TR/TD[@class=\"lftcontent\"]/DIV[@class=\"box\"]/UL[@class=\"directory\"]/LI[*]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return "http://www.tehnoinet.com.ua"
								+ element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/DIV[2]/TABLE[2]/TBODY/TR/TD[@class=\"content\"]/DIV[@class=\"box\"]/TABLE/TBODY/TR[*]/TD[*]/TABLE/TBODY/TR[2]/TD/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return "http://www.tehnoinet.com.ua/" + url;
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								"http://www.tehnoinet.com.ua/"
										+ element.getAttribute("href"));
					}
				});
	};

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;
		private String preambula;
		private String postambula;
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// http://www.tehnoinet.com.ua/ru/products/LED_televizory/index.html
				// http://www.tehnoinet.com.ua/ru/products/LED_televizory/pno/2/index.html
				// http://www.tehnoinet.com.ua/ru/products/LED_televizory/pno/3/index.html
				if(preambula==null){
					int index=this.getUrl().lastIndexOf('/');
					preambula=this.getUrl().substring(0,index)+"/pno/";
					postambula="/index.html";
				}
				return preambula+pageCounter+postambula;
			}
			return this.getUrl();
		}
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
		return "/HTML/BODY/DIV[2]/TABLE[2]/TBODY/TR/TD[@class=\"content\"]/DIV[@class=\"box\"]/TABLE/TBODY/TR[4]/TD";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TABLE[@class=\"product_block\"]/TBODY";
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
		return "/TR/TD[3]/H3/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TR/TD[3]/H3/A";
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
		return "/TR/TD[4]/TABLE/TBODY/TR/TD[@class=\"bg_price\"]/TABLE/TBODY/TR[2]/TD/P[@class=\"ukr\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/TR/TD[4]/TABLE/TBODY/TR/TD[@class=\"bg_price\"]/TABLE/TBODY/TR[1]/TD";
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
	protected Record prepareRecordBeforeSave(Record record) throws EParseException {
		return record;
	}
}