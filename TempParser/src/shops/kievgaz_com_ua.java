package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.*;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;


public class kievgaz_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://kievgaz.com.ua";
	}

	@Override
	protected String getCharset() {
		return "windows-1251";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		// // return new TestStubFinder(new
		// UniversalAnalisator("temp","http://",getAnalisator()));
		return new RecursiveFinder(
				this.parser
						.getNodeListFromUrl(
								"http://kievgaz.com.ua",
								"windows-1251",
								"/HTML/BODY/DIV[@id=\"wrapper\"]/TABLE[3]/TBODY/TR/TD[1]/TABLE/TBODY/TR[1]/TD/TABLE[@class=\"infoBox\"]/TBODY/TR/TD/TABLE[@class=\"infoBoxContents\"]/TBODY/TR[1]/TD[@class=\"boxText\"]/A[*]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return removeAfterSymbolIncludeIt(element.getAttribute("href"),'&');
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/DIV[@id=\"wrapper\"]/TABLE[3]/TBODY/TR/TD[2]/TABLE/TBODY/TR[3]/TD/TABLE/TBODY/TR[1]/TD/TABLE/TBODY/TR[*]/TD[@class=\"smallText\"]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return removeAfterSymbolIncludeIt(url,'&');
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(), removeAfterSymbolIncludeIt(element.getAttribute("href"),'&'), getAnalisator());
					}
				});
	};

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] { "http://kievgaz.com.ua/index.php?cPath=51_61",
				"http://kievgaz.com.ua/index.php?cPath=51_61&sort=2a&page=2",
				"http://kievgaz.com.ua/index.php?cPath=51_61&sort=2a&page=3", };
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_REPEAT_LAST,
			ESectionEnd.NEXT_RECORDS_SHOW_FIRST };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/DIV[@id=\"wrapper\"]/TABLE[3]/TBODY/TR/TD[2]/TABLE/TBODY/TR[3]/TD/TABLE[@class=\"productListing\"]/TBODY/";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "TR[*]";
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
		return "/td[2]/a";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/td[2]/a";
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
		return "/td[3]";
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
	protected Record prepareRecordBeforeSave(Record record) throws EParseException {
		record.setUrl(removeAfterSymbolLastIncludeIt(record.getUrl(),'&'));
		return record;
	}
}