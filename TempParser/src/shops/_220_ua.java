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

public class _220_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://220.ua";
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
		// removeNodeWithRepeatAttributes(list, "href");
		ArrayList<Node> listOfNode = this.parser
				.getNodeListFromUrl(
						"http://220.ua",
						"windows-1251",
						"/HTML/BODY/DIV[@id=\"wrapper\"]/DIV[@class=\"frame\"]/DIV[@id=\"wrapper2\"]/DIV[@id=\"wrapper3\"]/DIV[@id=\"catalog\"]/DIV[*]/DIV[@class=\"goods\"]/UL[*]/LI[*]/A");
		removeNodeFromListByTextContent(listOfNode, new
		String[]{
		"Фитнес станции",
		"Тренажеры",
		"Спортивное оборудование",
		"Скамьи гимнастические и под штангу",
		"Орбитреки",
		"Вибромассажеры",
		"Велотренажеры",
		"Беговые дорожки",
		"Мини-автомойки",
		"Комплекты ксенона",
		"Камеры заднего вида",
		"Громкая связь в авто",
		"Автомобильные держатели",
		"Автоинверторы",
		"Автобезопасность",
		"Товары для здоровья"
		});
		return new DirectFinder(listOfNode,
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

		private int pageCounter = 0;
		private String preambula=null;
		
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// http://220.ua/shop/3/index.html
				// http://220.ua/shop/3/page_2-15.html
				// http://220.ua/shop/3/page_3-15.html
				if(this.preambula==null){
					int lastIndex=this.getUrl().lastIndexOf('/');
					this.preambula=this.getUrl().substring(0, lastIndex)+"/page_";
				}
				// http://220.ua/shop/3/page_
				return this.preambula+pageCounter+"-15.html";
			}
			return this.getUrl();
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
		return "/HTML/BODY/DIV[@id=\"wrapper\"]/DIV[@class=\"frame\"]/DIV[@id=\"wrapper2\"]/DIV[@id=\"wrapper3\"]/DIV[@id=\"main\"]/DIV[@id=\"content\"]/DIV[@id=\"catalog-list\"]/FORM";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[*]/DIV[@class=\"goods-detail\"]";
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
		return "/DIV[@class=\"name-line\"]/P[@class=\"goods-model\"]/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/DIV[@class=\"name-line\"]/P[@class=\"goods-model\"]/A";
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
		return "/DIV[@class=\"price-line\"]/DIV[@class=\"price\"]/P";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		// 201 грн. (25 $)
		int index=priceText.indexOf("грн.");
		if(index<0)return null;
		return priceText.substring(0,index).replace("[^0-9]", "");
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/DIV[@class=\"price-line\"]/DIV[@class=\"price\"]/P/SPAN/SPAN";
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