package shops;

import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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

public class a_trade_com_ua extends BaseMultiPage{

	@Override
	protected ArrayList<INextSection> getSection() {
		try{
			// import org.w3c.dom.Node;
			// import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
			// ArrayList<Node> listOfNode=(new NodeListFinderByUrl(parser, getCharset(), "/html/body/div[4]/div/div[2]/div[3]/table/tbody/tr[*]/td[@width=\"25%\"]/div/div/ul/li[*]/a")).getNodeListByUrl(this.getShopUrlStartPage());
			ArrayList<Node> listOfNode=this.parser.getNodeListFromUrl(this.getShopUrlStartPage(), this.getCharset(), "/html/body/div[4]/div/div[2]/div[3]/table/tbody/tr[*]/td[@width=\"25%\"]/div/div/ul/li[*]/a");
			DirectFinder finder=new DirectFinder(listOfNode, 
					 new IResourceFromElementExtractor() {
						@Override
						public NextSection getResourceSection(Element element) {
							return new UserSection(element.getTextContent(), getShopUrlStartPage()+element.getAttribute("href"));
						}
					});
			return finder.getSection();
		}catch(Exception ex){
			return null;
		}
	}

	class UserSection extends NextSection{

		public UserSection(String name, String url) {
			super(name, url);
		}
		
		int counter=0;
		
		@Override
		public String getUrlToNextPage() {
			counter++;
			if(counter>1){
				String url=this.getUrl();
				return url.substring(0, url.length()-5)+"_"+counter+".html";
			}else{
				return this.getUrl();
			}
		}
		
	}
	
	@Override
	protected String getCharset() {
		return ECharset.WIN_1251.getName();
	}
	
	private ESectionEnd[] conditions=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_ZERO_SIZE};
	
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://a-trade.ua";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/div[4]/div/div[2]/div/div[2]/div/div[7]/div[3]/div/div/div/div/div/div/div/div";
	}

	@Override
	protected String getRecordTagNameInBlock() {
		return "div";
	}
	
	@Override
	protected Record getRecordFromElement(Element element) throws EParseException {
		try{
			Element elementName=(Element)this.parser.getNodeFromNode(element, "/h3/a");
			String url=elementName.getAttribute("href");
			String name=elementName.getTextContent();
			Element elementPrice=(Element)this.parser.getNodeFromNode(element, "/div[2]/div/strong");
			Float price=this.getFloatFromString(elementPrice.getTextContent().replaceAll("[^0-9]", ""));
			Element elementPriceUsd=(Element)this.parser.getNodeFromNode(element, "/div[2]/div[2]");
			Float priceUsd=this.getFloatFromString(elementPriceUsd.getTextContent().replaceAll("[^0-9]", ""));
			if(price.floatValue()==0)return null;
			return new Record(name, null, url, price, priceUsd, null);
		}catch(NullPointerException ex){
			throw new EParseExceptionItIsNotRecord();
		}
	}
}
