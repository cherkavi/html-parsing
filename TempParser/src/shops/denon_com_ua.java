package shops;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.single_page.TableSinglePage;

public class denon_com_ua extends TableSinglePage{

	@Override
	protected Record getRecordFromRow(Node node) {
		Record returnValue=null;
		try{
			String name=null;
			String url=null;
			String description=null;
			Float price=null;
			Float priceUSD=null;
			Float priceEURO=null;
			
			Element nodeName=(Element)this.parser.getNodeFromNode(node, "/td[1]/a");
			url=nodeName.getAttribute("href");
			name=nodeName.getTextContent().trim();
			
			Element priceElement=(Element)this.parser.getNodeFromNode(node, "/td[2]/nobr");
			// System.out.println("Source   price String: "+priceElement.getTextContent().trim());
			// System.out.println("Replaced price String: "+priceElement.getTextContent().trim().replaceAll("[\\$ ,]", ""));
			String priceElementText=priceElement.getTextContent().trim().toUpperCase().replaceAll("[, $]", "").toLowerCase().replaceAll("[à-ÿ].", "");
			priceUSD=Float.parseFloat(priceElementText);
			
			returnValue=new Record(name,description,url, price, priceUSD, priceEURO);
		}catch(Exception ex){
			returnValue=null;
			logger.error(this, "#getRecordFromRow Exception:"+ex.getMessage());
		}
		return returnValue;
	}

	public static void main(String[] args){
		String value="2000.00ó.å.";
		System.out.println(">>> "+value.replaceAll("[à-ÿ].", ""));
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
		return "http://www.denon.com.ua/index.php?show_price=yes";
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.denon.com.ua";
	}

	@Override
	protected String getXmlPathToMainBlock() {
		return "/html/body/div[@align=\"center\"]/table[5]/tbody/tr/td/div[@align=\"center\"]/table[@id=\"table1\"]/tbody/tr[2]/td/center/table/tbody";
	}

}
