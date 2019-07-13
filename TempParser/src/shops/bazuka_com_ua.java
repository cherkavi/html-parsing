package shops;

import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.standart.RecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.*;

public class bazuka_com_ua extends RecordListBaseMultiPage {
	// по первому блоку ссылок удалять всех, кто имеет HREF="javascript:"
	// /HTML/BODY/DIV[@class="minwidth"]/DIV[@class="main-container"]/DIV[@id="leftcolumn"]/DIV[@class="cat_short"]/DIV/A[@class="orang b_top_sel"]
	@Override
	public String getShopUrlStartPage() {
		return "http://bazuka.com.ua";
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
		// removeNodeWithRepeatAttributes(list, "href");
		ArrayList<Node> listOfNode = this.parser
				.getNodeListFromUrl(
						"http://bazuka.com.ua",
						"utf-8",
						"/HTML/BODY/DIV[@class=\"minwidth\"]/DIV[@class=\"main-container\"]/DIV[@id=\"leftcolumn\"]/DIV[@class=\"cat_short\"]/DIV/A[@class=\"orang b_top_sel\"]");
		ArrayList<Node> listOfNode2 = this.parser
		.getNodeListFromUrl(
				"http://bazuka.com.ua",
				"utf-8",
				"/HTML/BODY/DIV[@class=\"minwidth\"]/DIV[@class=\"main-container\"]/DIV[@id=\"leftcolumn\"]/DIV[@class=\"cat_short\"]/DIV/DIV[@class=\"subcat\"]/A[*]");
		listOfNode.addAll(listOfNode2);
		removeNodeFromListByTextContent(listOfNode, new
		String[]{
				"Карточки продления лицензии антивирусного ПО",
				"Антивирусное ПО",
				"Прикладное и офисное ПО",
				"Операционные системы"
				});
		
		
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						String href=element.getAttribute("href");
						if(href.indexOf("javascript:")>=0)return null;
						return new UserSection(element.getTextContent(),href);
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
				// http://bazuka.com.ua/products/show/c/10002/sc/KT07272.html
				// http://bazuka.com.ua/products/show/p/24/c/10002/sc/KT07272.html
				// http://bazuka.com.ua/products/show/p/48/c/10002/sc/KT07272.html
				
				// http://bazuka.com.ua/products/show/c/10002/sc/KT07272.html
				// http://bazuka.com.ua/products/show/p/24/c/10002/sc/KT07272.html
				// http://bazuka.com.ua/products/show/p/48/c/10002/sc/KT07272.html
				if(preambula==null){
					preambula="http://bazuka.com.ua/products/show/p/";
					int index=this.getUrl().indexOf("/show/c");
					postambula=this.getUrl().substring(index+5);
				}
				return preambula+(pageCounter-1)*24+postambula;
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
		return "/HTML/BODY[@id=\"lft_c_rgt\"]/DIV[@class=\"minwidth\"]/DIV[@class=\"main-container\"]/DIV[@class=\"wrap\"]/DIV[@id=\"contentcolumn\"]/DIV[@class=\"t-result\"]/TABLE[@id=\"result\"]/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]";
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
		return "/TD[3]/DIV[@class=\"desc\"]/A[@class=\"orang\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TD[3]/DIV[@class=\"desc\"]/A[@class=\"orang\"]";
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
		return "/TD[@class=\"cntr price\"]/STRONG[@class=\"price\"]";
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