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

public class abcphoto_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://abcphoto.com.ua";
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
		// removeNodeWithRepeatAttributes(list, "href");
		ArrayList<Node> listOfNode = this.parser
				.getNodeListFromUrl(
						"http://abcphoto.com.ua",
						"utf-8",
						"/HTML/BODY[@class=\"index\"]/DIV[@id=\"mainContainer\"]/DIV[@class=\"outer\"]/DIV[@class=\"inner\"]/DIV[@class=\"float-wrap\"]/DIV[@id=\"left\"]/UL[@class=\"catalog\"]/LI[*]/UL/LI[*]/A");
		removeNodeFromListByTextContent(listOfNode, new
		String[]{
		"Печать фотографий",
		"Широкоформатная печать",
		"Послепечатная обработка",
		"Фотокнига",
		"Бленды",
		"Экстендеры",
		"Фильтры",
		"Крышки",
		"Бинокли",
		"Телескопы",
		"Аксессуары к вспышкам",
		"Аксессуары к видео",
		"Аккумуляторы",
		"Батарейные блоки",
		"Зарядные устройства",
		"Защита экрана",
		"Конверторы",
		"Наглазники",
		"Пульты ду",
		"Переходные кольца",
		"Подводные боксы",
		"Средства для чистки оптики",
		"Средства для чистки матриц",
		"Ремни",
		"Прочее",
		"Чехлы",
		"Сумки",
		"Рюкзаки",
		"Осветители",
		"Системы крепления",
		"Держатели",
		"Зонты, софтбоксы, отражатели",
		"Насадки",
		"Фоны",
		"Аксессуары",
		"Штативы",
		"Штативы без головок",
		"Штативные головки",
		"Штативы настольные",
		"Моноподы",
		"Чехлы для штативов",
		"Аксессуары",
		"Видеоштативы",
		"Альбомы",
		"Рамки"
	});
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
				"http://abcphoto.com.ua/catalog/usb_flash_drives/",
				"http://abcphoto.com.ua/catalog/usb_flash_drives/2",
				"http://abcphoto.com.ua/catalog/usb_flash_drives/3", };
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
		return "/HTML/BODY[@id=\"catalog\"]/DIV[@id=\"mainContainer\"]/DIV[@class=\"outer\"]/DIV[@class=\"inner\"]/DIV[@class=\"float-wrap\"]/DIV[@id=\"content\"]/DIV[@class=\"contentWrap\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[@class=\"item\"]";
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
		return "/H4/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/H4/A";
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
		return "/P[@class=\"prices\"]/SPAN[@class=\"price\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/P[@class=\"prices\"]/SPAN[@class=\"price_main\"]";
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