	package shops;	
		
	import java.util.ArrayList;
	import org.w3c.dom.Node;
	import org.w3c.dom.Element;	
	import shop_list.html.parser.engine.multi_page.ESectionEnd;	
	import shop_list.html.parser.engine.multi_page.section.INextSection;	
	import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;	
	import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;	
	import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;	
	import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;	
	import shop_list.html.parser.engine.record.Record; 
	import shop_list.html.parser.engine.exception.EParseException; 
	import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.*; 

public class asteks_com_ua extends AnalisatorRecordListBaseMultiPage{ 
	@Override 
public String getShopUrlStartPage() {
	return "http://www.asteks.com.ua"; 
}
	@Override 
protected String getCharset() { 
	return "windows-1251"; 
}
	@Override 
	protected ISectionFinder getSectionFinder() throws Exception { 
 	// import org.w3c.dom.Node;
 	// import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl; 
 	// ArrayList<Node> listOfNode=(new NodeListFinderByUrl(parser, getCharset(), "/html/body/div[4]/div/div[2]/div[3]/table/tbody/tr[*]/td[@width="25%"]/div/div/ul/li[*]/a")).getNodeListByUrl(this.getShopUrlStartPage()); 
		ArrayList<Node> listOfNode=this.parser.getNodeListFromUrl("http://www.asteks.com.ua", "windows-1251",  
													   "/HTML/BODY/TABLE/TBODY/TR/TD/DIV/DIV[@id=\"container\"]/DIV[6]/TABLE[@id=\"middle\"]/TBODY/TR/TD[@id=\"rightCell\"]/DIV/DIV[@class=\"block\"]/TABLE[2]/TBODY/TR/TD[@class=\"hdb\"]/DIV/TABLE[*]/TBODY/TR[@class=\"dtree\"]/TD[@class=\"dtree\"]/A"); 
		return new DirectFinder(listOfNode,		new IResourceFromElementExtractor(){ 
			@Override 
			public INextSection getResourceSection( Element element) {								String url=addHttpPreambula("http://www.asteks.com.ua",element.getAttribute("href")); 
								return new UniversalAnalisator(element.getTextContent(), url, getAnalisator()); 
							} 
					    });} 
@Override
protected String[] getThreePageForAnalisator() {
	return new String[]{"http://www.asteks.com.ua/category_18.html","http://www.asteks.com.ua/category_18_offset_35.html","http://www.asteks.com.ua/category_18_offset_70.html", }; 
}
	private ESectionEnd[] conditions=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_REPEAT_LAST, ESectionEnd.NEXT_RECORDS_LOAD_ERROR, ESectionEnd.NEXT_RECORDS_ZERO_SIZE};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}
	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/TABLE/TBODY/TR/TD/DIV/DIV[@id=\"container\"]/DIV[6]/TABLE[@id=\"middle\"]/TBODY/TR/TD[1]/TABLE[5]/TBODY/TR/TD/TABLE/TBODY/TR/TD[@class=\"hmin\"]/TABLE[@class=\"gre\"]/TBODY"; 
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
		return "/TD[3]/TABLE/TBODY/TR/TD/A"; 
	} 
	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/TD[3]/TABLE/TBODY/TR/TD/A"; 
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
		return "/TD[1$]"; 
	} 
	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^,]", ""); 
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