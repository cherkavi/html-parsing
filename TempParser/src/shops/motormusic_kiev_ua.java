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

public class motormusic_kiev_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://motormusic.kiev.ua";
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
								"http://motormusic.kiev.ua",
								"utf-8",
								"/HTML/BODY[@class=\"bg_white\"]/CENTER/TABLE/TBODY/TR[4]/TD/TABLE/TBODY/TR/TD[1]/DIV[*]/DIV/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return "http://motormusic.kiev.ua/"
								+ element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY[@class=\"bg_white\"]/CENTER/TABLE/TBODY/TR[4]/TD/TABLE/TBODY/TR/TD[3]/DIV[@id=\"blockcenter\"]/DIV[@class=\"ser_left w100p\"]/DIV[@class=\"ser_right align_center w100p pad_top_4\"]/TABLE/TBODY/TR[*]/TD[*]/a"),
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								"http://motormusic.kiev.ua/"
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
				return this.getUrl()+"page;"+(pageCounter-1);
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
		return "/HTML/BODY[@class=\"bg_white\"]/CENTER/TABLE/TBODY/TR[4]/TD/TABLE/TBODY/TR/TD[3]/DIV[@id=\"blockcenter\"]/DIV[@class=\"ser_left w100p\"]/DIV[@class=\"ser_right align_center w100p pad_top_4\"]/table[2]/tbody";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/tr[*]/td[@align=\"left\"]/div[@class=\"ser_left w100p\"]/div/div/table/tbody/tr";
	}

	@Override
	protected String recordFromNodeIsPresent() {
		return "/td[@class=\"orng\"]/span";
	}

	@Override
	protected String recordFromNodeIsPresentText() {
		return "есть";
	}

	@Override
	protected String recordFromNodeInRecordToName() {
		return "/td[@width=\"290\"]/a";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/td[@width=\"290\"]/a";
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
		return "/td[@class=\"orng\"]/div";
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