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

public class perestrojka_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://perestrojka.com.ua";
	}

	@Override
	protected String getCharset() {
		return "utf-8";
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
								"http://perestrojka.com.ua/vstraivaemaya-bitovaya-technika",
								"utf-8",
								"/HTML/BODY[@id=\"s5_body\"]/DIV[@id=\"s5_outer_wrap\"]/DIV[@id=\"s5_top_wrap\"]/DIV[@id=\"s5_t_middle\"]/DIV[@id=\"s5_main_body_outer\"]/DIV[@id=\"s5_main_body_inner\"]/DIV[@id=\"s5_middle_wrapper\"]/DIV[@id=\"s5_right\"]/DIV[@id=\"s5_main_body_shadow\"]/DIV[@id=\"s5_main_body\"]/DIV[@id=\"s5_main_body2\"]/DIV[@id=\"s5_main_body3\"]/DIV[@id=\"s5_main_body4\"]/DIV[@id=\"vmMainPage\"]/DIV[2]/TABLE/TBODY/TR[*]/TD[*]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return addHttpPreambula("http://perestrojka.com.ua",
								element.getAttribute("href"));
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY[@id=\"s5_body\"]/DIV[@id=\"s5_outer_wrap\"]/DIV[@id=\"s5_top_wrap\"]/DIV[@id=\"s5_t_middle\"]/DIV[@id=\"s5_main_body_outer\"]/DIV[@id=\"s5_main_body_inner\"]/DIV[@id=\"s5_middle_wrapper\"]/DIV[@id=\"s5_right\"]/DIV[@id=\"s5_main_body_shadow\"]/DIV[@id=\"s5_main_body\"]/DIV[@id=\"s5_main_body2\"]/DIV[@id=\"s5_main_body3\"]/DIV[@id=\"s5_main_body4\"]/DIV[@id=\"vmMainPage\"]/DIV[2]/TABLE/TBODY/TR[*]/TD[*]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return addHttpPreambula("http://perestrojka.com.ua",
								element.getAttribute("href"));
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(), addHttpPreambula(
										"http://perestrojka.com.ua", element
												.getAttribute("href")),
								getAnalisator());
					}
				});
	};

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] { "http://perestrojka.com.ua/smeg-2",
				"http://perestrojka.com.ua/smeg-2?limit=20&limitstart=20",
				"http://perestrojka.com.ua/smeg-2?limit=20&limitstart=40", };
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
		return "/html/body[@id=\"s5_body\"]/div[@id=\"s5_outer_wrap\"]/div[@id=\"s5_top_wrap\"]/div[@id=\"s5_t_middle\"]/div[@id=\"s5_main_body_outer\"]/div[@id=\"s5_main_body_inner\"]/div[@id=\"s5_middle_wrapper\"]/div[@id=\"s5_right\"]/div[@id=\"s5_main_body_shadow\"]/div[@id=\"s5_main_body\"]/div[@id=\"s5_main_body2\"]/div[@id=\"s5_main_body3\"]/div[@id=\"s5_main_body4\"]/div[@id=\"vmMainPage\"]/div[@id=\"product_list\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/div[*]/div/div[@class=\"s5_browseProductContainer\"]";
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
		return "/h2/a";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/h2/a";
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
		return "/p/span[@class=\"productPrice\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "").replaceAll(".$", "");
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