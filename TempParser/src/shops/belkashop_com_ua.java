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

public class belkashop_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://belkashop.com.ua";
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
						"http://belkashop.com.ua",
						"windows-1251",
						"/HTML/BODY/DIV[@class=\"main\"]/DIV[@class=\"left_bar\"]/DIV[@class=\"ind\"]/DIV[@class=\"block\"]/DIV[@class=\"text menu_t\"]/DIV[*]/A[2]");
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						String url = addHttpPreambula(
								"http://belkashop.com.ua", element
										.getAttribute("href"));
						return new UniversalAnalisator(
								element.getTextContent(), url, getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] {
				"http://belkashop.com.ua/index.php?categoryID=172",
				"http://belkashop.com.ua/index.php?categoryID=172&offset=10",
				"http://belkashop.com.ua/index.php?categoryID=172&offset=20", };
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
		return "/HTML/BODY/DIV[@class=\"main\"]/DIV[@class=\"main_window\"]/DIV[@class=\"ind\"]/DIV[@class=\"content\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TABLE[*]/TBODY/TR/TD/DIV[@class=\"p_l_prod best\"]/DIV[@class=\"p_l\"]/DIV[@class=\"p_r\"]/DIV[@class=\"p_t\"]/DIV[@class=\"p_b\"]/DIV[@class=\"p_tl\"]/DIV[@class=\"p_tr\"]/DIV[@class=\"p_bl\"]";
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
		return "/DIV[@class=\"p_br\"]/DIV[@class=\"prod_descr\"]/DIV[@class=\"ind\"]/H2/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/DIV[@class=\"p_br\"]/DIV[@class=\"prod_descr\"]/DIV[@class=\"ind\"]/H2/A";
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
		return "/DIV[@class=\"p_br\"]/DIV[@class=\"prod_descr\"]/DIV[@class=\"ind\"]/DIV[@class=\"prod_descr2\"]/DIV[@class=\"price2\"]/SPAN[@class=\"ua\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/DIV[@class=\"p_br\"]/DIV[@class=\"prod_descr\"]/DIV[@class=\"ind\"]/DIV[@class=\"prod_descr2\"]/DIV[@class=\"price2\"]/SPAN[@class=\"us\"]";
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
		return record;
	}
}