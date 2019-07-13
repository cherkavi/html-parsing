package shops;

import org.w3c.dom.Element;

import org.w3c.dom.Node;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionItIsNotRecord;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.ListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section.ResourceSection;
import shop_list.html.parser.engine.multi_page.section_finder.DirectFinder;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.AnchorExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceSectionFactory;
import shop_list.html.parser.engine.record.Record;

public class a_techno_com_ua extends ListBaseMultiPage{

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new DirectFinder(this.parser.getNodeListFromUrl(this.getShopUrlStartPage(), getCharset(), "/html/body/fieldset[*]/ul/li[*]"),
							    new AnchorExtractor(new IResourceSectionFactory(){
									@Override
									public ResourceSection getResourceSection(String name, String url) {
										return new UserSection(name,url);
									}
							    	
							    })
						       );
	}
	
	class UserSection extends ResourceSection{

		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter=0;
		@Override
		public String getUrlToNextPage() {
			// http://a-techno.com.ua/182/953/
			// http://a-techno.com.ua/182/953/&offset=9
			pageCounter++;
			if(pageCounter>1){
				return this.getUrl()+"/&offset="+(pageCounter-1)*9;
			}
			return this.getUrl();
		}
		
	}

	ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_ZERO_SIZE};
	
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://a-techno.com.ua";
	}
	
	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/div/div[2]/div[2]/div[2]/div[2]/div[2]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/div[@class=\"item\"]";
	}

	@Override
	protected Record getRecordFromNode(Node node) throws EParseException {
		try{
			Element elementName=(Element)this.parser.getNodeFromNode(node, "/div/h4/a");
			String name=elementName.getTextContent().trim();
			String url=elementName.getAttribute("href");
			Element elementPrice=(Element)this.parser.getNodeFromNode(node, "p[3]/span");
			if(elementPrice==null)return null;
			//System.out.println(elementPrice.getTextContent());
			//System.out.println(elementPrice.getTextContent().replaceAll("[^0-9^.]", ""));
			Float price=this.getFloatFromString(elementPrice.getTextContent().replaceAll("[^0-9^.]", "").replaceAll(".$", ""));
			return new Record(name, null, url, price, null, null);
		}catch(NullPointerException npe){
			throw new EParseExceptionItIsNotRecord();
		}
	}

}
