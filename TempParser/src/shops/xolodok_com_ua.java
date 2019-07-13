package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.RecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.RecursiveFinder;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class xolodok_com_ua extends RecordListBaseMultiPage {
	// http://xolodok.com.ua/Pliti_Kombinirovannie_GORENJE-c-4_79_115.html
	// http://xolodok.com.ua/index.php?cPath=4_79_115&page=2
	// http://xolodok.com.ua/index.php?cPath=4_79_115&page=3
	@Override
	public String getShopUrlStartPage() {
		return "http://xolodok.com.ua";
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
		// removeNodeFromListByTextContent(listOfNode, new
		// String[]{"Банковское оборудование", "Автомобильные товары"});
		return new RecursiveFinder(
				this.parser
						.getNodeListFromUrl(
								"http://xolodok.com.ua",
								"windows-1251",
								"/HTML/BODY/TABLE[@class=\"main_t\"]/TBODY/TR[3]/TD/TABLE/TBODY/TR/TD[2]/TABLE/TBODY/TR[1]/TD/TABLE[@class=\"main_table\"]/TBODY/TR/TD[@class=\"box_width_td_left\"]/TABLE[@class=\"box_width_left\"]/TBODY/TR/TD[@class=\"left_col\"]/TABLE/TBODY/TR[1]/TD[@class=\"categories\"]/TABLE[@class=\"infoBoxContents1_table\"]/TBODY/TR/TD[@class=\"boxText\"]/TABLE[@class=\"infoBox1\"]/TBODY/TR/TD[@class=\"boxText\"]/TABLE[1]/TBODY/TR/TD[@class=\"infoBox1_td catalog\"]/TABLE[*]/TBODY/TR/TD[@class=\"main top-level-cat\"]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/TABLE[@class=\"main_t\"]/TBODY/TR[3]/TD/TABLE/TBODY/TR/TD[2]/TABLE/TBODY/TR[1]/TD/TABLE[@class=\"main_table\"]/TBODY/TR/TD[@class=\"content_width_td\"]/TABLE[@class=\"infoBoxHeading1_table\"]/TBODY/TR/TD[@class=\"content\"]/TABLE[@class=\"simple_table_box\"]/TBODY/TR[2]/TD/TABLE[@class=\"infoBoxContents1_table\"]/TBODY/TR/TD[@class=\"boxText\"]/TABLE[@class=\"infoBox1\"]/TBODY/TR/TD[@class=\"boxText\"]/TABLE[1]/TBODY/TR/TD[@class=\"infoBox1_td catalog\"]/TABLE/TBODY/TR[4]/TD/TABLE/TBODY/TR[1]/TD/TABLE/TBODY/TR[*]/TD[@class=\"smallText\"]/A"),
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
						return new UserSection(element.getTextContent(),
								element.getAttribute("href"));
					}
				});
	};

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;
		private String preambula=null;
		
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// http://xolodok.com.ua/Pliti_Kombinirovannie_GORENJE-c-4_79_115.html
				// http://xolodok.com.ua/index.php?cPath=4_79_115&page=2
				// http://xolodok.com.ua/index.php?cPath=4_79_115&page=3
				if(preambula==null){
					int indexBegin=this.getUrl().lastIndexOf('-');
					int indexEnd=this.getUrl().lastIndexOf('.');
					if(indexBegin>0 && indexEnd>0)
					preambula="http://xolodok.com.ua/index.php?cPath="+this.getUrl().substring(indexBegin+1, indexEnd)+"&page=";
				}
				return preambula+pageCounter;
			}
			return this.getUrl();
		}
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
		return "/HTML/BODY/TABLE[@class=\"main_t\"]/TBODY/TR[3]/TD/TABLE/TBODY/TR/TD[2]/TABLE/TBODY/TR[1]/TD/TABLE[@class=\"main_table\"]/TBODY/TR/TD[@class=\"content_width_td\"]/TABLE[@class=\"infoBoxHeading1_table\"]/TBODY/TR/TD[@class=\"content\"]/TABLE[@class=\"simple_table_box\"]/TBODY/TR/TD/TABLE/TBODY/TR[8]/TD/TABLE/TBODY/TR/TD/TABLE[2]/TBODY/TR/TD[@class=\"infoBox1_td\"]/TABLE/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD[@class=\"main product-list\"]/TABLE[@class=\"product-list\"]/TBODY/TR/TD[2]/TABLE/TBODY/TR[1]";
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
		return "/TD[1]/TABLE/TBODY/TR[1]/TD[@class=\"product-list-head\"]/A[@class=\"main bold\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TD[1]/TABLE/TBODY/TR[1]/TD[@class=\"product-list-head\"]/A[@class=\"main bold\"]";
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
		return "/TD[2]/TABLE/TBODY/TR/TD[@class=\"main\"]/SPAN[@class=\"productPrice bold\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "");
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