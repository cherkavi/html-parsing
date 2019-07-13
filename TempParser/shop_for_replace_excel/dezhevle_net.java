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
	import shop_list.html.parser.engine.record.Record; 
	import shop_list.html.parser.engine.exception.EParseException; 
	import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.*; 
public class dezhevle_net extends AnalisatorRecordListBaseMultiPage{ 
	@Override 
public String getShopUrlStartPage() {
	return "http://dezhevle.net"; 
}
	@Override 
protected String getCharset() { 
	return "windows-1251"; 
}
	@Override 
	protected ISectionFinder getSectionFinder() throws Exception { 
		return new DirectFinder(this.parser.getNodeListFromUrl("http://dezhevle.net", 
													   "windows-1251",  
													   "/html/body/table/tbody/tr[5]/td/table/tbody/tr/td[2]/table/tbody/tr[*]/td[*]/div[@class=\"main_sub_cat\"]/nobr[*]/a"), 
	new IResourceFromElementExtractor(){ 
		@Override 
	public INextSection getResourceSection( Element element) {								return new UniversalAnalisator(element.getTextContent(), element.getAttribute("href"), getAnalisator()); 
							} 
					    });} 
@Override
protected String[] getThreePageForAnalisator() {
	return new String[]{"http://dezhevle.net/index.php?p_id=6","http://dezhevle.net/index.php?&p_id=6&by=with_description&order=asc&page=1","http://dezhevle.net/index.php?&p_id=6&by=with_description&order=asc&page=2", }; 
}
	private ESectionEnd[] conditions=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_SHOW_FIRST};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}
	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table/tbody/tr[5]/td/table/tbody/tr/td[2]/form"; 
	} 
	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/table[*]/tbody"; 
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
		return "/tr[1]/td[2]/div[@class=\"h1name\"]/a"; 
	} 
	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/tr[1]/td[2]/div[@class=\"h1name\"]/a"; 
	} 
	@Override 
	protected String getAttributeForExtractRecordUrl() { 
		return "href"; 
	} 
		@Override 
	protected boolean recordIsRemoveStartPageFromUrl() { 
		return true; 
	} 
	@Override
	protected String recordFromNodeInRecordToPrice() {
		return "/tr[2]/td/div/span[@class=\"price_gr\"]"; 
	} 
	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^,]", ""); 
	} 
	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/tr[2]/td/div/span[@class=\"price\"]"; 
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
	protected Record recordPostProcessor(Record record) throws EParseException{ return record; } 
}