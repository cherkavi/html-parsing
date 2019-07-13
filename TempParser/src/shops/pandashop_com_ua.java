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
import shop_list.html.parser.engine.multi_page.section_finder.two_level_finder.*;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class pandashop_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://pandashop.com.ua";
	}

	@Override
	protected String getCharset() {
		return "utf-8";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new TwoLevelFinder(
				this.parser
						.getNodeListFromUrl(
								"http://pandashop.com.ua",
								"utf-8",
								"/HTML/BODY/DIV[@class=\"level_0\"]/FORM[@id=\"Form1\"]/TABLE/TBODY/TR[2]/TD[1]/DIV[@class=\"menu_left\"]/UL/LI[*]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return addHttpPreambula("http://pandashop.com.ua/",element.getAttribute("href"));
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/DIV[@class=\"level_0\"]/FORM[@id=\"defaultForm1\"]/TABLE/TBODY/TR[2]/TD[2]/DIV[@id=\"pnlMainContent\"]/DIV[@class=\"wrap_flourish wrap_subcategory\"]/UL[@class=\"ul_subcategory\"]/LI[*]/A"),
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								addHttpPreambula("http://pandashop.com.ua/",element.getAttribute("href")));
					}
				});
	}

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;
		// http://pandashop.com.ua/ru/catalog/foto/cameras/page_
		private String preambula=null;
		private String findValue="/default.aspx";
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				if(preambula==null){
					// http://pandashop.com.ua/ru/catalog/foto/cameras/default.aspx
					// http://pandashop.com.ua/ru/catalog/foto/cameras/page_2/default.aspx
					// http://pandashop.com.ua/ru/catalog/foto/cameras/page_3/default.aspx
					int index=this.getUrl().indexOf(findValue);
					if(index>0){
						preambula=this.getUrl().substring(0, index)+"/page_";
					}else{
						return null;
					}
				}
				
				return preambula+pageCounter+findValue;
			}
			return this.getUrl();
		}
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_REPEAT_LAST,
			ESectionEnd.NEXT_RECORDS_SHOW_FIRST };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/DIV[@class=\"level_0\"]/FORM[@id=\"defaultForm1\"]/TABLE/TBODY/TR[2]/TD[2]/DIV[@id=\"pnlMainContent\"]/DIV";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[@class=\"wrap_productTableView\"]/DIV[@class=\"catalog_item productTableView\"]";
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
		return "/DIV[@class=\"details\"]/DIV[@class=\"catalog_name\"]/A/SPAN[0$]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/DIV[@class=\"details\"]/DIV[@class=\"catalog_name\"]/A";
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
		return "/DIV[@class=\"brief\"]/DIV[@class=\"price_light\"]/STRONG";
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