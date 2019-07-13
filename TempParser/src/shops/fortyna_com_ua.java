package shops;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.RecursiveFinder;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class fortyna_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.fortyna.com.ua";
	}

	@Override
	protected String getCharset() {
		return "windows-1251";
	}
	
	private String[] arrayExclusive=new String[]{"Канцтовары","Офисное оборудование","Как сделать заказ?","Оплата","Доставка","Карта сайта","Новости и рассылка"};
	
	private boolean valueInStringArray(String value, String[] array){
		for(int counter=0;counter<array.length;counter++){
			if(array[counter].equalsIgnoreCase(value))return true;
		}
		return false;
	}
	
	private int getIndexOf(ArrayList<Node> listOfNode, String[] array){
		for(int counter=0;counter<listOfNode.size();counter++){
			String tempValue=listOfNode.get(counter).getTextContent();
			if(valueInStringArray(tempValue, array)){
				return counter;
			}
		}
		return -1;
	}
	
	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		// return new TestStubFinder(new
		// UniversalAnalisator("temp","http://",getAnalisator()));
		// import org.w3c.dom.Node;
		// import
		// shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
		// ArrayList<Node> listOfNode=(new NodeListFinderByUrl(parser,
		// getCharset(),
		// "/html/body/div[4]/div/div[2]/div[3]/table/tbody/tr[*]/td[@width="25%"]/div/div/ul/li[*]/a")).getNodeListByUrl(this.getShopUrlStartPage());
		ArrayList<Node> listOfNode=this.parser
		.getNodeListFromUrl(
				"http://www.fortyna.com.ua",
				"windows-1251",
				"/HTML/BODY/TABLE[@class=\"page\"]/TBODY/TR/TD[2]/TABLE/TBODY/TR[2]/TD[1]/TABLE[8]/TBODY/TR/TD[2]/TABLE/TBODY/TR/TD[@class=\"lb1_body\"]/DIV[@class=\"catalog_left\"]/TABLE/TBODY/TR[*]/TD[@class=\"level2\"]/TABLE/TBODY/TR/TD[2]/A");
		int removeIndex=(-1);
		while((removeIndex=getIndexOf(listOfNode, arrayExclusive))>=0){
			listOfNode.remove(removeIndex);
		}
		return new RecursiveFinder(listOfNode,
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return addHttpPreambula("http://www.fortyna.com.ua",
								element.getAttribute("href"));
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/TABLE[@class=\"page\"]/TBODY/TR/TD[2]/TABLE[2]/TBODY/TR[2]/TD[2]/TABLE[3]/TBODY/TR/TD[2]/TABLE/TBODY/TR/TD[@class=\"lb1_body\"]/DIV[@class=\"sub_catalog_hard\"]/TABLE/TBODY/TR[*]/TD[*]/TABLE/TBODY/TR/TD[2]/SPAN[@class=\"level1\"]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return addHttpPreambula("http://www.fortyna.com.ua",
								element.getAttribute("href"));
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(), addHttpPreambula(
										"http://www.fortyna.com.ua", element
												.getAttribute("href")),
								getAnalisator());
					}
				});
	};

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] { "http://www.fortyna.com.ua/dir.php?id=153",
				"http://www.fortyna.com.ua/dir.php?id=153&from=10",
				"http://www.fortyna.com.ua/dir.php?id=153&from=20", };
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
		return "/html/body[@leftmargin=\"0\"]/table[@align=\"center\"]/tbody/tr/td[2]/table[@border=\"0\"]/tbody/tr[2]/td[@align=\"center\"]/table[@border=\"0\"]/tbody/tr/td[@width=\"100%\"]/table[@border=\"0\"]/tbody/tr/td[@class=\"lbc_body\"]/table[@border=\"0\"]/tbody";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/tr[*]/form/td[@align=\"center\"]/table[@border=\"0\"]/tbody";
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
		return "/tr/td[@colspan=\"2\"]/p[@class=\"font_big_1\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/tr/td[@colspan=\"2\"]/p[@class=\"font_big_1\"]/a";
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
		return "/tr[2]/td/div/div/nobr/p/span";
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