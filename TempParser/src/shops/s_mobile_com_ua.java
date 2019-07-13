package shops;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.single_page.TableSinglePage;

public class s_mobile_com_ua extends TableSinglePage{

	@Override
	protected Record getRecordFromRow(Node node) {
		Record returnValue=null;
		try{
			if(this.parser.getTextContentFromNode(node, "/td[2]/nobr", "-").indexOf("+")>=0){
				String name=null;
				String url=null;
				String description=null;
				Float price=null;
				Float priceUSD=null;
				Float priceEURO=null;
				
				Element nodeName=(Element)this.parser.getNodeFromNode(node, "/td[1]/a");
				url=nodeName.getAttribute("href");
				name=nodeName.getTextContent().trim();
				
				Element priceElement=(Element)this.parser.getNodeFromNode(node, "/td[3]/nobr/b");
				String priceElementText=priceElement.getTextContent().replaceAll("[, $]", "").trim();
				// priceUSD=Float.parseFloat(priceElementText.substring(0,priceElementText.length()-1));
				priceUSD=Float.parseFloat(priceElementText);
				
				// priceElement=(Element)this.parser.getNodeFromNode(node, "/td[4]/nobr/b");
				// priceElementText=priceElement.getTextContent().replaceAll("[, $]", "").toLowerCase().replaceAll("грн.", "").trim();
				// price=Float.parseFloat(priceElementText.substring(0,priceElementText.length()-1));
				// price=Float.parseFloat(priceElementText);

				returnValue=new Record(name,description,url, price, priceUSD, priceEURO);
			}else{
				// нет на складе 
			}
		}catch(Exception ex){
			returnValue=null;
			logger.error(this, "#getRecordFromRow Exception:"+ex.getMessage());
		}
		return returnValue;
	}

	@Override
	protected String getSectionNameFromRow(Node node) {
		return this.parser.getTextContentFromNode(node, "/td/a", "");
	}

	@Override
	protected boolean isRecord(Node node) {
		if(this.parser.getChildElementCount(node, "td")==5){
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
		return "http://www.s-mobile.com.ua/index.php?show_price=yes";
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.s-mobile.com.ua";
	}

	@Override
	protected String getXmlPathToMainBlock() {
		return "/html/body/center/table/tbody/tr[4]/td/table/tbody/tr/td[2]/table/tbody/tr[4]/td[2]/center/p/table/tbody";
	}

}
