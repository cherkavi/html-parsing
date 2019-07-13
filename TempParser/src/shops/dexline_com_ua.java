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

public class dexline_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://dexline.com.ua/";
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
						"http://dexline.com.ua/",
						"windows-1251",
						"/HTML/BODY/CENTER/DIV[@class=\"bottom_style\"]/DIV[@class=\"main_t\"]/DIV[@class=\"page\"]/DIV[@id=\"page_box\"]/DIV[@class=\"ff\"]/DIV[@id=\"left\"]/DIV[@id=\"boxCategories\"]/DIV[@id=\"cat\"]/DIV[@class=\"c00\"]/DIV[*]/DIV[@class=\"c001\"]/A");
		ArrayList<Node> listOfNode2 = this.parser
		.getNodeListFromUrl(
						"http://dexline.com.ua/",
						"windows-1251",
						"/HTML/BODY/CENTER/DIV[@class=\"bottom_style\"]/DIV[@class=\"main_t\"]/DIV[@class=\"page\"]/DIV[@id=\"page_box\"]/DIV[@class=\"ff\"]/DIV[@id=\"left\"]/DIV[@id=\"boxCategories\"]/DIV[@id=\"cat\"]/DIV[@class=\"c00\"]/DIV[*]/DIV[@class=\"c00\"]/DIV[@id=\"menu1\"]/DIV[@class=\"c001\"]/A");
		listOfNode.addAll(listOfNode2);
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
		return new String[] { "http://dexline.com.ua/Dvd-pleer.ru.57.html",
				"http://dexline.com.ua/Dvd-pleer.ru.57.2.html",
				"http://dexline.com.ua/Dvd-pleer.ru.57.3.html", };
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
		return "/HTML/BODY/CENTER/DIV[@class=\"bottom_style\"]/DIV[@class=\"main_t\"]/DIV[@class=\"page\"]/DIV[@id=\"page_box\"]/DIV[@class=\"ff\"]/DIV[@id=\"wrapper\"]/DIV[@id=\"content\"]/FORM";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[@class=\"page\"]/DIV/TABLE[@class=\"class007\"]/TBODY";
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
		return "/TR[1]/TD[@class=\"productDescription\"]/A/B";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TR[1]/TD[@class=\"productDescription\"]/A";
	}

	@Override
	protected String getAttributeForExtractRecordUrl() {
		return "href";
	}

	@Override
	protected boolean recordIsRemoveStartPageFromUrl() {
		return true;
	}

	@Override
	protected String recordFromNodeInRecordToPrice() {
		return "/TR[1]/TD[@class=\"class006\"]/DIV[@class=\"class004\"]/H3/STRONG";
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