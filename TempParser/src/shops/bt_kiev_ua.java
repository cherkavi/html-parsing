package shops;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionItIsNotRecord;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
// import shop_list.html.parser.engine.multi_page.section_finder.TestStubFinder;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.RecursiveFinder;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.AnchorResourceExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceSectionFactory;
import shop_list.html.parser.engine.multi_page.standart.ListBaseMultiPage;
import shop_list.html.parser.engine.record.Record;

public class bt_kiev_ua extends ListBaseMultiPage{

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		// return new TestStubFinder(new UserSection("test", "http://www.bt.kiev.ua/category-674.html"));
return new RecursiveFinder(this.parser.getNodeListFromUrl(getShopUrlStartPage(), getCharset(), "/html/body/table[3]/tbody/tr/td/table[2]/tbody/tr/td[3]/table/tbody/tr/td/table[2]/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr[*]/td[2]/a[*]"),
								   new IUrlFromElementExtractor() {
									@Override
									public String getUrlFromElement(Element element) {
										return getShopUrlStartPage()+"/"+element.getAttribute("href");
									}
									},
									new NodeListFinderByUrl(this.parser, getCharset(), "/html/body/table[3]/tbody/tr/td/table[2]/tbody/tr/td[3]/table/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr/td[*]/table/tbody/tr[*]/td/a"),
									new IUrlFromElementExtractor(){
										@Override
										public String getUrlFromElement( Element element) {
											return getShopUrlStartPage()+"/"+element.getAttribute("href");
										}
										
									},
							    new AnchorResourceExtractor(new IResourceSectionFactory(){
									@Override
									public NextSection getResourceSection(String name, String url) {
										return new UserSection(name,getShopUrlStartPage()+"/"+url);
									}
							    	
							    })
						       );
	}
	
	class UserSection extends NextSection{

		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter=0;
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if(pageCounter>1){
				// http://www.bt.kiev.ua/index.php?categoryID=563&view_cat=yes&search_tovar_in_category=yes&offset=8#1
				int indexSlash=this.getUrl().lastIndexOf('-');
				int indexDot=this.getUrl().lastIndexOf('.');
				return getShopUrlStartPage()+"/index.php?categoryID="+this.getUrl().substring(indexSlash+1, indexDot)+"&view_cat=yes&search_tovar_in_category=yes&offset="+((pageCounter-1)*8)+"#1";
				         				
			}
			// http://www.bt.kiev.ua/category-563.html
			return this.getUrl();
		}
		
	}

	ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_SHOW_FIRST, ESectionEnd.NEXT_RECORDS_REPEAT_LAST};
	
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.bt.kiev.ua";
	}
	
	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table[3]/tbody/tr/td/table[2]/tbody/tr/td[3]/table/tbody/tr/td/table[0$]/tbody";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/tr[*]/td/table/tbody/tr/td[2]/table/tbody";
	}

	@Override
	protected Record getRecordFromNode(Node node) throws EParseException {
		try{
			Element elementName=(Element)this.parser.getNodeFromNode(node, "/tr[1]/td/a");
			String name=elementName.getTextContent().trim();
			String url=elementName.getAttribute("href");
			Element elementPrice=(Element)this.parser.getNodeFromNode(node, "/tr[2]/td/b/font");
			if(elementPrice==null)return null; // нет в наличии
			String tempText=elementPrice.getTextContent();
			int squareIndex=tempText.indexOf('(');
			Float price=this.getFloatFromString(tempText.substring(0,squareIndex).replaceAll("[^0-9]", ""));
			Float priceUsd=this.getFloatFromString(tempText.substring(squareIndex).replaceAll("[^0-9^.^,]", ""));
			return new Record(name, null, url, price, priceUsd, null);
		}catch(NullPointerException ex){
			throw new EParseExceptionItIsNotRecord();
		}
	}

	@Override
	protected String getCharset() {
		return ECharset.WIN_1251.getName();
	}
}
