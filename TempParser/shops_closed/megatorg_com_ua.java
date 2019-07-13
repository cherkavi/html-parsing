package shops;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.ECharset;
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

public class megatorg_com_ua extends ListBaseMultiPage{

	@Override
	protected String getCharset() {
		return ECharset.WIN_1251.getName();
	}
	
	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new DirectFinder(this.parser.getNodeListFromUrl(this.getShopUrlStartPage(), this.getCharset(), "/html/body/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[*]/td[2]/a"),
																new AnchorResourceExtractor(new IResourceSectionFactory() {
																	@Override
																	public NextSection getResourceSection(String name, String url) {
																		return new UserSection(name, getShopUrlStartPage()+"/"+url);
																	}
																})								
														);
	}
	
	class UserSection extends NextSection{

		public UserSection(String name, String url) {
			super(name, url);
		}

		private int counter;
		@Override
		public String getUrlToNextPage() {
			counter++;
			if(counter>1)return this.getUrl()+"&offset="+(counter-1)*12;
			return this.getUrl();
		}
		
	}

	private ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_PAGE_LOAD_ERROR};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://megatorg.com.ua";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/tr[*]/td[*]";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/p/table/tbody/tr[3]/td/table/tbody";
	}

	@Override
	protected Record getRecordFromNode(Node node) throws EParseException {
		try{
			Element nameElement=null;
			Node priceText=null;
			if(this.parser.getChildElementCount(node, "p")>1){
				nameElement=(Element)this.parser.getNodeFromNode(node, "/p[2]/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr/td/p/a");
				priceText=this.parser.getNodeFromNode(node, "/p[2]/table/tbody/tr/td[2]/table/tbody/tr[2]/td/b/span");
			}else{
				nameElement=(Element)this.parser.getNodeFromNode(node, "/p/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr/td/p/a");
				priceText=this.parser.getNodeFromNode(node, "/p/table/tbody/tr/td[2]/table/tbody/tr[2]/td/b/span");
			}
			
			String name=nameElement.getTextContent();
			String url=nameElement.getAttribute("href");
			if(priceText==null)return null;
			int index=priceText.getTextContent().indexOf('(');
			Float priceUsd=this.getFloatFromString(priceText.getTextContent().substring(0,index).replaceAll("[^0-9^.]", ""));
			Float price=this.getFloatFromString(priceText.getTextContent().substring(index).replaceAll("[^0-9^.]","").replaceAll(".$",""));
			return new Record(name, null, url, price, priceUsd, null);
		}catch(NullPointerException npe){
			throw new EParseExceptionItIsNotRecord();
		}
	}


}
