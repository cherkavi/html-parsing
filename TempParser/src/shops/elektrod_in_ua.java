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
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.RecursiveFinder;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class elektrod_in_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://elektrod.in.ua";
	}

	@Override
	protected String getCharset() {
		return "windows-1251";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		// return new TestStubFinder(new
		// UniversalAnalisator("temp","http://",getAnalisator()));
		// import org.w3c.dom.Node;
		// import
		// shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
		// ArrayList<Node> listOfNode=(new NodeListFinderByUrl(parser,
		// getCharset(),
		// "/html/body/div[4]/div/div[2]/div[3]/table/tbody/tr[*]/td[@width="25%"]/div/div/ul/li[*]/a")).getNodeListByUrl(this.getShopUrlStartPage());
		return new RecursiveFinder(
				this.parser
						.getNodeListFromUrl(
								"http://elektrod.in.ua",
								"windows-1251",
								"/HTML/BODY/TABLE/TBODY/TR/TD[@id=\"page\"]/TABLE[@id=\"content\"]/TBODY/TR/TD[@class=\"left\"]/TABLE/TBODY/TR[1]/TD[@class=\"categories\"]/DIV[@class=\"box_cat\"]/DIV[@class=\"body\"]/TABLE[*]/TBODY/TR/TD[@class=\"main top-level-cat\"]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return addHttpPreambula(getShopUrlStartPage(), element.getAttribute("href")) ;
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/TABLE/TBODY/TR/TD[@id=\"page\"]/TABLE[@id=\"content\"]/TBODY/TR/TD[@class=\"center\"]/TABLE[@class=\"simple_table_box\"]/TBODY/TR[2]/TD/TABLE/TBODY/TR[*]/TD/TABLE/TBODY/TR/TD/TABLE/TBODY/TR[*]/TD[@class=\"smallText\"]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return addHttpPreambula(getShopUrlStartPage(), url);
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(), addHttpPreambula(getShopUrlStartPage(), element.getAttribute("href")), getAnalisator());
					}
				});
	};

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] {
				"http://elektrod.in.ua/Bitovaya_tehnika_Holodilnik_s_verhney_morozilnoy_kameroy_Atlant-c-100400_100426_100523.html",
				"http://elektrod.in.ua/Bitovaya_tehnika_Holodilnik_s_verhney_morozilnoy_kameroy_Atlant-c-100400_100426_100523-page-2.html",
				"http://elektrod.in.ua/Bitovaya_tehnika_Holodilnik_s_verhney_morozilnoy_kameroy_Atlant-c-100400_100426_100523-page-3.html", };
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
		return "/HTML/BODY/TABLE/TBODY/TR/TD[@id=\"page\"]/TABLE[@id=\"content\"]/TBODY/TR/TD[@class=\"center\"]/TABLE[@class=\"simple_table_box\"]/TBODY/TR/TD/TABLE/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD[@class=\"main product-list\"]/DIV[@class=\"cboxc\"]/DIV/DIV/DIV/DIV/DIV/DIV/DIV/TABLE[@class=\"product-list\"]/TBODY/TR/TD[2]/TABLE/TBODY";
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
		return "/TR[1]/TD[1]/TABLE/TBODY/TR[1]/TD[@class=\"product-list-head\"]/A[@class=\"main bold\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TR[1]/TD[1]/TABLE/TBODY/TR[1]/TD[@class=\"product-list-head\"]/A[@class=\"main bold\"]";
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
		return "/TR[1]/TD[2]/TABLE/TBODY/TR/TD[@class=\"main\"]/SPAN";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/TR[1]/TD[2]/TABLE/TBODY/TR/TD[@class=\"main\"]/DIV[@class=\"productPrice bold\"]";
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