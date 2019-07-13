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
import shop_list.html.parser.engine.multi_page.section_finder.multi_level_finder.FindLevel;
import shop_list.html.parser.engine.multi_page.section_finder.multi_level_finder.MultiLevelFinder;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.AnchorResourceExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceSectionFactory;
import shop_list.html.parser.engine.multi_page.standart.ListBaseMultiPage;
import shop_list.html.parser.engine.record.Record;

public class pikmarket_kiev_ua extends ListBaseMultiPage{

	ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_LOAD_ERROR, 
											  ESectionEnd.NEXT_RECORDS_REPEAT_LAST, 
											  ESectionEnd.NEXT_RECORDS_SHOW_FIRST};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}
	
	@Override
	protected String getCharset() {
		return ECharset.WIN_1251.getName();
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://pikmarket.kiev.ua";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		// return new TestStubFinder(new UserResource("","http://pikmarket.kiev.ua/?path=0/2/774/1695/1782/1713/1753"));
return new MultiLevelFinder(
									this.getShopUrlStartPage(),
									 new AnchorResourceExtractor(new IResourceSectionFactory() {
											@Override
											public NextSection getResourceSection(String name, String url) {
												return new UserResource(name, getShopUrlStartPage()+url);
											}
										 }),
									new FindLevel(new NodeListFinderByUrl(this.parser, this.getCharset(), "/html/body/table[2]/tbody/tr/td[4]/div/a[*]"), 
												  new IUrlFromElementExtractor() {
													@Override
													public String getUrlFromElement(Element element) {
														return element.getAttribute("href");
													}
												}),
									new FindLevel(new NodeListFinderByUrl(this.parser, this.getCharset(), "/html/body/table[4]/tbody/tr/td[2]/div[2]/p[@class=\"pg10\"]/span/b/a"),
											  	  null)
								  );
	}
	
	class UserResource extends NextSection{

		public UserResource(String name, String url) {
			super(name, url);
		}
		
		private int counter=0;
		
		@Override
		public String getUrlToNextPage() {
			counter++;
			if(counter>1)return this.getUrl()+"&sortby=1&page="+counter;
			return this.getUrl();
		}
		
	}

	@Override
	protected Record getRecordFromNode(Node node) throws EParseException {
		try{
			String name=((Element)this.parser.getNodeFromNode(node, "/p")).getTextContent();
			String url=((Element)this.parser.getNodeFromNode(node, "/p/span/a")).getAttribute("href");
			Node priceNode=this.parser.getNodeFromNode(node, "/p[2]/span[2]");
			if(priceNode==null)return null;
			Float price=this.getFloatFromString(((Element)priceNode).getTextContent().replaceAll("[^0-9]", ""));
			if(price==null)return null;
			if(price.floatValue()==0)return null;
			return new Record(name, null, url, price, null, null);
		}catch(NullPointerException npe){
			throw new EParseExceptionItIsNotRecord();
		}
	}


	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/div[@class=\"pod_item\"]/table/tbody/tr/td[2]";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table[4]/tbody/tr/td[2]";
	}

}
