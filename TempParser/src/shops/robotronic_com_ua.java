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

public class robotronic_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://robotronic.com.ua";
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
						"http://robotronic.com.ua",
						"windows-1251",
						"/HTML/BODY/TABLE[3]/TBODY/TR/TD[1]/TABLE[@class=\"lightmenu\"]/TBODY/TR/TD[2]/A");
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						String url = addHttpPreambula(
								"http://robotronic.com.ua", element
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
		private String preambula;
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// http://robotronic.com.ua/29.html
				// http://robotronic.com.ua/index.php?categoryID=29&offset=10
				// http://robotronic.com.ua/index.php?categoryID=29&offset=20
				
				// http://robotronic.com.ua/index.php?categoryID=29&offset=
				if(preambula==null){
					int indexBegin=this.getUrl().lastIndexOf('/');
					int indexEnd=this.getUrl().lastIndexOf('.');
					if(indexBegin>0&&indexEnd>0&&indexBegin<indexEnd){
						this.preambula="http://robotronic.com.ua/index.php?categoryID="+this.getUrl().substring(indexBegin+1, indexEnd)+"&offset=";
					}
				}
				return this.preambula+(pageCounter-1)*10;
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
		return "/HTML/BODY/TABLE[3]/TBODY/TR/TD[3]/TABLE/TBODY/TR[3]/TD[2]/TABLE/TBODY/TR[3]/TD/TABLE/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD[*]/P[0$]/TABLE/TBODY/TR[2]/TD[1]/TABLE/TBODY";
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
		return "/TR[1]/TD[1]/TABLE/TBODY/TR/TD/P/A[@class=\"brief_title\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TR[1]/TD[1]/TABLE/TBODY/TR/TD/P/A[@class=\"brief_title\"]";
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
		return "/TR[2]/TD/B[@class=\"brief_descr\"]/FONT";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "").replaceAll(".$", "");
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