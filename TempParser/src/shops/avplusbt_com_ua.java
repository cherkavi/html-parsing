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

public class avplusbt_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://avplusbt.com.ua";
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
		// removeNodeWithRepeatAttributes(list, "href");
		ArrayList<Node> listOfNode = this.parser
				.getNodeListFromUrl(
						"http://avplusbt.com.ua",
						"windows-1251",
						"/HTML/BODY/TABLE[3]/TBODY/TR/TD[1]/TABLE/TBODY/TR[2]/TD/TABLE/TBODY/TR[2]/TD[@class=\"m_c\"]/DIV[@class=\"mb\"]/TABLE[@class=\"infoBoxContents\"]/TBODY/TR[2]/TD[@class=\"boxText\"]/DIV[@class=\"suckerdiv\"]/UL[@id=\"menu\"]/LI[@class=\"suckerdiv_dp\"]/UL[*]/LI[@class=\"suckerdiv2\"]/A");
		ArrayList<Node> listOfNode2 = this.parser
		.getNodeListFromUrl(
				"http://avplusbt.com.ua",
				"windows-1251",
				"/HTML/BODY/TABLE[3]/TBODY/TR/TD[1]/TABLE/TBODY/TR[2]/TD/TABLE/TBODY/TR[2]/TD[@class=\"m_c\"]/DIV[@class=\"mb\"]/TABLE[@class=\"infoBoxContents\"]/TBODY/TR[2]/TD[@class=\"boxText\"]/DIV[@class=\"suckerdiv\"]/UL[@id=\"menu\"]/LI[@class=\"suckerdiv_dp\"]/UL[*]/LI[@class=\"suckerdiv_dp\"]/UL[*]/LI[@class=\"suckerdiv2\"]/A");
		listOfNode.addAll(listOfNode2);
		// ‰ÓÔÓÎÌËÚÂÎ¸Ì‡ˇ ÒÂÍˆËˇ
		removeNodeFromListByTextContent(listOfNode, new
		String[]{
			"—“Œ… » œŒƒ ¿—",
			"Õ¿œŒÀ‹Õ€…  ”À≈– ƒÀﬂ ¬Œƒ€",
			"Õ¿—“ŒÀ‹Õ€…  ”À≈– ƒÀﬂ ¬Œƒ€"
		});
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						String url=element.getAttribute("href");
						url=removeAfterSymbolLastIncludeIt(url, '?');
						return new UserSection(element.getTextContent(),url);
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
				// ÒÂÍˆËË Û‰‡ÎËÚ¸ ÔÓÒÎÂ ÁÌ‡Í‡ ?
				// http://avplusbt.com.ua/nastennye kotly
				// zakrytaJA-kamera-sgoraniJA-c-122_125_126_336.html?osCsid=tj7uvk5dv809vc5j9evif7hrs7
				// http://avplusbt.com.ua/hifi-tehnika%20cd-proigryvateli-c-369_162.html?page=2&sort=products_sort_order&op=list
				// http://avplusbt.com.ua/hifi-tehnika%20cd-proigryvateli-c-369_162.html?page=3&sort=products_sort_order&op=list
				return removeAfterSymbolIncludeIt(this.getUrl(), '?')+"?page="+pageCounter+"&sort=products_sort_order&op=list";
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
		return "/HTML/BODY/TABLE[3]/TBODY/TR/TD[3]/P[2]/TABLE/TBODY/TR[6]/TD/TABLE[1]/TBODY/TR/TD/TABLE/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD[@class=\"smallText\"]/TABLE/TBODY/TR/TD[@class=\"smallText\"]";
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
		return "/B/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/B/A";
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
		return "/DIV[@class=\"price\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "").replaceAll(".$", "");
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
		if(record.getUrl()!=null){
			record.setUrl(removeAfterSymbolIncludeIt(record.getUrl(), '?'));
		}
		return record;
	}
}