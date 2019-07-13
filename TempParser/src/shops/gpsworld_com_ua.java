package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.RecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.*;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;


public class gpsworld_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://gpsworld.com.ua";
	}

	@Override
	protected String getCharset() {
		return "utf-8";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		// // return new TestStubFinder(new
		// UniversalAnalisator("temp","http://",getAnalisator()));
		return new RecursiveFinder(
				this.parser
						.getNodeListFromUrl(
								"http://gpsworld.com.ua",
								"utf-8",
								"/HTML/BODY[@id=\"bd\"]/DIV[@id=\"ja-wrapper\"]/DIV[@id=\"ja-wrapper-inner\"]/DIV[@id=\"ja-containerwrap\"]/DIV[@id=\"ja-container\"]/DIV[@id=\"ja-col\"]/DIV[@class=\"moduletable_menuhilite\"]/A[@class=\"mainlevel\"]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return "http://gpsworld.com.ua"
								+ element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY[@id=\"bd\"]/DIV[@id=\"ja-wrapper\"]/DIV[@id=\"ja-wrapper-inner\"]/DIV[@id=\"ja-containerwrap\"]/DIV[@id=\"ja-container\"]/DIV[@id=\"ja-mainbody\"]/DIV[@id=\"ja-content\"]/DIV[@id=\"vmMainPage\"]/DIV[2]/TABLE/TBODY/TR[*]/TD[*]/A[@class=\"category\"]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return "http://gpsworld.com.ua" + url;
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								"http://gpsworld.com.ua"
										+ element.getAttribute("href"));
					}
				});
	};

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;
		private String categoryId=null;
		
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// http://gpsworld.com.ua/shop.html?page=shop.browse&category_id=19
				// http://www.gpsworld.com.ua/shop.html?category_id=19&page=shop.browse&limit=20&start=20
				// http://www.gpsworld.com.ua/shop.html?category_id=19&page=shop.browse&limit=20&start=40
				if(categoryId==null){
					int index=this.getUrl().lastIndexOf('=');
					categoryId=this.getUrl().substring(index+1);
				}
				return "http://www.gpsworld.com.ua/shop.html?category_id="+categoryId+"&page=shop.browse&limit=20&start="+(pageCounter-1)*20;
			}
			return this.getUrl();
		}
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_LOAD_ERROR,
			ESectionEnd.NEXT_RECORDS_ZERO_SIZE,
			ESectionEnd.NEXT_RECORDS_REPEAT_LAST,
			ESectionEnd.NEXT_RECORDS_SHOW_FIRST
			};

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY[@id=\"bd\"]/DIV[@id=\"ja-wrapper\"]/DIV[@id=\"ja-wrapper-inner\"]/DIV[@id=\"ja-containerwrap\"]/DIV[@id=\"ja-container\"]/DIV[@id=\"ja-mainbody\"]/DIV[@id=\"ja-content\"]/DIV[@id=\"vmMainPage\"]/DIV[@id=\"product_list\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[*]/DIV[@class=\"browseProductContainer\"]";
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
		return "/h3/a";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/h3/a";
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
		return "/div[@class=\"browsePriceContainer\"]/span";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		int index=priceText.indexOf("(");
		if(index<0)return "";
		return priceText.substring(0,index).replaceAll("[^0-9^.]", "");
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