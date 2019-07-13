package shops;

import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.standart.RecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.*;

public class tehnohaza_kiev_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://tehnohaza.kiev.ua";
	}

	@Override
	protected String getCharset() {
		return "utf-8";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		// import org.w3c.dom.Node;
		// import
		// shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
		// ArrayList<Node> listOfNode=(new NodeListFinderByUrl(parser,
		// getCharset(),
		// "/html/body/div[4]/div/div[2]/div[3]/table/tbody/tr[*]/td[@width="25%"]/div/div/ul/li[*]/a")).getNodeListByUrl(this.getShopUrlStartPage());
		ArrayList<Node> listOfNode = this.parser
				.getNodeListFromUrl(
						"http://tehnohaza.kiev.ua",
						"utf-8",
						"/HTML/BODY/DIV[@id=\"container\"]/DIV[@id=\"globalLeft\"]/DIV[@id=\"categoryTree\"]/UL/LI[*]/H4/A");
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								"http://tehnohaza.kiev.ua/"
										+ element.getAttribute("href"));
					}
				});
	}

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;
		private String preambula;
		private String postambula;
		
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				if(preambula==null){
					String searchString="category-";
					int index=this.getUrl().indexOf(searchString);
					if(index>0){
						preambula="http://tehnohaza.kiev.ua/category.php?page=";
						// http://tehnohaza.kiev.ua/category-29-b0-LCD.html
						// http://tehnohaza.kiev.ua/category.php?page=2&category=29&keywords=&sort=shop_price&order=DESC&cat=29&brand=0&price_min=0&price_max=0&filter_attr=&display=list
						// http://tehnohaza.kiev.ua/category.php?page=3&category=29&keywords=&sort=shop_price&order=DESC&cat=29&brand=0&price_min=0&price_max=0&filter_attr=&display=list
						// http://tehnohaza.kiev.ua/category-8-b0-%D0%9C%D0%B5%D0%BB%D0%BA%D0%BE%D0%B1%D1%8B%D1%82%D0%BE%D0%B2%D0%B0%D1%8F+%D1%82%D0%B5%D1%85%D0%BD%D0%B8%D0%BA%D0%B0.html
						// http://tehnohaza.kiev.ua/category.php?page=2&category=8&keywords=&sort=shop_price&order=DESC&cat=8&brand=0&price_min=0&price_max=0&filter_attr=&display=list
						// http://tehnohaza.kiev.ua/category.php?page=3&category=8&keywords=&sort=shop_price&order=DESC&cat=8&brand=0&price_min=0&price_max=0&filter_attr=&display=list
						int postIndex=0;
						postIndex=this.getUrl().indexOf("-", index+searchString.length()+1);
						String categoryId=this.getUrl().substring(index+searchString.length(),postIndex);
						postambula="&category="+categoryId+"&keywords=&sort=shop_price&order=DESC&cat="+categoryId+"&brand=0&price_min=0&price_max=0&filter_attr=&display=list";
					}
				}
				return preambula+pageCounter+postambula;
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
		return "/HTML/BODY/DIV[@id=\"container\"]/DIV[@id=\"globalBigRight\"]/DIV[@id=\"globalMiddle\"]/DIV[@id=\"globalGoodsList\"]/FORM/DIV[@class=\"itemList\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[@class=\"item\"]/DIV[@class=\"itemListInfo\"]";
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
		return "/P[@class=\"name\"]/A";
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
		return "/P[@class=\"price\"]/SPAN[@class=\"goodsPrice\"]";
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