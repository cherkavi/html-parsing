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
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.two_level_finder.TwoLevelFinder;
import shop_list.html.parser.engine.record.Record;

public class dewelve_net_ua extends BaseMultiPage{

	@Override
	protected ArrayList<INextSection> getSection() {
		try{
			TwoLevelFinder finder=new TwoLevelFinder(this.parser.getNodeListFromUrl(this.getShopUrlStartPage(), this.getCharset(), "/html/body/div/div/table/tbody/tr/td/div/div[2]/div[*]/a"),
													 new IUrlFromElementExtractor() {
														@Override
														public String getUrlFromElement(Element element) {
															if(element==null)return null;
															return element.getAttribute("href");
														}
													 },
													 new NodeListFinderByUrl(this.parser, this.getCharset(), "/html/body/div/div/table/tbody/tr/td[2]/div/div[3]/div[@class=\"catBl\"]/div/a"),
													 new IResourceFromElementExtractor() {
														@Override
														public NextSection getResourceSection(Element element) {
															return new UserResource(element.getTextContent(), element.getAttribute("href"));
														}
													 }
													 );
			return finder.getSection();
		}catch(Exception ex){
			System.err.println("#getSection Exception:"+ex.getMessage());
			return null;
		}
	}
	@Override
	protected String getCharset() {
		return ECharset.WIN_1251.getName();
	}

	class UserResource extends NextSection{

		public UserResource(String name, String url) {
			super(name, url);
		}
		private int counter=0;
		@Override
		public String getUrlToNextPage() {
			counter++;
			if(counter==1)return this.getUrl();
			return this.getUrl()+"&p="+counter;
		}
		
	}
	
	ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_SHOW_FIRST};
	
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://dewevle.net.ua";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/div/div/table/tbody/tr/td[2]/div/div[3]";
	}

	@Override
	protected String getRecordTagNameInBlock() {
		return "div";
	}
	
	@Override
	protected Record getRecordFromElement(Element element) throws EParseException {
		try{
			if(!element.getAttribute("class").equals("prBl"))throw new EParseExceptionItIsNotRecord();
			Element elementName=(Element)this.parser.getNodeFromNode(element, "/div[2]/div/a");
			String name=elementName.getTextContent();
			String url=this.removeStartPage(elementName.getAttribute("href"));
			Element elementPrice=(Element)this.parser.getNodeFromNode(element, "/div[2]/div[3]/span");
			Float price=this.getFloatFromString(elementPrice.getTextContent().replaceAll("[^0-9]", ""));
			return new Record(name, null, url, price, null, null);
		}catch(NullPointerException ex){
			throw new EParseExceptionItIsNotRecord();
		}
	}
}
