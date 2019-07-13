package shops;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.single_page.TableSinglePage;

public class best_optics_com_ua extends TableSinglePage{

	@Override
	protected Record getRecordFromRow(Node node) {
		Record returnValue=null;
		try{
			String name=null;
			String description=null;
			String url=null;
			Float price=null;
			Float priceUSD=null;
			Float priceEURO=null;
			
			name=this.parser.getTextContentFromNode(node, "/td[1]/a", "").trim();
			url=((Element)this.parser.getNodeFromNode(node, "td[1]/a")).getAttribute("href");
			price=Float.parseFloat(this.parser.getTextContentFromNode(node, "/td[2]/nobr/b", "").replaceAll("[,а-€ј-€ ]", ""));
			if(price>0){
				returnValue=new Record(name, description, url, price, priceUSD, priceEURO);
			}
		}catch(Exception ex){
			returnValue=null;
			logger.error(this, "getRecordFromRow Exception: "+ex.getMessage());
		}
		return returnValue;
	}
	
	public static void main(String[] args){
		System.out.println("begin");
		String value="124 грн. 00 коп";
		System.out.println(value.replaceAll("[,а-€ј-€ ]", ""));
		System.out.println("-end-");
	}
	

	@Override
	protected String getSectionNameFromRow(Node node) {
		return this.parser.getTextContentFromNode(node, "/td/a", "");
	}

	@Override
	protected boolean isRecord(Node node) {
		if(this.parser.getChildElementCount(node, "td")==2){
			return true;
		}else{
			return false;
		}
	}

	@Override
	protected boolean isSection(Node node) {
		if(this.parser.getChildElementCount(node, "td")==1){
			return true;
		}else{
			return false;
		}
	}

	@Override
	protected String getCharset() {
		return "windows-1251";
	}

	@Override
	protected String getFullHttpUrlToPrice() {
		return "http://best-optics.com.ua/index.php?show_price=yes";
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://best-optics.com.ua";
	}

	@Override
	protected String getXmlPathToMainBlock() {
		return "/html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/center/p[2]/table/tbody";
	}

}
