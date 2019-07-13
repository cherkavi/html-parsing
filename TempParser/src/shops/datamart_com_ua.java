	package shops;	
		
	import org.w3c.dom.Element;	
	import shop_list.html.parser.engine.multi_page.ESectionEnd;	
	import shop_list.html.parser.engine.multi_page.section.INextSection;	
	import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;	
	import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;	
	import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;	
	import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;	
	import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.*;
	
public class datamart_com_ua extends AnalisatorRecordListBaseMultiPage{ 
	@Override 
public String getShopUrlStartPage() {
	return "http://www.datamart.com.ua"; 
}
	@Override 
protected String getCharset() { 
	return "windows-1251"; 
}
	@Override 
	protected ISectionFinder getSectionFinder() throws Exception { 
		return new DirectFinder(this.parser.getNodeListFromUrl("http://www.datamart.com.ua", 
													   "windows-1251",  
													   "/html/body/table/tbody/tr[7]/td/table/tbody/tr[*]/td[@style=\"padding-left: 21px;\"]/a"), 
	new IResourceFromElementExtractor(){ 
		@Override 
	public INextSection getResourceSection( Element element) {								
			return new UniversalAnalisator(element.getTextContent(), "http://www.datamart.com.ua"+element.getAttribute("href"), getAnalisator()); 
							} 
					    });} 
@Override
protected String[] getThreePageForAnalisator() {
	return new String[]{"http://www.datamart.com.ua/catalog/section.php?SECTION_ID=157","http://www.datamart.com.ua/catalog/section.php?PAGEN_1=2&SECTION_ID=157#nav_start","http://www.datamart.com.ua/catalog/section.php?PAGEN_1=3&SECTION_ID=157#nav_start", }; 
}
	private ESectionEnd[] conditions=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_SHOW_FIRST};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}
	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table/tbody/tr[7]/td[2]/table/tbody/tr[2]/td/p[2]/table[2]/tbody/tr/td/table/tbody"; 
	} 
	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/tr[*]"; 
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
		return "/td[3]/a"; 
	} 
	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/td[3]/a"; 
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
		return "/td[5]/b/font"; 
	} 
	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^,]", ""); 
	} 
	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return null; 
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
		return priceText.replaceAll("[^0-9^,]", ""); 
	} 
}
