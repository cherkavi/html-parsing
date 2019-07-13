package shops;

import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.standart.RecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.*;

public class bytovka_net extends RecordListBaseMultiPage {
	// http://bytovka.net/komp.html
	// http://bytovka.net/komp.html?p=2
	// http://bytovka.net/komp.html?p=3
	// http://bytovka.net/catalog/category/view/s/doed-d-d-d-n-d-n-n-d-d-d-n-n-d-n-d-d-d-d/id/182/
	// http://bytovka.net/catalog/category/view/s/doed-d-d-d-n-d-n-n-d-d-d-n-n-d-n-d-d-d-d/id/182/?p=2
	// http://bytovka.net/catalog/category/view/s/doed-d-d-d-n-d-n-n-d-d-d-n-n-d-n-d-d-d-d/id/182/?p=3
	@Override
	public String getShopUrlStartPage() {
		return "http://bytovka.net";
	}

	@Override
	protected String getCharset() {
		return "utf-8";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		// import org.w3c.dom.Node;
		// import
		// shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
		// ArrayList<Node> listOfNode=(new NodeListFinderByUrl(parser,
		// getCharset(),
		// "/html/body/div[4]/div/div[2]/div[3]/table/tbody/tr[*]/td[@width="25%"]/div/div/ul/li[*]/a")).getNodeListByUrl(this.getShopUrlStartPage());
		// removeNodeFromListByTextContent(listOfNode, new
		// String[]{"Банковское оборудование", "Автомобильные товары"});
		// removeNodeWithRepeatAttributes(list, "href");
		ArrayList<Node> listOfNode = this.parser
				.getNodeListFromUrl(
						"http://bytovka.net",
						"utf-8",
						"/HTML/BODY[@class=\" cms-index-index cms-home\"]/DIV[@class=\"wrapper\"]/DIV[@class=\"middle-container\"]/DIV[@class=\"middle col-3-layout\"]/DIV[@class=\"col-left side-col\"]/DIV[@class=\"vertnav-container\"]/DIV/UL[@id=\"vertnav\"]/LI[*]/SPAN[@class=\"vertnav-cat\"]/A");
		return new DirectFinder(listOfNode,
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
				// http://bytovka.net/komp.html
				// http://bytovka.net/komp.html?p=2
				// http://bytovka.net/komp.html?p=3
				// http://bytovka.net/catalog/category/view/s/doed-d-d-d-n-d-n-n-d-d-d-n-n-d-n-d-d-d-d/id/182/
				// http://bytovka.net/catalog/category/view/s/doed-d-d-d-n-d-n-n-d-d-d-n-n-d-n-d-d-d-d/id/182/?p=2
				// http://bytovka.net/catalog/category/view/s/doed-d-d-d-n-d-n-n-d-d-d-n-n-d-n-d-d-d-d/id/182/?p=3
				return this.getUrl().trim()+"?p="+pageCounter;
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
		return "/HTML/BODY/DIV[@class=\"wrapper\"]/DIV[@class=\"middle-container\"]/DIV[@class=\"middle col-3-layout\"]/DIV[@id=\"main\"]/DIV[@class=\"listing-type-grid catalog-listing\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/OL[*]/LI[@class=\"item\"]";
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
		return "/H5/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/H5/A";
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
		return "/DIV[@class=\"price-box\"]/SPAN[*]/SPAN[@class=\"price\"]";
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
	protected Record prepareRecordBeforeSave(Record record)
			throws EParseException {
		return record;
	}
}