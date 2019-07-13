package shops;

import java.util.ArrayList;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceSectionFactory;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.multi_page.section_finder.const_finder.ConstFinder;
import shop_list.html.parser.engine.multi_page.section_finder.const_finder.Pair;

public class vareos_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://vareos.com.ua";
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
		ArrayList<Pair<String,String>> list=new ArrayList<Pair<String,String>>();
		list.add(new Pair<String,String>("Бытовая техника","http://vareos.com.ua/ru/catalog/byttehn/"));
		return new ConstFinder(list, new IResourceSectionFactory() {
										@Override
										public INextSection getResourceSection(String name, String url) {
											return new UniversalAnalisator(name, url, getAnalisator());
										}
									});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] { "http://vareos.com.ua/ru/catalog/byttehn/",
							  "http://vareos.com.ua/ru/catalog/byttehn/page:2/",
							  "http://vareos.com.ua/ru/catalog/byttehn/page:3/", };
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_REPEAT_LAST,
			ESectionEnd.NEXT_RECORDS_LOAD_ERROR };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/DIV[@class=\"waves\"]/DIV[@class=\"container\"]/DIV[@class=\"wrapper\"]/DIV[@class=\"bottom\"]/DIV[@class=\"top clearfix\"]/DIV[@class=\"content\"]/DIV[@class=\"text\"]/DIV[@class=\"box clearfix\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[@class=\"unit clearfix\"]";
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
		return "/UL/LI[@class=\"title\"]/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/UL/LI[@class=\"title\"]/A";
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
		return "/UL/LI[@class=\"price\"]/SPAN";
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
	protected Record prepareRecordBeforeSave(Record record)
			throws EParseException {
		return record;
	}
}