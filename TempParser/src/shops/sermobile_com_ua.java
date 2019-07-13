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

// 2010.12.20 ���������� ���������
public class sermobile_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.sermobile.com.ua";
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
		// removeNodeFromListByTextContent(listOfNode, new
		// String[]{"���������� ������������", "������������� ������"});
		ArrayList<Node> listOfNode = this.parser
				.getNodeListFromUrl(
						"http://www.sermobile.com.ua",
						"windows-1251",
						"/HTML/BODY/TABLE/TBODY/TR/TD[2]/TABLE[@class=\"main_bg\"]/TBODY/TR[6]/TD[1]/TABLE/TBODY/TR/TD/P[1]/TABLE[@class=\"bbcodes_menu\"]/TBODY/TR/TD[@class=\"top_m\"]/TABLE[@class=\"mp3\"]/TBODY/TR[*]/TD[2]/A");
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						String url = addHttpPreambula(
								"http://www.sermobile.com.ua", element
										.getAttribute("href"));
						return new UniversalAnalisator(
								element.getTextContent(), url, getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] {
				"http://www.sermobile.com.ua/index.php?categoryID=42",
				"http://www.sermobile.com.ua/index.php?categoryID=42&offset=12",
				"http://www.sermobile.com.ua/index.php?categoryID=42&offset=24", };
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
		// return "/HTML/BODY/DIV/TABLE[3]/TBODY/TR/TD[2]/TABLE/TBODY/TR/TD[3]/TABLE/TBODY/TR[2]/TD/P/TABLE[@class=\"voting\"]/TBODY/TR[5]/TD/TABLE[@class=\"voting\"]/TBODY";
		return "/HTML/BODY/TABLE/TBODY/TR/TD[2]/TABLE[@class=\"main_bg\"]/TBODY/TR[6]/TD[2]/TABLE/TBODY/TR/TD/P/TABLE[@class=\"voting\"]/TBODY/TR/TD/TABLE[@class=\"voting\"]/TBODY";
			   		// /TR[1]/TD/P/TABLE[@class="bbcodes"]/TBODY/TR[@class="voting"]/TD[2]/TABLE[@class="voting"]/TBODY/TR[1]/TD[1]/TABLE[@class="voting"]/TBODY
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD/P[0$]/TABLE[@class=\"bbcodes\"]/TBODY/TR[@class=\"voting\"]/TD[2]/TABLE[@class=\"voting\"]/TBODY";
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
		return "/TR[1]/TD[1]/TABLE[@class=\"voting\"]/TBODY/TR/TD/P/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TR[1]/TD[1]/TABLE[@class=\"voting\"]/TBODY/TR/TD/P/A";
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
		return "/TR[2]/TD/FONT/SPAN";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "").replaceAll(".$", "");
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