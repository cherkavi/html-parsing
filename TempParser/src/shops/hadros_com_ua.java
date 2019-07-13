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

public class hadros_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://hadros.com.ua";
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
						"http://hadros.com.ua",
						"windows-1251",
						"/HTML/BODY/DIV[1]/TABLE[5]/TBODY/TR/TD[@class=\"pan_nav\"]/TABLE[1]/TBODY/TR[*]/TD[@class=\"line\"]/TABLE[2]/TBODY/TR/TD/TABLE/TBODY/TR[*]/TD[@class=\"podkat\"]/A[@class=\"podkat\"]");
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						String url = addHttpPreambula("http://hadros.com.ua",
								element.getAttribute("href"));
						return new UniversalAnalisator(
								element.getTextContent(), url, getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] {
				"http://hadros.com.ua/kratkie_opisaniya_tovarov.php?id_kat=4&id_tip=4",
				"http://hadros.com.ua/kratkie_opisaniya_tovarov.php?pageNum_kat=1&id_kat=4&id_tip=4",
				"http://hadros.com.ua/kratkie_opisaniya_tovarov.php?pageNum_kat=2&id_kat=4&id_tip=4", };
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
		return "/HTML/BODY/TABLE[4]/TBODY/TR[1]/TD[2]/TABLE[2]/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD[@class=\"podkat\"]/TABLE[@class=\"line\"]/TBODY/TR";
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
		return "/TD[@class=\"line_tx\"]/TABLE/TBODY/TR/TD[@class=\"tx\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TD[@class=\"line_tx\"]/a[@class=\"proizv\"]";
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
		return "/TD[0$]/DIV[0$]/SPAN[@class=\"Chena\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^,]", "");
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

	private String modelName="Модель:";
	private String manufacturerName="Производитель";
	
	@Override
	protected Record prepareRecordBeforeSave(Record record)
			throws EParseException {
		if(record.getName()!=null){
			int modelIndex=record.getName().indexOf(modelName);
			int manufacturerIndex=record.getName().indexOf(manufacturerName);
			if(modelIndex>=0&&manufacturerIndex>0&&modelIndex<manufacturerIndex){
				record.setName(record.getName().substring(modelIndex+modelName.length(), manufacturerIndex).trim());
			}
		}
		return record;
	}
	
	@Override
	protected int getWatchDogEmptyPageLimit() {
		return 2;
	}
}