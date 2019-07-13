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

public class cooltools_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.cooltools.com.ua";
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
		// removeNodeFromListByTextContent(listOfNode, new
		// String[]{"Банковское оборудование", "Автомобильные товары"});
		// removeNodeWithRepeatAttributes(list, "href");
		return new RecursiveFinder(
				this.parser
						.getNodeListFromUrl(
								"http://www.cooltools.com.ua",
								"utf-8",
								"/HTML/BODY/DIV[@class=\"siteBody\"]/DIV[@class=\"siteLayout clearfix\"]/DIV[@class=\"siteLeft\"]/DIV[@class=\"content\"]/UL[@class=\"menu\"]/A[*]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return addHttpPreambula("http://www.cooltools.com.ua",
								element.getAttribute("href"));
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/DIV[@class=\"siteBody\"]/DIV[@class=\"siteLayout clearfix\"]/DIV[@class=\"siteCenter\"]/DIV[@class=\"content\"]/DIV/DIV[@class=\"catalogList clearfix\"]/DIV[@id=\"right\"]/P[@class=\"subcatName\"]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return addHttpPreambula("http://www.cooltools.com.ua",
								element.getAttribute("href"));
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								addHttpPreambula("http://www.cooltools.com.ua",
										element.getAttribute("href")));
					}
				});
	};

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;

		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// http://www.cooltools.com.ua/index.html?cat=cpu&scat=intelcpu
				// http://www.cooltools.com.ua/index.html?cat=cpu&scat=intelcpu&Page=2
				// http://www.cooltools.com.ua/index.html?cat=cpu&scat=intelcpu&Page=3
				return this.getUrl().trim()+"&Page="+pageCounter;
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
		return "/HTML/BODY/DIV[@class=\"siteBody\"]/DIV[@class=\"siteLayout clearfix\"]/DIV[@class=\"siteCenter\"]/DIV[@class=\"content\"]/DIV";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[@class=\"catalogItem clearfix\"]/DIV[@class=\"right\"]";
	}

	@Override
	protected String recordFromNodeIsPresent() {
		return "/P[3]/IMG";
	}

	// обязательно есть аттрибут в Element, который содержит данные о наличии товара 
	protected String recordFromNodeIsPresentAttr(){
		return "title";
	}
	// текстовое значение аттрибута, который идентифицирует наличие товара
	protected String recordFromNodeIsPresentAttrValue(){
		return "есть";
	}
	
	@Override
	protected String recordFromNodeIsPresentText() {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToName() {
		return "/A[@class=\"title\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/A[@class=\"title\"]";
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
		return "/P[5]/B";
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
		if(record.getName()!=null){
			int index=record.getName().indexOf("] - ");
			if(index>0){
				record.setName(record.getName().substring(index+4));
			}
		}
		return record;
	}
}