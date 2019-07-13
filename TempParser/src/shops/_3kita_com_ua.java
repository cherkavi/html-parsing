package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.*;


public class _3kita_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.3kita.com.ua";
	}

	@Override
	protected String getCharset() {
		return "utf-8";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new DirectFinder(
				this.parser
						.getNodeListFromUrl(
								"http://www.3kita.com.ua",
								"utf-8",
								"/html/body/div/div/div/div/div/div/div/table/tbody/tr/td/div[2]/div/div[@class=\"item\"]/div[@class=\"text\"]/div[@class=\"links\"]/a[*]"),
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
				"http://www.3kita.com.ua/large-technics/coolers",
				"http://www.3kita.com.ua/large-technics/coolers/page=2/",
				"http://www.3kita.com.ua/large-technics/coolers/page=3/", };
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_PAGE_LOAD_ERROR,
			ESectionEnd.NEXT_RECORDS_LOAD_ERROR,
			ESectionEnd.NEXT_RECORDS_SHOW_FIRST};

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/div/div/div/div/div/div/div/table/tbody/tr/td/div[2]/div[4]/div/div";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/div[@class=\"item\"]";
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
		return "/a";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/a";
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
		return "/div[@class=\"price-order\"]/div[@class=\"price\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^,]", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/div[@class=\"price-order\"]/div[@class=\"price dollar\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		return priceText.replaceAll("[^0-9^,]", "");
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
}