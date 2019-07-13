package shops;

import java.util.ArrayList;


import org.w3c.dom.Element;

import org.w3c.dom.Node;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionItIsNotRecord;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.standart.ListBaseMultiPage;
import shop_list.html.parser.engine.record.Record;

public class citrus_ua extends ListBaseMultiPage{

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new ISectionFinder(){
			
			private ArrayList<Node> getNodeFromBranch(Node node, String xpath){
				try{
					return parser.getNodeListFromNode(node, xpath);
				}catch(Exception ex){
					return null;
				}
			}
			
			@Override
			public ArrayList<INextSection> getSection() {
				try{
					ArrayList<Node> elements=parser.getNodeListFromUrl(getShopUrlStartPage(), 
																	   getCharset(), 
																	   "/html/body/div[3]/div/ul/li/ul/li[*]");
					if((elements!=null)&&(elements.size()>0)){
						ArrayList<INextSection> returnValue=new ArrayList<INextSection>();
						for(int counter=0;counter<elements.size();counter++){
							ArrayList<Node> subList=getNodeFromBranch(elements.get(counter), "/ul/li[*]/a");
							if(subList!=null&&subList.size()>0){
								if(subList.get(0).getTextContent().trim().equalsIgnoreCase("Все товары")){
									addSectionIfExists(returnValue, subList.get(0));
								}else{
									for(int index=0;index<subList.size();index++){
										addSectionIfExists(returnValue, subList.get(index));
									}
								}
								
							}else{
								logger.warn(citrus_ua.this, "#getSection Check xpath for main section");
							}
						}
						return returnValue;
					}else{
						return null;
					}
				}catch(Exception ex){
					return null;
				}
			}

			private void addSectionIfExists(ArrayList<INextSection> returnValue, Node node) {
				try{
					String name=node.getTextContent();
					String url=((Element)node).getAttribute("href");
					if(url!=null&&(!url.equals(""))){
						returnValue.add(new UserSection(name, getShopUrlStartPage()+url));
					}
				}catch(Exception ex){
					logger.error(citrus_ua.this, "#getSection from Node Exception:"+ex.getMessage());
				};
				
			}
		};
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
				return this.getUrl()+"?PAGEN_1="+pageCounter;
			}
			return this.getUrl();
		}
		
	}

	ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_SHOW_FIRST,ESectionEnd.NEXT_RECORDS_REPEAT_LAST};
	
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.citrus.ua";
	}
	
	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/div[3]/div[3]/div[7]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/div[@class=\"catalog-top-element\"]";
	}

	@Override
	protected Record getRecordFromNode(Node node) throws EParseException {
		try{
			Element elementName=(Element)this.parser.getNodeFromNode(node, "/h4/a");
			String name=elementName.getTextContent().trim();
			String url=this.removeStartPage(elementName.getAttribute("href"));
			Element elementPrice=(Element)this.parser.getNodeFromNode(node, "/p/span");
			if(elementPrice==null)return null;
			//System.out.println(elementPrice.getTextContent());
			//System.out.println(elementPrice.getTextContent().replaceAll("[^0-9^.]", ""));
			Float price=this.getFloatFromString(elementPrice.getTextContent().replaceAll("[^0-9^.]", "").replaceAll(".$", ""));
			return new Record(name, null, url, price, null, null);
		}catch(NullPointerException npe){
			throw new EParseExceptionItIsNotRecord();
		}
	}

	@Override
	protected String getCharset() {
		return ECharset.WIN_1251.getName();
	}
}
