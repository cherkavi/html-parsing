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


public class bosch_siemens_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.bosch-siemens.com.ua";
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
								"http://www.bosch-siemens.com.ua",
								"windows-1251",
								"/HTML/BODY[@id=\"night\"]/DIV[@id=\"wrapper\"]/DIV[@id=\"footer_nav\"]/DIV[@id=\"footnav_wrap\"]/DIV[@class=\"col\"]/UL[*]/LI[*]/A"),
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								"http://www.bosch-siemens.com.ua/"
										+ element.getAttribute("href"));
					}
				});
	}

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;

		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				return null;
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
		return "/HTML/BODY[@id=\"night\"]/DIV[@id=\"inner_wrapper\"]/DIV[@id=\"wrap_bg\"]/DIV[@id=\"layout\"]/DIV[@class=\"clearfix\"]/DIV[@class=\"hot_offer\"]/DIV[@class=\"hot_wrap clearfix\"]/DIV[@id=\"goods\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[@class=\"li\"]";
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
		return "/div[@class=\"wrap\"]/h3/a/b";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/div[@class=\"wrap\"]/h3/a/b";
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
		return "/div[@class=\"wrap\"]/div[@class=\"price clearfix\"]/span/b";
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
	protected Record prepareRecordBeforeSave(Record record) throws EParseException {
		return record;
	}
}