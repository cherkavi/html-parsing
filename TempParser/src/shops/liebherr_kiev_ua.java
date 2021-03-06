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

public class liebherr_kiev_ua extends RecordListBaseMultiPage{ 
	@Override 
public String getShopUrlStartPage() {
	return "http://liebherr.kiev.ua"; 
}
	@Override 
protected String getCharset() { 
	return "utf-8"; 
}
	@Override 
	protected ISectionFinder getSectionFinder() throws Exception { 
		return new TwoLevelFinder(this.parser.getNodeListFromUrl("http://liebherr.kiev.ua/catalog/", 
													   "utf-8",  
													   "/HTML/BODY/DIV[@id=\"main_wrapper\"]/DIV[@id=\"wrapper\"]/DIV[@id=\"main\"]/DIV[@class=\"leftbar\"]/UL[@class=\"left_menu\"]/LI[*]/A"), 
	new IUrlFromElementExtractor() {
		@Override
		public String getUrlFromElement(Element element) {
		return addHttpPreambula("http://liebherr.kiev.ua",element.getAttribute("href"));
	}
},
		new NodeListFinderByUrl(this.parser, 
						this.getCharset(), 
						"/HTML/BODY/DIV[@id=\"main_wrapper\"]/DIV[@id=\"wrapper\"]/DIV[@id=\"main\"]/DIV[@class=\"rightbar\"]/DIV[@class=\"txt\"]/TABLE/TBODY/TR[*]/TD[@valign=\"top\"]/P[1]/STRONG/A"), 
					    new IResourceFromElementExtractor() { 
							@Override 
							public INextSection getResourceSection(Element element) { 
								return new UserSection(element.getTextContent(), addHttpPreambula("http://liebherr.kiev.ua",element.getAttribute("href"))); 
							} }); } 
	class UserSection extends NextSection{ 
	public UserSection(String name, String url) {super(name, url); } 
	private int pageCounter=0; 
	@Override 
	public String getUrlToNextPage() { 
		pageCounter++; 
		if(pageCounter>1){ 
			return this.getUrl()+"page"+(pageCounter-1)+".html"; 
		} 
		return this.getUrl(); 
	}} 
	private ESectionEnd[] conditions=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_REPEAT_LAST, ESectionEnd.NEXT_RECORDS_LOAD_ERROR, ESectionEnd.NEXT_RECORDS_ZERO_SIZE};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}
	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/DIV[@id=\"main_wrapper\"]/DIV[@id=\"wrapper\"]/DIV[@id=\"main\"]/DIV[@class=\"rightbar\"]/TABLE[@class=\"catalog_table\"]/TBODY"; 
	} 
	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD[*]/DIV[@class=\"catalog_wrap\"]/FORM[@class=\"catalog_form\"]/DIV[@class=\"catalog_content\"]"; 
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
		return "/BIG[@class=\"c_title\"]/STRONG"; 
	} 
	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/SPAN[@class=\"p_more\"]/A[@class=\"more\"]"; 
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
		return "/SPAN[@class=\"p_price\"]"; 
	} 
	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9]", ""); 
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
	protected Record prepareRecordBeforeSave(Record record) throws EParseException{ return record; } 
}