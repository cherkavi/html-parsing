package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.RecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.RecursiveFinder;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class nevo_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://nevo.com.ua";
	}

	@Override
	protected String getCharset() {
		return "utf-8";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		// return new TestStubFinder(new
		// UniversalAnalisator("temp","http://",getAnalisator()));
		// import org.w3c.dom.Node;
		// import
		// shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
		// ArrayList<Node> listOfNode=(new NodeListFinderByUrl(parser,
		// getCharset(),
		// "/html/body/div[4]/div/div[2]/div[3]/table/tbody/tr[*]/td[@width="25%"]/div/div/ul/li[*]/a")).getNodeListByUrl(this.getShopUrlStartPage());
		return new RecursiveFinder(
				this.parser
						.getNodeListFromUrl(
								"http://nevo.com.ua",
								"utf-8",
								"/HTML/BODY/DIV[@class=\"width_table\"]/TABLE[@class=\"main_table\"]/TBODY/TR[1]/TD[@class=\"content_width_td\"]/DIV[*]/TABLE/TBODY/TR/TD[2]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/DIV[@class=\"width_table\"]/TABLE[@class=\"main_table\"]/TBODY/TR[1]/TD[@class=\"content_width_td\"]/DIV[*]/TABLE/TBODY/TR/TD[2]/A[@class=\"dafault_c1\"]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return url;
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								element.getAttribute("href"));
					}
				});
	};

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
				// http://nevo.com.ua/catalog25.html
				// http://nevo.com.ua/catalog/25/2.html
				// http://nevo.com.ua/catalog/25/3.html
				if(preambula==null){
					String findValue="/catalog";
					int index=this.getUrl().indexOf(findValue);
					int lastIndex=this.getUrl().lastIndexOf('.');
					if(index>0){
						
						this.preambula="http://nevo.com.ua/catalog/"+(this.getUrl().substring(index+findValue.length(),lastIndex))+"/";
					}else{
						return null;
					}
				}
				// http://nevo.com.ua/catalog/25/
				return preambula+pageCounter+".html";
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
		return "/HTML/BODY/DIV[@class=\"width_table\"]/TABLE[@class=\"main_table\"]/TBODY/TR[1]/TD[@class=\"content_width_td\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TABLE[@class=\"list\"]/TBODY/TR[*]/TD[@class=\"list_item\"]";
	}

	@Override
	protected String recordFromNodeIsPresent() {
		return "/TABLE[@class=\"list_item_content_table\"]/TBODY/TR[2]/TD[@class=\"list_item_presence\"]/LABEL[@class=\"yes\"]";
	}

	@Override
	protected String recordFromNodeIsPresentText() {
		return "Есть";
	}

	@Override
	protected String recordFromNodeInRecordToName() {
		return "/TABLE[@class=\"list_item_content_table\"]/TBODY/TR[1]/TD[@class=\"list_item_name\"]/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TABLE[@class=\"list_item_content_table\"]/TBODY/TR[1]/TD[@class=\"list_item_name\"]/A";
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
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/DIV[@class=\"bg_mid\"]/DIV[@class=\"bg_bot1\"]/DIV[@class=\"bg_bot2\"]/DIV[@class=\"list_item_price\"]";
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