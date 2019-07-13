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

public class eurotehnica_kiev_ua extends RecordListBaseMultiPage {
	// ѕроверить магазин на возможные коллизии в ссылках на разделы
	// -------
	// http://eurotehnica.kiev.ua/krupnaya-bytovaya-tehnika/mikrovolnovye-pechi/
	// http://eurotehnica.kiev.ua/krupnaya-bytovaya-tehnika/mikrovolnovye-pechi/page2.html
	// http://eurotehnica.kiev.ua/krupnaya-bytovaya-tehnika/mikrovolnovye-pechi/page3.html
	@Override
	public String getShopUrlStartPage() {
		return "http://eurotehnica.kiev.ua";
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
		// String[]{"Ѕанковское оборудование", "јвтомобильные товары"});
		return new RecursiveFinder(
				this.parser
						.getNodeListFromUrl(
								"http://eurotehnica.kiev.ua",
								"windows-1251",
								"/HTML/BODY/CENTER/TABLE[@id=\"Table_01\"]/TBODY/TR[3]/TD[2]/TABLE/TBODY/TR[*]/TD[@class=\"top\"]/P/A[*]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return addHttpPreambula("http://eurotehnica.kiev.ua",
								element.getAttribute("href"));
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/CENTER/TABLE[@id=\"Table_01\"]/TBODY/TR[3]/TD[2]/TABLE/TBODY/TR/TD/CENTER[1]/TABLE/TBODY/TR[*]/TD[*]/CENTER/A[@class=\"style7\"]"),
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
						return new UserSection(element.getTextContent(),
								element.getAttribute("href"));
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
				// http://eurotehnica.kiev.ua/krupnaya-bytovaya-tehnika/mikrovolnovye-pechi/
				// http://eurotehnica.kiev.ua/krupnaya-bytovaya-tehnika/mikrovolnovye-pechi/page2.html
				// http://eurotehnica.kiev.ua/krupnaya-bytovaya-tehnika/mikrovolnovye-pechi/page3.html
				return this.getUrl().trim()+"page"+pageCounter+".html";
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
		return "/HTML/BODY/CENTER/TABLE[@id=\"Table_01\"]/TBODY/TR[3]/TD[2]/CENTER[1]/TABLE/TBODY/TR/TD/CENTER";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TABLE[*]/TBODY/TR/TD[2]";
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
		return "/CENTER[1]/SPAN[@class=\"style7\"]/B/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/CENTER[1]/SPAN[@class=\"style7\"]/B/A";
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
		return "/CENTER[2]/FONT/B";
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