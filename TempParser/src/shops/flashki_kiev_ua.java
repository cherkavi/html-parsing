package shops;

import org.w3c.dom.Element;

import org.w3c.dom.Node;

import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.single_page.TableSinglePage;

public class flashki_kiev_ua extends TableSinglePage{

	@Override
	protected Record getRecordFromRow(Node node) {
		Record returnValue=null;
		try{
			if(this.parser.getTextContentFromNode(node, "/td[2]/nobr", "").toLowerCase().indexOf("םוע")<0){
				String name=null;
				String url=null;
				String description=null;
				Float price=null;
				Float priceUSD=null;
				Float priceEURO=null;
				
				Element nodeName=(Element)this.parser.getNodeFromNode(node, "/td[1]/a");
				url=nodeName.getAttribute("href");
				name=nodeName.getTextContent().trim();
				
				Element priceElement=(Element)this.parser.getNodeFromNode(node, "/td[3]/b/nobr");
				String priceElementText=priceElement.getTextContent().toLowerCase().replaceAll("[ $א-ÿa-z.]", "").replaceAll(",",".").trim();
				priceUSD=Float.parseFloat(priceElementText);
				
				returnValue=new Record(name,description,url, price, priceUSD, priceEURO);
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
		if(this.parser.getChildElementCount(node, "td")==3){
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
		return "utf-8";
	}

	@Override
	protected String getFullHttpUrlToPrice() {
		return "http://flashki.kiev.ua/pricelist/";
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://flashki.kiev.ua";
	}

	@Override
	protected String getXmlPathToMainBlock() {
		return "/html/body/div[2]/table/tbody/tr/td[2]/table[3]/tbody/tr/td/table/tbody/tr/td[2]/div/div/center/table[2]/tbody";
	}

}
