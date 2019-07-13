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

public class delonghi_market_com extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://delonghi-market.com";
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
						"http://delonghi-market.com",
						"windows-1251",
						"/HTML/BODY[@id=\"body_bg\"]/TABLE[@id=\"header_3\"]/TBODY/TR/TD/TABLE[2]/TBODY/TR/TD/DIV[@id=\"container_box\"]/DIV[@id=\"container_box_7\"]/DIV[@id=\"container_box_8\"]/DIV[@id=\"container_box_9\"]/DIV[@id=\"container_box_10\"]/DIV[@id=\"container_box_1\"]/DIV[@id=\"container_box_2\"]/DIV[@id=\"container_box_3\"]/DIV[@id=\"container_box_4\"]/DIV[@id=\"container_box_5\"]/DIV[@id=\"container_box_6\"]/DIV[@id=\"container_box_bg_pad\"]/DIV[@id=\"container_box_bg\"]/DIV[@id=\"container_box_11\"]/TABLE/TBODY/TR/TD[1]/UL[@class=\"catalog\"]/LI[*]/DIV/STRONG/A");
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						String url = addHttpPreambula(
								"http://delonghi-market.com", element
										.getAttribute("href"));
						return new UniversalAnalisator(
								element.getTextContent(), url, getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] { "http://delonghi-market.com/shop/CID_16.html",
				"http://delonghi-market.com/shop/CID_16_2.html",
				"http://delonghi-market.com/shop/CID_16_3.html", };
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
		return "/HTML/BODY[@id=\"body_bg\"]/TABLE[@id=\"header_3\"]/TBODY/TR/TD/TABLE[2]/TBODY/TR/TD/DIV[@id=\"container_box\"]/DIV[@id=\"container_box_7\"]/DIV[@id=\"container_box_8\"]/DIV[@id=\"container_box_9\"]/DIV[@id=\"container_box_10\"]/DIV[@id=\"container_box_1\"]/DIV[@id=\"container_box_2\"]/DIV[@id=\"container_box_3\"]/DIV[@id=\"container_box_4\"]/DIV[@id=\"container_box_5\"]/DIV[@id=\"container_box_6\"]/DIV[@id=\"container_box_bg_pad\"]/DIV[@id=\"container_box_bg\"]/DIV[@id=\"container_box_11\"]/TABLE/TBODY/TR/TD[@class=\"logo_container\"]/TABLE[2]/TBODY/TR[2]/TD[@class=\"sp_mid\"]/TABLE/TBODY/TR[1]/TD/TABLE/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD[*]";
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
		return "/DIV[@class=\"product_forma\"]/DIV[1]/A[@class=\"product_name\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/DIV[@class=\"product_forma\"]/DIV[1]/A[@class=\"product_name\"]";
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
	protected Node getPriceNodeFromLeafNode(Node node) {
		Node returnValue=this.parser.getNodeFromNode(node, this.recordFromNodeInRecordToPrice());
		if(returnValue==null){
			returnValue=this.parser.getNodeFromNode(node, "/DIV[@class=\"product_forma\"]/TABLE/TBODY/TR[2]/TD[1]/DIV/SPAN[@class=\"price\"]");
		}
		return returnValue;
	}
	
	@Override
	protected String recordFromNodeInRecordToPrice() {
		return "/DIV[@class=\"product_forma\"]/DIV[@class=\"price_2\"]/DIV[@class=\"price\"]";
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