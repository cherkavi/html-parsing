	package shops;	
		
	import java.util.ArrayList;
	import org.w3c.dom.Node;
	import org.w3c.dom.Element;	
	import shop_list.html.parser.engine.multi_page.ESectionEnd;	
	import shop_list.html.parser.engine.multi_page.section.INextSection;	
	import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;	
	import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;	
	import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;	
	import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;	
	import shop_list.html.parser.engine.record.Record; 
	import shop_list.html.parser.engine.exception.EParseException; 
	import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.*; 

public class photo_ua extends AnalisatorRecordListBaseMultiPage{ 
	@Override 
public String getShopUrlStartPage() {
	return "http://photo.ua"; 
}
	@Override 
protected String getCharset() { 
	return "utf-8"; 
}
	@Override 
	protected ISectionFinder getSectionFinder() throws Exception { 
 	// import org.w3c.dom.Node;
 	// import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl; 
 	// ArrayList<Node> listOfNode=(new NodeListFinderByUrl(parser, getCharset(), "/html/body/div[4]/div/div[2]/div[3]/table/tbody/tr[*]/td[@width="25%"]/div/div/ul/li[*]/a")).getNodeListByUrl(this.getShopUrlStartPage()); 
		ArrayList<Node> listOfNode=this.parser.getNodeListFromUrl("http://photo.ua", "utf-8",  
													   "/HTML/BODY/TABLE/TBODY/TR/TD[2]/TABLE/TBODY/TR[9]/TD/TABLE/TBODY/TR/TD[1]/DIV[@id=\"block\"]/DIV[@id=\"block_content\"]/DIV[@id=\"catalog_menu\"]/UL/DIV[@class=\"cpt_category_tree\"]/LI[*]/A[@class=\"cat_link\"]"); 
		return new DirectFinder(listOfNode,		new IResourceFromElementExtractor(){ 
			@Override 
			public INextSection getResourceSection( Element element) {								return new UniversalAnalisator(element.getTextContent(), "http://photo.ua"+element.getAttribute("href"), getAnalisator()); 
							} 
					    });} 
@Override
protected String[] getThreePageForAnalisator() {
	return new String[]{"http://photo.ua/category/aksessuari/","http://photo.ua/category/aksessuari/offset20/","http://photo.ua/category/aksessuari/offset40/", }; 
}
	private ESectionEnd[] conditions=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_REPEAT_LAST, ESectionEnd.NEXT_RECORDS_LOAD_ERROR};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}
	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/TABLE/TBODY/TR/TD[2]/TABLE/TBODY/TR[9]/TD/TABLE/TBODY/TR/TD[@id=\"container_main_content\"]/DIV[@class=\"cpt_maincontent cptovst_iwgkmz\"]/DIV[@class=\"cpt_maincontent\"]/CENTER/TABLE/TBODY"; 
	} 
	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[*]/TD[*]/FORM[@class=\"product_brief_block\"]"; 
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
		return "/DIV[@class=\"prdbrief_name\"]/A"; 
	} 
	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/DIV[@class=\"prdbrief_name\"]/A"; 
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
		return "/DIV[@class=\"prdbrief_price\"]/SPAN[@class=\"totalPrice\"]"; 
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