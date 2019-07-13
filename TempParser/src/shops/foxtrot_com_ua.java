package shops;

import org.w3c.dom.Element;

import org.w3c.dom.Node;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionItIsNotRecord;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.DirectFinder;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.AnchorResourceExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceSectionFactory;
import shop_list.html.parser.engine.multi_page.standart.ListBaseMultiPage;
import shop_list.html.parser.engine.record.Record;

public class foxtrot_com_ua extends ListBaseMultiPage{

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new DirectFinder(this.parser.getNodeListFromUrl("http://www.foxtrot.com.ua/Catalog_first.aspx?CityID=1", 
																 getCharset(), 
																 "/html/body/form/table/tbody/tr[2]/td/table/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr[*]/td[2]/table/tbody/tr[*]/td[@valign=\"top\"]/table/tbody/tr/td/a"
																 ),
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
			// http://www.foxtrot.com.ua/catalog.aspx?catalogId=10&classId=154&cityId=1
			// http://www.foxtrot.com.ua/catalog.aspx?catalogid=10&classid=154&cityid=1&sort=&brand=0&propertycritery=&page=2
			pageCounter++;
			if(pageCounter>1){
				return this.getUrl()+"&sort=&brand=0&propertycritery=&page="+pageCounter;
			}
			return this.getUrl();
		}
	}

	ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_LOAD_ERROR, ESectionEnd.NEXT_RECORDS_ZERO_SIZE};
	
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.foxtrot.com.ua";
	}
	
	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/form/table/tbody/tr[2]/td/table/tbody/tr[2]/td[2]/table[3]/tbody";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/tr[*]/td[@valign=\"top\"]/table/tbody/";
	}

	@Override
	protected Record getRecordFromNode(Node node) throws EParseException {
		try{
			Element elementName=(Element)this.parser.getNodeFromNode(node, "/tr/td/strong/a");
			String name=elementName.getTextContent().trim();
			String url=elementName.getAttribute("href");
			Element elementPrice=(Element)this.parser.getNodeFromNode(node, "/tr[4]/td/font/strong/span");
			if(elementPrice==null)return null;
			Float price=null;
			if(elementPrice!=null){
				price=this.getFloatFromString(elementPrice.getTextContent().replaceAll("[^0-9]", ""));
			}
			//System.out.println(elementPrice.getTextContent());
			//System.out.println(elementPrice.getTextContent().replaceAll("[^0-9^.]", ""));
			return new Record(name, null, url, price, null, null);
		}catch(NullPointerException npe){
			throw new EParseExceptionItIsNotRecord();
		}
	}

}
