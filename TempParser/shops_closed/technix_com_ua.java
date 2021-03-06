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

public class technix_com_ua extends ListBaseMultiPage{

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new DirectFinder(this.parser.getNodeListFromUrl(this.getShopUrlStartPage(), getCharset(), "/html/body/table/tbody/tr/td/table[3]/tbody/tr/td/table/tbody/tr[2]/td/div[*]/a"),
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
			// http://technix.com.ua/index.php?categ=32&parent=0&p=shop&navop=32&area=1
			// http://technix.com.ua/index.php?categ=32&parent=0&p=shop&navop=32&area=1&sort=&q_shop=&justtitle=&page=4
			pageCounter++;
			if(pageCounter>1){
				return this.getUrl()+"&sort=&q_shop=&justtitle=&page="+pageCounter;
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
	protected String getCharset() {
		return ECharset.WIN_1251.getName();
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://technix.com.ua";
	}
	
	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table/tbody/tr/td/table[3]/tbody/tr/td[2]/div";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/div[@class=\"itembox\"]/div[2]/";
	}

	@Override
	protected Record getRecordFromNode(Node node) throws EParseException {
		try{
			Element elementName=(Element)this.parser.getNodeFromNode(node, "/div/a");
			String name=elementName.getTextContent().trim();
			String url=elementName.getAttribute("href");
			Element elementPrice=(Element)this.parser.getNodeFromNode(node, "span");
			if(elementPrice==null)return null;
			//System.out.println(elementPrice.getTextContent());
			//System.out.println(elementPrice.getTextContent().replaceAll("[^0-9^.]", ""));
			Float price=this.getFloatFromString(elementPrice.getTextContent().replaceAll("[^0-9^,]", ""));
			return new Record(name, null, url, price, null, null);
		}catch(NullPointerException npe){
			throw new EParseExceptionItIsNotRecord();
		}
	}

}
