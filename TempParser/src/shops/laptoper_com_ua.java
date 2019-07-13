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

public class laptoper_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.laptoper.com.ua";
	}

	@Override
	protected String getCharset() {
		return "utf-8";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new DirectFinder(
				this.parser
						.getNodeListFromUrl(
								"http://www.laptoper.com.ua",
								"utf-8",
								"/HTML/BODY/TABLE[@id=\"main\"]/TBODY/TR[@id=\"content\"]/TD[@class=\"leftcol left-img\"]/UL[@id=\"ctl00__categories\"]/LI[*]/A"),
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								"http://www.laptoper.com.ua/"
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
				// http://www.laptoper.com.ua//category-261-58-12-1-0.html
				// http://www.laptoper.com.ua//category-261-58-1-1-0.html
				// http://www.laptoper.com.ua/category-270-60-1-1-W3sicCI6OTUwMSwidiI6WzE4Ml19XQ.html
				// http://www.laptoper.com.ua/category-270-60-2-1-W3sicCI6OTUwMSwidiI6WzE4Ml19XQ.html
				if(preambula==null){
					int index=0;
					for(int counter=1;counter<=3;counter++){
						index=this.getUrl().indexOf('-', index+1);
					}
					int indexEnd=this.getUrl().indexOf('-',index+1);
					preambula=this.getUrl().substring(0,index+1);
					postambula=this.getUrl().substring(indexEnd);
				}
				return preambula+(pageCounter)+postambula;
			}
			return this.getUrl();
		}
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_LOAD_ERROR,
			ESectionEnd.NEXT_RECORDS_ZERO_SIZE };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/TABLE[@id=\"main\"]/TBODY/TR[@id=\"content\"]/TD[@class=\"centercol\"]/TABLE[@class=\"catalog\"]/TBODY/TR[@id=\"items\"]/TD[@id=\"ctl00__center__left\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[@class=\"catalog-items\"]/DIV[@class=\"item\"]";
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
		return "/h5";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/div[@class=\"info\"]/a";
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
		return "/div[@class=\"info\"]/span/b";
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
	protected Record prepareRecordBeforeSave(Record record) throws EParseException {
		return record;
	}
}