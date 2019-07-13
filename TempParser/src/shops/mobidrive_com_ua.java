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

public class mobidrive_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://mobidrive.com.ua";
	}

	@Override
	protected String getCharset() {
		return "windows-1251";
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
		ArrayList<Node> listOfNode = this.parser
				.getNodeListFromUrl(
						"http://mobidrive.com.ua",
						"windows-1251",
						"/html/body[@class=\"uo-container \"]/div[@id=\"page-container\"]/div[@id=\"page-container2\"]/div[@id=\"content-container\"]/div[@id=\"content-container2\"]/div[@id=\"center\"]/div[@id=\"center-main\"]/table[@align=\"center\"]/tbody/tr[*]/td[@align=\"center\"]/table[@align=\"center\"]/tbody/tr[4]/td[@colspan=\"2\"]/table[@border=\"0\"]/tbody/tr[*]/td[@align=\"left\"]/a");
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
				return this.getUrl().trim()+"?page="+pageCounter;
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
		return "/html/body[@class=\"uo-container \"]/div[@id=\"page-container\"]/div[@id=\"page-container2\"]/div[@id=\"content-container\"]/div[@id=\"content-container2\"]/div[@id=\"center\"]/div[@id=\"center-main\"]/div[@class=\"dialog products-dialog dialog-category-products-list list-dialog\"]/div[@class=\"content\"]/div[@class=\"products products-list\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/div[*]/form";
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
		return "/div[@class=\"details\"]/a[1]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/div[@class=\"details\"]/a[1]";
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
		return "/DIV[@class=\"details\"]/TABLE[1]/TBODY/TR[2]/TD/TABLE[@class=\"back_t1\"]/TBODY/TR/TD/TABLE[@class=\"table1\"]/TBODY/TR[2]/TD[4]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/DIV[@class=\"details\"]/TABLE[1]/TBODY/TR[2]/TD/TABLE[@class=\"back_t1\"]/TBODY/TR/TD/TABLE[@class=\"table1\"]/TBODY/TR[2]/TD[3]";
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