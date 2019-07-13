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

public class moyo_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://www.moyo.ua";
	}

	@Override
	protected String getCharset() {
		return "windows-1251";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		return new DirectFinder(
				this.parser
						.getNodeListFromUrl(
								"http://www.moyo.ua/dir.html",
								"windows-1251",
								"/HTML/BODY[@class=\"inner_bg\"]/DIV[@class=\"center_page\"]/TABLE/TBODY/TR[1]/TD[2]/DIV[@class=\"search_wrap\"]/TABLE[@class=\"cat_table\"]/TBODY/TR[2]/TD[@class=\"catl_22\"]/DIV[@class=\"full_content\"]/TABLE[@class=\"cat_list_table\"]/TBODY/TR[*]/TD[@width=\"50%\"]/DIV/A[*]"),
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(),
								"http://www.moyo.ua"
										+ element.getAttribute("href"));
					}
				});
	}

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;
		private String preambula=null;
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// http://www.moyo.ua/dir/it/notebooks.html
				// http://www.moyo.ua/dir/it/notebooks/page-2.html
				// http://www.moyo.ua/dir/it/notebooks/page-3.html
				if(preambula==null){
					int lastIndex=this.getUrl().lastIndexOf('.');
					preambula=this.getUrl().substring(0,lastIndex);
				}
				return preambula+"/page-"+pageCounter+".html";
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
		return "/HTML/BODY[@class=\"inner_bg\"]/DIV[@class=\"center_page\"]/TABLE/TBODY/TR[1]/TD[2]/DIV[@class=\"search_wrap\"]/DIV[@class=\"articles_block\"]/DIV[@class=\"articles_block_in\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/table[@class=\"cat_table\"]/tbody/tr[2]/td[@class=\"catl_22\"]/div/table/tbody/tr";
	}

	@Override
	protected String recordFromNodeIsPresent() {
		return "/td[@class=\"prod_right\"][@width=\"250\"][3]/div[@class=\"prod_right_div\"]/span[@class=\"stat\"]/b";
	}

	@Override
	protected String recordFromNodeIsPresentText() {
		return "Есть";
	}

	@Override
	protected String recordFromNodeInRecordToName() {
		return "/td[@style=\"padding-right:10px\"][2]/div[@class=\"text_wrap\"]/h3[@class=\"title\"]";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/td[@style=\"padding-right:10px\"][2]/div[@class=\"text_wrap\"]/h3[@class=\"title\"]/a";
	}

	@Override
	protected String getAttributeForExtractRecordUrl() {
		return "href";
	}

	@Override
	protected boolean recordIsRemoveStartPageFromUrl() {
		return false;
	}

	@Override
	protected String recordFromNodeInRecordToPrice() {
		return "/td[@class=\"prod_right\"]/div[@class=\"prod_right_div\"]/span[@class=\"price\"]/span";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "");
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