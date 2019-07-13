package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.RecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.RecursiveFinder;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class tehnokomfort_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://tehnokomfort.com.ua";
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
								"http://tehnokomfort.com.ua",
								"utf-8",
								"/HTML/BODY/DIV[@class=\"wrapper\"]/DIV[@class=\"wrapper1\"]/DIV[@class=\"header\"]/DIV[@class=\"main\"]/TABLE[@class=\"main_table\"]/TBODY/TR/TD[@class=\"main_left\"]/DIV[@class=\"catalog\"]/DIV[@class=\"c1\"]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/DIV[@class=\"wrapper\"]/DIV[@class=\"wrapper1\"]/DIV[@class=\"header\"]/DIV[@class=\"main\"]/TABLE[@class=\"main_table\"]/TBODY/TR/TD[@class=\"main_center\"]/DIV[@class=\"main_border\"]/DIV[@class=\"main_content\"]/DIV[@class=\"panel_tovar_block\"]/DIV[@class=\"panel_tovar_block1\"]/DIV[@class=\"panel_tovar_block2\"]/DIV[@class=\"panel_tovar_block3\"]/DIV[@class=\"panel_tovar_block4\"]/DIV[@class=\"panel_tovar_block5\"]/DIV[@class=\"panel_tovar_block6\"]/DIV[@class=\"panel_tovar_block7\"]/DIV[*]/TABLE/TBODY/TR/TD[2]/DIV[@class=\"listitem_c1\"]/A"),
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
		private String postambula=null;
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// http://tehnokomfort.com.ua/catalog90.html
				// http://tehnokomfort.com.ua/catalog/90/2.html
				// http://tehnokomfort.com.ua/catalog/90/3.html
				if(preambula==null){
					String searchString="/catalog";
					int index=this.getUrl().indexOf(searchString);
					int indexEnd=this.getUrl().indexOf('.',index+1);
					preambula="http://tehnokomfort.com.ua/catalog/"+this.getUrl().substring(index+searchString.length(),indexEnd)+"/";
					postambula=".html";
				}
				return preambula+pageCounter+postambula;
			}
			return this.getUrl();
		}
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_REPEAT_LAST,
			ESectionEnd.NEXT_RECORDS_ZERO_SIZE };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/DIV[@class=\"wrapper\"]/DIV[@class=\"wrapper1\"]/DIV[@class=\"header\"]/DIV[@class=\"main\"]/TABLE[@class=\"main_table\"]/TBODY/TR/TD[@class=\"main_center\"]/DIV[@class=\"main_border\"]/DIV[@class=\"main_content\"]/DIV[@class=\"panel_tovar_block\"]/DIV[@class=\"panel_tovar_block1\"]/DIV[@class=\"panel_tovar_block2\"]/DIV[@class=\"panel_tovar_block3\"]/DIV[@class=\"panel_tovar_block4\"]/DIV[@class=\"panel_tovar_block5\"]/DIV[@class=\"panel_tovar_block6\"]/DIV[@class=\"panel_tovar_block7\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TABLE[@class=\"tovar_block_catalog\"]/TBODY";
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
		return "/TR/TD[2]/DIV[@class=\"catalog_name\"]/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TR/TD[2]/DIV[@class=\"catalog_name\"]/A";
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
		return "/TR/TD[2]/DIV[@class=\"catalog_price_box\"]/LABEL[@class=\"header_block_regular_price\"]";
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