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

public class technika_kiev_ua extends ListBaseMultiPage{

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new DirectFinder(this.parser.getNodeListFromUrl(getShopUrlStartPage(), getCharset(), "/html/body/div/div/div[2]/table/tbody/tr/td/div/table[*]/tbody/tr/td/a"),
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
				int index=this.getUrl().indexOf(".html");
				return this.getUrl().substring(0, index)+"_offset_"+((pageCounter-1)*8)+".html";
			}
			return this.getUrl();
		}
		
	}

	ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_LOAD_ERROR};
	
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://technika.kiev.ua";
	}
	
	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/div/div/div[3]/table/tbody";
	}

	@Override
	protected int getFirstPositionInRecordsBlock() {
		return 1;
	}
	
	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/tr[*]/td";
	}

	@Override
	protected Record getRecordFromNode(Node node) throws EParseException {
		Element elementName=(Element)this.parser.getNodeFromNode(node, "/div[2]/h3/a");
		if(elementName==null)throw new EParseExceptionItIsNotRecord();
		String name=elementName.getTextContent();
		String url=elementName.getAttribute("href");
		Element elementPrice=(Element)this.parser.getNodeFromNode(node, "div[3]/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td");
		if(elementPrice==null)return null;
		Float price=this.getFloatFromString(elementPrice.getTextContent().replaceAll("[^0-9]", ""));
		return new Record(name, null, url, price, null, null);
	}

	@Override
	protected String getCharset() {
		return ECharset.WIN_1251.getName();
	}
}
