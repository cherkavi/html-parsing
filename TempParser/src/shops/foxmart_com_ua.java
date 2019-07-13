package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.RecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.two_level_finder.*;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class foxmart_com_ua extends RecordListBaseMultiPage {
	// http://www.foxmart.com/products/ref/kiev.html
	// http://www.foxmart.com/products/ref/page=2/kiev.html
	// http://www.foxmart.com/products/ref/page=3/kiev.html
	@Override
	public String getShopUrlStartPage() {
		return "http://www.foxmart.com.ua";
	}

	@Override
	protected String getCharset() {
		return "utf-8";
	}

	// removeNodeFromListByTextContent(listOfNode, new
	// String[]{"Банковское оборудование", "Автомобильные товары"});
	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new TwoLevelFinder(
				this.parser
						.getNodeListFromUrl(
								"http://www.foxmart.com.ua",
								"utf-8",
								"/HTML/BODY/DIV[@class=\"all_page\"]/DIV[@class=\"top_header\"]/DIV[@class=\"top_header_center\"]/DIV[@class=\"bottom_header_center\"]/TABLE[@class=\"main_menu\"]/TBODY/TR/TD[*]/A[@class=\"rollover_main\"]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/FORM[@id=\"aspnetForm\"]/DIV[@class=\"all_page\"]/DIV[@class=\"main_page\"]/DIV[@class=\"page_1\"]/TABLE/TBODY/TR/TD[1]/DIV[@class=\"left_menu\"]/DIV[@class=\"left_menu_item\"]/A[*]"),
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								addHttpPreambula("http://www.foxmart.com/products/",
										element.getAttribute("href")));
					}
				});
	}

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;
		private String preambula=null;
		// http://www.foxmart.com/products/ref/kiev.html
		// http://www.foxmart.com/products/ref/page=2/kiev.html
		// http://www.foxmart.com/products/ref/page=3/kiev.html

		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				if(this.preambula==null){
					int index=this.getUrl().lastIndexOf('/');
					this.preambula=this.getUrl().substring(0, index)+"/page=";
				}
				return this.preambula+pageCounter+"/kiev.html";
			}
			return this.getUrl();
		}
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
		return "/HTML/BODY/FORM[@id=\"aspnetForm\"]/DIV[@class=\"all_page\"]/DIV[@class=\"main_page\"]/DIV[@class=\"page_1\"]/TABLE/TBODY/TR/TD[2]/TABLE[2]/TBODY/TR/TD";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[*]/DIV[@class=\"about_goods_field\"]";
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
		return "/DIV[@class=\"title_of_catalog\"]/DIV[1]/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/DIV[@class=\"title_of_catalog\"]/DIV[1]/A";
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
		return "/DIV[@class=\"price_buy_catalog\"]/TABLE[@class=\"price_buy_catalog\"]/TBODY/TR/TD[@class=\"price_field\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9]", "");
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