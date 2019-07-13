package shops;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import shop_list.html.parser.engine.multi_page.BaseMultiPage;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.record.Record;

public class _5_star_com_ua extends BaseMultiPage{
	private ESectionEnd[] sectionEndCondition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_REPEAT_LAST};

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return sectionEndCondition;
	}

	class UserSectionElement extends NextSection{
		private int counter=0;
		public UserSectionElement(String name, String url) {
			super(name, url);
		}

		@Override
		public String getUrlToNextPage() {
			counter++;
			if(counter>1){
				return this.getUrl()+"offset"+(counter-1)*8;
			}else{
				return this.getUrl();
			}
			
		}
		
	}
	
	@Override
	public String getShopUrlStartPage() {
		return "http://www.5star.com.ua";
	}


	@Override
	protected ArrayList<INextSection> getSection() {
		/*return new ArrayList<ResourceSection>(){
			{
				this.add(new UserSectionElement("temp", "http://www.590.com.ua/catalog/cat/25"));
			}
		};*/
		try{
			ArrayList<Node> mainSectionElement=this.parser.getNodeListFromUrl(this.getShopUrlStartPage(), this.getCharset(), "/html/body/body/table[3]/tbody/tr/td[2]/div/div[2]/ul/li[@class=\"parent\"]");
			ArrayList<INextSection> returnValue=new ArrayList<INextSection>();
			for(int index=0;index<mainSectionElement.size();index++){
				Element elementA=(Element)this.parser.getNodeFromNode(mainSectionElement.get(index), "/a");
				if(elementA!=null){
					if(elementA.getTextContent().indexOf("Установка")<0)
					returnValue.add(new UserSectionElement(elementA.getTextContent(), this.getShopUrlStartPage()+elementA.getAttribute("href")));
				}else{
					this.logger.error(this, "#getSection get from TAG A - not found");
				}
			}
			return returnValue;
		}catch(Exception ex){
			return null;
			
		}
	}
	
	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/body/table[3]/tbody/tr/td[3]/div/center/table/tbody";
	}

	// protected readRecordsFromBlock(Node node)
	@Override
	protected ArrayList<Record> getRecordsFromBlock(Node node) {
		ArrayList<Node> listOfNode=this.parser.getNodeListFromNode(node, "/tr[*]/td[@valign=\"top\"]");
		ArrayList<Record> returnValue=new ArrayList<Record>();
		for(int index=0;index<listOfNode.size();index++){
			try{
				Element form=(Element)this.parser.getNodeFromNode(listOfNode.get(index), "/form");
				NodeList list=form.getChildNodes();
				String name=null;
				String url=null;
				Float price=null;
				for(int counter=0;counter<list.getLength();counter++){
					if(list.item(counter) instanceof Element){
						Element element=(Element)list.item(counter);
						if(element.getTagName().equals("div")&&element.getAttribute("class").equals("prdbrief_name")){
							Element elementA=(Element)this.parser.getNodeFromNode(element, "/a");
							name=elementA.getTextContent();
							url=elementA.getAttribute("href");
						}
						if(element.getTagName().equals("div")&&element.getAttribute("class").equals("prdbrief_price")){
							Element elementSpan=(Element)this.parser.getNodeFromNode(element, "/span");
							price=this.getFloatFromString(elementSpan.getTextContent().replaceAll("[^0-9]", ""));
						}
					}
				}
				if((name!=null)&&(url!=null)&&(price!=null)){
					returnValue.add(new Record(name, null, url, price, null, null));
				}
			}catch(Exception ex){
				logger.error(this, "#readRecordFromBlock "+index+" Exception:"+ex.getMessage());
			}
		}
		return returnValue;
	}
}