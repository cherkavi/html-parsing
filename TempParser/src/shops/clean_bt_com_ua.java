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


public class clean_bt_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://clean-bt.com.ua";
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
								"http://clean-bt.com.ua",
								"utf-8",
								"/html/body/div/table/tbody/tr[4]/td/table/tbody/tr/td[2]/div[2]/ul/ul[*]/li/div/b/a"),
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								element.getAttribute("href"));
					}
				});
	}

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;
		private String cat=null;
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				if(cat==null){
					int index=this.getUrl().indexOf("cat=");
					if(index>0){
						cat=this.getUrl().substring(index+"cat=".length(), this.getUrl().indexOf("&", index));
					}
				}
				return "http://clean-bt.com.ua/index.php?cat="+cat+"&page="+pageCounter;
			}
			return this.getUrl();
		}
	}

	private ESectionEnd[] conditions = new ESectionEnd[] { ESectionEnd.NEXT_RECORDS_REPEAT_LAST, ESectionEnd.NEXT_RECORDS_SHOW_FIRST };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/div/table/tbody/tr[4]/td/table/tbody/tr/td/div/form";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/div[@class=\"cont\"]";
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
		return "/p/a";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/p/a";
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
		return "/span";
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
		if((record!=null)&&(record.getUrl()!=null)){
			record.setUrl(this.removeAfterSymbolIncludeIt(record.getUrl(),'&'));
		}
		return record;
	}
}