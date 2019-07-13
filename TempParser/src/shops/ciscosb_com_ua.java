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

public class ciscosb_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://ciscosb.com.ua";
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
						"http://ciscosb.com.ua",
						"windows-1251",
						"/HTML/BODY/DIV[@class=\"main\"]/DIV[@class=\"left_bar\"]/DIV[@class=\"ind\"]/DIV[@class=\"block_text\"]/UL[@class=\"menu\"]/LI[*]/A");
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						String url = addHttpPreambula("http://ciscosb.com.ua",
								element.getAttribute("href"));
						return new UniversalAnalisator(
								element.getTextContent(), url, getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] { "http://ciscosb.com.ua/ip_telephony_console/",
				"http://ciscosb.com.ua/ip_telephony_console/?page=2",
				"http://ciscosb.com.ua/ip_telephony_console/?page=3", };
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
		return "/HTML/BODY/DIV[@class=\"main\"]/DIV[@class=\"main_window\"]/DIV[@class=\"ind\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[@class=\"prod_short\"]";
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
		return "/DIV[@class=\"s_descr\"]/DIV[@class=\"ind\"]/H3/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/DIV[@class=\"s_descr\"]/DIV[@class=\"ind\"]/H3/A";
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
		return "/DIV[@class=\"s_descr\"]/DIV[@class=\"ind\"]/DIV[@class=\"price\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		int index=priceText.indexOf('/');
		if(index>0){
			return priceText.substring(index).replaceAll("[^0-9^.]", "");
		}else{
			return "";
		}
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/DIV[@class=\"s_descr\"]/DIV[@class=\"ind\"]/DIV[@class=\"price\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		int index=priceText.indexOf('/');
		if(index>0){
			return priceText.substring(0, index).replaceAll("[^0-9^.]", "");
		}else{
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