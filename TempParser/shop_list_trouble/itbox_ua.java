package shops;

import java.util.ArrayList;

import org.w3c.dom.Element;

import shop_list.html.parser.engine.multi_page.BaseMultiPage;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.ResourceSection;
import shop_list.html.parser.engine.multi_page.section_finder.DirectFinder;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.AnchorExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;

public class itbox_ua extends BaseMultiPage{

	@Override
	protected ArrayList<ResourceSection> getSection() {
		try{
			DirectFinder finder=new DirectFinder(this.parser.getNodeListFromUrl(this.getShopUrlStartPage(), this.getCharset(), "/html/body/table[3]/tbody/tr/td[2]/div/div/div[*]/div/a"),
												 new AnchorExtractor(UserSection.class, this.getShopUrlStartPage())
												 );
			return finder.getSection();
		}catch(Exception ex){
			System.out.println("#getSection Exception:"+ex.getMessage());
			return null;
		}
	}

	class UserSection extends ResourceSection{

		public UserSection(String name, String url) {
			super(name, url);
		}

		
		@Override
		public String getUrlToNextPage() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getShopUrlStartPage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		// TODO Auto-generated method stub
		return null;
	}

}
