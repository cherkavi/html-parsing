package shops;

import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.*;

public class osminog_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://osminog.com.ua";
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
		ArrayList<Node> listOfNode = this.parser.getNodeListFromUrl("http://osminog.com.ua","windows-1251",
																	"/HTML/BODY/DIV[@id=\"body\"]/DIV[@id=\"Colums\"]/DIV[@id=\"colLeft\"]/DIV[@class=\"menuLeft\"]/UL/LI[*]/ul/li[*]/A");
		ArrayList<Node> listOfNode2 = this.parser.getNodeListFromUrl("http://osminog.com.ua","windows-1251",
																	"/HTML/BODY/DIV[@id=\"body\"]/DIV[@id=\"Colums\"]/DIV[@id=\"colLeft\"]/DIV[@class=\"menuLeft\"]/UL/LI[*]/A");
		listOfNode.addAll(listOfNode2);
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						if(element.getAttribute("href").trim().equals("#"))return null;
						String url = addHttpPreambula("http://osminog.com.ua",element.getAttribute("href"));
						return new UniversalAnalisator(element.getTextContent(), url, getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] {
				"http://osminog.com.ua/catalog/index.php?SECTION_ID=1383",
				"http://osminog.com.ua/catalog/index.php?SECTION_ID=1383&PAGEN_2=2",
				"http://osminog.com.ua/catalog/index.php?SECTION_ID=1383&PAGEN_2=3", };
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
		return "/HTML/BODY/DIV[@id=\"body\"]/DIV[@id=\"Colums\"]/DIV[@id=\"colCenter\"]/DIV[@id=\"colCenterHeight\"]/DIV[@class=\"content\"]/DIV/DIV[@class=\"listTovar\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[@class=\"item\"]";
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
		return "/DIV[@class=\"descrip\"]/H2/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/DIV[@class=\"descrip\"]/H2/A";
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
		return "/DIV[@class=\"descrip\"]/DIV[@class=\"BlockPrice\"]/SPAN[@class=\"price\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		int index=priceText.indexOf("/");
		if(index>0){
			return priceText.substring(0,index).replaceAll("[^0-9^.]", "").replaceAll(".$", "");
		}
		return priceText.replaceAll("[^0-9^.]", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/DIV[@class=\"descrip\"]/DIV[@class=\"BlockPrice\"]/SPAN[@class=\"price\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		int index=priceText.indexOf("/");
		if(index>0){
			return priceText.substring(index).replaceAll("[^0-9^.]", "");
		}
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