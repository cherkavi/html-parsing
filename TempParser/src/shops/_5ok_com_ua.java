package shops;

import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionRecordListEmpty;
import shop_list.html.parser.engine.exception.EParseExceptionRecordListLoadError;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.two_level_finder.TwoLevelFinder;
import shop_list.html.parser.engine.multi_page.standart.ListBaseMultiPage;
import shop_list.html.parser.engine.record.Record;

public class _5ok_com_ua extends ListBaseMultiPage{

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;
		private String preambula=null;
		
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// http://www.5ok.com.ua/Products_150_290_0_0_0_0_0_1.html
				// http://www.5ok.com.ua/Products_150_290_0_0_0_0_0_2.html
				// http://www.5ok.com.ua/Products_150_290_0_0_0_0_0_3.html
				if(preambula==null){
					int index=this.getUrl().lastIndexOf('_');
					this.preambula=this.getUrl().substring(0,index+1);
				}
				return preambula+pageCounter+".html";
			}
			return this.getUrl();
		}
	}
	
	protected String getCharset() {
		return ECharset.UTF_8.getName();
	};
	
	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_REPEAT_LAST,
			ESectionEnd.NEXT_RECORDS_SHOW_FIRST };
	
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.5ok.com.ua";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/FORM[@id=\"aspnetForm\"]/DIV[@id=\"container\"]/DIV[@class=\"container\"]/DIV[@id=\"content\"]/DIV[@id=\"catalog\"]/TABLE/TBODY/TR[1]/TD[@id=\"items\"]/DIV/DIV[@id=\"catalog-items\"]";
	}
	
	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/div[*]";
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
			
			try{
				Element elementName=(Element)this.parser.getNodeFromNode(currentNode, "/a");
				Element elementUrl=(Element)this.parser.getNodeFromNode(currentNode, "/a");
				Element elementPrice=(Element)this.parser.getNodeFromNode(currentNode2, "/table/tbody/tr[2]/td");
				//System.out.println(elementName.getTextContent());
				if(elementName!=null&&elementPrice!=null&&elementUrl!=null){
					Float priceUsd=this.getFloatFromString(elementPrice.getTextContent().trim().replaceAll("[^0-9]", ""));
					if(priceUsd==null)continue;
					returnValue.add(new Record(elementName.getTextContent().trim(), 
											   null, 
											   elementUrl.getAttribute("href"),
											   priceUsd,
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

	// removeNodeFromListByTextContent(listOfNode, new
	// String[]{"Банковское оборудование", "Автомобильные товары"});
	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		ArrayList<Node> list=this.parser
		.getNodeListFromUrl(
				"http://www.5ok.com.ua",
				"utf-8",
				"/html/body/form/div[@id=\"container\"][5]/div[@class=\"container\"]/div[@id=\"leftcol\"]/div[@class=\"left-menu\"][*]/ul[@class=\"left-menu\"]/li[*]/a");
		removeNodeWithRepeatAttributes(list, "href");
		return new TwoLevelFinder(list,
				new IUrlFromElementExtractor() {
					@Override
					public String getUrlFromElement(Element element) {
						return addHttpPreambula("http://www.5ok.com.ua/",
								element.getAttribute("href"));
					}
				},
				new NodeListFinderByUrl(
						this.parser,
						this.getCharset(),
						"/html/body/form/div[@id=\"container\"]/div[@class=\"container\"]/div[@id=\"content\"]/div[@class=\"blocks catalog\"]/div[@class=\"block tech\"]/ul/li[*]/a"),
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								addHttpPreambula("http://www.5ok.com.ua/",
										element.getAttribute("href")));
					}
				});
	}

}
