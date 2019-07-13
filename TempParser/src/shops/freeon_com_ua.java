package shops;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.ECharset;
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

public class freeon_com_ua extends ListBaseMultiPage{

	class UserSection extends NextSection{

		public UserSection(String name, String url) {
			super(name, url);
		}

		int pageCounter=0;
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if(pageCounter==1)return this.getUrl();
			// http://freeon.com.ua/ru32_s-verhnei-zagruzkoi.html
			// http://freeon.com.ua/ru32_2_s-verhnei-zagruzkoi.html
			int index=this.getUrl().indexOf('_');
			return this.getUrl().substring(0, index)+"_"+pageCounter+this.getUrl().substring(index);
		}
	}
	
	protected String getCharset() {
		return ECharset.WIN_1251.getName();
	};
	
	ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_REPEAT_LAST};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://freeon.com.ua";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table/tbody/tr/td[3]/form";
	}
	
	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/div[@class=\"page\"]/table/tbody/tr/";
	}

	@Override
	protected Record getRecordFromNode(Node node) throws EParseException {
		Element elementName=(Element)this.parser.getNodeFromNode(node, "/td[2]/a");
		if(elementName==null)throw new EParseExceptionItIsNotRecord();
		String name=elementName.getTextContent().trim();
		String url="ru"+this.removeStartPage(elementName.getAttribute("href"));
		Element elementPrice=(Element)this.parser.getNodeFromNode(node, "/td[3]/div/u/b");
		if(elementPrice==null)return null;
		Float price=this.getFloatFromString(elementPrice.getTextContent().replaceAll("[^0-9^.]", "").replaceAll(".$", ""));
		return new Record(name, null, url, price, null, null);
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new RecursiveFinder(this.parser.getNodeListFromUrl(this.getShopUrlStartPage(), 
																  getCharset(),
																  "/html/body/table/tbody/tr/td[3]/div/table/tbody/tr[*]/td[*]/table/tbody/tr/th/a" ),
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
						  				  "/html/body/table/tbody/tr/td[3]/div/div/a[*]"),
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
