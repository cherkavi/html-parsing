package shops;

import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.*;

public class mobisklad_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://mobisklad.com.ua";
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
						"http://mobisklad.com.ua",
						"utf-8",
						"/HTML/BODY/DIV[@id=\"left_border\"]/DIV[@id=\"right_border\"]/DIV[@id=\"bottom\"]/DIV[@id=\"bottom_right\"]/DIV[@id=\"bottom_left\"]/DIV[@id=\"container\"]/DIV[@id=\"maincontent\"]/DIV[@id=\"left_out\"]/DIV[@class=\"module\"]/DIV/DIV/DIV/TABLE/TBODY/TR[*]/TD/A[@class=\"mainlevel\"]");
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						String url = addHttpPreambula(
								"http://mobisklad.com.ua", element
										.getAttribute("href"));
						return new UniversalAnalisator(
								element.getTextContent(), url, getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] {
				"http://mobisklad.com.ua/index.php?option=com_content&view=article&id=7&Itemid=10",
				"http://mobisklad.com.ua/index.php?option=com_content&view=article&id=7&Itemid=10&limitstart=1",
				"http://mobisklad.com.ua/index.php?option=com_content&view=article&id=7&Itemid=10&limitstart=2", };
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
		return "/HTML/BODY/DIV[@id=\"left_border\"]/DIV[@id=\"right_border\"]/DIV[@id=\"bottom\"]/DIV[@id=\"bottom_right\"]/DIV[@id=\"bottom_left\"]/DIV[@id=\"container\"]/DIV[@id=\"maincontent\"]/DIV[@id=\"content_outleft\"]/DIV[@id=\"content\"]/DIV[@id=\"content_border_right\"]/DIV[@id=\"content_border_left\"]/DIV[@id=\"content_bottom_right\"]/DIV[@id=\"content_bottom_left\"]/TABLE[@class=\"contentpaneopen\"]/TBODY/TR/TD/TABLE/TBODY";
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
		return "/TD[2]/A[@class=\"h2\"]/H2/FONT";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TD[2]/A[@class=\"h2\"]";
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
		return "/TD[2]/H2";
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