package shops;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.RecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.RecursiveFinder;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class rozetka_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://rozetka.com.ua";
	}

	@Override
	protected String getCharset() {
		return "utf-8";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		// // return new TestStubFinder(new
		// UniversalAnalisator("temp","http://",getAnalisator()));
		ArrayList<Node> nodeList=(new NodeListFinderByUrl(parser, "utf-8",
				"/HTML/BODY/DIV[@class=\"body\"]/DIV[@class=\"header\"]/DIV[@class=\"menu-main\"]/DIV[@class=\"body-layout\"]/DIV[@class=\"bg\"]/DIV[@class=\"bg-l\"]/DIV[@class=\"bg-r\"]/TABLE[@id=\"action-zone\"]/TBODY/TR/TD[*]/DIV[*]/TABLE[@class=\"popup popup-green\"]/TBODY/TR[1]/TD[@class=\"container bg\"]/OL[*]/LI[*]/OL[*]/LI[*]/UL[*]/LI[*]/A",
				"/HTML/BODY/DIV[@class=\"body\"]/DIV[@class=\"header\"]/DIV[@class=\"menu-main\"]/DIV[@class=\"body-layout\"]/DIV[@class=\"bg\"]/DIV[@class=\"bg-l\"]/DIV[@class=\"bg-r\"]/TABLE[@id=\"action-zone\"]/TBODY/TR/TD[*]/DIV[*]/TABLE[@class=\"popup popup-green\"]/TBODY/TR[1]/TD[@class=\"container bg\"]/OL[*]/LI[*]/UL[*]/LI[*]/A"				
															).getNodeListByUrl("http://rozetka.com.ua"));
		return new RecursiveFinder(
				nodeList,
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/DIV[@class=\"body\"]/DIV[@class=\"content\"]/DIV[@class=\"body-layout\"]/DIV[@class=\"columns\"]/DIV[@class=\"col-right\"]/DIV[@class=\"portal-page\"]/DIV[3]/DIV[@class=\"container\"]/DIV[1]/TABLE/TBODY/TR/TD[1]/A/SPAN",
						"/HTML/BODY/DIV[@class=\"body\"]/DIV[@class=\"content\"]/DIV[@class=\"body-layout\"]/DIV[@class=\"columns\"]/DIV[@class=\"col-right\"]/DIV[@class=\"portal-page\"]/DIV[3]/DIV[@class=\"container\"]/DIV[@class=\"row submenu-categories\"]/DIV[@class=\"cell item\"]/A",
						"/HTML/BODY/DIV[@class=\"body\"]/DIV[@class=\"content\"]/DIV[@class=\"body-layout\"]/DIV[@class=\"columns\"]/DIV[@class=\"col-right\"]/DIV[@class=\"portal-page\"]/DIV[3]/DIV[@class=\"container\"]/DIV[1]/DIV[@class=\"row submenu-categories\"]/DIV[@class=\"cell item\"]/A",
						"/HTML/BODY/DIV[@class=\"body\"]/DIV[@class=\"content\"]/DIV[@class=\"body-layout\"]/DIV[@class=\"columns\"]/DIV[@class=\"col-right\"]/DIV[@class=\"portal-page\"]/DIV[3]/DIV[@class=\"container\"]/DIV[1]/DIV[@class=\"row tierce\"]/DIV[*]/H3/A"
						),
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
						return new UserSection(element.getTextContent(),element.getAttribute("href"));
					}
				});
	};

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;
		private String preambula;
		private String postambula;
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// http://rozetka.com.ua/notebooks/c80004/filter/preset=netbooks/
				// http://rozetka.com.ua/notebooks/c80004/filter/page=2;preset=netbooks/
				// http://rozetka.com.ua/notebooks/c80004/filter/page=3;preset=netbooks/
				if(preambula==null){
					String findString="/filter/";
					int index=this.getUrl().indexOf(findString);
					if(index>=0){
						this.preambula=this.getUrl().substring(0, index+findString.length());
						this.postambula=this.getUrl().substring(index+findString.length());
					}
				}
				return preambula+"page="+pageCounter+";"+postambula;
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
		return "/HTML/BODY/DIV[@class=\"body\"]/DIV[@class=\"content\"]/DIV[@class=\"body-layout\"]/DIV[@class=\"columns trio\"]/DIV[@class=\"col-middle\"]/DIV[@class=\"container\"]/DIV[@class=\"goods list\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TABLE[*]/TBODY/TR";
	}

	@Override
	protected String recordFromNodeIsPresent() {
		return "/TD[@class=\"detail\"]/table[@class=\"price-label\"]/tbody/tr/td[@class=\"price-status\"]/div[@class=\"status\"]/span";
	}

	@Override
	protected String recordFromNodeIsPresentText() {
		return "Есть";
	}

	@Override
	protected String recordFromNodeInRecordToName() {
		return "/TD[@class=\"detail\"]/div[@class=\"title\"]/a";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TD[@class=\"detail\"]/div[@class=\"title\"]/a";
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
		return "/TD[@class=\"detail\"]/table[@class=\"price-label\"]/tbody/tr/td[@class=\"price-status\"]/div[@class=\"price\"]/div[@class=\"uah\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^,]", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/TD[@class=\"detail\"]/table[@class=\"price-label\"]/tbody/tr/td[@class=\"price-status\"]/div[@class=\"price\"]/div[@class=\"usd\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		return priceText.replaceAll("[^0-9^,]", "");
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
	protected Record recordPostProcessor(Record record) throws EParseException {
		return record;
	}
}