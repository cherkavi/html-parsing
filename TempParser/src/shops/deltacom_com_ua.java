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

public class deltacom_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.deltacom.com.ua";
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
								"http://www.deltacom.com.ua",
								"windows-1251",
								"/HTML/BODY/TABLE/TBODY/TR/TD/TABLE[3]/TBODY/TR/TD[2]/TABLE/TBODY/TR[2]/TD[1]/TABLE[2]/TBODY/TR[*]/TD[*]/TABLE/TBODY/TR/TD[2]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url=element.getAttribute("href");
						return addHttpPreambula("http://www.deltacom.com.ua", url);
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/TABLE/TBODY/TR/TD/TABLE[3]/TBODY/TR/TD[2]/TABLE/TBODY/TR[2]/TD[1]/TABLE[1]/TBODY/TR/TD[@class=\"main\"]/TABLE[2]/TBODY/TR/TD[@class=\"main\"]/TABLE/TBODY/TR[@class=\"contentBoxContents1\"]/TD[@class=\"contentBoxContents1\"]/TABLE/TBODY/TR/TD[@class=\"contents\"]/STRONG/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						return addHttpPreambula("http://www.deltacom.com.ua", url);
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						String url=addHttpPreambula("http://www.deltacom.com.ua", element.getAttribute("href"));
						return new UniversalAnalisator(element.getTextContent(), url, getAnalisator());
					}
				});
	};

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] {
				"http://www.deltacom.com.ua/index.php?cat=c6_Noutbuki.html",
				"http://www.deltacom.com.ua/index.php?cat=c6_Noutbuki.html&page=2",
				"http://www.deltacom.com.ua/index.php?cat=c6_Noutbuki.html&page=3", };
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
		return "/HTML/BODY/TABLE/TBODY/TR/TD/TABLE[3]/TBODY/TR/TD[2]/TABLE/TBODY/TR[2]/TD[1]/TABLE[3]/TBODY/TR[1]/TD/TABLE/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD[@class=\"main\"]/TABLE/TBODY/TR[@class=\"contentBoxContents1\"]/TD[@class=\"contentBoxContents1\"]/TABLE/TBODY";
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
		return "/TR/TD[@class=\"contents\"]/STRONG[1]/A[1]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TR/TD[@class=\"contents\"]/STRONG[1]/A[1]";
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
		return "/TR/TD[2]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		int index = priceText.indexOf("Цена:");
		if (index > 0) {
			return priceText.substring(index + 5).replaceAll("[^0-9]", "");
		} else {
			return "";
		}
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