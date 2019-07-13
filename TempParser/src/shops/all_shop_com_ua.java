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
import shop_list.html.parser.engine.multi_page.section_finder.two_level_finder.*;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class all_shop_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.all-shop.com.ua";
	}

	@Override
	protected String getCharset() {
		return "windows-1251";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new TwoLevelFinder(
				this.parser
						.getNodeListFromUrl(
								"http://www.all-shop.com.ua/",
								"windows-1251",
								"/HTML/BODY/TABLE[@class=\"body\"]/TBODY/TR[1]/TD/TABLE/TBODY/TR[3]/TD[1]/DIV[@class=\"leftContainer\"]/DIV[@class=\"leftBlock\"]/DIV[@class=\"category\"]/DIV[*]/DIV/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return "http://www.all-shop.com.ua/"
								+ element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/TABLE[@class=\"body\"]/TBODY/TR[1]/TD/TABLE/TBODY/TR[3]/TD[2]/DIV[@class=\"rightBlock\"]/TABLE[@class=\"categoryList\"]/TBODY/TR[*]/TH/DIV/A"),
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(),
								"http://www.all-shop.com.ua/"
										+ element.getAttribute("href"),
								getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] {
				"http://www.all-shop.com.ua/?PAGE=list&CATEGORY=57470",
				"http://www.all-shop.com.ua/?PAGE=list&p=2&CATEGORY=57470",
				"http://www.all-shop.com.ua/?PAGE=list&p=3&CATEGORY=57470", };
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
		return "/HTML/BODY/TABLE[@class=\"body\"]/TBODY/TR[1]/TD/TABLE/TBODY/TR[3]/TD[2]/DIV[2]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TABLE[@class=\"productList\"]/TBODY";
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
		return "/TR[1]/TD[3]/DIV[@class=\"title\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TR[3]/TD[@class=\"links\"]/A[1]";
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
		return "/TR[3]/TD[@class=\"price\"]/A";
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