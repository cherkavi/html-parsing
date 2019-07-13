package shops;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionItIsNotRecord;
import shop_list.html.parser.engine.multi_page.BaseMultiPage;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.record.Record;

public class avmarket_com_ua extends BaseMultiPage{

	@Override
	protected ArrayList<INextSection> getSection() {
		ArrayList<INextSection> returnValue=new ArrayList<INextSection>();
		try{
			ArrayList<Node> list=this.parser.getNodeListFromUrl(this.getShopUrlStartPage(), 
																this.getCharset(), 
																"/html/body/table[3]/tbody/tr/td/table/tbody/tr/td/div/a[*]");
			for(int index=0;index<list.size();index++){
				Element element=(Element)list.get(index);
				String href=element.getAttribute("href");
				String name=element.getTextContent();
				if(name.indexOf("крепления")>0)continue;
				if(name.indexOf("Готовые решения")>=0)continue;
				if(name.indexOf("Подставки")>=0)continue;
				if(name.indexOf("Монтаж")>=0)continue;
				if(name.indexOf("архив")>=0)continue;
				returnValue.add(new UserSection(name, href));
			}
			return returnValue;
		}catch(Exception ex){
			this.logger.error(this, "#getSection Excetption:"+ex.getMessage());
			return null;
		}
	}
	
	@Override
	protected String getCharset() {
		return ECharset.WIN_1251.getName();
	}
	
	class UserSection extends NextSection{

		public UserSection(String name, String url) {
			super(name, url);
		}

		private int counter=0;
		@Override
		public String getUrlToNextPage() {
			counter++;
			if(counter==1){
				return this.getUrl();
			}else{
				return this.getUrl()+"&sort=products_sort_order&&page="+counter;
			}
		}
		
	}

	private ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_REPEAT_LAST};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://avmarket.com.ua";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table[3]/tbody/tr/td[2]/table[2]/tbody/tr[4]/td/table[2]/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr[2]/td/form/table[2]/tbody";
	}

	@Override
	protected String getRecordTagNameInBlock() {
		return "tr";
	}
	@Override
	protected Record getRecordFromElement(Element element) throws EParseException {
		String className=this.getNodeAttribute(element, "class");
		if((element!=null)&&(className!=null)&&(className.indexOf("productListing")>=0)){
			Element elementName=(Element)this.parser.getNodeFromNode(element, "/td[3]/a");
			Element elementPrice=(Element)this.parser.getNodeFromNode(element, "/td[4]/font/b");
			if(elementName!=null && elementPrice!=null){
				String name=elementName.getTextContent();
				String url=this.removeStartPage(elementName.getAttribute("href"));
				Float price=this.getFloatFromString(elementPrice.getTextContent().replaceAll("[^0-9]", ""));
				return new Record(name, null, url, price, null, null);
			}else{
				logger.error(this, "data not found into block ");
				throw new EParseExceptionItIsNotRecord();
			}
		}else{
			throw new EParseExceptionItIsNotRecord();
		}
	}
}
