package shops;

import java.util.ArrayList;

import org.w3c.dom.Element;

import shop_list.html.parser.engine.multi_page.BaseMultiPage;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.ResourceSection;
import shop_list.html.parser.engine.multi_page.section_finder.DirectFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IResourceFromElementExtractor;
import shop_list.html.parser.engine.record.Record;

public class copoka_com_ua extends BaseMultiPage{

	@Override
	protected ArrayList<ResourceSection> getSection() {
		try{
			DirectFinder finder=new DirectFinder(this.parser.getNodeListFromUrl(getShopUrlStartPage(), getCharset(), "/html/body/table/tbody/tr/td/table[2]/tbody/tr/td/span[3]/div/div/div[2]/div[@class=\"clip\"]/div[*]/a"),
												 new IResourceFromElementExtractor() {
													@Override
													public ResourceSection getResourceSection(Element element) {
														if(element==null)return null; 
														return new UserSection(element.getTextContent(), removeStartPage(element.getAttribute("href")));
													}
												});
			return finder.getSection();
		}catch(Exception ex){
			return null;
		}
	}
	
	class UserSection extends ResourceSection{

		public UserSection(String name, String url) {
			super(name, url);
		}

		int counter=0;
		@Override
		public String getUrlToNextPage() {
			counter=0;
			if(counter>1){
				return this.getUrl()+"/"+(counter-1)*10;
			}else{
				return this.getUrl();
			}
			
		}
		
	}

	private ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_ZERO_SIZE};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://copoka.com.ua";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table/tbody/tr/td/table[2]/tbody/tr/td[2]/span";
	}
	
	@Override
	protected String getRecordTagNameInBlock() {
		return "table";
	}

	@Override
	protected Record getRecordFromElement(Element element) throws Exception {
		try{
			Element nameElement=(Element)this.parser.getNodeFromNode(element, "/tbody/tr/td/a");
			Element existsElement=(Element)this.parser.getNodeFromNode(element, "/tbody/tr[3]/td/table/tbody/tr[4]/td[2]/span");
			Element priceElement=(Element)this.parser.getNodeFromNode(element, "/tbody/tr[3]/td/table/tbody/tr[5]/td[2]");
			if((nameElement==null)||(existsElement==null)||(priceElement==null))return null;
			if(existsElement.getTextContent().indexOf("≈—“‹")<0)return null;
			return new Record(nameElement.getTextContent(), null, this.removeStartPage(nameElement.getAttribute("href")), this.getFloatFromString(priceElement.getTextContent().replaceAll("[^0-9^.]", "")), null, null);
		}catch(Exception ex){
			return null;
		}
	}
}
