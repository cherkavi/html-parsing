package shops;

import java.util.ArrayList;

import org.w3c.dom.Element;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionItIsNotRecord;
import shop_list.html.parser.engine.multi_page.BaseMultiPage;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.DirectFinder;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.record.Record;

public class newmart_com_ua extends BaseMultiPage{

	@Override
	protected ArrayList<INextSection> getSection() {
		try{
			DirectFinder finder=new DirectFinder(this.parser.getNodeListFromUrl(this.getShopUrlStartPage(), this.getCharset(), "/html/body/div/div/table[2]/tbody/tr/td/div/ul/li[*]/a"),
					 new IResourceFromElementExtractor() {
						@Override
						public NextSection getResourceSection(Element element) {
							return new UserSection(element.getTextContent(), getShopUrlStartPage()+element.getAttribute("href"));
						}
						}
					 );
			return finder.getSection();
		}catch(Exception ex){
			return null;
		}
	}

	class UserSection extends NextSection{

		public UserSection(String name, String url) {
			super(name, url);
		}

		private int counter=0;
		@Override
		public String getUrlToNextPage() {
			counter++;
			if(counter==1)return this.getUrl();
			return this.getUrl()+"&offset="+(counter-1)*5;
		}
		
	}
	
	private ESectionEnd[] conditions=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_REPEAT_LAST};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.newmart.com.ua";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/div/div/table[2]/tbody/tr/td[2]/div[2]/center/table/tbody";
	}

	@Override
	protected String getRecordTagNameInBlock() {
		return "tr";
	}
	
	@Override
	protected Record getRecordFromElement(Element element) throws EParseException {
		try{
			Element elementName=(Element)this.parser.getNodeFromNode(element, "/td/form/div/table/tbody/tr/td[2]/div/a");
			String name=elementName.getTextContent();
			String url=elementName.getAttribute("href");
			Element elementPrice=(Element)this.parser.getNodeFromNode(element, "/td/form/div/table/tbody/tr/td[2]/div[2]/span");
			Float priceUsd=this.getFloatFromString(elementPrice.getTextContent().replaceAll("[^0-9]", ""));
			if(priceUsd.floatValue()==0)return null;
			return new Record(name, null, url, null, priceUsd, null);
		}catch(NullPointerException ex){
			throw new EParseExceptionItIsNotRecord();
		}
	}
}
