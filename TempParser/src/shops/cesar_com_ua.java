package shops;

import java.util.ArrayList;

import org.w3c.dom.Element;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionItIsNotRecord;
import shop_list.html.parser.engine.multi_page.BaseMultiPage;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.DirectFinder;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.record.Record;

public class cesar_com_ua extends BaseMultiPage{

	@Override
	protected ArrayList<INextSection> getSection() {
		try{
			DirectFinder finder=new DirectFinder(this.parser.getNodeListFromUrl(getShopUrlStartPage(), getCharset(), "/html/body/table/tbody/tr/td/table[3]/tbody/tr/td[2]/table/tbody/tr[2]/td/div[*]/a"),
												 new IResourceFromElementExtractor() {
													@Override
													public NextSection getResourceSection(Element element) {
														if(element!=null){
															return new UserResource(element.getTextContent(), getShopUrlStartPage()+"/"+element.getAttribute("href")); 
														}
														return null;
													}
												});
			return finder.getSection();
		}catch(Exception ex){
			return null;
		}
	}
	
	class UserResource extends NextSection{
		public UserResource(String name, String url) {
			super(name, url);
		}
		private int counter=0;
		@Override
		public String getUrlToNextPage() {
			counter++;
			if(counter>1){
				return this.getUrl()+"&offset="+((counter-1)*15);
			}else{
				return this.getUrl();
			}
		}
		
	}

	private ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_ZERO_SIZE};
	
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://cesar.com.ua";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table/tbody/tr/td/table[3]/tbody/tr/td[4]/table/tbody/tr/td/table[3]/tbody/tr/td/table/tbody";
	}

	@Override
	protected String getRecordTagNameInBlock() {
		return "tr";
	}
	
	protected String getCharset() {
		return ECharset.WIN_1251.getName();
	};
	
	
	@Override
	protected Record getRecordFromElement(Element element) throws EParseException {
		Element nameElement=(Element)this.parser.getNodeFromNode(element, "/td/table/tbody/tr/td/a");
		Element priceElement=(Element)this.parser.getNodeFromNode(element, "/td/table/tbody/tr[3]/td/b/font");
		if((nameElement!=null)&&(priceElement!=null)){
			String name=nameElement.getTextContent();
			String url=nameElement.getAttribute("href");
			Float priceUsd=this.getFloatFromString(priceElement.getTextContent().replaceAll("[^0-9^.]", "").replaceAll(".$", "").replaceAll(".$", ""));
			if(priceUsd!=null)return new Record(name, null, url, null, priceUsd, null);
			return null;
		}else{
			this.logger.error(this, "#getRecordFromElement elements does not found");
			throw new EParseExceptionItIsNotRecord();
		}
	}
}
