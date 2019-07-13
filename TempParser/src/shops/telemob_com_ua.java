package shops;

import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.*;

public class telemob_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://telemob.com.ua";
	}

	@Override
	protected String getCharset() {
		return "windows-1251";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		// import org.w3c.dom.Node;
		// import
		// shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
		// ArrayList<Node> listOfNode=(new NodeListFinderByUrl(parser,
		// getCharset(),
		// "/html/body/div[4]/div/div[2]/div[3]/table/tbody/tr[*]/td[@width="25%"]/div/div/ul/li[*]/a")).getNodeListByUrl(this.getShopUrlStartPage());
		ArrayList<Node> listOfNode = this.parser
				.getNodeListFromUrl(
						"http://telemob.com.ua",
						"windows-1251",
						"/HTML/BODY/CENTER/TABLE[3]/TBODY/TR/TD[1]/TABLE[1]/TBODY/TR[2]/TD/A[@class=\"light_1\"]");
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(),
								"http://telemob.com.ua/"
										+ element.getAttribute("href"),
								getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] { "http://telemob.com.ua/index.php?categoryID=183",
				"http://telemob.com.ua/index.php?categoryID=183&offset=15",
				"http://telemob.com.ua/index.php?categoryID=183&offset=30", };
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
		return "/HTML/BODY/CENTER/TABLE[3]/TBODY/TR/TD[2]/TABLE/TBODY/TR/TD/TABLE/TBODY/TR[3]/TD/TABLE[2]/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD/P/TABLE/TBODY/TR/TD[2]";
	}

	@Override
	protected String recordFromNodeIsPresent() {
		return "/TABLE/TBODY/TR[2]/TD";
	}

	@Override
	protected String recordFromNodeIsPresentText() {
		return "нет в наличии";
	}

	@Override
	protected boolean isPresentTextExistsMustPresent() {
		return false;
	}

	@Override
	protected String recordFromNodeInRecordToName() {
		return "/TABLE/TBODY/TR[1]/TD[1]/TABLE/TBODY/TR/TD/P/A[@class=\"cat_1\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TABLE/TBODY/TR[1]/TD[1]/TABLE/TBODY/TR/TD/P/A[@class=\"cat_1\"]";
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
		return "/TABLE/TBODY/TR[2]/TD/B/FONT[@class=\"price\"]";
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