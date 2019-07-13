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

public class boneco_ua_com extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://boneco-ua.com";
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
		// removeNodeWithRepeatAttributes(list, "href");
		ArrayList<Node> listOfNode = this.parser
				.getNodeListFromUrl(
						"http://boneco-ua.com",
						"windows-1251",
						"/HTML/BODY/CENTER/TABLE[1]/TBODY/TR/TD/TABLE/TBODY/TR[3]/TD[1]/TABLE/TBODY/TR[1]/TD/P/TABLE/TBODY/TR[2]/TD/FONT/B/A[*]");
		
		removeNodeFromListByTextContent(listOfNode, new
		String[]{
				"| Сферы применения приборов обработки воздуха Boneco Air-O-Swiss |",
				"| Сертификаты |",
				"| Сервисные центры |",
				"| О нас |",
				"| Как выбрать (очиститель) (увлажнитель) воздуха |",
				"| История  компании Plaston |",
				"| Интересные факты о Boneco Air-O-Swiss |",
				"| Вопрос  - ответ |",
				"Фильтры и аксесуары"
				});
		
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(), addHttpPreambula(getShopUrlStartPage(), element.getAttribute("href")), getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] { "http://boneco-ua.com/index.php?categoryID=95",
				"http://boneco-ua.com/index.php?categoryID=95&offset=10",
				"http://boneco-ua.com/index.php?categoryID=95&offset=20", };
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
		return "/HTML/BODY/CENTER/TABLE[1]/TBODY/TR/TD/TABLE/TBODY/TR[3]/TD[2]/TABLE/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD[*]/P/TABLE/TBODY";
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
		return "/TR/TD[2]/TABLE/TBODY/TR[1]/TD[1]/TABLE/TBODY/TR/TD/A[@class=\"cat\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TR/TD[2]/TABLE/TBODY/TR[1]/TD[1]/TABLE/TBODY/TR/TD/A[@class=\"cat\"]";
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
		return "/TR/TD[2]/TABLE/TBODY/TR[2]/TD/B/FONT[@class=\"cat\"]";
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