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

public class e7_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.e7.com.ua";
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
						"http://www.e7.com.ua",
						"windows-1251",
						"/HTML/BODY/TABLE[@id=\"table1\"]/TBODY/TR[1]/TD/TABLE[@id=\"table3\"]/TBODY/TR/TD[3]/TABLE[@class=\"root_table\"]/TBODY/TR[5]/TD/TABLE[@id=\"table9\"]/TBODY/TR[1]/TD[1]/TABLE[@id=\"table12\"]/TBODY/TR/TD/TABLE[2]/TBODY/TR/TD/TABLE[*]/TBODY/TR/TD/TABLE/TBODY/TR[1]/TD/TABLE[2]/TBODY/TR[*]/TD[*]/A[@class=\"start_menu1\"]");
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								"http://www.e7.com.ua/"
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
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// http://www.e7.com.ua/index.php?go=Magaz&in=cat&id=7836
				// http://www.e7.com.ua/Mcat7836p1/
				if(preambula==null){
					int index=this.getUrl().lastIndexOf('=');
					if(index>0){
						String categoryId=this.getUrl().substring(index+1);
						preambula="http://www.e7.com.ua/Mcat"+categoryId+"p";
					}
				}
				return preambula+(pageCounter-1)+"/";
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
		return "/HTML/BODY[1]/TABLE[@id=\"table1\"]/TBODY/TR[1]/TD/TABLE[@id=\"table3\"]/TBODY/TR/TD[3]/TABLE[@class=\"root_table\"]/TBODY/TR[5]/TD/TABLE[@id=\"table9\"]/TBODY/TR[1]/TD[1]/TABLE[@id=\"table12\"]/tbody/tr/td/form[@name=\"tovar_sort\"]/table[@border=\"0\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/tr[*]";
	}

	@Override
	protected String recordFromNodeIsPresent() {
		return "/td[2]/table/tbody/tr/TD[3]/TABLE/TBODY/TR/TD[2]/FORM/TABLE/TBODY/TR[2]/TD[2]";
	}

	@Override
	protected String recordFromNodeIsPresentText() {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToName() {
		return "/td[2]/table/tbody/tr/td[2]/a/b";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/td[2]/table/tbody/tr/td[2]/a[1]";
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
		return "/td[2]/table/tbody/tr/TD[3]/TABLE/TBODY/TR/TD[2]/FORM/TABLE/TBODY/TR[2]/TD[2]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/td[2]/table/tbody/tr/TD[3]/TABLE/TBODY/TR/TD[2]/FORM/TABLE/TBODY/TR[2]/TD[1]";
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