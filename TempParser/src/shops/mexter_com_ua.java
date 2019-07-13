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

public class mexter_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.mexter.com.ua";
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
						"http://www.mexter.com.ua",
						"windows-1251",
						"/HTML/BODY/TABLE[1]/TBODY/TR[2]/TD[@class=\"MPAGE\"]/TABLE/TBODY/TR/TD/TABLE[2]/TBODY/TR[*]/TD[*]/A[@class=\"HC_LINK\"]");
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								"http://www.mexter.com.ua/"
										+ element.getAttribute("href"));
					}
				});
	}

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;

		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				return this.getUrl().trim()+"&offset="+(pageCounter-1)*30;
			}
			return this.getUrl();
		}
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_SHOW_FIRST,
			ESectionEnd.NEXT_RECORDS_REPEAT_LAST,
			ESectionEnd.NEXT_PAGE_LOAD_ERROR,
			ESectionEnd.NEXT_RECORDS_LOAD_ERROR };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/TABLE/TBODY/TR[2]/TD[@class=\"MPAGE\"]/TABLE/TBODY/TR/TD/TABLE[2]/TBODY/TR[2]/TD[@class=\"PAGE\"]/TABLE/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD/TABLE/TBODY/TR[2]/TD[2]/TABLE/TBODY";
	}

	@Override
	protected String recordFromNodeIsPresent() {
		return "/TR[1]/TD[3]/TABLE/TBODY/TR[1]/TD/FONT[*]/FONT";
	}

	@Override
	protected String recordFromNodeIsPresentText() {
		return "нет в наличии";
	}

	@Override
	protected boolean isPresentTextExistsMustPresent() {
		return false;
	}

	@Override
	protected String recordFromNodeInRecordToName() {
		return "/TR[1]/TD[@class=\"P_HEAD\"]/A[@class=\"P_LINKH\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TR[1]/TD[@class=\"P_HEAD\"]/A[@class=\"P_LINKH\"]";
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
		return "/TR[1]/TD[3]/TABLE/TBODY/TR[1]/TD/FONT[@class=\"P_PRICE_Y\"]";
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
	protected Record prepareRecordBeforeSave(Record record)
			throws EParseException {
		return record;
	}
}