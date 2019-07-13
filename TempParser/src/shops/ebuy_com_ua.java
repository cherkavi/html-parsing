package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.RecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.CurrentResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.element_extractor.CurrentUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.RecursiveFinder;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class ebuy_com_ua extends RecordListBaseMultiPage {
	// ------------------
	@Override
	public String getShopUrlStartPage() {
		return "http://ebuy.com.ua";
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
		return new RecursiveFinder(
				this.parser
						.getNodeListFromUrl("http://ebuy.com.ua", 
											"utf-8",
											"/HTML/BODY/DIV[@id=\"sidebar\"]/UL[@id=\"menu\"]/LI[*]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return addHttpPreambula("http://ebuy.com.ua", 
											    element.getAttribute("href"));
					}
				},
				new NodeListFinderByUrl(this.parser,
										this.getCharset(),
										"/HTML/BODY/DIV[@id=\"content\"]/DIV[@class=\"content_box clearfix\"]/DIV[*]/DIV[@class=\"box_full\"]/H2/A"),
				new CurrentUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return addHttpPreambula(this.getCurrentUrl(), element.getAttribute("href"));
					}
				}, 
				new CurrentResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
											   addHttpPreambula(getCurrentUrl(), element.getAttribute("href"))
											   );
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
				// http://ebuy.com.ua/catalog/server/srvdev/srvmem/
				// http://ebuy.com.ua/catalog/server/srvdev/srvmem/?page=2
				// http://ebuy.com.ua/catalog/server/srvdev/srvmem/?page=3
				String value=this.getUrl().trim();
				if(value.endsWith("/")){
					return this.getUrl()+"?page="+pageCounter;
				}else{
					return this.getUrl()+"/?page="+pageCounter;
				}
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
		return "/HTML/BODY/DIV[@id=\"content\"]/DIV[@class=\"content_box clearfix\"]/div[1]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[*]";
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
		return "/H3/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/H3/A";
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
		return "/DIV[@class=\"item_p\"]/SPAN[@class=\"col\"]/SPAN[@class=\"grn\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9]", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/DIV[@class=\"item_p\"]/SPAN[@class=\"col\"]/SPAN[@class=\"bax\"]";
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

	private String prepareUrl(String value){
		int index=value.lastIndexOf('/');
		return value.substring(0, index+1);
	}
	
	@Override
	protected Record prepareRecordBeforeSave(Record record)
			throws EParseException {
		if(record.getUrl()!=null){
			record.setUrl(this.removeStartPage(prepareUrl(this.getCurrentUrl())+record.getUrl()));
		}
		return record;
	}
}