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

public class tehnoroom_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://tehnoroom.com.ua";
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
								"http://tehnoroom.com.ua",
								"utf-8",
								"/HTML/BODY/DIV[@id=\"main\"]/DIV[@id=\"mainnavwrap\"]/DIV[@id=\"mainnav\"]/UL[@class=\"vm_cat_menu\"]/LI[*]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return "http://tehnoroom.com.ua"
								+ element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/DIV[@id=\"main\"]/DIV[@id=\"ja-container\"]/DIV[@id=\"ja-container2\"]/DIV[@id=\"ja-mainbody\"]/DIV[@id=\"ja-contentwrap\"]/DIV[@id=\"ja-content\"]/DIV[@id=\"vmMainPage\"]/DIV[2]/TABLE/TBODY/TR[*]/TD[*]/A[@class=\"category\"]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return "http://tehnoroom.com.ua"+url;
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								"http://tehnoroom.com.ua"+element.getAttribute("href"));
					}
				});
	};

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;
		private String preambula=null;
		private String postambula=null;
		
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// http://tehnoroom.com.ua/3-%D0%9D%D0%BE%D1%83%D1%82%D0%B1%D1%83%D0%BA%D0%B8.html
				// http://tehnoroom.com.ua/3-%D0%9D%D0%BE%D1%83%D1%82%D0%B1%D1%83%D0%BA%D0%B8/Page-2-5.html
				// http://tehnoroom.com.ua/3-%D0%9D%D0%BE%D1%83%D1%82%D0%B1%D1%83%D0%BA%D0%B8/Page-3-5.html
				if(this.preambula==null){
					int dotIndex=this.getUrl().lastIndexOf('.');
					this.preambula=this.getUrl().substring(0, dotIndex)+"/Page-";
					this.postambula="-5.html";
				}
				return this.preambula+pageCounter+this.postambula;
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
		return "/HTML/BODY/DIV[@id=\"main\"]/DIV[@id=\"ja-container\"]/DIV[@id=\"ja-container2\"]/DIV[@id=\"ja-mainbody\"]/DIV[@id=\"ja-contentwrap\"]/DIV[@id=\"ja-content\"]/DIV[@id=\"vmMainPage\"]/TABLE/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]";
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
		return "/TD/DIV[@class=\"browseProductContainer\"]/H3[@class=\"browseProductTitle\"]/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TD/DIV[@class=\"browseProductContainer\"]/H3[@class=\"browseProductTitle\"]/A";
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
		return "/TD/DIV[@class=\"browseProductContainer\"]/DIV[@class=\"browsePriceContainer\"]/DIV[@class=\"price\"]/DIV[@class=\"grn\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "").replaceAll(".$", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/TD/DIV[@class=\"browseProductContainer\"]/DIV[@class=\"browsePriceContainer\"]/DIV[@class=\"price\"]/DIV[@class=\"dol\"]";
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
	protected Record prepareRecordBeforeSave(Record record) throws EParseException {
		return record;
	}
}