package shops;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.DirectFinder;
import shop_list.html.parser.engine.multi_page.section_finder.element_extractor.AnchorElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.AnchorResourceExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceSectionFactory;
import shop_list.html.parser.engine.multi_page.standart.RecordListBaseMultiPage;

public class ctline_com_ua extends RecordListBaseMultiPage{

	@Override
	protected String recordFromNodeInRecordToName() {
		return "/td[2]/a/h1";
	}

	@Override
	protected String recordFromNodeInRecordToPrice() {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToPriceEuro() {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToPriceEuroBeforeConvert(
			String priceText) {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/td[3]/div";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9]", "");
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/td[2]/a";
	}

	@Override
	protected String recordFromNodeIsPresent() {
		return null;
	}

	@Override
	protected String recordFromNodeIsPresentText() {
		return null;
	}

	@Override
	protected boolean recordIsRemoveStartPageFromUrl() {
		return false;
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new DirectFinder(this.parser.getNodeListFromUrl(getShopUrlStartPage(), getCharset(), "/html/body/table[3]/tbody/tr/td/div[@class=\"row\"]/b/a"),
								new AnchorResourceExtractor(new IResourceSectionFactory() {
									
																								@Override
																								public INextSection getResourceSection(String name, String url) {
																									return new UserSection(name, url);
																								}
																							},
															getShopUrlStartPage()
															)
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
			if(pageCounter>1)return null;
			return getUrl();
		}
		
	}
	
	@Override
	protected String getCharset() {
		return ECharset.WIN_1251.getName();
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/tr[*]";
	}

	ESectionEnd[] conditions=new ESectionEnd[]{ESectionEnd.NEXT_PAGE_LOAD_ERROR};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.ctline.com.ua";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table[3]/tbody/tr/td/table/tbody/tr/td[2]/table[2]/tbody";
	}

}
