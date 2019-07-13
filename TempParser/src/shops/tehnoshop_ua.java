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
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.ListBaseMultiPage;
import shop_list.html.parser.engine.record.Record;

public class tehnoshop_ua extends ListBaseMultiPage{

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new DirectFinder(this.parser.getNodeListFromUrl(getShopUrlStartPage(), getCharset(), "/HTML/BODY/DIV[@class=\"header\"]/DIV[@class=\"goods-menu\"]/UL[@class=\"row one-sixth\"]/LI[@class=\"cell\"]/DIV[@class=\"container\"]/UL/LI[*]"),
								new IResourceFromElementExtractor() {
									@Override
									public NextSection getResourceSection(Element element) {
										if(isStringEmpty(element.getAttribute("class"))){
											Element anchor=(Element)parser.getNodeFromNode(element, "/a");
											if(anchor==null)return null;
											String name=anchor.getTextContent();
											String url=getShopUrlStartPage()+removeStartPage(anchor.getAttribute("href"));
											return new UserSection(name, url);
										}else{
											return null;
										}
									}
								}
						       );
	}
	
	class UserSection extends NextSection{

		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter=0;
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if(pageCounter>1)return this.getUrl()+"page="+(pageCounter)+"/";
			return this.getUrl();
		}
		
	}

	ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_PAGE_LOAD_ERROR, ESectionEnd.NEXT_RECORDS_REPEAT_LAST, ESectionEnd.NEXT_RECORDS_SHOW_FIRST};

	@Override
	protected int getWatchDogEmptyPageLimit() {
		return 5;
	}
	
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.tehnoshop.ua";
	}
	
	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/DIV[@class=\"content\"]/DIV[@class=\"catalog\"]/DIV[@class=\"column-right\"]/DIV[@class=\"container\"]/TABLE[@class=\"tierce goods\"]/TBODY";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/TR[@class=\"details\"]/TD[*]/DIV[@class=\"container\"]";
	}

	@Override
	protected Record getRecordFromNode(Node node) throws EParseException {
		Element elementName=this.getElementByAttributeName(node, "/div[@class=\"title\"]/a");
		if(elementName==null)throw new EParseExceptionItIsNotRecord();
		String name=null;
		try{
			name=((Element)(this.parser.getNodeFromNode(elementName, "/span"))).getTextContent().trim();
		}catch(Exception ex){};
		String url=elementName.getAttribute("href");		
		Element priceUsdElement=this.getElementByAttributeName(node, "/div[@class=\"price\"]/div/b");
		if(priceUsdElement==null)return null;

		Float priceUsd=this.getFloatFromString(priceUsdElement.getTextContent().replaceAll("[^0-9]", ""));
		return new Record(name, null, url, null, priceUsd, null);
	}

	@Override
	protected String getCharset() {
		return ECharset.UTF_8.getName();
	}
}
