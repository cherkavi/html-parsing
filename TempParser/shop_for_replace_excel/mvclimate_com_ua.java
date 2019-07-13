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
public class mvclimate_com_ua extends AnalisatorRecordListBaseMultiPage{ 
	@Override 
public String getShopUrlStartPage() {
	return "http://www.mvclimate.com.ua"; 
}
	@Override 
protected String getCharset() { 
	return "windows-1251"; 
}
	@Override 
	protected ISectionFinder getSectionFinder() throws Exception { 
		return new DirectFinder(this.parser.getNodeListFromUrl("http://www.mvclimate.com.ua", 
													   "windows-1251",  
													   "/html/body/table[3]/tbody/tr/td/table/tbody/tr[*]/td/a[@class=\"subcategory\"]"), 
	new IResourceFromElementExtractor(){ 
		@Override 
	public INextSection getResourceSection( Element element) {								return new UniversalAnalisator(element.getTextContent(), element.getAttribute("href"), getAnalisator()); 
							} 
					    });} 
@Override
protected String[] getThreePageForAnalisator() {
	return new String[]{"http://www.mvclimate.com.ua/27/","http://www.mvclimate.com.ua/27/2.1-0.html","http://www.mvclimate.com.ua/27/3.1-0.html", }; 
}
	private ESectionEnd[] conditions=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_LOAD_ERROR, ESectionEnd.NEXT_RECORDS_ZERO_SIZE};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}
	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table[3]/tbody/tr/td[2]/table/tbody/tr[3]/td/table/tbody"; 
	} 
	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/tr[*]/td[*]/table/tbody/"; 
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
		return "/tr[2]/td/a"; 
	} 
	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/tr[2]/td/a"; 
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
		return "/tr[3]/td/div[@class=\"price\"]/span"; 
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
}