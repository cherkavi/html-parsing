package shops;

import org.w3c.dom.Element;

import org.w3c.dom.Node;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionItIsNotRecord;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.AnchorResourceExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceSectionFactory;
import shop_list.html.parser.engine.multi_page.section_finder.two_level_finder.TwoLevelFinder;
import shop_list.html.parser.engine.multi_page.standart.ListBaseMultiPage;
import shop_list.html.parser.engine.record.Record;

public class avento_com_ua extends ListBaseMultiPage{

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new TwoLevelFinder(this.parser.getNodeListFromUrl(this.getShopUrlStartPage(), getCharset(), "/html/body/table/tbody/tr[2]/td/table/tbody/tr/td/table[2]/tbody/tr[3]/td/table/tbody/tr/td[2]/table/tbody/tr[*]/td[2]/a"),
								new IUrlFromElementExtractor() {
									@Override
									public String getUrlFromElement(Element element) {
										return getShopUrlStartPage()+"/ru/"+element.getAttribute("href");
									}
								},
							    new NodeListFinderByUrl(this.parser, this.getCharset(), "/html/body/table/tbody/tr[2]/td/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr[*]/td[@width=\"48%\"]/a"),
							    new AnchorResourceExtractor(new IResourceSectionFactory(){
									@Override
									public NextSection getResourceSection(String name, String url) {
										return new UserSection(name,getShopUrlStartPage()+"/ru/"+url);
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
			// http://avento.com.ua/ru/index.php?p=cat&cat=mikrovolnovyie_pechi
			// http://avento.com.ua/ru/index.php?p=cat&cat=mikrovolnovyie_pechi&page_num=2&ipp=10&sort=&so=desc&lm_mode=
			// http://avento.com.ua/ru/index.php?p=cat&cat=mikrovolnovyie_pechi&page_num=9&ipp=10&sort=&so=desc&lm_mode=
			pageCounter++;
			if(pageCounter>1){
				return this.getUrl()+"&page_num="+pageCounter+"&ipp=10&sort=&so=desc&lm_mode=";
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
		return "http://avento.com.ua";
	}
	
	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table/tbody/tr[2]/td/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr/td/table[3]/tbody";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/tr[*]/td[2]/table/tbody";
	}

	@Override
	protected Record getRecordFromNode(Node node) throws EParseException {
		try{
			Element elementName=(Element)this.parser.getNodeFromNode(node, "/tr/td/table/tbody/tr/td/table/tbody/tr/td/a");
			String name=elementName.getTextContent().trim();
			String url="ru/"+elementName.getAttribute("href");
			Element elementPriceUsd=(Element)this.parser.getNodeFromNode(node, "/tr[2]/td/table/tbody/tr/td[3]/table/tbody/tr/td/span");
			Element elementPrice=(Element)this.parser.getNodeFromNode(node, "/tr[2]/td/table/tbody/tr/td[3]/table/tbody/tr/td/span[2]");
			if(elementPrice==null&&elementPriceUsd==null)return null;
			
			Float price=null;
			if(elementPrice!=null){
				price=this.getFloatFromString(elementPrice.getTextContent().replaceAll("[^0-9]", ""));
			}
			Float priceUsd=null;
			if(elementPrice!=null){
				priceUsd=this.getFloatFromString(elementPriceUsd.getTextContent().replaceAll("[^0-9^.]", ""));
			}
			//System.out.println(elementPrice.getTextContent());
			//System.out.println(elementPrice.getTextContent().replaceAll("[^0-9^.]", ""));
			return new Record(name, null, url, price, priceUsd, null);
		}catch(NullPointerException npe){
			throw new EParseExceptionItIsNotRecord();
		}
	}

	@Override
	protected String getCharset() {
		return ECharset.WIN_1251.getName();
	}
}
