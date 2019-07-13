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

public class buy_ua extends AnalisatorRecordListBaseMultiPage{ 
		// "Автокресла",
		// "Програмное обеспечение",
		// "Мини АТС"
	@Override 
public String getShopUrlStartPage() {
	return "http://www.buy.ua"; 
}
	@Override 
protected String getCharset() { 
	return "utf-8"; 
}
	@Override 
	protected ISectionFinder getSectionFinder() throws Exception { 
    // return new TestStubFinder(new UniversalAnalisator("temp","http://",getAnalisator())); 
 	// import org.w3c.dom.Node;
 	// import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl; 
 	// ArrayList<Node> listOfNode=(new NodeListFinderByUrl(parser, getCharset(), "/html/body/div[4]/div/div[2]/div[3]/table/tbody/tr[*]/td[@width="25%"]/div/div/ul/li[*]/a")).getNodeListByUrl(this.getShopUrlStartPage()); 
    // removeNodeFromListByTextContent(listOfNode, new String[]{"Банковское оборудование", "Автомобильные товары"});
     // removeNodeWithRepeatAttributes(list, "href");
 		return new RecursiveFinder(this.parser.getNodeListFromUrl("http://www.buy.ua", 
													   "utf-8",  
													   "/HTML/BODY/DIV[@id=\"page\"]/DIV[@id=\"main\"]/DIV[@id=\"leftcol\"]/DIV[@class=\"nav\"]/UL[*]/LI[*]/A"), 
	new IUrlFromElementExtractor() {
	@Override
	public String getUrlFromElement(Element element) {
		return addHttpPreambula("http://www.buy.ua",element.getAttribute("href"));
	}
},
	new NodeListFinderByUrl(this.parser, 
						this.getCharset(), 
						"/HTML/BODY/DIV[@id=\"page\"]/DIV[@id=\"main\"]/DIV[@id=\"rightcol\"]/DIV[@class=\"brands\"]/H3/A"), 
	new IUrlFromElementExtractor() { 
	@Override 
	public String getUrlFromElement(Element element) { 
		String url=element.getAttribute("href"); 
		if(isStringEmpty(url))return null; 
		return  addHttpPreambula("http://www.buy.ua",element.getAttribute("href")); 
	} 
}, 
					    new IResourceFromElementExtractor() { 
							@Override 
							public INextSection getResourceSection(Element element) { 
								return new UniversalAnalisator(element.getTextContent(), addHttpPreambula("http://www.buy.ua", element.getAttribute("href")), getAnalisator()); 
							} 
					    });}; 
@Override
protected String[] getThreePageForAnalisator() {
	return new String[]{"http://www.buy.ua/shop/1400019/1400021/","http://www.buy.ua/shop/1400019/1400021/index.php?p=15","http://www.buy.ua/shop/1400019/1400021/index.php?p=30", }; 
}
	private ESectionEnd[] conditions=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_REPEAT_LAST, ESectionEnd.NEXT_RECORDS_LOAD_ERROR, ESectionEnd.NEXT_RECORDS_ZERO_SIZE};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}
	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/DIV[@id=\"page\"]/DIV[@id=\"main\"]/DIV[@id=\"rightcol\"]/DIV[@class=\"product-block\"]/DIV[@class=\"hits\"]/DIV[@class=\"hide\"]/UL[@class=\"products\"]"; 
	} 
	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/LI[*]"; 
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
		return "/A[1]"; 
	} 
	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/A[1]"; 
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
		return "/DIV[@class=\"price\"]/STRONG"; 
	} 
	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", ""); 
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
	protected Record prepareRecordBeforeSave(Record record) throws EParseException{ return record; } 
}