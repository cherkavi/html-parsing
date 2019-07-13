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

public class datamag_com_ua extends RecordListBaseMultiPage {
	// http://www.datamag.com.ua/Mcat6288/
	// http://www.datamag.com.ua/Mcat6288p1/
	// http://www.datamag.com.ua/Mcat6288p2/
	// XPath цены указывает на одну и ту же величину, пример:
	// ==============:0:b:==============
	// 117.16
	// 943.14
	// ==============:1:b:==============
	// 125.44
	// 1009.79
	@Override
	public String getShopUrlStartPage() {
		return "http://www.datamag.com.ua";
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
						"http://www.datamag.com.ua",
						"windows-1251",
						"/HTML/BODY/TABLE[@id=\"table4\"]/TBODY/TR[3]/TD/TABLE[@id=\"table9\"]/TBODY/TR/TD[1]/TABLE[@id=\"table11\"]/TBODY/TR[2]/TD/TABLE[@id=\"table13\"]/TBODY/TR/TD/TABLE/TBODY/TR[*]/TD/TABLE/TBODY/TR[2]/TD/DIV[*]/TABLE/TBODY/TR[*]/TD[1]/A");
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						String url = addHttpPreambula(
								"http://www.datamag.com.ua", element
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
		private String preambula=null;
		
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// http://www.datamag.com.ua/Mcat6288/
				// http://www.datamag.com.ua/Mcat6288p1/
				// http://www.datamag.com.ua/Mcat6288p2/
				if(preambula==null){
					int index=this.getUrl().lastIndexOf('/');
					this.preambula=this.getUrl().substring(0, index);
				}
				return preambula+"p"+(pageCounter-1)+"/";
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
		return "/HTML/BODY/TABLE[@id=\"table4\"]/TBODY/TR[3]/TD/TABLE[@id=\"table9\"]/TBODY/TR/TD[3]/TABLE[2]/TBODY";
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
		return "/TD[@class=\"tovar_title\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TD[@class=\"tovar_title\"]";
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
		return "/TD[5]/FONT/B";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		// XPath цены указывает на одну и ту же величину, пример:
		// ==============:0:b:==============
		// 117.16
		// 943.14
		// ==============:1:b:==============
		// 125.44
		// 1009.79
		int dotIndex=priceText.indexOf('.');
		dotIndex+=2;
		if(dotIndex<priceText.length()){
			return priceText.substring(dotIndex+1).replaceAll("[^0-9^.]", "");
		}
		return priceText.replaceAll("[^0-9^.]", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/TD[5]/FONT/B";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		// XPath цены указывает на одну и ту же величину, пример:
		// ==============:0:b:==============
		// 117.16
		// 943.14
		// ==============:1:b:==============
		// 125.44
		// 1009.79
		int dotIndex=priceText.indexOf('.');
		dotIndex+=2;
		if(dotIndex<priceText.length()){
			return priceText.substring(0, dotIndex+1).replaceAll("[^0-9^.]", "");
		}
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