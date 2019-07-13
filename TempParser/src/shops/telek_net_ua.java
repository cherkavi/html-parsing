package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.two_level_finder.*;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class telek_net_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.telek.net.ua";
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
								"http://www.telek.net.ua",
								"windows-1251",
								"/HTML/BODY/DIV[@class=\"center\"]/DIV[@id=\"block_left\"]/DIV[@class=\"menu\"]/A[*]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/DIV[@class=\"center\"]/DIV[@id=\"block_right\"]/TABLE[@class=\"category\"]/TBODY/TR[*]/TD[2]/H3[@class=\"blue\"]/A[@class=\"none_decoration\"]"),
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
		return new String[] { "http://www.telek.net.ua/subcategory/238",
				"http://www.telek.net.ua/subcategory/238/0/1/10",
				"http://www.telek.net.ua/subcategory/238/0/1/20", };
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
		return "/HTML/BODY/DIV[@class=\"center\"]/DIV[@id=\"block_right\"]/FORM";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TABLE[@class=\"subcat\"]/TBODY";
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
		return "/TR[1]/TD[@class=\"prod_name\"]/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TR[1]/TD[@class=\"prod_name\"]/A";
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
		return "/TR[3]/TD[@class=\"price\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		int index=priceText.indexOf('(');
		if(index>0){
			return priceText.substring(index).replaceAll("[^0-9]", "");
		}
		return priceText.replaceAll("[^0-9]", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/TR[3]/TD[@class=\"price\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		int index=priceText.indexOf('(');
		if(index>0){
			return priceText.substring(0, index).replaceAll("[^0-9]", "");
		}
		return priceText.replaceAll("[^0-9]", "");
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