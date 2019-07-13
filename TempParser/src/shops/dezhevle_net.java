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

public class dezhevle_net extends ListBaseMultiPage{

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new DirectFinder(this.parser.getNodeListFromUrl(getShopUrlStartPage(), getCharset(), "/html/body/table/tbody/tr[5]/td/table/tbody/tr/td[2]/table/tbody/tr[*]/td/div/nobr[*]/a"),
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
			// http://dezhevle.net/index.php?p_id=16
			// http://dezhevle.net/index.php?&p_id=16&by=with_description&order=asc&page=1
			if(pageCounter>1){
				return this.getUrl()+"&by=with_description&order=asc&page="+(pageCounter-1);
			}
			return this.getUrl();
		}
		
	}

	ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_SHOW_FIRST};
	
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://dezhevle.net";
	}
	
	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table/tbody/tr[5]/td/table/tbody/tr/td[2]/form";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/table[*]/tbody";
	}

	@Override
	protected Record getRecordFromNode(Node node) throws EParseException {
		Element elementName=(Element)this.parser.getNodeFromNode(node, "/tr[1]/td[2]/div/a");
		if(elementName==null)throw new EParseExceptionItIsNotRecord();
		String name=null;
		try{
			name=((Element)(this.parser.getNodeFromNode(elementName, "/h1"))).getTextContent().trim();
		}catch(Exception ex){};
		String url=this.removeStartPage(elementName.getAttribute("href"));		
		Element priceUsdElement=this.getElementByAttributeName(node, "/tr[2]/td/div/span");
		if(priceUsdElement==null)return null;

		Float priceUsd=this.getFloatFromString(priceUsdElement.getTextContent().replaceAll("[^0-9]", ""));
		return new Record(name, null, url, null, priceUsd, null);
	}

	@Override
	protected String getCharset() {
		return ECharset.WIN_1251.getName();
	}
}
