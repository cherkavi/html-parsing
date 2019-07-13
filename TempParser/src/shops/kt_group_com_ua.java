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

public class kt_group_com_ua extends ListBaseMultiPage{
	
	public class UserSection extends NextSection{
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter=0;
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if(pageCounter==1)return this.getUrl();
			return this.getUrl()+"&offset="+(pageCounter-1)*15;
		}
		
	}

	private ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_REPEAT_LAST};
	
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://kt-group.com.ua";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/div/div/div/div/table/tbody/tr/td[2]/div";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/div[@class=\"cat_item\"]/table/tbody/tr/td[2]";
	}
	
	@Override
	protected Record getRecordFromNode(Node node) throws EParseException{
		try{
			Element elementName=(Element)this.parser.getNodeFromNode(node, "/h2/a");
			Element elementPrice=(Element)this.parser.getNodeFromNode(node, "/div[2]/div");
			Element elementPriceUsd=(Element)this.parser.getNodeFromNode(node, "/div[2]/div[2]");
			Element exists=(Element)this.parser.getNodeFromNode(node, "/div[2]/div[3]");
			if(exists.getTextContent().indexOf("Есть")<0)return null;
			String name=elementName.getTextContent().trim();
			String url=elementName.getAttribute("href");
			if((elementPrice==null)&&(elementPriceUsd==null))return null;
			Float price=this.getFloatFromString(elementPrice.getTextContent().replaceAll("[^0-9]", ""));
			Float priceUsd=this.getFloatFromString(elementPriceUsd.getTextContent().replaceAll("[^0-9^,]", ""));
			return new Record(name, null, url, price, priceUsd, null);
		}catch(NullPointerException ex){
			throw new EParseExceptionItIsNotRecord();
		}
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception{
		return new DirectFinder(this.parser.getNodeListFromUrl(this.getShopUrlStartPage(), 
															   this.getCharset(),
															   "/html/body/div/div/div/div/table/tbody/tr/td/div[2]/div/ul/li[*]/a"),
				 				new AnchorResourceExtractor(new IResourceSectionFactory() {
									@Override
									public NextSection getResourceSection(String name, String url) {
										return new UserSection(name, url);
									}
				 				}, this.getShopUrlStartPage()+"/")
		 );	

	}



/**
 */

}
