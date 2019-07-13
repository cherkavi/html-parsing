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

public class technozuk_com_ua extends AnalisatorRecordListBaseMultiPage {
	// "Золотая формула",
	// "Посуда",
	// "Фильтры для воды",
	// "Автомобильная электроника",
	// "Сумки и чехлы"
	@Override
	public String getShopUrlStartPage() {
		return "http://www.technozuk.com.ua";
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
						"http://www.technozuk.com.ua",
						"windows-1251",
						"/HTML/BODY/TABLE[@class=\"main\"]/TBODY/TR[3]/TD[@class=\"content\"]/TABLE[@class=\"adn\"]/TBODY/TR/TD[@class=\"hdbtop vleft\"]/TABLE[@class=\"adn nl\"]/TBODY/TR[*]/TD[*]/TABLE[@class=\"adw\"]/TBODY/TR/TD[@class=\"mid\"]/A[@class=\"upc bf\"]");
		removeNodeFromListByTextContent(listOfNode, new String[]{
				"Золотая формула",
				"Посуда",
				"Фильтры для воды",
				"Автомобильная электроника",
				"Сумки и чехлы"
				});
		
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						String url = addHttpPreambula("http://www.technozuk.com.ua", element.getAttribute("href"));
						return new UniversalAnalisator(
								element.getTextContent(), url, getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] { "http://www.technozuk.com.ua/category_302.html",
				"http://www.technozuk.com.ua/category_302_offset_10.html",
				"http://www.technozuk.com.ua/category_302_offset_20.html", };
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
		return "/HTML/BODY/TABLE[@class=\"main\"]/TBODY/TR[3]/TD[@class=\"content\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TABLE[@class=\"adn\"]/TBODY/TR[*]";
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
		return "/TD/TABLE[@class=\"adn blmtop\"]/TBODY/TR/TD/TABLE[@class=\"adn\"]/TBODY/TR/TD[@class=\"hd upc bf\"]/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TD/TABLE[@class=\"adn blmtop\"]/TBODY/TR/TD/TABLE[@class=\"adn\"]/TBODY/TR/TD[@class=\"hd upc bf\"]/A";
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
		return "/td[@width=\"100%\"]/table[@class=\"adn\"]/tbody/tr/td[@class=\"hdbtop vleft\"]/table[@class=\"adn\"]/tbody/tr/td[@width=\"100%\"]/table[@class=\"adn\"]/tbody/tr/td/table[@class=\"adw\"]/tbody/tr/td[@class=\"price\"]";
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