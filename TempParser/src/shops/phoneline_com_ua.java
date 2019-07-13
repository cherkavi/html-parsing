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

public class phoneline_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.phoneline.com.ua";
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
		// removeNodeFromListByTextContent(listOfNode, new
		// String[]{"Банковское оборудование", "Автомобильные товары"});
		ArrayList<Node> listOfNode = this.parser
				.getNodeListFromUrl(
						"http://www.phoneline.com.ua",
						"windows-1251",
						"/HTML/body/BODY/TABLE[2]/TBODY/TR/TD[3]/TABLE/TBODY/TR/TD[1]/TABLE[3]/TBODY/TR/TD/TABLE/TBODY/TR[1]/TD/TABLE[1]/TBODY");
		ArrayList<Node> listOfNode2 = this.parser
		.getNodeListFromUrl(
				"http://www.phoneline.com.ua",
				"windows-1251",
				"/HTML/body/BODY/TABLE[2]/TBODY/TR/TD[1]/TABLE[2]/TBODY/TR[*]/TD/TABLE[*]/TBODY/TR[*]/TD[3]/A");
		listOfNode.addAll(listOfNode2);
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						String url = addHttpPreambula(
								"http://www.phoneline.com.ua", element
										.getAttribute("href"));
						return new UserSection(element.getTextContent(), url);
					}
				});
	}

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;
		private String valueT;
		private String valueNB;
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if(valueT==null){
				valueT=this.getParameter(this.getUrl(), "t");
				valueNB=this.getParameter(this.getUrl(), "nb");
			}
			return "http://www.phoneline.com.ua/product_list.php?"+"t="+valueT+"&nb="+valueNB+"&link="+pageCounter;
		}
		
		private String getParameter(String value, String parameterName){
			String searchString=parameterName.trim()+"=";
			int index=value.indexOf(searchString);
			if(index>0){
				int indexEnd=value.indexOf('&', index+searchString.length());
				if(indexEnd>0){
					// &
					return value.substring(index+searchString.length(), indexEnd);
				}else{
					return value.substring(index+searchString.length());
				}
			}else{
				return null;
			}
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
		return "/HTML/body/BODY/TABLE[2]/TBODY/TR/TD[3]/TABLE/TBODY/TR/TD[1]/TABLE[3]/TBODY/TR/TD/TABLE/TBODY/TR[1]/TD/TABLE[1]/TBODY";
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
		return "/TD[3]/STRONG/A[@class=\"link_18_1\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TD[3]/STRONG/A[@class=\"link_18_1\"]";
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
		return "/TD[3]/TABLE/TBODY/TR/TD[1]/DIV/FONT/STRONG/FONT";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		return priceText.replaceAll("[^0-9^.]", "");
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