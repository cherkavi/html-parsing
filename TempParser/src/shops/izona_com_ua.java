package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.*;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;


public class izona_com_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.izona.com.ua";
	}

	@Override
	protected String getCharset() {
		return "windows-1251";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		// return new TestStubFinder(new UniversalAnalisator("temp ","http://www.izona.com.ua/?c=39&op_id=,",getAnalisator())); 
		
		return new RecursiveFinder(
				this.parser
						.getNodeListFromUrl(
								"http://www.izona.com.ua",
								"windows-1251",
								"/html/body/table/tbody/tr/td[2]/table/tbody/tr[3]/td/div[@class=\"menu\"]/div[*]/a"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return "http://www.izona.com.ua"+ element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(this.parser, this.getCharset(),
						"/html/body/table/tbody/tr/td[2]/table/tbody/tr[3]/td[3]/a[@class=\"biga\"]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return "http://www.izona.com.ua"+ url;
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(element.getTextContent(),"http://www.izona.com.ua"+ element.getAttribute("href"),getAnalisator());
					}
				});
	};

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] { "http://www.izona.com.ua/?c=265&op_id=,",
				"http://www.izona.com.ua/?c=265&m=&p=&n=1",
				"http://www.izona.com.ua/?c=265&m=&p=&n=2", };
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_LOAD_ERROR,
			ESectionEnd.NEXT_RECORDS_ZERO_SIZE };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table/tbody/tr/td[2]/table/tbody/tr[3]/td[3]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/table[@class=\"art_full\"]/tbody/tr/";
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
		return "/td[@width=\"79%\"]/div/a";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/td[@width=\"79%\"]/div/a";
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
		return "/td[@class=\"price\"]/div[@class=\"ue_price\"]";
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
}