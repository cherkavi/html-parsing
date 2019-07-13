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

public class sis_tema_com_ua extends AnalisatorRecordListBaseMultiPage {
	// [ 38752 ] Аксессуар Подставка под ноутбук Genius NB STAND 100 USB
	// (31280194100)
	@Override
	public String getShopUrlStartPage() {
		return "http://www.sis-tema.com.ua";
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
		// String[]{"Банковское оборудование", "Автомобильные товары"});
		// removeNodeWithRepeatAttributes(list, "href");
		ArrayList<Node> listOfNode = this.parser
				.getNodeListFromUrl(
						"http://www.sis-tema.com.ua",
						"windows-1251",
						"/HTML/BODY/TABLE/TBODY/TR[1]/TD/TABLE/TBODY/TR[3]/TD/TABLE/TBODY/TR[3]/TD[1]/TABLE[1]/TBODY/TR[3]/TD[2]/TABLE/TBODY/TR[*]/TD[*]/TABLE/TBODY/TR/TD[@class=\"title_b\"]/DIV/A[@class=\"simple_full_catalog\"]");
		removeNodeFromListByTextContent(listOfNode, new String[]{
				"Услуги",
				"Автомобильные аксессуары",
				"Подскавки под ноутбуки",
				"Сумки и чехлы для ноутбуков",
				"Шнуры безопасности",
				"Продукты xDSL",
				"Сетевая безопасность",
				"Бинокли",
				"ЗУ, аккумуляторы, батарейки",
				"АТС офисные и аксессуары",
				"Картриджи",
				"Бумага",
				"Диски",
				"Дискеты",
				"Коробочки для дисков",
				"Конверты",
				"Сумки для дисков",
				"Инструменты",
				"Чистящие средства",
				"APPLE",
				"Microsoft",
				"Novell",
				"ПО Прочее",
				"Специализированные принтеры",
				"Детекторы валют",
				"Счетчик банкнот и монет",
				"Аксессуары для банковского оборудования",
				"Сканер штрих-кодов",
				"Упаковщик банкнот",
				"Принтеры специализированные",
				"Уничтожители документов",
				"Ламинаторы и аксессуары",
				"Услуги банка",
				"Услуги сервисного центра",
				"Услуги транспортного отдела"
		});
		
		
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						String url = addHttpPreambula(
								"http://www.sis-tema.com.ua", element
										.getAttribute("href"));
						return new UniversalAnalisator(
								element.getTextContent(), url, getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] {
				"http://www.sis-tema.com.ua/index.php?type=catalog&open_id=72049&part_id=2528",
				"http://www.sis-tema.com.ua/index.php?type=catalog&open_id=72049&part_id=2528&show_me_page=2",
				"http://www.sis-tema.com.ua/index.php?type=catalog&open_id=72049&part_id=2528&show_me_page=3", };
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
		return "/HTML/BODY/TABLE/TBODY/TR[1]/TD/TABLE/TBODY/TR[3]/TD/TABLE/TBODY/TR[3]/TD[2]/TABLE/TBODY/TR[5]/TD/TABLE/TBODY/TR/TD/TABLE[2]/TBODY/TR[6]/TD";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TABLE[*]/TBODY";
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
		return "/TR[1]/TD[2]/DIV";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TR[3]/TD/A";
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
		return "/TR[4]/TD[@class=\"input\"]/FONT[2]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "");
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
		if(record!=null&&record.getName()!=null){
			int index=record.getName().indexOf(']');
			if(index>0){
				record.setName(record.getName().substring(index+1));
			}
		}
		return record;
	}
}