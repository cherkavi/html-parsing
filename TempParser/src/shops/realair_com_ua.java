package shops;

import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.*;

public class realair_com_ua extends AnalisatorRecordListBaseMultiPage {

	@Override
	public String getShopUrlStartPage() {
		return "http://www.realair.com.ua";
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
		// removeNodeWithRepeatAttributes(list, "href");
		ArrayList<Node> listOfNode = this.parser
				.getNodeListFromUrl(
						"http://www.realair.com.ua",
						"windows-1251",
						"/HTML/BODY/TABLE/TBODY/TR[4]/TD/TABLE/TBODY/TR/TD[1]/TABLE/TBODY/TR[2]/TD/TABLE/TBODY/TR[2]/TD[2]/TABLE[@class=\"text\"]/TBODY/TR[2]/TD/DIV[@class=\"cat_menu\"]/DIV[2]/A[@class=\"cat_menu\"]");
		removeNodeFromListByTextContent(listOfNode, new
				String[]{
				"Установка кондиционеров",
				"Обслуживание кондиционеров",
				"Аксессуары для увлажнителей",
				"Аксессуары для обогревателей",
				"Аксессуары для кондиционеров"
			});
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(), element
										.getAttribute("href"), getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] { "http://www.realair.com.ua/shop/osushenie.html",
				"http://www.realair.com.ua/shop/osushenie/1.html",
				"http://www.realair.com.ua/shop/osushenie/2.html", };
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
		return "/HTML/BODY/TABLE/TBODY/TR[4]/TD/TABLE/TBODY/TR/TD[2]/TABLE/TBODY/TR[2]/TD[2]/TABLE/TBODY/TR[2]/TD[@class=\"text\"]/TABLE/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD[*]/TABLE[@class=\"text\"]/TBODY";
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
		return "/TR/TD[2]/TABLE[@class=\"text\"]/TBODY/TR[1]/TD/A[@class=\"prd_title\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TR/TD[2]/TABLE[@class=\"text\"]/TBODY/TR[1]/TD/A[@class=\"prd_title\"]";
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
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		// return this.parser.getNodeFromNode(node, this.recordFromNodeInRecordToPriceUsd());
	    return "/TR/TD[2]/TABLE[@class=\"text\"]/TBODY/TR[4]/TD[@class=\"text\"]/TABLE[@class=\"text\"]/TBODY/TR/TD[1]/NOBR/DIV/SPAN";
	}

	/** получить цену USD из элемента, который содержит все данные о текущей позиции в целом   */
	protected Node getPriceUsdNodeFromLeafNode(Node node){
		return getNodeFirstExists(node, priceUrl);
	}
	
	private String[] priceUrl=new String[]{
			"/TR[4]/TD[@class=\"text\"]/TABLE[@class=\"text\"]/TBODY/TR/TD[1]/NOBR/DIV/SPAN",
			"/TR[2]/TD[2]/TABLE[@class=\"text\"]/TBODY/TR/TD/DIV[@class=\"price_cond\"]/SPAN",
			"/TR/TD[2]/TABLE[@class=\"text\"]/TBODY/TR[2]/TD[2]/TABLE[@class=\"text\"]/TBODY/TR/TD/DIV[@class=\"price_cond\"]/SPAN",
			"/TR/TD[2]/TABLE[@class=\"text\"]/TBODY/TR[4]/TD[@class=\"text\"]/TABLE[@class=\"text\"]/TBODY/TR/TD[1]/NOBR/DIV/SPAN"
	};
	
	private Node getNodeFirstExists(Node node, String[] urls){
		for(int counter=0;counter<urls.length;counter++){
			Node tempNode=this.parser.getNodeFromNode(node, urls[counter]);
			if(tempNode!=null){
				return tempNode;
			}
		}
		return null;
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