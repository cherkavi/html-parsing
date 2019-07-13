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
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.ListBaseMultiPage;
import shop_list.html.parser.engine.record.Record;

public class protoria_ua extends ListBaseMultiPage{


	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new MultiLevelFinder(this.getShopUrlStartPage(), 
									new IResourceFromElementExtractor() {
										@Override
										public NextSection getResourceSection(Element element) {
											return new UserSection(element.getAttribute("alt"), getShopUrlStartPage()+element.getAttribute("href"));
										}
									} , 
									new FindLevel(new NodeListFinderByUrl(this.parser, this.getCharset(), "/HTML/BODY/DIV[@id=\"body\"]/DIV[@id=\"head\"]/DIV[@id=\"main_menu\"]/UL[@class=\"main_menu\"]/LI[*]/A"),
												  new IUrlFromElementExtractor() {
													@Override
													public String getUrlFromElement(Element element) {
														return getShopUrlStartPage()+element.getAttribute("href");
													}
												}),
									new FindLevel(new NodeListFinderByUrl(this.parser, this.getCharset(), "/HTML/BODY/DIV[@id=\"body\"]/DIV[@class=\"main_content\"]/DIV[@class=\"center_right\"]/DIV[@class=\"one_block\"]/DIV[@class=\"content1\"]/DIV[@class=\"category_item\"]/DIV[@class=\"img\"]/A"),
												  null)
									);
	}

	class UserSection extends NextSection{

		public UserSection(String name, String url) {
			super(name, url);
		}

		private int counter=0;
		@Override
		public String getUrlToNextPage() {
			counter++;
			if(counter>1)return this.getUrl()+"?page="+counter;
			return this.getUrl();
		}
		
	}
	

	ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_REPEAT_LAST};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://protoria.ua";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/DIV[@id=\"body\"]/DIV[@class=\"main_content\"]/DIV[@class=\"center_right\"]/DIV[@class=\"one_block\"]/FORM/DIV[@id=\"content_products\"]/DIV[@class=\"list_type1\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/div[@class=\"list_item\"]/";
	}
	
	@Override
	protected String getCharset() {
		return ECharset.KOI8_U.getName();
	}
	
	@Override
	protected Record getRecordFromNode(Node node) throws EParseException{
		Element elementName=(Element)this.parser.getNodeFromNode(node, "/a");
		if(elementName==null)throw new EParseExceptionItIsNotRecord();
		Element elementPrice=(Element)this.parser.getNodeFromNode(node, "/div[3]/strong");
		String name=elementName.getTextContent();
		String url=elementName.getAttribute("href");
		Float price=null;
		try{
			price=this.getFloatFromString(elementPrice.getTextContent().replaceAll("[^0-9^.]", ""));
		}catch(Exception ex){};
		if(price==null)return null;
		return new Record(name, null, url, price, null, null);
	}


}
