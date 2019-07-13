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

public class duhovka_kiev_ua extends ListBaseMultiPage{

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
				return this.getUrl()+"?from_goods="+(pageCounter*60);
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
		return "http://www.duhovka.kiev.ua";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/TABLE/TBODY/TR[1]/TD[3]/TABLE/TBODY/TR[4]/TD[@id=\"canvas\"]/TABLE[@class=\"cataloggoods\"]/tbody/";
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
		// получить порции по три элемента 
		for(int index=getFirstPositionInRecordsBlock();index<nodeList.size();index+=3){
			Node currentNode=nodeList.get(index);
			Node currentNode2=nodeList.get(index+1);
			// Node currentNode3=nodeList.get(index+2);
			for(int counter=1; counter<=3;counter++){
				try{
					Element elementName=(Element)this.parser.getNodeFromNode(currentNode, "/td[2]/h2");
					Element elementUrl=(Element)this.parser.getNodeFromNode(currentNode2, "/td[2]/a");
					Element elementPrice=(Element)this.parser.getNodeFromNode(currentNode2, "/td[1]/span");
					//System.out.println(elementName.getTextContent());
					if(elementName!=null&&elementPrice!=null&&elementUrl!=null){
						Float priceUsd=this.getFloatFromString(elementPrice.getTextContent().trim());
						if(priceUsd==null)continue;
						returnValue.add(new Record(elementName.getTextContent().trim(), 
												   null, 
												   elementUrl.getAttribute("href"),
												   null,
												   priceUsd, 
												   null ));
						anyDataExists=true;
					}else{
						continue;
					}
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
																  "/HTML/BODY/TABLE/TBODY/TR[1]/TD[3]/TABLE/TBODY/TR[2]/TD/TABLE/TBODY/TR[2]/TD/UL[@id=\"udm\"]/LI[*]/DIV/A" ),
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
						  				  "/HTML/BODY/TABLE/TBODY/TR[1]/TD[3]/TABLE/TBODY/TR[4]/TD[@id=\"canvas\"]/UL[@class=\"catalognodes\"]/LI[*]/A"),
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
