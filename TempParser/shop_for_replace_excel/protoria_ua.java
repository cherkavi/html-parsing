	package shops;	
		
	import org.w3c.dom.Element;	
	import shop_list.html.parser.engine.multi_page.ESectionEnd;	
	import shop_list.html.parser.engine.multi_page.section.INextSection;	
	import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;	
	import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;	
	import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;	
	import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;	
	import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;	
	import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.*;	
	import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;	
	import shop_list.html.parser.engine.record.Record; 
	import shop_list.html.parser.engine.exception.EParseException; 

public class protoria_ua extends AnalisatorRecordListBaseMultiPage{ 
	@Override 
public String getShopUrlStartPage() {
	return "http://protoria.ua"; 
}
	@Override 
protected String getCharset() { 
	return "KOI8-U"; 
}
	@Override 
	protected ISectionFinder getSectionFinder() throws Exception { 
    // // return new TestStubFinder(new UniversalAnalisator("temp","http://",getAnalisator())); 
		return new RecursiveFinder(this.parser.getNodeListFromUrl("http://protoria.ua", 
													   "KOI8-U",  
													   "/HTML/BODY/DIV[@id=\"body\"]/DIV[@id=\"head\"]/DIV[@id=\"main_menu\"]/UL[@class=\"main_menu\"]/LI[*]/A"), 
	new IUrlFromElementExtractor() {
	@Override
	public String getUrlFromElement(Element element) {
		return "http://protoria.ua"+element.getAttribute("href");
	}
},
	new NodeListFinderByUrl(this.parser, 
						this.getCharset(), 
						"/HTML/BODY/DIV[@id=\"body\"]/DIV[@class=\"main_content\"]/DIV[@class=\"center_right\"]/DIV[@class=\"one_block\"]/DIV[@class=\"content1\"]/DIV[@class=\"category_item\"]/div/a"), 
	new IUrlFromElementExtractor() { 
	@Override 
	public String getUrlFromElement(Element element) { 
		String url=element.getAttribute("href"); 
		if(isStringEmpty(url))return null; 
		return  "http://protoria.ua"+url; 
	} 
}, 
					    new IResourceFromElementExtractor() { 
							@Override 
							public INextSection getResourceSection(Element element) { 
								return new UniversalAnalisator(element.getTextContent(), "http://protoria.ua"+element.getAttribute("href"), getAnalisator()); 
							} 
					    });}; 
@Override
protected String[] getThreePageForAnalisator() {
	return new String[]{"http://protoria.ua/computers/displays/","http://protoria.ua/computers/displays/?page=2","http://protoria.ua/computers/displays/?page=3", }; 
}
	private ESectionEnd[] conditions=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_REPEAT_LAST, ESectionEnd.NEXT_RECORDS_SHOW_FIRST};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}
	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/DIV[@id=\"body\"]/DIV[@class=\"main_content\"]/DIV[@class=\"center_right\"]/DIV[@class=\"one_block\"]/FORM/DIV[@id=\"content_products\"]/DIV[@class=\"list_type1\"]"; 
	} 
	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[@class=\"list_item\"]"; 
	} 
	@Override
	protected String recordFromNodeIsPresent() {
		return "/div[@class=\"list_item_col4\"]/div[@class=\"dop1\"]/strong"; 
	} 
	@Override 
	protected String recordFromNodeIsPresentText() { 
		return "есть"; 
	} 
	@Override
	protected String recordFromNodeInRecordToName() {
		return "/A[@class=\"item_name\"]"; 
	} 
	@Override
	protected String recordFromNodeInRecordToUrl() {
		return ""; 
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
		return "/div[@class=\"list_item_col3\"]/strong"; 
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
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(String priceText) {
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
	protected Record recordPostProcessor(Record record) throws EParseException{ return record; } 
}