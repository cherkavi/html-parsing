package shops;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

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

public class newbest_org_ua extends ListBaseMultiPage{

	class UserSection extends NextSection{

		public UserSection(String name, String url) {
			super(name, url);
		}

		int pageCounter=0;
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if(pageCounter==1)return this.getUrl();
			return null;
		}
	}
	
	ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_PAGE_LOAD_ERROR};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://newbest.org.ua";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/body/BODY/TABLE/TBODY/TR[1]/TD[2]/TABLE[3]/TBODY/TR/TD[2]/TABLE/TBODY/TR[1]/TD[2]/TABLE[2]/TBODY/TR/TD/TABLE/TBODY";
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
			try{
				Element elementName=(Element)this.parser.getNodeFromNode(currentNode, "/td[2]/a/strong");
				Element elementUrl=(Element)this.parser.getNodeFromNode(currentNode, "/td[2]/a");
				Element elementPrice=(Element)this.parser.getNodeFromNode(currentNode2, "/td[3]/nobr/span");
				// System.out.println(elementName.getTextContent());
				// System.out.println(elementPrice.getTextContent());
				if(elementName!=null&&elementPrice!=null&&elementUrl!=null){
					Float price=this.getFloatFromString(elementPrice.getTextContent().trim().replaceAll("[^0-9]", ""));
					if(price==null)continue;
					returnValue.add(new Record(elementName.getTextContent().trim(), 
											   null, 
											   this.removeStartPage(elementUrl.getAttribute("href")),
											   price,
											   null, 
											   null ));
					anyDataExists=true;
				}else{
					continue;
				}
			}catch(Exception ex){
				this.logger.debug(this, "next record find Exception, maybe end of page: "+ex.getMessage());
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
		// return new TestStubFinder(new UserSection("test", "http://newbest.org.ua/index.php?cat=229"));
		return new RecursiveFinder(this.parser.getNodeListFromUrl(this.getShopUrlStartPage(), 
																  getCharset(),
																  "/html/body/body/table[@width=\"100%\"]/tbody/tr/td[2]/table[3]/tbody/tr/td[2]/table[@width=\"100%\"]/tbody/tr/td[@class=\"lefttd\"]/div[2]/ul[@class=\"menuleft\"]/b[*]/a" ),
				  new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						String url=element.getAttribute("href");
						if(isStringEmpty(url))return null;
						return url;
					}
				  },
				  
				  new NodeListFinderByUrl(this.parser, 
						  				  getCharset(), 
						  				  "/HTML/body/BODY/TABLE/TBODY/TR[1]/TD[2]/TABLE[3]/TBODY/TR/TD[2]/TABLE/TBODY/TR[1]/TD[2]/TABLE/TBODY/TR/TD[@class=\"main\"]/TABLE[2]/TBODY/TR[*]/TD[@class=\"main\"]/TABLE/TBODY/TR[2]/TD/STRONG/A"),
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
	}

}
