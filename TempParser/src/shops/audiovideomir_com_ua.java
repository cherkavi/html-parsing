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

public class audiovideomir_com_ua extends ListBaseMultiPage{

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new DirectFinder(this.parser.getNodeListFromUrl(this.getShopUrlStartPage(), getCharset(), "/html/body/div/div[2]/div[2]/div/div[3]/table/tbody/tr[*]/td[2]/ul/li[*]/a"),
			    new AnchorResourceExtractor(new IResourceSectionFactory(){
					@Override
					public NextSection getResourceSection(String name, String url) {
						return new UserResource(name, getShopUrlStartPage()+url+"?display_all");
					}
			    })
		       );
	}

	class UserResource extends NextSection{
		public UserResource(String name, String url) {
			super(name, url);
		}
		private int counter=0;
		@Override
		public String getUrlToNextPage() {
			// http://www.audiovideomir.com.ua/ctg_23.htm?display_all
			// http://www.audiovideomir.com.ua/index.php?lang=ru&theme=cat&mode_cat=ctg&ctg=23&display_all&begin=11
			// http://www.audiovideomir.com.ua/ctg_24.htm?display_all
			// http://www.audiovideomir.com.ua/index.php?lang=ru&theme=cat&mode_cat=ctg&ctg=23&display_all&begin=11
			// http://www.audiovideomir.com.ua/index.php?lang=ru&theme=cat&mode_cat=ctg&ctg_24&display_all&begin=11
			// http://www.audiovideomir.com.ua/index.php?lang=ru&theme=cat&mode_cat=ctg&ctg=24&display_all
			// http://www.audiovideomir.com.ua/index.php?lang=ru&theme=cat&mode_cat=ctg&ctg=24&display_all&begin=21
			counter++;
			if(counter==1)return this.getUrl();
			int indexBegin=this.getUrl().lastIndexOf('/');
			int indexEnd=this.getUrl().lastIndexOf(".htm?");
			return getShopUrlStartPage()+"/index.php?lang=ru&theme=cat&mode_cat=ctg&"+this.getUrl().substring(indexBegin+1, indexEnd).replace('_', '=')+"&display_all&begin="+((counter-1)*10+1);
		}
		
	}
	
	ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_ZERO_SIZE};
	
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.audiovideomir.com.ua";
	}

	protected String getXmlPathToDataBlock() {
		return "/html/body/div/div[2]/div[2]/div/div[4]/ul";
	}
	
	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/li[*]";
	}
	
	@Override
	protected Record getRecordFromNode(Node node) throws EParseException {
		
		Element elementName=(Element)this.parser.getNodeFromNode(node, "/h2/a");
		if(elementName==null)throw new EParseExceptionItIsNotRecord();
		String name=elementName.getTextContent().trim();
		String url=elementName.getAttribute("href");
		
		Element elementPrice=(Element)this.parser.getNodeFromNode(node, "/div");
		if(elementPrice==null)return null;
		Float price=this.getFloatFromString(elementPrice.getTextContent().replaceAll("[^0-9]", ""));
		return new Record(name, null, url, null, price, null);
	}

}
