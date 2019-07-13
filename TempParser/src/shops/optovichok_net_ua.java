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
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;

public class optovichok_net_ua extends AnalisatorRecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://optovichok.net.ua";
	}

	@Override
	protected String getCharset() {
		return "utf-8";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		// // return new TestStubFinder(new
		// UniversalAnalisator("temp","http://",getAnalisator()));
		return new RecursiveFinder(
				this.parser
						.getNodeListFromUrl(
								"http://optovichok.net.ua",
								"utf-8",
								"/HTML/BODY/DIV[@id=\"mainBorder\"]/DIV[@id=\"main\"]/DIV[@id=\"my_d_basket\"]/DIV[@id=\"mainCategories\"]/UL[@class=\"categoriesList\"]/LI[*]/DIV[@class=\"container\"]/DIV[@class=\"categoryMainLevel\"]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return "http://optovichok.net.ua"
								+ element.getAttribute("href");
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/DIV[@id=\"mainBorder\"]/DIV[@id=\"main\"]/DIV[@id=\"my_d_basket\"]/DIV[@class=\"catalogBorder\"]/DIV[2]/TABLE/TBODY/TR[*]/TD[*]/A[2]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return "http://optovichok.net.ua" + url;
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UniversalAnalisator(
								element.getTextContent(),
								"http://optovichok.net.ua"
										+ element.getAttribute("href"),
								getAnalisator());
					}
				});
	};

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[] {
				"http://optovichok.net.ua/catalog/krupnaya-bitovaya-tehnika/holodilniki/",
				"http://optovichok.net.ua/catalog/krupnaya-bitovaya-tehnika/holodilniki/page2/?sort=price&asc_desc=asc&exist=",
				"http://optovichok.net.ua/catalog/krupnaya-bitovaya-tehnika/holodilniki/page3/?sort=price&asc_desc=asc&exist=", };
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
		return "/HTML/BODY/DIV[@id=\"mainBorder\"]/DIV[@id=\"main\"]/DIV[@id=\"my_d_basket\"]/DIV[@class=\"catalogBorder\"]/DIV[@id=\"sort_content\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[@class=\"catalogPosBlock\"]/DIV[@class=\"catSpecPos\"]/div[@class=\"catalogImgBlock\"]";
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
		return "/div[@class=\"catNameCaption\"]/a";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/div[@class=\"catNameCaption\"]/a";
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
		return "/div[@class=\"catalogPrice\"]/span[@class=\"priceUAH\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^,]", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/div[@class=\"catalogPrice\"]/span[@class=\"priceUSD\"]";
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
	protected Record prepareRecordBeforeSave(Record record) throws EParseException {
		return record;
	}
}