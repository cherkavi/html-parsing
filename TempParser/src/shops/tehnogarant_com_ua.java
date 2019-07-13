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

public class tehnogarant_com_ua extends ListBaseMultiPage{

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new DirectFinder(this.parser.getNodeListFromUrl(getShopUrlStartPage(), getCharset(), "/html/body/body/table[4]/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/div/ul/li[*]/a"),
							    new AnchorResourceExtractor(new IResourceSectionFactory(){
									@Override
									public NextSection getResourceSection(String name, String url) {
										return new UserSection(name,getShopUrlStartPage()+url);
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
				return this.getUrl()+"offset"+((pageCounter-1)*10)+"/";
			}
			return this.getUrl();
		}
		
	}

	ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_REPEAT_LAST};
	
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://tehnogarant.com.ua";
	}
	
	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/body/table[4]/tbody/tr/td/table/tbody/tr/td[2]/div[2]/center/table/tbody";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/tr[*]/td/table/form/tbody/tr/td[3]/";
	}

	@Override
	protected Record getRecordFromNode(Node node) throws EParseException {
		Element elementName=(Element)this.parser.getNodeFromNode(node, "/div[1]/a");
		if(elementName==null)throw new EParseExceptionItIsNotRecord();
		String name=elementName.getTextContent().trim();
		String url=elementName.getAttribute("href");
		Element elementPriceUsd=(Element)this.parser.getNodeFromNode(node, "div[2]/span");
		Element elementPrice=(Element)this.parser.getNodeFromNode(node, "div[2]/span[2]");
		if((elementPriceUsd==null)&&(elementPrice==null))return null;
		Float price=this.getFloatFromString(elementPrice.getTextContent().replaceAll("[^0-9^.]", ""));
		Float priceUsd=this.getFloatFromString(elementPriceUsd.getTextContent().replaceAll("[^0-9]", ""));
		return new Record(name, null, url, price, priceUsd, null);
	}

}
