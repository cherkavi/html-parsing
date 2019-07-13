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

public class tehnozon_com_ua extends ListBaseMultiPage{

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new DirectFinder(this.parser.getNodeListFromUrl(getShopUrlStartPage(), getCharset(), "/html/body/table[3]/tbody/tr/td[2]/div[@class=\"cat_col\"]/div[@class=\"cat_block\"]/div[@class=\"cat_in\"]/ul/li[*]/a"),
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
			if(pageCounter>1)return this.getUrl()+"&page="+pageCounter;
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
		return "http://tehnozon.com.ua";
	}
	
	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table[3]/tbody/tr/td[2]/table/tbody";
	}

	@Override
	protected int getFirstPositionInRecordsBlock() {
		return 1;
	}
	
	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/tr[*]";
	}

	@Override
	protected Record getRecordFromNode(Node node) throws EParseException {
		Element elementName=(Element)this.parser.getNodeFromNode(node, "/td[2]/div/a");
		if(elementName==null)throw new EParseExceptionItIsNotRecord();
		String name=elementName.getTextContent();
		String url=elementName.getAttribute("href");
		Element elementPrice=(Element)this.parser.getNodeFromNode(node, "/td[3]/div[2]");
		Element elementPriceUsd=(Element)this.parser.getNodeFromNode(node, "/td[3]/div[1]");
		
		if(elementPrice==null&&elementPriceUsd==null)return null;
		Float price=null;
		if(elementPrice!=null){
			price=this.getFloatFromString(elementPrice.getTextContent().replaceAll("[^0-9]", ""));
		}
		Float priceUsd=null;
		if(elementPriceUsd!=null){
			priceUsd=this.getFloatFromString(elementPriceUsd.getTextContent().replaceAll("[^0-9^.]", "").replaceAll(".$", ""));
		}
		return new Record(name, null, url, price, priceUsd, null);
	}

	@Override
	protected String getCharset() {
		return ECharset.WIN_1251.getName();
	}
}
