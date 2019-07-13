package shops;

import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.*;

public class fialca_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://fialca.com.ua";
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
		ArrayList<Node> listOfNode=(new NodeListFinderByUrl(parser,
															getCharset(),
															"/HTML/BODY/DIV/DIV[@id=\"container\"]/TABLE[@id=\"content\"]/TBODY/TR[2]/TD[@class=\"left-column\"]/DIV[@class=\"menu-sitemap-tree\"]/UL/LI[*]/DIV[@class=\"item-text\"]/A")
								    ).getNodeListByUrl(this.getShopUrlStartPage());
		ArrayList<Node> listOfNode2=(new NodeListFinderByUrl(parser,
															getCharset(),
															"/HTML/BODY/DIV/DIV[@id=\"container\"]/TABLE[@id=\"content\"]/TBODY/TR[2]/TD[@class=\"left-column\"]/DIV[@class=\"menu-sitemap-tree\"]/UL/LI[*]/UL/LI[*]/DIV[@class=\"item-text\"]/A")
									).getNodeListByUrl(this.getShopUrlStartPage());
		listOfNode.addAll(listOfNode2);
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						String url = addHttpPreambula("http://fialca.com.ua",
								element.getAttribute("href"));
						return new UniversalAnalisator(
								element.getTextContent(), url, getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] {
				"http://fialca.com.ua/catalog/index.php?SECTION_ID=1816",
				"http://fialca.com.ua/catalog/index.php?SECTION_ID=1816&PAGEN_1=2",
				"http://fialca.com.ua/catalog/index.php?SECTION_ID=1816&PAGEN_1=3", };
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_REPEAT_LAST,
			ESectionEnd.NEXT_RECORDS_SHOW_FIRST };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/DIV/DIV[@id=\"container\"]/TABLE[@id=\"content\"]/TBODY/TR[2]/TD[@class=\"main-column\"]/TABLE/TBODY/TR[2]/TD/DIV[@class=\"catalog-section\"]/TABLE/TBODY";
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
		return "/TD/TABLE/TBODY/TR/TD[2]/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TD/TABLE/TBODY/TR/TD[2]/A";
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
		return "/TD/SPAN[@class=\"catalog-price\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		return priceText.replaceAll("[^0-9^.]", "").replaceAll(".$", "")
				.replaceAll(".$", "");
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