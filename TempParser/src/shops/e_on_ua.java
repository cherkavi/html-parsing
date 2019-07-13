package shops;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.RecursiveFinder;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class e_on_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://e-on.ua";
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
								"http://e-on.ua",
								"windows-1251",
								"/HTML/BODY/TABLE/TBODY/TR[3]/TD[1]/TABLE/TBODY/TR[1]/TD/DIV/TABLE/TBODY/TR[8]/TD[@class=\"faq_gray_Category\"]/TABLE/TBODY/TR[*]/TD[2]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return addHttpPreambula("http://e-on.ua", element
								.getAttribute("href"));
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/TABLE/TBODY/TR[3]/TD[2]/TABLE/TBODY/TR[4]/TD/P/TABLE/TBODY/TR[2]/TD/P/A[@class=\"subCategory\"]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return addHttpPreambula("http://e-on.ua", element
								.getAttribute("href"));
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(), addHttpPreambula(
										"http://e-on.ua", element
												.getAttribute("href")),
								getAnalisator());
					}
				});
	};

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] {
				"http://e-on.ua/audiotehnika/akusticheskie_sistemi/jbl/",
				"http://e-on.ua/audiotehnika/akusticheskie_sistemi/jbl/?offset=15",
				"http://e-on.ua/audiotehnika/akusticheskie_sistemi/jbl/?offset=30", };
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
	protected Node getDataBlockFromUrl(String nextUrl, String charset)throws Exception {
		Node returnValue=null;
		returnValue=this.parser.getNodeFromUrl(nextUrl, this.getCharset(), this.getXmlPathToDataBlock());
		if(returnValue==null){
			returnValue=this.parser.getNodeFromUrl(nextUrl, this.getCharset(), "/html/body/table/tbody/tr[3]/td[2]/table/tbody/tr[4]/td/p/table/tbody/tr[3]/td/table/tbody");
		}
		return returnValue;
	}
	// protected Node getDataBlockFromUrl	
	
	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table/tbody/tr[3]/td[2]/table/tbody/tr[4]/td/p/table/tbody/tr[2]/tr/td[@colspan=\"3\"]/table[@border=\"0\"]/tbody";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/tr[*]";
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
		return "/td[@format=\"%d\"]/p[2]/table[@border=\"0\"]/tbody/tr/td[@align=\"center\"]/a";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/td[@format=\"%d\"]/p[2]/table[@border=\"0\"]/tbody/tr/td[@align=\"center\"]/a";
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
		return "/td[@format=\"%d\"]/p[0$]/table[@border=\"0\"]/tbody/tr/td[@valign=\"top\"]/table[@border=\"0\"]/tbody/tr/td[@align=\"right\"]/b/font[@class=\"cat\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^,]", "");
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