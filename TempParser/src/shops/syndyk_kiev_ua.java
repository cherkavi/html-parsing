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

public class syndyk_kiev_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://syndyk.kiev.ua";
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
						"http://syndyk.kiev.ua/catalog/",
						"windows-1251",
						"/HTML/BODY/TABLE[@id=\"all\"]/TBODY/TR/TD/TABLE[2]/TBODY/TR/TD[3]/TABLE/TBODY/TR[2]/TD[1]/TABLE[2]/TBODY/TR/TD[*]/TABLE/TBODY/TR[*]/TD/A[@class=\"razdel2\"]");
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						String url = addHttpPreambula("http://syndyk.kiev.ua", (element).getAttribute("href"));
						return new UserSection(element.getTextContent(), url);
					}
				});
	}

	
	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}
		private int pageCounter = 0;
		// http://syndyk.kiev.ua/catalog/podr2/
		// http://syndyk.kiev.ua/catalog/podr2/index1.html
		// http://syndyk.kiev.ua/catalog/podr2/index2.html
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				String tempString=this.getUrl().trim();
				if(tempString.endsWith("/")){
					return tempString+"index"+(pageCounter-1)+".html";
				}else{
					return tempString+"/index"+(pageCounter-1)+".html";
				}
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
		return "/HTML/BODY/TABLE[@id=\"all\"]/TBODY/TR/TD/TABLE[2]/TBODY/TR/TD[3]/TABLE/TBODY/TR[2]/TD[1]/TABLE[3]/TBODY";
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
		return "/TD[2]/A[@class=\"razdel2\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TD[2]/A[@class=\"razdel2\"]";
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
		return "/TD[2]/B";
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