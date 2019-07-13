package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.standart.RecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.*;


public class climatica_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.climatica.com.ua";
	}

	@Override
	protected String getCharset() {
		return "windows-1251";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new DirectFinder(
				this.parser
						.getNodeListFromUrl("http://www.climatica.com.ua",
								"windows-1251",
								"/HTML/BODY/DIV[@id=\"container\"]/DIV[@id=\"cont\"]/DIV/DIV[@id=\"c1\"]/DIV[@id=\"eshopMenu\"]/DIV/DIV[@class=\"lm1\"]/A"),
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(), 
											   element.getAttribute("href"));
					}
				});
	}

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}
		private String categoryId=null;
		private int pageCounter = 0;
		// http://www.climatica.com.ua/eshop/c21/mod/boylera_gvs_kombinirovannye
		// http://www.climatica.com.ua/index.php?mp=eshop&category_id=21&page=2
		// http://www.climatica.com.ua/index.php?mp=eshop&category_id=21&page=3
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				String controlValue="eshop/";
				if(categoryId==null){
					int index=this.getUrl().indexOf(controlValue);
					if(index>0){
						categoryId=this.getUrl().substring(index+controlValue.length()+1, this.getUrl().indexOf("/",index+controlValue.length()+1));
					}
				}
				return "http://www.climatica.com.ua/index.php?mp=eshop&category_id="+categoryId+"&page="+pageCounter;
			}
			return this.getUrl();
		}
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_LOAD_ERROR,
			ESectionEnd.NEXT_RECORDS_ZERO_SIZE };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/div/div[4]/div/div/div/div/table[2]/tbody";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/tr[*]";
	}

	@Override
	protected String recordFromNodeIsPresent() {
		return "/td[@valign=\"top\"]";
	}

	@Override
	protected String recordFromNodeIsPresentText() {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToName() {
		return "/td[2]/a[@class=\"etitle\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/td[2]/a[@class=\"etitle\"]";
	}

	@Override
	protected String getAttributeForExtractRecordUrl() {
		return "href";
	}

	@Override
	protected boolean recordIsRemoveStartPageFromUrl() {
		return true;
	}

	@Override
	protected String recordFromNodeInRecordToPrice() {
		return "/td[3]/div/span";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^,]", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
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
	protected Record prepareRecordBeforeSave(Record record) throws EParseException {
		return record;
	}
}