package shops;

import org.w3c.dom.Element;

import org.w3c.dom.Node;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionItIsNotRecord;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.DirectFinder;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.AnchorResourceExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceSectionFactory;
import shop_list.html.parser.engine.multi_page.standart.ListBaseMultiPage;
import shop_list.html.parser.engine.record.Record;

public class tehnotrade_com_ua extends ListBaseMultiPage{

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new DirectFinder(this.parser.getNodeListFromUrl("http://tehnotrade.com.ua/sitemap", getCharset(), "/html/body/div/div/div[8]/div/div/div[2]/table/tbody/tr[*]/td/div[@class=\"comm\"]/a"),
							    new AnchorResourceExtractor(new IResourceSectionFactory(){
									@Override
									public NextSection getResourceSection(String name, String url) {
										return new UserSection(name,getShopUrlStartPage()+"/"+url);
									}
							    	
							    })
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
			if(pageCounter>1){
				return this.getUrl()+(pageCounter)+"/";
			}
			return this.getUrl();
		}
		
	}

	ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_ZERO_SIZE, ESectionEnd.NEXT_RECORDS_REPEAT_LAST, ESectionEnd.NEXT_RECORDS_SHOW_FIRST};
	
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://tehnotrade.com.ua";
	}
	
	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/div/div/div[8]/div/div/div[2]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/div[@id=\"tm\"]";
	}

	@Override
	protected Record getRecordFromNode(Node node) throws EParseException {
		Element elementName=(Element)this.parser.getNodeFromNode(node, "/h1/a");
		if(elementName==null)throw new EParseExceptionItIsNotRecord();
		String name=elementName.getTextContent().trim();
		String url=elementName.getAttribute("href");
		Element elementPriceUsd=(Element)this.parser.getNodeFromNode(node, "div/table/tbody/tr/td[2]/b");
		if(elementPriceUsd==null)return null;
		Float price=this.getFloatFromString(elementPriceUsd.getTextContent().replaceAll("[^0-9^.]", ""));
		return new Record(name, null, url, price, null, null);
	}

}
