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

public class citrus_ua extends AnalisatorRecordListBaseMultiPage{ 
	@Override 
public String getShopUrlStartPage() {
	return "http://www.citrus.ua"; 
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
		ArrayList<Node> listOfNode=this.parser.getNodeListFromUrl("http://www.citrus.ua", "windows-1251",  
													   "/HTML/BODY/DIV[@id=\"content\"]/DIV[@id=\"content-left\"]/UL[@class=\"leftmenu\"]/LI[1]/UL/LI[*]/UL[*]/LI[*]/A"); 
		return new DirectFinder(listOfNode,		new IResourceFromElementExtractor(){ 
			@Override 
			public INextSection getResourceSection( Element element) {								return new UniversalAnalisator(element.getTextContent(), "http://www.citrus.ua"+element.getAttribute("href"), getAnalisator()); 
							} 
					    });} 
@Override
protected String[] getThreePageForAnalisator() {
	return new String[]{"http://www.citrus.ua/shop/goods/mobile/150/","http://www.citrus.ua/shop/goods/mobile/150/?PAGEN_1=2","http://www.citrus.ua/shop/goods/mobile/150/?PAGEN_1=3", }; 
}
	private ESectionEnd[] conditions=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_REPEAT_LAST, ESectionEnd.NEXT_RECORDS_SHOW_FIRST};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}
	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/DIV[@id=\"content\"]/DIV[@id=\"content-center\"]/DIV[@class=\"catalog-top\"]"; 
	} 
	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[@class=\"catalog-top-element\"]"; 
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
		return "/H4[@class=\"name\"]/A"; 
	} 
	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/H4[@class=\"name\"]/A"; 
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
		return "/P[@class=\"prices\"]/SPAN[@class=\"catalog-price\"]"; 
	} 
	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "").replaceAll(".$",""); 
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