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

public class kolibri_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://kolibri.com.ua";
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
		// removeNodeFromListByTextContent(listOfNode, new
		// String[]{"Банковское оборудование", "Автомобильные товары"});
		// removeNodeWithRepeatAttributes(list, "href");
		ArrayList<Node> listOfNode = this.parser
				.getNodeListFromUrl(
						"http://kolibri.com.ua",
						"utf-8",
						"/HTML/BODY/DIV[@class=\"osn-l\"]/DIV[@class=\"osn-r\"]/DIV[@class=\"osn-l-b\"]/DIV[@class=\"osn-r-b\"]/TABLE[@class=\"osn-t\"]/TBODY/TR/TD[@class=\"osn\"]/TABLE[@class=\"c-b-r\"]/TBODY/TR/TD[@class=\"left\"]/DIV[@class=\"l-b\"]/DIV[@class=\"l-b-menu\"]/DIV[@class=\"m\"]/DIV[@class=\"m1\"]/DIV[@class=\"p1\"]/A");
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(), element
										.getAttribute("href"), getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] {
				"http://kolibri.com.ua/melkaya-bytovaya-tehnika.html",
				"http://kolibri.com.ua/melkaya-bytovaya-tehnika.html&p=2",
				"http://kolibri.com.ua/melkaya-bytovaya-tehnika.html&p=3", };
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_LOAD_ERROR,
			ESectionEnd.NEXT_RECORDS_ZERO_SIZE };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/DIV[@class=\"osn-l\"]/DIV[@class=\"osn-r\"]/DIV[@class=\"osn-l-b\"]/DIV[@class=\"osn-r-b\"]/TABLE[@class=\"osn-t\"]/TBODY/TR/TD[@class=\"osn\"]/TABLE[@class=\"c-b-r\"]/TBODY/TR/TD[@class=\"c-b-l\"]/DIV[@class=\"center-t-c\"]/DIV[@class=\"center-t-l\"]/DIV[@class=\"center-t-r\"]/DIV[@id=\"vtov\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[@class=\"otd\"]/TABLE[*]/TBODY/TR";
	}

	@Override
	protected String recordFromNodeIsPresent() {
		return "/TD[*]/DIV[@class=\"ind-t-b\"]/TABLE/TBODY/TR[1]/TD[@class=\"kup\"]/DIV[@class=\"kup1\"]/DIV[@class=\"kup2\"]/DIV[@class=\"kup-r\"]";
	}

	@Override
	protected String recordFromNodeIsPresentText() {
		return "есть";
	}

	@Override
	protected String recordFromNodeInRecordToName() {
		return "/TD[@class=\"ind-t-l3\"]/TABLE[@class=\"ind-t-l\"]/TBODY/TR[@class=\"sr-t1\"]/TD[2]/DIV[@class=\"op\"]/H2/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TD[@class=\"ind-t-l3\"]/TABLE[@class=\"ind-t-l\"]/TBODY/TR[@class=\"sr-t1\"]/TD[2]/DIV[@class=\"op\"]/H2/A";
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
		return "/TD[*]/DIV[@class=\"ind-t-b\"]/TABLE/TBODY/TR[1]/TD[@class=\"kup\"]/DIV[@class=\"kup1\"]/DIV[@class=\"kup2\"]/DIV[@class=\"kup-r\"]/STRONG";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/TD[*]/DIV[@class=\"ind-t-b\"]/TABLE/TBODY/TR[1]/TD[@class=\"kup\"]/DIV[@class=\"kup1\"]/DIV[@class=\"kup2\"]/DIV[@class=\"kup-r\"]/STRONG";
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