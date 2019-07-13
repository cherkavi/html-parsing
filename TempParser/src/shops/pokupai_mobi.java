package shops;

import org.w3c.dom.Element;
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

public class pokupai_mobi extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.pokupai.mobi";
	}

	@Override
	protected String getCharset() {
		return "windows-1251";
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
		return new RecursiveFinder(
				this.parser
						.getNodeListFromUrl(
								"http://www.pokupai.mobi",
								"windows-1251",
								"/HTML/BODY/DIV[@class=\"main\"]/TABLE[@class=\"content\"]/TBODY/TR/TD[@class=\"left\"]/DIV[@class=\"menu\"]/DIV[@class=\"first_menu\"]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/DIV[@class=\"main\"]/TABLE[@class=\"content\"]/TBODY/TR/TD[@class=\"center\"]/TABLE[@class=\"categories\"]/TBODY/TR/TD[2]/H3/A[@class=\"none_decoration\"]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return url;
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(), element
										.getAttribute("href"), getAnalisator());
					}
				});
	};

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] { "http://www.pokupai.mobi/subcategory/460",
				"http://www.pokupai.mobi/subcategory/460/0/1/10",
				"http://www.pokupai.mobi/subcategory/460/0/1/20", };
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
		return "/HTML/BODY/DIV[@class=\"main\"]/TABLE[@class=\"content\"]/TBODY/TR/TD[@class=\"center\"]/DIV[@class=\"padding\"]/FORM";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TABLE[@class=\"subcat\"]/TBODY";
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
		return "/TR[1]/TD[2]/DIV[@class=\"prod_name\"]/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TR[1]/TD[2]/DIV[@class=\"prod_name\"]/A";
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
		return "/TR[2]/TD[1]/DIV[@class=\"price\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		int index=priceText.indexOf('(');
		if(index>=0){
			return priceText.substring(index).replaceAll("[^0-9]", "");
		}else{
			return "";
		}
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/TR[2]/TD[1]/DIV[@class=\"price\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(String priceText) {
		int index=priceText.indexOf('(');
		if(index>=0){
			return priceText.substring(0, index).replaceAll("[^0-9]", "");
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