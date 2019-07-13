package shops;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

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

public class vint_com_ua extends RecordListBaseMultiPage {

	protected Node getDataBlockFromUrl(String nextUrl, String charset)
			throws Exception {
		Node returnValue = this.parser.getNodeFromUrl(nextUrl, this
				.getCharset(), this.getXmlPathToDataBlock());
		if (returnValue == null) {
			this.parser
					.getNodeFromUrl(
							nextUrl,
							this.getCharset(),
							"/HTML/BODY/TABLE/TBODY/TR/TD/TABLE/TBODY/TR[4]/TD/TABLE/TBODY/TR/TD[3]/TABLE/TBODY/TR[3]/TD[@class=\"pr_bg\"]/LI/TABLE/TBODY/TR[4]/TD/TABLE/TBODY");
		}
		return returnValue;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.vint.com.ua";
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
								"http://www.vint.com.ua",
								"windows-1251",
								"/HTML/BODY/TABLE/TBODY/TR/TD/TABLE/TBODY/TR[4]/TD/TABLE/TBODY/TR/TD[3]/TABLE/TBODY/TR[3]/TD[@class=\"pr_bg\"]/TABLE/TBODY/TR/TD[*]/TABLE/TBODY/TR[*]/TD[2]/H2/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return addHttpPreambula("http://www.vint.com.ua",
								element.getAttribute("href"));
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/TABLE/TBODY/TR/TD/TABLE/TBODY/TR[4]/TD/TABLE/TBODY/TR/TD[3]/TABLE/TBODY/TR[3]/TD[@class=\"pr_bg\"]/TABLE/TBODY/TR/TD[*]/TABLE/TBODY/TR[*]/TD[2]/H2/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return addHttpPreambula("http://www.vint.com.ua",
								element.getAttribute("href"));
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								addHttpPreambula("http://www.vint.com.ua",
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
				// http://www.vint.com.ua/catalog/monitors/
				// http://www.vint.com.ua/catalog/monitors/index2.html
				// http://www.vint.com.ua/catalog/monitors/index3.html
				String returnValue=this.getUrl().trim();
				if(!returnValue.endsWith("/")){
					returnValue=returnValue+"/";
				}
				return returnValue+"index"+pageCounter+".html";
			}
			return this.getUrl();
		}
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
		return "/HTML/BODY/TABLE/TBODY/TR/TD/TABLE/TBODY/TR[4]/TD/TABLE/TBODY/TR/TD[3]/TABLE/TBODY/TR[3]/TD[@class=\"pr_bg\"]/TABLE/TBODY/TR[4]/TD/TABLE/TBODY";
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
		return "/TD[@class=\"pri_content\"]/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TD[@class=\"pri_content\"]/A";
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
		return "/TD[@class=\"pri_price\"]/B";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "");
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