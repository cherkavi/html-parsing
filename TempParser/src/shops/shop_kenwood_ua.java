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

public class shop_kenwood_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://shop.kenwood.ua";
	}

	@Override
	protected String getCharset() {
		return "windows-1251";
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
						"http://shop.kenwood.ua",
						"windows-1251",
						"/HTML/BODY/TABLE[@id=\"header_1\"]/TBODY/TR/TD/TABLE[2]/TBODY/TR/TD[@class=\"catalog\"]/DIV[@class=\"catalog_border_4\"]/DIV[@class=\"catalog_border_2\"]/DIV[@class=\"catalog_border\"]/UL[@class=\"catalog_border_3\"]/LI[*]/strong/a");
		ArrayList<Node> listOfNode2 = this.parser
		.getNodeListFromUrl(
						"http://shop.kenwood.ua",
						"windows-1251",
						"/HTML/BODY/TABLE[@id=\"header_1\"]/TBODY/TR/TD/TABLE[2]/TBODY/TR/TD[@class=\"catalog\"]/DIV[@class=\"catalog_border_4\"]/DIV[@class=\"catalog_border_2\"]/DIV[@class=\"catalog_border\"]/UL[@class=\"catalog_border_3\"]/LI[*]/DIV/div/ul/li[*]/a");
		listOfNode.addAll(listOfNode2);
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								addHttpPreambula(getShopUrlStartPage(), element.getAttribute("href")));
					}
				});
	}

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;
		private String preambula=null;
		private String suffix=".html";
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// http://shop.kenwood.ua/shop/CID_17.html
				// http://shop.kenwood.ua/shop/CID_17_2.html
				if(preambula==null){
					int dotIndex=this.getUrl().lastIndexOf('.');
					this.preambula=this.getUrl().substring(0, dotIndex)+"_";
				}
				return preambula+pageCounter+suffix;
			}
			return this.getUrl();
		}
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_REPEAT_LAST,
			ESectionEnd.NEXT_RECORDS_LOAD_ERROR };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/TABLE[@id=\"header_1\"]/TBODY/TR/TD/TABLE[2]/TBODY/TR/TD[@class=\"pad_30\"]/DIV[3]/TABLE/TBODY/TR/TD[1]/TABLE[2]/TBODY/TR[1]/TD/TABLE/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD[*]/DIV[@class=\"product_forma\"]";
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
		return "/DIV[1]/A[@class=\"product_name\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/DIV[1]/A[@class=\"product_name\"]";
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
		return "/DIV[@class=\"price_2\"]/DIV[@class=\"price\"]";
	}

	@Override
	protected Node getPriceNodeFromLeafNode(Node node) {
		Node returnValue=super.getPriceNodeFromLeafNode(node);
		if(returnValue==null){
			returnValue=this.parser.getNodeFromNode(node, "/TABLE/TBODY/TR[2]/TD[1]/DIV/SPAN[@class=\"price\"]");
		}
		return returnValue;
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