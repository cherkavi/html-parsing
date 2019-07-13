package shops;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.two_level_finder.*;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class gadgetstyle_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.gadgetstyle.com.ua";
	}

	@Override
	protected String getCharset() {
		return "windows-1251";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		ArrayList<String> urlList=new ArrayList<String>();
		urlList.add("http://www.gadgetstyle.com.ua/computers");
		urlList.add("http://www.gadgetstyle.com.ua/cameras");
		urlList.add("http://www.gadgetstyle.com.ua/tel-tv-dvd");
		urlList.add("http://www.gadgetstyle.com.ua/audio");
		
		return new TwoLevelFinder(urlList,
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/html/body/form/table[@border=\"0\"]/tbody/tr[2]/table[@bgcolor=\"#FFFFFF\"]/tbody/tr/td[2]/table[@border=\"0\"]/tbody/tr/td/table[@border=\"0\"]/tbody/tr/td/table[@border=\"0\"]/tbody/tr/td[@valign=\"top\"]/table[@border=\"0\"]/tbody/tr/div[*]/a[@class=\"submenu_blue\"]"),
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(),
								"http://www.gadgetstyle.com.ua"
										+ element.getAttribute("href"),
								getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] {
				"http://www.gadgetstyle.com.ua/computers/netbooks",
				"http://www.gadgetstyle.com.ua/computers/netbooks/page/2/",
				"http://www.gadgetstyle.com.ua/computers/netbooks/page/3/", };
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
		return "/html/body/form/table[@border=\"0\"]/tbody/tr[2]/table[@bgcolor=\"#FFFFFF\"]/tbody/tr/td[2]/table[@border=\"0\"]/tbody/tr/td/table[@border=\"0\"]/tbody/tr/td[@valign=\"top\"]/table[@border=\"0\"]/tbody/tr/td/table[2]/tbody/tr[1]/td[1]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/table[@align=\"center\"]/tbody/tr[*]";
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
		return "/TD[2]/FORM/H2/A[@class=\"productsname\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TD[2]/FORM/H2/A[@class=\"productsname\"]";
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
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/TD[2]/FORM/SPAN[@class=\"products_price\"]/input[1]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdGetTextFromElement(
			Node element) {
		try{
			return ((Element)element).getAttribute("value");
		}catch(Exception ex){
			return "";
		}
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