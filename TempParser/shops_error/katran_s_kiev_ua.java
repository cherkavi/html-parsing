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
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.AnchorResourceExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceSectionFactory;
import shop_list.html.parser.engine.multi_page.standart.ListBaseMultiPage;
import shop_list.html.parser.engine.record.Record;

public class katran_s_kiev_ua extends ListBaseMultiPage{

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new DirectFinder(this.parser.getNodeListFromUrl(getShopUrlStartPage(), getCharset(), "/html/body/table/tbody/tr/td/div/div[2]/div[@class=\"m0\"]/a"),
								new AnchorResourceExtractor(new IResourceSectionFactory() {
									@Override
									public NextSection getResourceSection(String name, String url) {
										return new UserSection(name, url);
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
			// http://katran-s.kiev.ua/catalog/7.html
			// http://katran-s.kiev.ua/catalog/7/2.html
			if(pageCounter>1){
				int index=this.getUrl().indexOf(".html");
				return this.getUrl().substring(0, index)+"/"+(pageCounter)+".html";
			}
			return this.getUrl();
		}
		
	}

	ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_ZERO_SIZE};
	
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://katran-s.kiev.ua";
	}
	
	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table/tbody/tr/td[2]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/div[@class=\"tovar_item\"]";
	}

	@Override
	protected Record getRecordFromNode(Node node) throws EParseException {
		Element elementName=(Element)this.parser.getNodeFromNode(node, "/div/h1/a");
		if(elementName==null)throw new EParseExceptionItIsNotRecord();
		String name=elementName.getTextContent();
		String url=this.removeStartPage(elementName.getAttribute("href"));
		
		Element priceElement=this.getElementByAttributeName(node, "/table/tbody/tr/td[@class=\"tovar_price\"]/div[@class=\"tovar_price\"]");
		if(priceElement==null)return null;

		Float price=this.getFloatFromString(priceElement.getTextContent().replaceAll("[^0-9]", ""));
		return new Record(name, null, url, price, null, null);
	}

	@Override
	protected String getCharset() {
		return ECharset.WIN_1251.getName();
	}
}
