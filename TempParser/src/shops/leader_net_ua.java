package shops;

import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.standart.RecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.*;

public class leader_net_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.leader.net.ua";
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
		ArrayList<Node> listOfNode = this.parser
				.getNodeListFromUrl(
						"http://www.leader.net.ua",
						"windows-1251",
						"/html/body/table[@border=\"0\"]/tbody/tr/td[@valign=\"top\"]/table[@border=\"0\"]/tbody/tr/td[@class=\"left_body\"]/table[@border=\"0\"]/tbody/tr[3]/td[@colspan=\"3\"]/table[@border=\"0\"]/tbody/tr[*]/td[@colspan=\"3\"]/table[@border=\"0\"]/tbody/tr[*]/td[@class=\"menu_lev2\"]/a[@class=\"menu_lev2\"]");
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								"http://www.leader.net.ua"
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
				return this.getUrl().trim()+"?p0="+pageCounter;
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
		return "/HTML/BODY/TABLE[@class=\"main\"]/TBODY/TR/TD/TABLE/TBODY/TR/TD[@class=\"center_body\"]/TABLE/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD[@align=\"center\"]/TABLE/TBODY";
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
		return "/TR[1]/TD/A[@class=\"title_name_phone\"]/H2";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TR[1]/TD/A[@class=\"title_name_phone\"]";
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
		return "/TR[3]/TD[2]/SPAN[@class=\"cena_cifra\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		// √–Ќ
		// 63.18 у.е. (502.28 грн.)
		int index=priceText.indexOf('(');
		if(index>0){
			return priceText.substring(index).replaceAll("[^0-9^.]", "").replaceAll(".$", "");
		}else{
			return "";
		}
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/TR[3]/TD[2]/SPAN[@class=\"cena_cifra\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		// USD
		// 63.18 у.е. (502.28 грн.)
		int index=priceText.indexOf('(');
		if(index>0){
			return priceText.substring(0, index).replaceAll("[^0-9^.]", "").replaceAll(".$", "").replaceAll(".$", "");
		}else{
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
	protected Record prepareRecordBeforeSave(Record record)
			throws EParseException {
		return record;
	}
}