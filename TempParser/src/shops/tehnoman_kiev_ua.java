package shops;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

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

public class tehnoman_kiev_ua extends RecordListBaseMultiPage {
	// "—≈–¬»— ÷≈Õ“–€",
	// "ÿ¬≈…Õ€≈  Ã¿ÿ»Õ€"
	@Override
	public String getShopUrlStartPage() {
		return "http://www.tehnoman.kiev.ua";
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
		// removeNodeWithRepeatAttributes(list, "href");
		
		ArrayList<Node> listOfNode=this.parser
		.getNodeListFromUrl("http://www.tehnoman.kiev.ua",
				"windows-1251",
				"/HTML/BODY/TABLE[2]/TBODY/TR[1]/TD[1]/DIV[@class=\"list_catalog\"]/A");
		removeNodeFromListByTextContent(listOfNode, new
				String[]{
					"¡‡ÌÍÓ‚ÒÍÓÂ Ó·ÓÛ‰Ó‚‡ÌËÂ", "¿‚ÚÓÏÓ·ËÎ¸Ì˚Â ÚÓ‚‡˚"
					});
		int counter=listOfNode.size()-1;
		
		while(counter>0){
			if(listOfNode.get(counter) instanceof Element){
				Element element=(Element)listOfNode.get(counter);
				String href=element.getAttribute("href");
				if(href==null){
					listOfNode.remove(counter);
					counter--;
					continue;
				}
				if(href.indexOf("/nick")<0){
					listOfNode.remove(counter);
					counter--;
					continue;
				}
				counter--;
			}
		}
		
		return new RecursiveFinder(
								listOfNode,
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return addHttpPreambula("http://www.tehnoman.kiev.ua",
								element.getAttribute("href"));
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/HTML/BODY/TABLE[2]/TBODY/TR[1]/TD[3]/TABLE/TBODY/TR/TD/P[1]/TABLE/TBODY/TR[2]/TD/A[*]"),
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url = element.getAttribute("href");
						if (isStringEmpty(url))
							return null;
						return addHttpPreambula("http://www.tehnoman.kiev.ua",
								element.getAttribute("href"));
					}
				}, new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								addHttpPreambula("http://www.tehnoman.kiev.ua",
										element.getAttribute("href")));
					}
				});
	};

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;

		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// http://tehnoman.kiev.ua/eshop/nick3/nick7/nick501/
				// http://tehnoman.kiev.ua/eshop/nick3/nick7/nick501/index1.html
				// http://tehnoman.kiev.ua/eshop/nick3/nick7/nick501/index2.html
				return this.getUrl().trim()+"index"+(pageCounter-1)+".html";
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
		return "/HTML/BODY/TABLE[2]/TBODY/TR[1]/TD[3]/TABLE/TBODY/TR/TD/TABLE/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]";
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
		return "/TD/H5";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TD/H5";
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
		return "/TD/DIV[@class=\"price\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		return priceText.replaceAll("[^0-9^.]", "").replaceAll(".$", "")
				.replaceAll(".$", "");
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