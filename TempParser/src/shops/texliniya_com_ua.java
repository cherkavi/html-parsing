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

public class texliniya_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://texliniya.com.ua";
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
						"http://texliniya.com.ua",
						"utf-8",
						"/HTML/BODY[@id=\"s5_body\"]/DIV[@id=\"s5_outer_wrap\"]/DIV[@id=\"s5_top_wrap\"]/DIV[@id=\"s5_t_middle\"]/DIV[@id=\"s5_main_body_outer\"]/DIV[@id=\"s5_main_body_inner\"]/DIV[@id=\"s5_middle_wrapper\"]/DIV[@id=\"s5_left\"]/DIV[@id=\"s5_left_inner\"]/DIV[@class=\"module_shadow_wrap\"]/DIV[@class=\"module_shadow\"]/DIV/DIV/DIV/A[@class=\"mainlevel\"]");
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(), getShopUrlStartPage()+element.getAttribute("href"), getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] { "http://texliniya.com.ua/htc.html",
				"http://texliniya.com.ua/htc/Page-2-20.html",
				"http://texliniya.com.ua/htc/Page-3-20.html", };
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
		return "/HTML/BODY[@id=\"s5_body\"]/DIV[@id=\"s5_outer_wrap\"]/DIV[@id=\"s5_top_wrap\"]/DIV[@id=\"s5_t_middle\"]/DIV[@id=\"s5_main_body_outer\"]/DIV[@id=\"s5_main_body_inner\"]/DIV[@id=\"s5_middle_wrapper\"]/DIV[@id=\"s5_right\"]/DIV[@id=\"s5_main_body_shadow\"]/DIV[@id=\"s5_main_body\"]/DIV[@id=\"s5_main_body2\"]/DIV[@id=\"s5_main_body3\"]/DIV[@id=\"s5_main_body4\"]/DIV[@id=\"vmMainPage\"]/DIV[@id=\"product_list\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[*]/DIV[@class=\"browseProductContainer\"]";
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
		return "/H3[@class=\"browseProductTitle\"]/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/H3[@class=\"browseProductTitle\"]/A";
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
		return "/DIV[@class=\"browsePriceContainer\"]/SPAN[@class=\"productPrice\"]";
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