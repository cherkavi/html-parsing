package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section_finder.two_level_finder.*;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;


public class flash_card_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.flash-card.com.ua";
	}

	@Override
	protected String getCharset() {
		return "windows-1251";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new TwoLevelFinder(
				this.parser
						.getNodeListFromUrl(
								"http://www.flash-card.com.ua",
								"windows-1251",
								"/HTML/BODY/DIV[@class=\"main_gray\"]/DIV[@class=\"main_bottom\"]/DIV[@class=\"main_padding\"]/DIV[@class=\"content\"]/TABLE[@class=\"content\"]/TBODY/TR/TD[@class=\"left\"]/DIV[@class=\"gray_border\"]/DIV[@class=\"menu\"]/A[*]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/DIV[@class=\"main_gray\"]/DIV[@class=\"main_bottom\"]/DIV[@class=\"main_padding\"]/DIV[@class=\"content\"]/TABLE[@class=\"content\"]/TBODY/TR/TD[@class=\"center\"]/TABLE/TBODY/TR/TD[1]/TABLE[@class=\"category\"]/TBODY/TR/TD[2]/H3[@class=\"blue\"]/A[@class=\"none_decoration\"]"),
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
		return new String[] { "http://www.flash-card.com.ua/subcategory/178",
				"http://www.flash-card.com.ua/subcategory/178/0/1/10",
				"http://www.flash-card.com.ua/subcategory/178/0/1/20", };
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_LOAD_ERROR,
			ESectionEnd.NEXT_RECORDS_ZERO_SIZE };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY[1]/DIV[@class=\"main_gray\"]/DIV[@class=\"main_bottom\"]/DIV[@class=\"main_padding\"]/DIV[@class=\"content\"]/TABLE[@class=\"content\"]/TBODY/TR/TD[@class=\"center\"]/TABLE/TBODY/TR/TD[1]/FORM";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TABLE[@class=\"subcat\"]/TBODY/TR";
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
		return "/td[2]/div[@class=\"prod_name\"]/a";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/td[2]/div[@class=\"prod_name\"]/a";
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
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/TD[1]/div";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		int index=priceText.indexOf('$');
		if(index<0)return "";
		return priceText.substring(0, index).replaceAll("[^0-9]", "");
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