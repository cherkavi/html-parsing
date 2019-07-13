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

public class mobilife_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://mobilife.com.ua";
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
						"http://mobilife.com.ua",
						"windows-1251",
						"/html/body/center/table[@width=\"1000\"]/tbody/tr[3]/td[@valign=\"top\"]/table/tbody/tr/td[@width=\"224\"]/table[@width=\"148\"]/tbody/tr/td[@colspan=\"2\"]/table/tbody/tr[2]/td/table[@class=\"click-menu\"]/tbody/tr/td[@width=\"100\"]/div[@class=\"section\"][2]/div[@class=\"box2\"]/h3/a");
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						String url = addHttpPreambula("http://mobilife.com.ua",element.getAttribute("href"));
						return new UserSection(element.getTextContent(), url);
					}
				});
	}

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;
		private String preambula;
		
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// http://mobilife.com.ua/cat1926-Akkumulyatori.html
				// http://mobilife.com.ua/category1926-16.html
				// http://mobilife.com.ua/category1926-32.html
				if(preambula==null){
					int catIndex=this.getUrl().indexOf("/cat");
					int lastCatIndex=this.getUrl().lastIndexOf('-');
					preambula="http://mobilife.com.ua/category"+this.getUrl().substring(catIndex+4, lastCatIndex);
				}
				return preambula+"-"+(pageCounter-1)*16+".html";
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
		return "/HTML/BODY/CENTER/TABLE/TBODY/TR[3]/TD/TABLE/TBODY/TR/TD[2]/DIV/TABLE[2]/TBODY/TR/TD/TABLE[2]/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD[*]/TABLE/TBODY";
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
		return "/TR[1]/TD/H2/A[@class=\"product_name\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TR[1]/TD/H2/A[@class=\"product_name\"]";
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
	 // return "/TR[2]/TD[2]/TABLE/TBODY/TR[1]/TD/TABLE/TBODY/TR/TD[1]/FONT[@class=\"pric_hot\"]";
		return "/TR[2]/TD[2]/TABLE/TBODY/TR[1]/TD/TABLE/TBODY/TR/TD[1]/NOBR/FONT[@class=\"pric_new\"]";
	}

	@Override
	protected Node getPriceNodeFromLeafNode(Node node) {
		Node returnValue=this.parser.getNodeFromNode(node, this.recordFromNodeInRecordToPrice());
		if(returnValue!=null){
			return returnValue;
		}else{
			return this.parser.getNodeFromNode(node, "/TR[2]/TD[2]/TABLE/TBODY/TR[1]/TD/TABLE/TBODY/TR/TD[1]/FONT[@class=\"pric_hot\"]");
		}
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