package shops;

import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.standart.RecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.*;

public class instep_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://instep.ua";
	}

	@Override
	protected String getCharset() {
		return "windows-1251";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		// import org.w3c.dom.Node;
		// import
		// shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
		// ArrayList<Node> listOfNode=(new NodeListFinderByUrl(parser,
		// getCharset(),
		// "/html/body/div[4]/div/div[2]/div[3]/table/tbody/tr[*]/td[@width="25%"]/div/div/ul/li[*]/a")).getNodeListByUrl(this.getShopUrlStartPage());
		// removeNodeFromListByTextContent(listOfNode, new
		// String[]{"Банковское оборудование", "Автомобильные товары"});
		ArrayList<Node> listOfNode = this.parser
				.getNodeListFromUrl(
						"http://instep.ua",
						"windows-1251",
						"/HTML/BODY/DIV[@class=\"main\"]/DIV[@class=\"clear paddingT10px\"]/DIV[@class=\"left_part left\"]/UL[@class=\"cat_menu\"]/LI[@class=\"cat_menu_sub\"]/UL[*]/LI[*]/A");
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						String url = addHttpPreambula("http://instep.ua",
								element.getAttribute("href"));
						return new UserSection(element.getTextContent(), url);
					}
				});
	}

	// http://instep.ua/catalog/kotly/kotly-gazovye.html
	// http://instep.ua/catalog/kotly/kotly-gazovye/page_2.html
	// http://instep.ua/catalog/kotly/kotly-gazovye/page_3.html
	class UserSection extends NextSection{
		public UserSection(String name, String url) {
			super(name, url);
		}
		private int pageCounter=0;
		private String preambula=null;
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if(pageCounter>1){
				if(preambula==null){
					int dotPosition=this.getUrl().lastIndexOf('.');
					this.preambula=this.getUrl().substring(0,dotPosition);
				}
				return this.preambula+"/page_"+pageCounter+".html";
			}else{
				return this.getUrl();
			}
		}
	}
	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_REPEAT_LAST,
			ESectionEnd.NEXT_RECORDS_LOAD_ERROR,
			ESectionEnd.NEXT_RECORDS_ZERO_SIZE };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/DIV[@class=\"main\"]/DIV[@class=\"clear paddingT10px\"]/DIV[@class=\"center_part left\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/table[*]/tbody/tr";
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
	protected String recordFromNodeInRecordToName() {
		return "/td[2]/div/a";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/td[2]/div/a";
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
		return "/TD[@class=\"textR\"]/DIV[@class=\"price_uah\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/TD[@class=\"textR\"]/DIV[@class=\"price_usd\"]";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		return priceText.replaceAll("[^0-9^.]", "");
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
	protected Record prepareRecordBeforeSave(Record record)
			throws EParseException {
		return record;
	}
}