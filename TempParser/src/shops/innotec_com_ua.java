package shops;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.multi_page.BaseMultiPage;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.DirectFinder;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.record.Record;

public class innotec_com_ua extends BaseMultiPage{

	@Override
	protected ArrayList<INextSection> getSection() {
		try{
			DirectFinder finder=new DirectFinder(this.parser.getNodeListFromUrl("http://innotec.com.ua", this.getCharset(), "/html/body/center/div[2]/div/div/table/tbody/tr[2]/td/table/tbody/tr[*]/td[2]/a"),
												 new IResourceFromElementExtractor() {
													@Override
													public NextSection getResourceSection(Element element) {
														return new UserResource(element.getTextContent(), element.getAttribute("href"));
													}
												}
												);
			return finder.getSection();
		}catch(Exception ex){
			return null;
		}
	}
	
	class UserResource extends NextSection{

		public UserResource(String name, String url) {
			super(name, url);
		}
		private int pageCounter=0;
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if(pageCounter==1)return this.getUrl();
			int index=this.getUrl().lastIndexOf("/");
			return this.getUrl().substring(0,index)+"/p"+(pageCounter-1)+this.getUrl().substring(index);
		}
		
	}
	
	@Override
	protected String getCharset() {
		return ECharset.WIN_1251.getName();
	}

	private ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_ZERO_SIZE}; 
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://innotec.com.ua";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/center/div[2]/div/div/table/tbody/tr[2]/td";
	}

	@Override
	protected ArrayList<Record> getRecordsFromBlock(Node node) {
		try{
			ArrayList<Record> returnValue=new ArrayList<Record>();
			ArrayList<Node> list=this.parser.getNodeListFromNode(node, "/table[*]/tbody/tr/td[2]/table/tbody/tr");
			for(Node currentNode:list){
				try{
					Element elementName=(Element)this.parser.getNodeFromNode(currentNode, "/td/h4/a");
					Element elementPriceUsd=(Element)this.parser.getNodeFromNode(currentNode, "/td[2]/table/tbody/tr/td/font/b");
					Element elementPrice=(Element)this.parser.getNodeFromNode(currentNode, "/td[2]/table/tbody/tr[2]/td/b");
					String name=elementName.getTextContent();
					String url=this.removeStartPage(elementName.getAttribute("href"));
					Float price=this.getFloatFromString(elementPrice.getTextContent());
					Float priceUsd=this.getFloatFromString(elementPriceUsd.getTextContent().replaceAll("[^0-9]", ""));
					returnValue.add(new Record(name, null, url, price, priceUsd, null));
				}catch(Exception ex){
				}
			}
			return returnValue;
		}catch(Exception ex){
			System.err.println("#getRecordsFromBlock Exception:"+ex.getMessage());
			return null;
		}
	}
}
