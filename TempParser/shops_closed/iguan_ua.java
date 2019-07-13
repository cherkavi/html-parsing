package shops;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.multi_page.BaseMultiPage;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.RecursiveFinder;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.record.Record;

public class iguan_ua extends BaseMultiPage{

	@Override
	protected ArrayList<INextSection> getSection() {
		try{
			ISectionFinder finder=new RecursiveFinder(this.parser.getNodeListFromUrl(this.getShopUrlStartPage(), getCharset(),"/html/body/div/div/div[2]/div[2]/div[2]/div/table/tbody/tr/td[*]/a" ),
													  new IUrlFromElementExtractor() {
														@Override
														public String getUrlFromElement(Element element) {
															String url=element.getAttribute("href");
															if(isStringEmpty(url))return null;
															return url;
														}
													  },
													  
													  new NodeListFinderByUrl(this.parser, getCharset(), "/html/body/div/div[2]/div/div[2]/div/div[3]/div[@class=\"cell\"]/ul/li[*]/a"),
													  new IUrlFromElementExtractor() {
															@Override
															public String getUrlFromElement(Element element) {
																String url=element.getAttribute("href");
																if(isStringEmpty(url))return null;
																return url;
															}
														  },
													   new IResourceFromElementExtractor() {
															 @Override
															 public NextSection getResourceSection(Element element) {
																 return new UserSection(element.getTextContent(), element.getAttribute("href"));
															 }
														 }
													  );
			return finder.getSection();
		}catch(Exception ex){
			this.logger.error(this, "#getSection Exception:"+ex.getMessage());
			return null;
		}
	}

	
	class UserSection extends NextSection{

		public UserSection(String name, String url) {
			super(name, url);
		}

		int pageCounter=0;
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if(pageCounter==1)return this.getUrl();
			return this.getUrl()+"page="+pageCounter+"/";
		}
		
	}
	
	ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_LOAD_ERROR};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.iguan.ua";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/div/div[2]/div/div[2]/div/div[5]/div/table/tbody";
	}

	@Override
	protected ArrayList<Record> getRecordsFromBlock(Node node) {
		ArrayList<Record> returnValue=new ArrayList<Record>();
		ArrayList<Node> elementsList=this.parser.getNodeListFromNode(node, "/tr[@class=\"details\"]/td[*]/div");
		for(Node element : elementsList){
			try{
				Element elementName=null;
				Element elementPrice=null;
				Element elementPriceUsd=null;
				ArrayList<Node> divList=this.parser.getNodeListFromNode(element, "/div[*]");
				for(int counter=0;counter<divList.size();counter++){
					if(divList.get(counter) instanceof Element){
						String className=((Element)divList.get(counter)).getAttribute("class");
						if(className.equals("title")){
							elementName=(Element)this.parser.getNodeFromNode(divList.get(counter), "a");
						}
						if(className.equals("price")){
							elementPrice=(Element)this.parser.getNodeFromNode(divList.get(counter), "div/span");
							elementPriceUsd=(Element)this.parser.getNodeFromNode(divList.get(counter), "div/span[2]");
						}
					}
				}
				String name=elementName.getTextContent();
				// System.out.println("Name: "+name);
				String url=this.removeStartPage(elementName.getAttribute("href"));
				Float price=this.getFloatFromString(elementPrice.getTextContent().replaceAll("[^0-9]",""));
				Float priceUsd=this.getFloatFromString(elementPriceUsd.getTextContent().replaceAll("[^0-9]",""));
				returnValue.add( new Record(name, null, url, price, priceUsd, null));
			}catch(Exception ex){
				System.err.println("#readRecordsFromBlock Exception: "+ex.getMessage());
			}
		}
		return returnValue;
	}
}
