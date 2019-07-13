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

public class tehnobest_com extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.tehnobest.com";
	}

	@Override
	protected String getCharset() {
		return "utf-8";
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
								"http://www.tehnobest.com",
								"utf-8",
								"/HTML/BODY/DIV[@id=\"art-main\"]/DIV[@class=\"art-Sheet\"]/DIV[@class=\"art-Sheet-body\"]/DIV[@class=\"art-contentLayout\"]/DIV[@class=\"art-sidebar1\"]/DIV[@class=\"art-Block\"]/DIV[@class=\"art-Block-body\"]/DIV[@class=\"art-BlockContent\"]/DIV[@class=\"art-BlockContent-body\"]/DIV[@class=\"mainlevel\"]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return addHttpPreambula("http://www.tehnobest.com",
								element.getAttribute("href"));
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/DIV[@id=\"art-main\"]/DIV[@class=\"art-Sheet\"]/DIV[@class=\"art-Sheet-body\"]/DIV[@class=\"art-contentLayout\"]/DIV[@class=\"art-content\"]/DIV[@class=\"art-Post\"]/DIV[@class=\"art-Post-body\"]/DIV[@class=\"art-Post-inner\"]/DIV[@class=\"art-PostContent\"]/SPAN/DIV/DIV[@class=\"categorystile\"]/TABLE/TBODY/TR[*]/TD[*]/A"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return addHttpPreambula("http://www.tehnobest.com",
								element.getAttribute("href"));
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								addHttpPreambula("http://www.tehnobest.com",
										element.getAttribute("href")));
					}
				});
	};

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;
		private String preambula=null;
		private String postambula="-20.html";
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// .html
				// /Page-2-20.html
				// /Page-3-20.html
				if(this.preambula==null){
					int index=this.getUrl().lastIndexOf('.');
					this.preambula=this.getUrl().substring(0, index)+"/Page-";
				}
				return preambula+pageCounter+postambula;
			}
			return this.getUrl();
		}
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_REPEAT_LAST,
			ESectionEnd.NEXT_RECORDS_LOAD_ERROR };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/div[2]/div[@class=\"art-Sheet\"]/div[@class=\"art-Sheet-body\"][14]/div[4]/div[@class=\"art-content\"][2]/div[@class=\"art-Post\"]/div[@class=\"art-Post-body\"]/div[@class=\"art-Post-inner\"]/div[@class=\"art-PostContent\"]/span[@style=\"display: inline-block;\"]/div/div[@id=\"product_list\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/div[*]";
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
		return "/div[@class=\"Tovaru\"]/span/a";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/div[@class=\"Tovaru\"]/span/a";
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
		return "/DIV[@class=\"Tovaru\"]/P[1]/SPAN[@class=\"price_prise\"]/SPAN[@class=\"productPrice\"]";
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