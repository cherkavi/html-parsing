package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.*;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class kramnica_com extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://kramnica.com";
	}

	@Override
	protected String getCharset() {
		return "windows-1251";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		// // return new TestStubFinder(new
		// UniversalAnalisator("temp","http://",getAnalisator()));
		return new RecursiveFinder(
				this.parser
						.getNodeListFromUrl(
								"http://kramnica.com",
								"windows-1251",
								"/HTML/BODY/TABLE[3]/TBODY/TR[1]/TD[1]/TABLE[1]/TBODY/TR[2]/TD/TABLE[@class=\"bbrd\"]/TBODY/TR/TD[@class=\"boxText\"]/TABLE[@class=\"infoBoxContents\"]/TBODY/TR[*]/TD[@class=\"boxText\"]/SPAN[@class=\"hmenu\"]/STRONG/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/TABLE[3]/TBODY/TR[1]/TD[2]/P/TABLE/TBODY/TR[3]/TD/TABLE/TBODY/TR[1]/TD/TABLE/TBODY/TR[*]/TD[@class=\"smallText\"]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return url;
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(), element
										.getAttribute("href"), getAnalisator());
					}
				});
	};

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] {
				"http://www.kramnica.com/Vstraivaemaja-tehnika-Duhovki-c-14_21.html",
				"http://www.kramnica.com/Vstraivaemaja-tehnika-Duhovki-c-14_21.html?page=2&sort=products_sort_order",
				"http://www.kramnica.com/Vstraivaemaja-tehnika-Duhovki-c-14_21.html?page=3&sort=products_sort_order", };
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {ESectionEnd.NEXT_RECORDS_REPEAT_LAST, ESectionEnd.NEXT_RECORDS_REPEAT_LAST, ESectionEnd.NEXT_RECORDS_LOAD_ERROR};

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/TABLE[3]/TBODY/TR[1]/TD[2]/P/TABLE/TBODY/TR[8]/TD/TABLE[@class=\"productListing\"]/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD[@class=\"smallText\"]/table/tbody/tr[2]/td/table/tbody";
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
		return "/tr[1]/td[2]/a";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/tr[1]/td[2]/a";
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
		return "/tr[2]/td/strong";
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

	@Override
	protected Record prepareRecordBeforeSave(Record record) throws EParseException {
		return record;
	}
}