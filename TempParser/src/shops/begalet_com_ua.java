package shops;

import java.util.ArrayList;



import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionRecordListEmpty;
import shop_list.html.parser.engine.exception.EParseExceptionRecordListLoadError;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.RecursiveFinder;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.ListBaseMultiPage;
import shop_list.html.parser.engine.record.Record;

public class begalet_com_ua extends ListBaseMultiPage{

	class UserSection extends NextSection{

		public UserSection(String name, String url) {
			super(name, url);
		}

		int pageCounter=0;
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if(pageCounter==1)return this.getUrl();
			if(pageCounter==2){
				try{
					String urlToSecondPage=((Element)parser.getNodeFromUrl(this.getUrl(), getCharset(), "/html/body/div/div[3]/div[5]/div/div[2]/a")).getAttribute("href");
					int indexBegin=urlToSecondPage.indexOf('?');
					int indexEnd=urlToSecondPage.lastIndexOf('=');
					this.setUrl(this.getUrl()+urlToSecondPage.substring(indexBegin, indexEnd));
				}catch(Exception ex){
					logger.debug(begalet_com_ua.this, "#getUrlToNextPage Exception:"+ex.getMessage());
					return null;
				}
			}
			return this.getUrl()+"="+pageCounter;
		}
	}
	
	protected String getCharset() {
		return ECharset.WIN_1251.getName();
	};
	
	ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_SHOW_FIRST};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.begalet.com.ua";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/div/div[3]/div[5]/table/tbody";
	}
	
	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/tr[*]";
	}

	
	@Override
	protected ArrayList<Record> getRecordsFromBlock(Node node) throws EParseException{
		ArrayList<Record> returnValue=new ArrayList<Record>();
		ArrayList<Node> nodeList=null;
		try{
			nodeList=this.parser.getNodeListFromNode(node,getXmlPathToRecordListFromDataBlock());
		}catch(Exception ex){
			throw new EParseExceptionRecordListLoadError();
		}
		if((nodeList==null))throw new EParseExceptionRecordListLoadError();
		if(nodeList.size()==0) throw new EParseExceptionRecordListEmpty();
		boolean anyDataExists=false;
		for(int index=getFirstPositionInRecordsBlock();index<nodeList.size();index+=3){
			Node currentNode=nodeList.get(index);
			Node currentNode2=nodeList.get(index+1);
			for(int counter=1; counter<=3;counter++){
				try{
					Element elementName=(Element)this.parser.getNodeFromNode(currentNode, "/td["+counter+"]/table/tbody/tr[2]/td/a");
					//System.out.println(elementName.getTextContent());
					if(elementName==null)break;
					anyDataExists=true;
					Element elementPrice=(Element)this.parser.getNodeFromNode(currentNode2, "/td["+counter+"]/span/b");
					Float price=this.getFloatFromString(elementPrice.getTextContent().trim());
					returnValue.add(new Record(elementName.getTextContent().trim(), null, this.removeStartPage(elementName.getAttribute("href")),price,null, null ));
				}catch(Exception ex){
					this.logger.debug(this, "next record find Exception, maybe end of page: "+ex.getMessage());
				}
			}
		}
		// если все записи не принадлежали к списку 
		if(anyDataExists==false)throw new EParseExceptionRecordListEmpty();
		return returnValue;
	}
	
	@Override
	protected Record getRecordFromNode(Node node) throws EParseException {
		System.err.println("this method never called in this object");
		return null;
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		/*
		return new TestStubFinder(new UserSection("test", "http://www.begalet.com.ua/notebook/notebooks/"));
		*/
		return new RecursiveFinder(this.parser.getNodeListFromUrl(this.getShopUrlStartPage(), 
																  getCharset(),
																  "/html/body/div/div[2]/div[3]/ul/li[*]/a" ),
				  new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url=element.getAttribute("href");
						if(isStringEmpty(url))return null;
						return getShopUrlStartPage()+url;
					}
				  },
				  
				  new NodeListFinderByUrl(this.parser, 
						  				  getCharset(), 
						  				  "/html/body/div/div[3]/div[3]/div/ul/li[*]/a"),
				  new IUrlFromElementExtractor() {
						@Override
						public String getUrlFromElement(Element element) {
							String url=element.getAttribute("href");
							if(isStringEmpty(url))return null;
							return getShopUrlStartPage()+url;
						}
					  },
				   new IResourceFromElementExtractor() {
						 @Override
						 public NextSection getResourceSection(Element element) {
							 return new UserSection(element.getTextContent(), getShopUrlStartPage()+element.getAttribute("href"));
						 }
					 }
				  );
	}

}
