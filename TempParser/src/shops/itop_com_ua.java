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
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.RecursiveFinder;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class itop_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.itop.com.ua";
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
								"http://www.itop.com.ua",
								"windows-1251",
								"/html[@xml:lang=\"en\"]/body/div[@class=\"container\"][2]/div[@class=\"content\"][2]/div[@class=\"block block-content\"]/div[@class=\"block block-shadowed\"][2]/div[@class=\"left\"]/ul[@class=\"leftmenu\"]/li[@class=\"item\"][7]/p/a"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return "http://www.itop.com.ua"
								+ element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/html[@xml:lang=\"en\"]/body/div[@class=\"container\"][2]/div[@class=\"content\"][2]/div[@class=\"block block-content\"]/div[@class=\"block block-shadowed\"][2]/div[@class=\"right\"][2]/div[@class=\"catalog\"][2]/div[@class=\"cell\"]/p[@class=\"name\"]/a"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return "http://www.itop.com.ua" + url;
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(),
								"http://www.itop.com.ua"
										+ element.getAttribute("href"),
								getAnalisator());
					}
				});
	};

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] {
				"http://www.itop.com.ua/catalog/list.php?SECTION_ID=531&IBLOCK_ID=32",
				"http://www.itop.com.ua/catalog/list.php?SECTION_ID=531&IBLOCK_ID=32&PAGEN_1=2",
				"http://www.itop.com.ua/catalog/list.php?SECTION_ID=531&IBLOCK_ID=32&PAGEN_1=3", };
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
		return "/html[@xml:lang=\"en\"]/body/div[@class=\"container\"]/div[@class=\"content\"]/div[@class=\"block block-content\"]/div[@class=\"block block-shadowed\"]/div[@class=\"right\"]/div[@class=\"catalog-in catalog-in-list\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/div[@class=\"cell\"]";
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
		return "/p[@class=\"name\"]/a";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/P[@class=\"name\"]/A";
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
		return "/div[@class=\"block block-shadowed block-border-shadowed priceblock\"][2]/div[@class=\"priceblock-space\"]/div[@class=\"price\"]/small";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "").replaceAll(".$", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/div[@class=\"block block-shadowed block-border-shadowed priceblock\"][2]/div[@class=\"priceblock-space\"]/div[@class=\"price\"]/big";
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