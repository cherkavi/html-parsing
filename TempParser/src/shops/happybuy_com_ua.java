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

public class happybuy_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://happybuy.com.ua";
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
								"http://happybuy.com.ua",
								"utf-8",
								"/HTML/BODY[@id=\"page\"]/DIV[@id=\"page-body\"]/DIV[@class=\"wrapper floatholder\"]/DIV[@id=\"middle\"]/DIV[@class=\"background\"]/DIV[@id=\"main\"]/DIV[@id=\"main_container\"]/DIV[@class=\"main-r\"]/DIV[@class=\"main-m\"]/DIV[@id=\"mainmiddle\"]/DIV[@id=\"content\"]/DIV[@id=\"content_container\"]/DIV[@class=\"floatbox\"]/DIV[@id=\"vmMainPage\"]/TABLE/TBODY/TR[*]/TD[*]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return addHttpPreambula("http://happybuy.com.ua",
								element.getAttribute("href"));
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY[@id=\"page\"]/DIV[@id=\"page-body\"]/DIV[@class=\"wrapper floatholder\"]/DIV[@id=\"middle\"]/DIV[@class=\"background\"]/DIV[@id=\"main\"]/DIV[@id=\"main_container\"]/DIV[@class=\"main-r\"]/DIV[@class=\"main-m\"]/DIV[@id=\"mainmiddle\"]/DIV[@id=\"content\"]/DIV[@id=\"content_container\"]/DIV[@class=\"floatbox\"]/DIV[@id=\"vmMainPage\"]/DIV[2]/TABLE/TBODY/TR[*]/TD[*]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return addHttpPreambula("http://happybuy.com.ua",
								element.getAttribute("href"));
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								addHttpPreambula("http://happybuy.com.ua",
										element.getAttribute("href")));
					}
				});
	};

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;
		private String preambula=null;
		
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// http://happybuy.com.ua/home.html?page=shop.browse&category_id=9
				// http://happybuy.com.ua/home.html?category_id=9&page=shop.browse&limit=20&start=20
				// http://happybuy.com.ua/home.html?category_id=9&page=shop.browse&limit=20&start=40
				if(preambula==null){
					int lastIndex=this.getUrl().lastIndexOf('=');
					preambula="http://happybuy.com.ua/home.html?category_id="+this.getUrl().substring(lastIndex+1)+"&page=shop.browse&limit=20&start=";
				}
				return preambula+(pageCounter-1)*20;
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
		return "/HTML/BODY[@id=\"page\"]/DIV[@id=\"page-body\"]/DIV[@class=\"wrapper floatholder\"]/DIV[@id=\"middle\"]/DIV[@class=\"background\"]/DIV[@id=\"main\"]/DIV[@id=\"main_container\"]/DIV[@class=\"main-r\"]/DIV[@class=\"main-m\"]/DIV[@id=\"mainmiddle\"]/DIV[@id=\"content\"]/DIV[@id=\"content_container\"]/DIV[@class=\"floatbox\"]/DIV[@id=\"vmMainPage\"]/DIV[@id=\"product_list\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[*]/DIV[@class=\"browseProductContainer\"]";
	}

	@Override
	protected String recordFromNodeIsPresent() {
		return "/P/SPAN/SPAN[@class=\"productPrice\"]";
	}

	@Override
	protected String recordFromNodeIsPresentText() {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToName() {
		return "/H2/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/H2/A";
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
		return "/P/STRONG";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "").replaceAll(".$", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/P/SPAN/SPAN[@class=\"productPrice\"]";
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