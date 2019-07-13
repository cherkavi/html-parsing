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
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.RecursiveFinder;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class technoprestige_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://technoprestige.com.ua";
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
		// removeNodeFromListByTextContent(listOfNode, new
		// String[]{"Банковское оборудование", "Автомобильные товары"});
		// removeNodeWithRepeatAttributes(list, "href");
		return new RecursiveFinder(
				this.parser
						.getNodeListFromUrl(
								"http://technoprestige.com.ua",
								"windows-1251",
								"/HTML/BODY/TABLE[2]/TBODY/TR[2]/TD/TABLE[2]/TBODY/TR/TD[2]/TABLE/TBODY/TR/TD/TABLE[2]/TBODY/TR/TD/TABLE/TBODY/TR[*]/TD[@class=\"superclass\"]/TABLE/TBODY/TR/TD/TABLE/TBODY/TR[1]/TD[2]/TABLE/TBODY/TR[*]/TD/TABLE/TBODY/TR/TD/TABLE/TBODY/TR/TD[2]/DIV/A[@class=\"category_items\"]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return addHttpPreambula("http://technoprestige.com.ua",
								element.getAttribute("href"));
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/TABLE[2]/TBODY/TR[2]/TD/TABLE[2]/TBODY/TR/TD[2]/TABLE/TBODY/TR/TD/TABLE[2]/TBODY/TR/TD/DIV[@id=\"producer\"]/DIV[@class=\"producer\"]/H3/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return addHttpPreambula("http://technoprestige.com.ua",
								element.getAttribute("href"));
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								addHttpPreambula(
										"http://technoprestige.com.ua", element
												.getAttribute("href")));
					}
				});
	};

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;
		private String preambula=null;
		
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// http://technoprestige.com.ua/cat_278.htm
				// http://technoprestige.com.ua/index.php?categoryID=278&offset=10
				// http://technoprestige.com.ua/index.php?categoryID=278&offset=20
				if(preambula==null){
					int index=this.getUrl().lastIndexOf('_');
					if(index>0){
						int indexEnd=this.getUrl().indexOf('.', index);
						this.preambula="http://technoprestige.com.ua/index.php?categoryID="+this.getUrl().substring(index+1, indexEnd)+"&offset=";
					}
				}
				// http://technoprestige.com.ua/index.php?categoryID=278&offset=
				return preambula+(pageCounter-1)*10;
			}
			return this.getUrl();
		}
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
		return "/HTML/BODY/TABLE[2]/TBODY/TR[2]/TD/TABLE[2]/TBODY/TR/TD[2]/TABLE/TBODY/TR/TD/TABLE[2]/TBODY/TR/TD/CENTER/TABLE/TBODY/TR/TD[1]/TABLE/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD/TABLE[@class=\"ShopContent\"]/TBODY";
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
		return "/TR/TD/TABLE[1]/TBODY/TR/TD[2]/TABLE/TBODY/TR[1]/TD[1]/TABLE/TBODY/TR/TD/A[@class=\"productTitle\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TR/TD/TABLE[1]/TBODY/TR/TD[2]/TABLE/TBODY/TR[1]/TD[1]/TABLE/TBODY/TR/TD/A[@class=\"productTitle\"]";
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
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/TR/TD/TABLE[1]/TBODY/TR/TD[2]/TABLE/TBODY/TR[2]/TD/B/FONT";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
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