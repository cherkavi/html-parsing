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

public class e_m_kiev_ua extends ListBaseMultiPage{

	class UserSection extends NextSection{

		public UserSection(String name, String url) {
			super(name, url);
		}

		int pageCounter=0;
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if(pageCounter==1)return this.getUrl();
			// http://www.e-m.kiev.ua/?rid=950
			// http://www.e-m.kiev.ua/?rid=950&offset=16
			return this.getUrl()+"&offset="+(pageCounter-1)*16;
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
		return "http://www.e-m.kiev.ua";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table[6]/tbody/tr/td[3]/table/tbody/tr/td/font[2]/table/tbody";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/tr[*]/td[*]/table/tbody/tr/td[2]/table/tbody";
	}

	@Override
	protected Record getRecordFromNode(Node node) throws EParseException {
		Element elementName=(Element)this.parser.getNodeFromNode(node, "/tr/td/table/tbody/tr/td/a");
		Element elementPrice=(Element)this.parser.getNodeFromNode(node,    "/tr[2]/td/b/font");
		Element elementPriceUsd=(Element)this.parser.getNodeFromNode(node, "/tr[2]/td/font");

		if(elementName==null)throw new EParseExceptionItIsNotRecord();
		String name=elementName.getTextContent().trim();
		String url=this.removeStartPage(elementName.getAttribute("href"));

		if((elementPrice==null)&&(elementPriceUsd==null))return null;
		Float price=null;
		if(elementPrice!=null){
			price=this.getFloatFromString(elementPrice.getTextContent().replaceAll("[^0-9]", ""));
		}
		Float priceUsd=null;
		if(elementPriceUsd!=null){
			priceUsd=this.getFloatFromString(elementPriceUsd.getTextContent().replaceAll("[^0-9]", ""));
		}
		return new Record(name, null, url, price, priceUsd, null);
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		// return new TestStubFinder(new UserSection("test","http://www.e-m.kiev.ua/?rid=1836"));
		return new RecursiveFinder(this.parser.getNodeListFromUrl(this.getShopUrlStartPage(), 
																  getCharset(),
																  "/html/body/table[4]/tbody/tr/td/table/tbody/tr[2]/td[@valign=\"top\"]/div/a"),
								  new IUrlFromElementExtractor() {
									@Override
									public String getUrlFromElement(Element element) {
										String url=element.getAttribute("href");
										if(isStringEmpty(url))return null;
										return getShopUrlStartPage()+"/"+url;
									}
								  },
								  
								  new NodeListFinderByUrl(this.parser, 
										  				  getCharset(), 
										  				  "/html/body/table[6]/tbody/tr/td[3]/table/tbody/tr/td/font/table[2]/tbody/tr/td[*]/table/tbody/tr[*]/td/a"),
								  new IUrlFromElementExtractor() {
										@Override
										public String getUrlFromElement(Element element) {
											String url=element.getAttribute("href");
											if(isStringEmpty(url))return null;
											return getShopUrlStartPage()+"/"+url;
										}
									  },
									  
								   new IResourceFromElementExtractor() {
										 @Override
										 public NextSection getResourceSection(Element element) {
											 return new UserSection(element.getTextContent(), getShopUrlStartPage()+"/"+element.getAttribute("href"));
										 }
									 }
								  );
	}

}
