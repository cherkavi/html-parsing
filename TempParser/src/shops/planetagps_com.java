package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.standart.RecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.*;

public class planetagps_com extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://planetagps.com";
	}

	@Override
	protected String getCharset() {
		return "windows-1251";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new DirectFinder(
				this.parser
						.getNodeListFromUrl(
								"http://planetagps.com",
								"windows-1251",
								"/HTML/BODY/TABLE[@class=\"main left\"]/TBODY/TR[1]/TD[@class=\"right\"]/DIV[@class=\"body\"]/DIV[@class=\"top\"]/DIV[@class=\"top-left\"]/DIV[@class=\"top-right pad\"]/TABLE/TBODY/TR/TD[@class=\"left-pane\"]/DIV[@class=\"menu cat\"]/A[*]"),
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								"http://planetagps.com"
										+ element.getAttribute("href"));
					}
				});
	}

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;
		private String preambula=null;
		private String postambula=null;
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// http://planetagps.com/c/GPS_navigatory.html
				// http://planetagps.com/c/2/10/GPS_navigatory.html
				// http://planetagps.com/c/3/10/GPS_navigatory.html
				if(preambula==null){
					int index=this.getUrl().indexOf("/c/");
					if(index>0){
						preambula=this.getUrl().substring(0, index+3);
						postambula=this.getUrl().substring(index+3);
					}else{
						return null;
					}
				}
				
				return preambula+pageCounter+"/10/"+postambula;
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
		return "/HTML/BODY/TABLE[@class=\"main left\"]/TBODY/TR[1]/TD[@class=\"right\"]/DIV[@class=\"body\"]/DIV[@class=\"top\"]/DIV[@class=\"top-left\"]/DIV[@class=\"top-right pad\"]/TABLE/TBODY/TR/TD[@class=\"middle-pane\"]/TABLE[@class=\"catalog\"]/TBODY/TR/TD/TABLE[@class=\"items\"]/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[@class=\"up\"]";
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
		return "/TD[@class=\"text\"]/a/span";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TD[@class=\"text\"]/a";
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
		return "/TD[@class=\"buy\"]/div[1]/div[@class=\"value\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		return priceText.replaceAll("[^0-9^,]", "");
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
	protected Record prepareRecordBeforeSave(Record record) throws EParseException {
		return record;
	}
}