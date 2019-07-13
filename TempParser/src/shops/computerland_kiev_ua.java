package shops;

import org.w3c.dom.Element;

import org.w3c.dom.Node;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionItIsNotRecord;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.RecursiveFinder;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.standart.ListBaseMultiPage;
import shop_list.html.parser.engine.record.Record;

public class computerland_kiev_ua extends ListBaseMultiPage{

	class UserSection extends NextSection{

		public UserSection(String name, String url) {
			super(name, url);
		}

		int pageCounter=0;
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if(pageCounter==1)return this.getUrl();
			// http://podarki.computerland.kiev.ua/robots/
			// http://podarki.computerland.kiev.ua/robots/?inpbrands=&priceFrom=&priceTo=&ipp=25&page=2&oo=
			return this.getUrl()+"?inpbrands=&priceFrom=&priceTo=&ipp=25&page="+pageCounter+"&oo=";
		}
	}
	
	ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_ZERO_SIZE, ESectionEnd.NEXT_RECORDS_REPEAT_LAST, ESectionEnd.NEXT_RECORDS_SHOW_FIRST};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.computerland.kiev.ua";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/div/div/div[2]/div[2]";
	}
	
	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/div[@class=\"item-list\"]";
	}

	@Override
	protected Record getRecordFromNode(Node node) throws EParseException {
		// this.debugPrintNode("c:\\out.tmp", node);
		Element elementName=(Element)this.parser.getNodeFromNode(node, "/h3/a");
		if(elementName==null)throw new EParseExceptionItIsNotRecord();
		String name=elementName.getTextContent().trim();
		String url=this.removeStartPage(elementName.getAttribute("href"));
		
		Node exists=this.parser.getNodeFromNode(node, "/div[2]/span[3]/strong");
		if(exists==null)return null;
		if(exists.getTextContent().indexOf("на складе")<0)return null;
		
		Element elementPrice=(Element)this.parser.getNodeFromNode(node, "/h3/span");
		if(elementPrice==null)return null;
		String tempText=elementPrice.getTextContent();
		int delimeterIndex=tempText.indexOf('/');
		String priceUsdText=tempText.substring(0, delimeterIndex);
		String priceText=tempText.substring(delimeterIndex+1);
		Float price=this.getFloatFromString(priceText.replaceAll("[^0-9]", ""));
		Float priceUsd=this.getFloatFromString(priceUsdText.replaceAll("[^0-9]", ""));
		return new Record(name, null, url, priceUsd, price, null);
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		// FIXME
		return new RecursiveFinder(this.parser.getNodeListFromUrl(this.getShopUrlStartPage(), 
																  getCharset(),
																  "/html/body/div/div/div[2]/div/ul/li[*]/a" ),
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
						  				  "/html/body/div/div/div[2]/div[2]/div[@class=\"entry\"]/h3/a"),
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
							 return new UserSection(element.getTextContent().trim(), element.getAttribute("href"));
						 }
					 }
				  );
	}

	@Override
	protected int getWatchDogEmptyPageLimit() {
		return 7;
	}
	
	@Override
	protected boolean getWatchDogEmptyPageLimitShowError() {
		return false;
	}
}
