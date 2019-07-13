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

public class winauto_in_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.winauto.in.ua";
	}

	@Override
	protected String getCharset() {
		return "utf-8";
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
						"http://www.winauto.in.ua",
						"utf-8",
						"/html/body/table[@border=\"0\"]/tbody/tr/td/table[@border=\"0\"]/tbody/tr/td[@bgcolor=\"#ffffff\"]/table[@border=\"0\"]/tbody/tr/td/table[@border=\"0\"]/tbody/tr/td/table[@border=\"0\"]/tbody/tr/td[@align=\"left\"]/form/table[@border=\"0\"]/tbody/tr/td[@class=\"phplmnormal\"]/span[@class=\"treemenudiv\"]/a[@class=\"phplm \"]");
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(),
								"http://www.winauto.in.ua" + removeAfterSymbolIncludeIt(element.getAttribute("href"),'?'),
								getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] {
				"http://www.winauto.in.ua/Paryoovochnye-sistemy-c-425.html",
				"http://www.winauto.in.ua/Paryoovochnye-sistemy-c-425.html?page=2&sort=products_sort_order",
				"http://www.winauto.in.ua/Paryoovochnye-sistemy-c-425.html?page=3&sort=products_sort_order", };
	}

	private ESectionEnd[] conditions = new ESectionEnd[] { ESectionEnd.NEXT_RECORDS_REPEAT_LAST };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/TABLE/TBODY/TR/TD/TABLE[3]/TBODY/TR/TD[2]/TABLE/TBODY/TR[6]/TD/TABLE[@class=\"productListing\"]/TBODY";
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
		return "/TD[@class=\"productListing-data\"]/H2/A[@class=\"title_prod\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TD[@class=\"productListing-data\"]/H2/A[@class=\"title_prod\"]";
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
		return "/TD[@class=\"productListing-data\"]/PRE/SPAN[@class=\"productSpecialPrice\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9]", "");
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
		record.setUrl(removeAfterSymbolIncludeIt(record.getUrl(),'?'));
		return record;
	}
}