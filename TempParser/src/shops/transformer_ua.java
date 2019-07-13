package shops;

import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.*;

public class transformer_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.transformer.ua";
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
		ArrayList<Node> listOfNode = this.parser
				.getNodeListFromUrl(
						"http://www.transformer.ua",
						"utf-8",
						"/html/body/body/div[@class=\"content\"]/div[@id=\"left\"]/div[@class=\"cpt_root_categories\"]/div[@id=\"cat_bar\"]/div[@id=\"cat_cont\"]/ul[*]/li[*]/a");
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(),
								"http://www.transformer.ua"
										+ element.getAttribute("href"),
								getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] {
				"http://www.transformer.ua/category/varochnye_poverhnosti_gazovye/",
				"http://www.transformer.ua/category/varochnye_poverhnosti_gazovye/offset10/",
				"http://www.transformer.ua/category/varochnye_poverhnosti_gazovye/offset20/", };
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {ESectionEnd.NEXT_RECORDS_LOAD_ERROR, ESectionEnd.NEXT_RECORDS_REPEAT_LAST, ESectionEnd.NEXT_RECORDS_SHOW_FIRST};

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/body/div[@class=\"content\"]/div[@class=\"middle\"]/table[@cellpadding=\"0\"]/tbody/tr/td[@valign=\"top\"]/div[@class=\"cpt_maincontent\"]/div[@class=\"cat_products_table\"]/table[@cellpadding=\"3\"]/tbody";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/tr[*]";
	}

	@Override
	protected String recordFromNodeIsPresent() {
		return "/TD[@class=\"main\"]/DIV[@id=\"price\"]/FORM[@id=\"form_cart\"]/A[1]/IMG";
	}

	@Override
	protected String recordFromNodeIsPresentText() {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToName() {
		return "/td[@class=\"main\"]/h2/a";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/td[@class=\"main\"]/h2/a";
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
		return "/TD[@class=\"main\"]/DIV[@id=\"price\"]/SPAN/SMALL";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "").replaceAll(".$", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/TD[@class=\"main\"]/DIV[@id=\"price\"]/SPAN";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		int index=priceText.indexOf('(');
		priceText=priceText.substring(0,index);
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