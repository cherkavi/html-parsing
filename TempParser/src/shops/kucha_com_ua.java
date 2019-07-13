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

public class kucha_com_ua extends ListBaseMultiPage{

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new DirectFinder(this.parser.getNodeListFromUrl(this.getShopUrlStartPage(), getCharset(), "/html/body/div/div[2]/div/div/ul/li[*]/ul/li[*]/a"),
							    new AnchorResourceExtractor(new IResourceSectionFactory(){
									@Override
									public NextSection getResourceSection(String name, String url) {
										return new UserSection(name,url);
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
			// http://www.kucha.com.ua/ru/products/noutbuki/index.html
			// http://www.kucha.com.ua/ru/products/noutbuki/pno/2/index.html
			pageCounter++;
			if(pageCounter>1){
				int index=this.getUrl().indexOf("/index.html");
				return this.getUrl().substring(0,index)+"/pno/"+(pageCounter)+"/index.html";
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
		return "http://www.kucha.com.ua";
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
			String url=this.removeStartPage(elementName.getAttribute("href"));
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
