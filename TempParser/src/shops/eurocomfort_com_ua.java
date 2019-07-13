package shops;

import java.util.ArrayList;

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

public class eurocomfort_com_ua extends ListBaseMultiPage{

	@Override
	protected Record getRecordFromNode(Node node) throws EParseException {
		try{
			ArrayList<Node> listOfName=this.parser.getNodeListFromNode(node, "/div[@class=\"prdbrief_name\"/a");
			if(listOfName.size()==0)throw new EParseExceptionItIsNotRecord();
			Element elementName=(Element)listOfName.get(0);

			ArrayList<Node> listOfPrice=this.parser.getNodeListFromNode(node, "/div[@class=\"prdbrief_price\"/span");
			Element elementPrice=(Element)listOfPrice.get(0);
			String name=elementName.getTextContent().trim();
			String url=elementName.getAttribute("href");
			if(elementPrice==null)return null;
			Float priceUsd=this.getFloatFromString(elementPrice.getTextContent().replaceAll("[^0-9^.]", ""));
			return new Record(name, null, url, null, priceUsd, null);
		}catch(NullPointerException npe){
			throw new EParseExceptionItIsNotRecord();
		}
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new DirectFinder(this.parser.getNodeListFromUrl(this.getShopUrlStartPage(), this.getCharset(), "/html/body/table[2]/tbody/tr/td[2]/div[2]/div[2]/table/tbody/tr[*]/td[@class=\"cat_name\"]/div/a"), 
								 new AnchorResourceExtractor(new IResourceSectionFactory(){
									@Override
									public NextSection getResourceSection(String name, String url) {
										return new UserSection(name, getShopUrlStartPage()+url);
									}
								 })
								);
	}

	class UserSection extends NextSection{

		public UserSection(String name, String url) {
			super(name, url);
		}

		private int counter=0;
		@Override
		public String getUrlToNextPage() {
			counter++;
			if(counter>1)return this.getUrl()+"offset"+((counter-1)*20)+"/";
			return this.getUrl();		
		}
		
	}
	
	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/tr[*]/td[*]/form/";
	}

	ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_REPEAT_LAST, ESectionEnd.NEXT_RECORDS_SHOW_FIRST, ESectionEnd.NEXT_RECORDS_ZERO_SIZE};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://eurocomfort.com.ua";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table[2]/tbody/tr/td[2]/div[2]/center/table/tbody";
	}

}
