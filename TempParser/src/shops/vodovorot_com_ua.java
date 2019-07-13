package shops;

import java.util.ArrayList;

import org.w3c.dom.Element;
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

public class vodovorot_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://vodovorot.com.ua";
	}

	@Override
	protected String getCharset() {
		return "windows-1251";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		ArrayList<String> listUrl=new ArrayList<String>();
		listUrl.add("http://vodovorot.com.ua/showCats.php?cid=1");
		listUrl.add("http://vodovorot.com.ua/showCats.php?cid=2");
		listUrl.add("http://vodovorot.com.ua/showCats.php?cid=76");
		listUrl.add("http://vodovorot.com.ua/showCats.php?cid=129");
		
		return new TwoLevelFinder(listUrl,
				new NodeListFinderByUrl(this.parser, this.getCharset(),
						"/HTML/BODY/UL/LI[*]/A"),
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(),
								"http://vodovorot.com.ua"
										+ element.getAttribute("href"),
								getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] { "http://vodovorot.com.ua/cat/conditioners/",
				"http://vodovorot.com.ua/cat/conditioners/&page=2",
				"http://vodovorot.com.ua/cat/conditioners/&page=3", };
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_REPEAT_LAST,
			ESectionEnd.NEXT_RECORDS_ZERO_SIZE };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/DIV[@id=\"main\"]/DIV[@class=\"holder\"]/DIV[@class=\"content-holder\"]/DIV[@id=\"content\"]/DIV[@class=\"products\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[@class=\"product\"]";
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
		return "/H3/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/H3/A";
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
		return "/DIV/DIV[@class=\"about-holder1\"]/DIV[@class=\"about-holder2\"]/TABLE[@class=\"prise\"]/TBODY/TR/TD[@class=\"td2\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		int indexDelimeter=priceText.indexOf('/');
		if(indexDelimeter>0){
			return priceText.substring(0, indexDelimeter).replaceAll("[^0-9]", "");
		}else{
			return "0";
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