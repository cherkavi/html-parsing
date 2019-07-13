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

public class avt_co_ua extends RecordListBaseMultiPage{ 
		// не найден ни один раздел с двум€ и более страницами
	@Override 
public String getShopUrlStartPage() {
	return "http://avt.co.ua"; 
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
    // removeNodeFromListByTextContent(listOfNode, new String[]{"Ѕанковское оборудование", "јвтомобильные товары"});
     // removeNodeWithRepeatAttributes(list, "href");
 		return new RecursiveFinder(this.parser.getNodeListFromUrl("http://avt.co.ua", 
													   "utf-8",  
													   "/HTML/BODY/DIV[@id=\"s5_outer_wrap\"]/DIV[@id=\"s5_top_wrap\"]/DIV[@id=\"s5_t_middle\"]/DIV[@id=\"s5_main_body_outer\"]/DIV[@id=\"s5_main_body_inner\"]/DIV[@id=\"s5_middle_wrapper\"]/DIV[@id=\"s5_left\"]/DIV[@id=\"s5_left_inner\"]/DIV[@class=\"module_shadow_wrap\"]/DIV[@class=\"module_shadow\"]/DIV/DIV/DIV/DL[@class=\"accordion\"]/DT[*]/A"), 
	new IUrlFromElementExtractor() {
	@Override
	public String getUrlFromElement(Element element) {
		return addHttpPreambula("http://avt.co.ua",element.getAttribute("href"));
	}
},
	new NodeListFinderByUrl(this.parser, 
						this.getCharset(), 
						"/HTML/BODY[@id=\"s5_body\"]/DIV[@id=\"s5_outer_wrap\"]/DIV[@id=\"s5_top_wrap\"]/DIV[@id=\"s5_t_middle\"]/DIV[@id=\"s5_main_body_outer\"]/DIV[@id=\"s5_main_body_inner\"]/DIV[@id=\"s5_middle_wrapper\"]/DIV[@id=\"s5_right\"]/DIV[@id=\"s5_main_body_shadow\"]/DIV[@id=\"s5_main_body\"]/DIV[@id=\"s5_main_body2\"]/DIV[@id=\"s5_main_body3\"]/DIV[@id=\"s5_main_body4\"]/DIV[@id=\"vmMainPage\"]/DIV[2]/TABLE/TBODY/TR[*]/TD[*]/A"), 
	new IUrlFromElementExtractor() { 
	@Override 
	public String getUrlFromElement(Element element) { 
		String url=element.getAttribute("href"); 
		if(isStringEmpty(url))return null; 
		return  addHttpPreambula("http://avt.co.ua",element.getAttribute("href")); 
	} 
}, 
					    new IResourceFromElementExtractor() { 
							@Override 
							public INextSection getResourceSection(Element element) { 
								return new UserSection(element.getTextContent(), addHttpPreambula("http://avt.co.ua", element.getAttribute("href"))); 
							} 
					    });}; 
	class UserSection extends NextSection{ 
	public UserSection(String name, String url) {super(name, url); } 
	private int pageCounter=0; 
	@Override 
	public String getUrlToNextPage() { 
		pageCounter++; 
		if(pageCounter>1){ 
			return this.getUrl(); 
		} 
		return this.getUrl(); 
	}} 
	private ESectionEnd[] conditions=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_REPEAT_LAST, ESectionEnd.NEXT_RECORDS_SHOW_FIRST, ESectionEnd.NEXT_RECORDS_LOAD_ERROR};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}
	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY[@id=\"s5_body\"]/DIV[@id=\"s5_outer_wrap\"]/DIV[@id=\"s5_top_wrap\"]/DIV[@id=\"s5_t_middle\"]/DIV[@id=\"s5_main_body_outer\"]/DIV[@id=\"s5_main_body_inner\"]/DIV[@id=\"s5_middle_wrapper\"]/DIV[@id=\"s5_right\"]/DIV[@id=\"s5_main_body_shadow\"]/DIV[@id=\"s5_main_body\"]/DIV[@id=\"s5_main_body2\"]/DIV[@id=\"s5_main_body3\"]/DIV[@id=\"s5_main_body4\"]/DIV[@id=\"vmMainPage\"]/DIV[@id=\"product_list\"]"; 
	} 
	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[*]"; 
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
		return "/DIV/DIV[@class=\"s5_browseProductContainer\"]/H2/A"; 
	} 
	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/DIV/DIV[@class=\"s5_browseProductContainer\"]/H2/A"; 
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
		return "/DIV/DIV[@class=\"s5_browseProductContainer\"]/P/SPAN[@class=\"productPrice\"]"; 
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