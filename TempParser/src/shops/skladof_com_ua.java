package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.two_level_finder.*;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class skladof_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://skladof.com.ua";
	}

	@Override
	protected String getCharset() {
		return "utf-8";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new TwoLevelFinder(
				this.parser
						.getNodeListFromUrl(
								"http://skladof.com.ua",
								"utf-8",
								"/HTML/BODY/TABLE[2]/TBODY/TR[1]/TD[2]/TABLE/TBODY/TR/TD[@id=\"lay_f2\"]/DIV[1]/DIV[@class=\"topmenu\"]/UL/LI[*]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/TABLE[2]/TBODY/TR/TD[2]/TABLE/TBODY/TR/TD[@id=\"lay_body\"]/DIV[@class=\"dsn_center_block1\"]/DIV[@class=\"dsn_center_block1_content\"]/TABLE[1]/TBODY/TR[5]/TD[@class=\"catD_detail\"]/TABLE/TBODY/TR[*]/TD[@class=\"catD_row\"]/TABLE/TBODY/TR[1]/TD[2]/A[@class=\"catD_name\"]"),
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(),
								"http://skladof.com.ua/"
										+ element.getAttribute("href"),
								getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] {
				"http://skladof.com.ua/katalog/katalog-igrushek/krupnobytovaja-tehnika/vytjazhki",
				"http://skladof.com.ua/katalog/katalog-igrushek/krupnobytovaja-tehnika/vytjazhki?&catid=20075&action=rsrtme&catid=20075&offset=30",
				"http://skladof.com.ua/katalog/katalog-igrushek/krupnobytovaja-tehnika/vytjazhki?&catid=20075&action=rsrtme&catid=20075&offset=60", };
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
		return "/HTML/BODY/TABLE[2]/TBODY/TR/TD[2]/TABLE/TBODY/TR/TD[@id=\"lay_body\"]/DIV[@class=\"dsn_center_block1\"]/DIV[@class=\"dsn_center_block1_content\"]/TABLE[1]/TBODY/TR[7]/TD/TABLE/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD[@class=\"eshop_list_item_row\"]";
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
		return "/A[@class=\"name\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/A[@class=\"name\"]";
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
		return "/table/tbody/tr/td[@valign=\"top\"]/div[@class=\"prices\"]/div[@class=\"item_base_price\"]/span[@class=\"price_price\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "").replaceAll(".$","");
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
	protected Record prepareRecordBeforeSave(Record record) throws EParseException {
		return record;
	}
}