package shops;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

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

public class _54321_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://54321.com.ua";
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
		// removeNodeWithRepeatAttributes(list, "href");
		
		ArrayList<Node> list=this.parser
		.getNodeListFromUrl(
				"http://54321.com.ua",
				"utf-8",
				"/HTML/BODY/DIV[@id=\"wrap\"]/DIV[@id=\"content\"]/DIV[@id=\"vmMainPage\"]/TABLE[@id=\"katalog\"]/TBODY/TR[*]/TD[*]/SPAN/A");
		removeNodeFromListByTextContent(list, new
				String[]{
						"Мой тест",
						"Дешево",
						"ТОВАРЫ Б/У",
						"Расходные материалы",
						"Офисное оборудование",
						"Программное обеспечение"
				});
		return new RecursiveFinder(
				list,
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return addHttpPreambula("http://54321.com.ua", element
								.getAttribute("href"));
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/DIV[@id=\"wrap\"]/DIV[@id=\"content\"]/DIV[@id=\"vmMainPage\"]/DIV[2]/TABLE[@id=\"katalog\"]/TBODY/TR[*]/TD[*]/SPAN/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return addHttpPreambula("http://54321.com.ua", element
								.getAttribute("href"));
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								addHttpPreambula("http://54321.com.ua", element
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
				// http://54321.com.ua/185-flash-memory-cards-sd-mmc.html
				// http://54321.com.ua/index.php?option=com_virtuemart&page=shop.browse&category_id=185&keyword=&manufacturer_id=0&Itemid=1&orderby=product_price&limitstart=20
				// http://54321.com.ua/index.php?option=com_virtuemart&page=shop.browse&category_id=185&keyword=&manufacturer_id=0&Itemid=1&orderby=product_price&limitstart=40
				if(preambula==null){
					String findString="category_id=";
					int indexBegin=this.getUrl().lastIndexOf(findString);
					int indexEnd=this.getUrl().indexOf('&', indexBegin);
					this.preambula="http://54321.com.ua/index.php?option=com_virtuemart&page=shop.browse&category_id="+this.getUrl().substring(indexBegin+findString.length(), indexEnd) +"&keyword=&manufacturer_id=0&Itemid=1&orderby=product_price&limitstart=";
				}
				return preambula+(pageCounter-1)*20;
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
		return "/HTML/BODY/DIV[@id=\"wrap\"]/DIV[@id=\"content\"]/DIV[@id=\"vmMainPage\"]/TABLE[@id=\"full\"]/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD/DIV[@class=\"foto_a\"]";
	}

	@Override
	protected String recordFromNodeIsPresent() {
		return "/TABLE/TBODY/TR/TD[3]/TABLE/TBODY/TR[2]/TD[2]/SPAN";
	}

	@Override
	protected String recordFromNodeIsPresentText() {
		return "есть";
	}

	@Override
	protected String recordFromNodeInRecordToName() {
		return "/A/SPAN/SPAN";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/A/SPAN/SPAN";
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
		return "/TABLE/TBODY/TR/TD[3]/TABLE/TBODY/TR[@class=\"wite_blue\"]/TD[2]/SPAN/SPAN[@class=\"productPrice\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9]", "");
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