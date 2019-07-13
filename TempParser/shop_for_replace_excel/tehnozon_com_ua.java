	package shops;	
		
	import org.w3c.dom.Element;	
	import shop_list.html.parser.engine.multi_page.ESectionEnd;	
	import shop_list.html.parser.engine.multi_page.section.INextSection;	
	import shop_list.html.parser.engine.multi_page.section.UniversalAnalisator;	
	import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;	
	import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;	
	import shop_list.html.parser.engine.multi_page.standart.RecordListBaseMultiPage; 
	import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;	
	import shop_list.html.parser.engine.multi_page.section_finder.element_extractor.AnchorElementExtractor;	
	import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.*;	
	import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;	
	import shop_list.html.parser.engine.multi_page.standart.AnalisatorRecordListBaseMultiPage;	
	import shop_list.html.parser.engine.multi_page.section_finder.two_level_finder.*;
	import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.*;
	import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.AnchorResourceExtractor; 
	import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceSectionFactory; 
	import shop_list.html.parser.engine.multi_page.section.NextSection; 
	import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.*; 
public class tehnozon_com_ua extends AnalisatorRecordListBaseMultiPage{ 
	@Override 
public String getShopUrlStartPage() {
	return "http://www.tehnozon.com.ua"; 
}
	@Override 
protected String getCharset() { 
	return "windows-1251"; 
}
	@Override 
	protected ISectionFinder getSectionFinder() throws Exception { 
		return new DirectFinder(this.parser.getNodeListFromUrl("http://www.tehnozon.com.ua", 
													   "windows-1251",  
													   "/html/body/table[3]/tbody/tr/td[2]/div[@class=\"cat_col\"]/div[@class=\"cat_block\"]/div[@class=\"cat_in\"]/ul/li[*]/a"), 
	new IResourceFromElementExtractor(){ 
		@Override 
	public INextSection getResourceSection( Element element) {								return new UniversalAnalisator(element.getTextContent(), "http://www.tehnozon.com.ua/"+element.getAttribute("href"), getAnalisator()); 
							} 
					    });} 
@Override
protected String[] getThreePageForAnalisator() {
	return new String[]{"http://www.tehnozon.com.ua/?id=10&trid=86","http://www.tehnozon.com.ua/?id=10&trid=86&page=2","http://www.tehnozon.com.ua/?id=10&trid=86&page=3", }; 
}
	private ESectionEnd[] conditions=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_LOAD_ERROR, ESectionEnd.NEXT_RECORDS_ZERO_SIZE};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}
	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table[3]/tbody/tr/td[2]/table/tbody"; 
	} 
	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/tr[*]"; 
	} 
	@Override
	protected String recordFromNodeIsPresent() {
		return "/td[@class=\"tovtd\"]"; 
	} 
	@Override 
	protected String recordFromNodeIsPresentText() { 
		return null; 
	} 
	@Override
	protected String recordFromNodeInRecordToName() {
		return "/td[2]/div/a"; 
	} 
	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/td[2]/div/a"; 
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
		return "/td[3]/div"; 
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
}