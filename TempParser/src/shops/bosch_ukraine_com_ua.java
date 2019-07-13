package shops;

import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.standart.RecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.*;


public class bosch_ukraine_com_ua extends RecordListBaseMultiPage {
	@Override
	public String getShopUrlStartPage() {
		return "http://bosch-ukraine.com.ua";
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
								"http://bosch-ukraine.com.ua",
								"windows-1251",
								"/html/body/table/tbody/tr/td[2]/table/tbody/tr/td/div/table/tbody/tr/td/div/div/table/tbody/tr[*]/td[*]/div[@class=\"first-level\"]/a"),
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						return new UserSection(element.getTextContent(), element.getAttribute("href"));
					}
				});
	}

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;
		// "http://bosch-ukraine.com.ua/component/page,shop.browse/option,com_virtuemart/task,view/category_id,69/Itemid,97/",
		// "http://bosch-ukraine.com.ua/index.php?option=com_virtuemart&page=shop.browse&category_id=69&keyword=&manufacturer_id=0&Itemid=97&orderby=product_name&limit=10&limitstart=10",
		// "http://bosch-ukraine.com.ua/index.php?option=com_virtuemart&page=shop.browse&category_id=69&keyword=&manufacturer_id=0&Itemid=97&orderby=product_name&limit=10&limitstart=20" 

		private String categoryId=null;
		private String itemId=null;
		
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				if(categoryId==null){
					int indexBegin=this.getUrl().indexOf("category_id,");
					int indexEnd=0;
					if(indexBegin>0){
						indexEnd=this.getUrl().indexOf("/",indexBegin);
						categoryId=this.getUrl().substring(indexBegin+"category_id,".length(), indexEnd);
					}
					indexBegin=this.getUrl().indexOf("Itemid,");
					indexEnd=0;
					if(indexBegin>0){
						indexEnd=this.getUrl().indexOf("/",indexBegin);
						itemId=this.getUrl().substring(indexBegin+"Itemid,".length(), indexEnd);
					}
					
				}
				return "http://bosch-ukraine.com.ua/index.php?option=com_virtuemart&page=shop.browse&category_id="+categoryId+"&keyword=&manufacturer_id=0&Itemid="+itemId+"&orderby=product_name&limit=10&limitstart="+((pageCounter-1)*10);
			}
			return this.getUrl();
		}
	}

	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_LOAD_ERROR,
			ESectionEnd.NEXT_RECORDS_ZERO_SIZE,
			ESectionEnd.NEXT_RECORDS_REPEAT_LAST,
			ESectionEnd.NEXT_RECORDS_SHOW_FIRST
			};

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table/tbody/tr/td[2]/div[@id=\"product_list\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/div[*]/div";
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
		return "/div[2]/h3/a";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/div[2]/h3/a";
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
		return "/div[1]/span[@class=\"myPrice\"]/span";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		return priceText.replaceAll("[^0-9^.]", "").replaceAll(".$", "");
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
}