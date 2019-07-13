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

public class xshop_ua extends RecordListBaseMultiPage {
	// http://www.xshop.ua/26596/8410/
	// http://www.xshop.ua/26596/8410/page=2/
	@Override
	public String getShopUrlStartPage() {
		return "http://www.xshop.ua";
	}

	@Override
	protected String getCharset() {
		return "utf-8";
	}

	// removeNodeFromListByTextContent(listOfNode, new
	// String[]{"Банковское оборудование", "Автомобильные товары"});
	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new TwoLevelFinder(
				this.parser
						.getNodeListFromUrl(
								"http://www.xshop.ua",
								"utf-8",
								"/HTML/BODY/DIV[@class=\"body\"]/DIV[@class=\"header\"]/DIV[@class=\"columns\"]/DIV[@class=\"column-right\"]/DIV[@class=\"catalog\"]/UL[@id=\"menu-1\"]/LI[*]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/html/body/div[@class=\"body\"]/div[@class=\"header\"]/div[@class=\"columns\"]/div[@class=\"column-right\"]/div[@class=\"catalog\"]/ul[@class=\"menu-2\"]/li[*]/a"),
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

		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// http://www.xshop.ua/26596/8410/
				// http://www.xshop.ua/26596/8410/page=2/
				String returnValue = this.getUrl().trim();
				if (!returnValue.endsWith("/")) {
					returnValue = returnValue + "/";
				}
				return returnValue + "page=" + pageCounter + "/";
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
		return "/HTML/BODY/DIV[@class=\"body\"]/DIV[@class=\"content\"]/DIV[@class=\"goods\"]/DIV[@class=\"container\"]/TABLE[@class=\"one-fouth\"]/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[@class=\"details\"]/TD[*]/DIV[@class=\"container\"]";
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
		return "/DIV[@class=\"title\"]/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/DIV[@class=\"title\"]/A";
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
		return "/DIV[@class=\"row\"]/DIV[@class=\"cell price\"]/SPAN[@class=\"uah\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9]", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/DIV[@class=\"row\"]/DIV[@class=\"cell price\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		String returnValue=priceText;
		int index=returnValue.indexOf('$');
		if(index>0){
			returnValue=returnValue.substring(0, index);
		}
		return returnValue.replaceAll("[^0-9]", "");
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