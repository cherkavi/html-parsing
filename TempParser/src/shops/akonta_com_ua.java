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

public class akonta_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://akonta.com.ua";
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
								"http://akonta.com.ua",
								"utf-8",
								"/HTML/BODY[@id=\"bd\"]/DIV[@id=\"ja-wrapper\"]/DIV[@id=\"ja-containerwrap-fr\"]/DIV[@id=\"ja-containerwrap2\"]/DIV[@id=\"ja-container\"]/DIV[@id=\"ja-container2\"]/DIV[@id=\"ja-mainbody-fr\"]/DIV[@id=\"ja-col1\"]/DIV[@class=\"moduletable\"]/DIV[@id=\"wrap\"]/DIV[@id=\"menu\"]/TABLE/TBODY/TR[*]/TD/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY[@id=\"bd\"]/DIV[@id=\"ja-wrapper\"]/DIV[@id=\"ja-containerwrap-fr\"]/DIV[@id=\"ja-containerwrap2\"]/DIV[@id=\"ja-container\"]/DIV[@id=\"ja-container2\"]/DIV[@id=\"ja-mainbody-fr\"]/DIV[@id=\"ja-contentwrap\"]/DIV[@id=\"ja-content\"]/DIV[@id=\"vmMainPage\"]/DIV[2]/TABLE/TBODY/TR[*]/TD[*]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return addHttpPreambula("http://akonta.com.ua", element
								.getAttribute("href"));
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								addHttpPreambula("http://akonta.com.ua",
										element.getAttribute("href")));
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
			if(preambula==null){
				String findString="category_id=";
				int indexBegin=this.getUrl().indexOf(findString);
				int indexEnd=this.getUrl().indexOf('&',indexBegin);
				if(indexEnd<0){
					this.preambula="http://www.akonta.com.ua/tovar.html?category_id="+this.getUrl().substring(indexBegin+findString.length())+"&page=shop.browse&limit=20&limitstart=";
				}else{
					this.preambula="http://www.akonta.com.ua/tovar.html?category_id="+this.getUrl().substring(indexBegin+findString.length(), indexEnd)+"&page=shop.browse&limit=20&limitstart=";
				}
				
			}
			// http://www.akonta.com.ua/index.php?page=shop.browse&category_id=2&vmcchk=1&option=com_virtuemart&Itemid=16
			// http://www.akonta.com.ua/tovar.html?category_id=2&page=shop.browse&limit=20&limitstart=20
			return this.preambula+(pageCounter-1)*20;
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
		return "/HTML/BODY[@id=\"bd\"]/DIV[@id=\"ja-wrapper\"]/DIV[@id=\"ja-containerwrap-fr\"]/DIV[@id=\"ja-containerwrap2\"]/DIV[@id=\"ja-container\"]/DIV[@id=\"ja-container2\"]/DIV[@id=\"ja-mainbody-fr\"]/DIV[@id=\"ja-contentwrap\"]/DIV[@id=\"ja-content\"]/DIV[@id=\"vmMainPage\"]/DIV[@id=\"product_list\"]";
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
		return "/TABLE[2]/TBODY/TR[2]/TD[2]/H2";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TABLE[2]/TBODY/TR[3]/TD[2]/A";
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
		return "/TABLE[2]/TBODY/TR[3]/TD[3]/SPAN[@class=\"productPrice\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "");
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