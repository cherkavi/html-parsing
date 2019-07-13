package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.RecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.two_level_finder.*;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class e_market_kiev_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.e-market.kiev.ua";
	}

	@Override
	protected String getCharset() {
		return "windows-1251";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new TwoLevelFinder(
				this.parser
						.getNodeListFromUrl(
								"http://www.e-market.kiev.ua",
								"windows-1251",
								"/HTML/BODY[@class=\"default\"]/DIV[@class=\"wrap\"]/DIV[@class=\"face-pad\"]/DIV[@class=\"face-content\"]/TABLE[@class=\"face-tbl\"]/TBODY/TR/TD[@class=\"left\"]/DIV[@class=\"col-wid\"]/DIV[@class=\"col-pad\"]/DIV[@class=\"m2\"]/DIV[@class=\"list1\"]/DIV[@class=\"lev1\"]/DIV[@class=\"ls1\"]/DIV[@class=\"rs1\"]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY[@class=\"default\"]/DIV[@class=\"wrap\"]/DIV[@class=\"face-pad\"]/DIV[@class=\"face-content\"]/TABLE[@class=\"face-tbl\"]/TBODY/TR/TD[@class=\"main\"]/DIV[@class=\"col-wid\"]/DIV[@class=\"col-pad\"]/DIV[@class=\"catalog\"]/DIV[@class=\"ind\"]/DIV[@class=\"sec\"]/DIV[@class=\"lt\"]/DIV[@class=\"it\"]/H3/A"),
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
		private String left;

		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// http://www.e-market.kiev.ua/subcategory/Pilesosi__LG__1__0.html
				// http://www.e-market.kiev.ua/subcategory/Pilesosi__LG__1__10.html
				// http://www.e-market.kiev.ua/subcategory/Pilesosi__LG__1__20.html
				if (left == null) {
					left = this.getUrl().substring(0,
							this.getUrl().length() - 6);
				}
				return left + ((pageCounter - 1) * 10) + ".html";
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
		return "/HTML/BODY[@class=\"default\"]/DIV[@class=\"wrap\"]/DIV[@class=\"face-pad\"]/DIV[@class=\"face-content\"]/TABLE[@class=\"face-tbl\"]/TBODY/TR/TD[@class=\"main\"]/DIV[@class=\"col-wid\"]/DIV[@class=\"col-pad\"]/DIV[@class=\"catalog\"]/DIV[@class=\"list\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[@class=\"it\"]";
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
		return "/div[@class=\"name\"]/a";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/div[@class=\"name\"]/a";
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
		return "/div[@class=\"price\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		int index = priceText.indexOf('(');
		if (index > 0) {
			return priceText.substring(0, index).replaceAll("[^0-9]", "");
		} else {
			return "";
		}
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