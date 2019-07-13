package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.element_extractor.AnchorElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.*;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;

public class coolmart_com_ua extends AnalisatorRecordListBaseMultiPage{

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		/*
		return new TestStubFinder(new NextSection("test","http://www.coolmart.com.ua/index.php?cat=14") {
			private int pageCounter=0;
			@Override
			public String getUrlToNextPage() {
				pageCounter++;
				if(pageCounter==1)
				return this.getUrl();
				return null;
			}
		});*/
		return new RecursiveFinder(this.parser.getNodeListFromUrl(this.getShopUrlStartPage(), 
				  												  getCharset(),
				  												  "/html/body/div/div[4]/div/div/table[*]/tbody/tr/td[2]/a/strong" ),
				  												  
									new IUrlFromElementExtractor() {
										@Override
										public String getUrlFromElement(Element element) {
											String url=((Element)element.getParentNode()).getAttribute("href");
											if(isStringEmpty(url))return null;
											return url;
										}
									},
					
									new NodeListFinderByUrl(this.parser, 
														    getCharset(), 
															"/html/body/div/div[4]/div/div[2]/div/dl[*]/dd[2]/a"),
									
									new AnchorElementExtractor(),
									
									new IResourceFromElementExtractor() {
										@Override
										public INextSection getResourceSection(Element element) {
											return new UniversalAnalisator(element.getTextContent(), element.getAttribute("href"), getAnalisator());
										}
									}
				  );
	}
	
	@Override
	protected String recordFromNodeInRecordToName() {
		return "/dd/a";
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
	protected String recordFromNodeInRecordToPriceEuro() {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToPriceEuroBeforeConvert(String priceText) {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/dd[3]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^,]", "");
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/dd/a";
	}
	
	@Override
	protected String getAttributeForExtractRecordUrl() {
		return super.getAttributeForExtractRecordUrl();
	}

	@Override
	protected boolean recordIsRemoveStartPageFromUrl() {
		return true;
	}

	@Override
	protected String recordFromNodeIsPresent() {
		return null;
	}

	@Override
	protected String recordFromNodeIsPresentText() {
		return null;
	}


	private ESectionEnd[] conditions=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_REPEAT_LAST};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/div[@class=\"page\"]/div[@class=\"pageItem\"]/dl";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/div/div[4]/div";
	}

	@Override
	protected String[] getThreePageForAnalisator() {
		return new String[]{"http://www.coolmart.com.ua/index.php?cat=2", "http://www.coolmart.com.ua/index.php?cat=2&page=2", "http://www.coolmart.com.ua/index.php?cat=2&page=3"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.coolmart.com.ua";
	}

	@Override
	protected String getCharset() {
		return super.getCharset();
	}
}
