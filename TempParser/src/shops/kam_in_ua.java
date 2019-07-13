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

public class kam_in_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.kam.in.ua";
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
						"http://www.kam.in.ua",
						"windows-1251",
						"/HTML/BODY/CENTER/TABLE[1]/TBODY/TR/TD/TABLE/TBODY/TR/TD[2]/TABLE/TBODY/TR[2]/TD/P[1]/TABLE/TBODY/TR[*]/TD[*]/A[@class=\"cat\"]");
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						String url = addHttpPreambula("http://www.kam.in.ua",
								element.getAttribute("href"));
						return new UserSection(element.getTextContent(), url);
					}
				});
	}

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
				// http://www.kam.in.ua/category20522.html
				// http://www.kam.in.ua/index.php?categoryID=20522&offset=15
				// http://www.kam.in.ua/index.php?categoryID=20522&offset=30
				
				// http://www.kam.in.ua/index.php?categoryID=20522&offset=
				if(preambula==null){
					String findValue="/category";
					int indexBegin=this.getUrl().indexOf(findValue);
					int indexEnd=this.getUrl().indexOf(".html");
					if(indexBegin>0&&indexEnd>0&&indexBegin<indexEnd){
						preambula="http://www.kam.in.ua/index.php?categoryID="+this.getUrl().substring(indexBegin+findValue.length(), indexEnd)+"&offset=";
					}
				}
				return preambula+(pageCounter-1)*15;
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
		return "/HTML/BODY/CENTER/TABLE/TBODY/TR/TD/TABLE[1]/TBODY/TR/TD[2]/TABLE/TBODY/TR/TD/P/TABLE/TBODY/TR/TD[1]/TABLE[2]/TBODY/TR/TD/TABLE/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD[*]";
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
		return "/P/TABLE[3]/TBODY/TR/TD/TABLE/TBODY/TR[1]/TD/A[@class=\"cat\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/P/TABLE[3]/TBODY/TR/TD/TABLE/TBODY/TR[1]/TD/A[@class=\"cat\"]";
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
		return "/p/table[2]/tbody/tr/td[@valign=\"top\"]/form/table/tr/td/b/font";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		// 96.04 грн. (12.02 у.е.)
		int index=priceText.indexOf('(');
		if(index>0){
			return priceText.substring(0, index).replaceAll("[^0-9^.]", "").replaceAll(".$", "");
		}else{
			return "";
		}
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/p/table[2]/tbody/tr/td[@valign=\"top\"]/form/table/tr/td/b/font";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		// 96.04 грн. (12.02 у.е.)
		int index=priceText.indexOf('(');
		if(index>0){
			return priceText.substring(index).replaceAll("[^0-9^.]", "").replaceAll(".$", "").replaceAll(".$", "");
		}else{
			return "";
		}
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