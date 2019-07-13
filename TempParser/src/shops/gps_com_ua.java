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


public class gps_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://gps.com.ua/";
	}

	@Override
	protected String getCharset() {
		return "utf-8";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		// // return new TestStubFinder(new
		// UniversalAnalisator("temp","http://",getAnalisator()));
		return new RecursiveFinder(
				this.parser
						.getNodeListFromUrl(
								"http://gps.com.ua/",
								"utf-8",
								"/HTML/BODY/TABLE[1]/TBODY/TR[2]/TD/TABLE/TBODY/TR/TD[@class=\"bg\"]/TABLE/TBODY/TR/TD[@class=\"col_left\"]/TABLE[@class=\"box_width_left\"]/TBODY/TR/TD[1]/TABLE/TBODY/TR[3]/TD/TABLE[2]/TBODY/TR/TD/TABLE[@class=\"box_body\"]/TBODY/TR[4]/TD/TABLE[2]/TBODY/TR/TD/TABLE[@class=\"box_body\"]/TBODY/TR/TD/UL/LI[@class=\"bg_list\"]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/TABLE[1]/TBODY/TR[2]/TD/TABLE/TBODY/TR/TD[@class=\"bg\"]/TABLE/TBODY/TR/TD[@class=\"col_center\"]/TABLE[2]/TBODY/TR/TD/TABLE[@class=\"heading_top_3\"]/TBODY/TR/TD[@class=\"padd_33\"]/TABLE[@class=\"box_width_cont product\"]/TBODY/TR[*]/TD[@align=\"center\"]/TABLE/TBODY/TR/TD[@class=\"vam pic\"]/A[2]"),
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
				"http://www.gps.com.ua/GPS-navigatory/Avtomobil-nye-GPS---1_12.html",
				"http://www.gps.com.ua/GPS-navigatory/Avtomobil-nye-GPS---1_12-2.html",
				"http://www.gps.com.ua/GPS-navigatory/Avtomobil-nye-GPS---1_12-3.html", };
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
		return "/HTML/BODY/TABLE[1]/TBODY/TR[2]/TD/TABLE/TBODY/TR/TD[@class=\"bg\"]/TABLE/TBODY/TR/TD[@class=\"col_center\"]/TABLE[2]/TBODY/TR/TD/TABLE[@class=\"heading_top_3\"]/TBODY/TR/TD[@class=\"padd_33\"]/TABLE[@class=\"box_width_cont product\"]/TBODY/TR/TD/TABLE[@class=\"tableBox_output\"]/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD[@align=\"center\"]/TABLE/TBODY/TR/TD[@style=\"width:100%;\"]/TABLE/TBODY";
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
		return "/tr[1]/td/a";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/tr[1]/td/a";
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
		return "/tr[3]/td/table/tbody/tr/td[@nowrap=\"nowrap\"]/table/tbody/tr/td/span";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^,]", "");
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