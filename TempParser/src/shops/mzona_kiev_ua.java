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
import shop_list.html.parser.engine.multi_page.section_finder.two_level_finder.*;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class mzona_kiev_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://mzona.kiev.ua";
	}

	@Override
	protected String getCharset() {
		return "windows-1251";
	}

	private String[] controlValues=new String[]{"Бытовая техника", "Мобильные телефоны", "MP3 плееры", "Видеотехника", "Фототехника", "Аксессуары"};
	private boolean elementIntoControlValues(String value){
		String tempValue=value.trim();
		boolean returnValue=false;
		for(int counter=0;counter<controlValues.length;counter++){
			if(controlValues[counter].trim().equalsIgnoreCase(tempValue)){
				returnValue=true;
				break;
			}
		}
		return returnValue;
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new TwoLevelFinder(
				this.parser
						.getNodeListFromUrl(
								"http://mzona.kiev.ua",
								"windows-1251",
								"/html/body/table[3]/tbody/tr/td[@width=\"200\"]/table/tbody/tr[2]/td[@width=\"200\"]/table[@width=\"200\"]/tbody/tr[*]/td[@class=\"menu\"]/a"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						if(elementIntoControlValues(element.getTextContent())){
							return addHttpPreambula("http://mzona.kiev.ua/",element.getAttribute("href"));
						}else{
							return null;
						}
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/TABLE[3]/TBODY/TR/TD[3]/TABLE/TBODY/TR[2]/TD/TABLE/TBODY/TR/TD[3]/P/TABLE/TBODY/TR/TD[*]/TABLE/TBODY/TR[*]/TD[@class=\"shop_nt_td\"]/DIV[@class=\"shop_nt_razdel_name\"]/A"),
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								addHttpPreambula("http://mzona.kiev.ua/",
										element.getAttribute("href")));
					}
				});
	}

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;

		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				String tempUrl=this.getUrl().trim();
				if(tempUrl.endsWith("/")){
					return tempUrl+"index"+(pageCounter-1)+".html";
				}else{
					return tempUrl+"/index"+(pageCounter-1)+".html";
				}
			}
			return this.getUrl();
		}
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
		return "/HTML/BODY/TABLE[3]/TBODY/TR/TD[3]/TABLE/TBODY/TR[2]/TD/TABLE/TBODY/TR/TD[3]/TABLE[0$]/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[@valign=\"top\"]";
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
		return "/TD[2]/DIV[@class=\"shop_list_name\"]/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TD[2]/DIV[@class=\"shop_list_name\"]/A";
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
		return "/TD[2]/SPAN[@class=\"shop_list_price_conv\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/TD[2]/SPAN[@class=\"shop_list_price\"]";
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