package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section_finder.two_level_finder.*;


public class buytut_com extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://buytut.com";
	}

	@Override
	protected String getCharset() {
		return "utf-8";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new TwoLevelFinder(
				this.parser
						.getNodeListFromUrl("http://buytut.com", "utf-8",
								"/html/body/body[@id=\"bg\"]/div[@class=\"clearfix\"]/div[@class=\"page980\"]/div[@id=\"tools\"]/div[@class=\"page960\"]/div[@align=\"center\"]/ul[@id=\"gkvm_menu\"]/li[*]/a"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/html/body/body/div[2]/div[4]/div[3]/table/tbody/tr/td[2]/div[2]/div/div[@class=\"title_catalog_list_sub\"]/a[*]"),
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(), element
										.getAttribute("href"), getAnalisator());
					}
				});
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] { "http://buytut.com/electronics/notebooks/asus/",
				"http://buytut.com/electronics/notebooks/asus/page-2/",
				"http://buytut.com/electronics/notebooks/asus/page-3/", };
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
		return "/html/body/body/div[2]/div[4]/div[3]/table/tbody/tr/td[2]/div[2]/div/div[2]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/div[@style=\"margin-right: 5px; width: 49%; float: left;height:280px;\"]/div/div[@class=\"rf\"]";
	}

	@Override
	protected String recordFromNodeIsPresent() {
		return "/p/span";
	}

	@Override
	protected String recordFromNodeIsPresentText() {
		return "есть";
	}

	@Override
	protected String recordFromNodeInRecordToName() {
		return "/h2/a";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/h2/a";
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
		return "/span";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		int index=priceText.indexOf("(");
		if(index>0){
			return priceText.substring(0,index).replaceAll("[^0-9^,]", "");
		}else{
			return "";
		}
	}
	
	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/span";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(String priceText) {
		int index=priceText.indexOf("(");
		if(index>0){
			return priceText.substring(index).replaceAll("[^0-9^,]", "");
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
}