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

public class cezar_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.cezar.ua";
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
								"http://www.cezar.ua",
								"windows-1251",
								"/HTML/BODY/TABLE/TBODY/TR[3]/TD/TABLE/TBODY/TR/TD[4]/TABLE/TBODY/TR/TD[1]/TABLE/TBODY/TR[*]/TD[*]/TABLE/TBODY/TR[2]/TD[@class=\"tbb\"]/DIV[@class=\"tl1\"]/DIV[@class=\"tc\"]/TABLE/TBODY/TR/TD[2]/A[1]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return "http://www.cezar.ua/"
								+ element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/TABLE/TBODY/TR[3]/TD/TABLE/TBODY/TR/TD[4]/TABLE[2]/TBODY/TR/TD[1]/TABLE[1]/TBODY/TR[*]/TD[*]/TABLE/TBODY/TR[2]/TD[@class=\"tbb\"]/DIV[@class=\"tl1\"]/DIV[@class=\"tc\"]/TABLE/TBODY/TR/TD[2]/A[1]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						if(url.startsWith("../")){
							url=url.substring(3);
						}
						return "http://www.cezar.ua/" + url;
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						String url=element.getAttribute("href");
						if(url.startsWith("../")){
							url=url.substring(3);
						}
						return new UniversalAnalisator(
								element.getTextContent(),
								"http://www.cezar.ua/"
										+ url,
								getAnalisator());
					}
				});
	};

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] { "http://www.cezar.ua/group/900-Detektory-valyut",
				"http://www.cezar.ua/group/900-Detektory-valyut-p2",
				"http://www.cezar.ua/group/900-Detektory-valyut-p3", };
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_REPEAT_LAST,
			ESectionEnd.NEXT_PAGE_LOAD_ERROR,
			ESectionEnd.NEXT_RECORDS_LOAD_ERROR };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/TABLE/TBODY/TR[3]/TD/TABLE/TBODY/TR/TD[4]/TABLE[2]/TBODY/TR[2]/TD[1]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TABLE[*]/TBODY/TR[2]/TD[@class=\"tbb\"]";
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
		return "/DIV[@class=\"tl1\"]/DIV[@class=\"tc\"]/TABLE/TBODY/TR/TD[2]/H1/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/DIV[@class=\"tl1\"]/DIV[@class=\"tc\"]/TABLE/TBODY/TR/TD[2]/H1/A";
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
		return "/DIV[@class=\"tl1\"]/DIV[@class=\"tc\"]/TABLE/TBODY/TR/TD[2]/TABLE/TBODY/TR[2]/TD[4]/FORM/TABLE/TBODY/TR/TD/P[1]/FONT/STRONG";
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
		if(record.getUrl()!=null){
			if(record.getUrl().startsWith("..")){
				record.setUrl(record.getUrl().substring(2));
			}
		}
		return record;
	}
}