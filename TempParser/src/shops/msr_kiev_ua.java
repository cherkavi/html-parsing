package shops;

import org.w3c.dom.Element;

import org.w3c.dom.Node;

import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.single_page.TableSinglePage;

public class msr_kiev_ua extends TableSinglePage{

	@Override
	protected Record getRecordFromRow(Node node) {
		Record returnValue=null;
		try{
			if(this.parser.getTextContentFromNode(node, "/td[3]/nobr", "").toLowerCase().indexOf("нет")<0){
				String name=null;
				String url=null;
				String description=null;
				Float price=null;
				Float priceUSD=null;
				Float priceEURO=null;
				
				Element nodeName=(Element)this.parser.getNodeFromNode(node, "/td[2]/a");
				url=nodeName.getAttribute("href");
				name=nodeName.getTextContent().trim();
				
				Element priceElement=(Element)this.parser.getNodeFromNode(node, "/td[4]/b/nobr");
				String priceElementText=priceElement.getTextContent().toLowerCase().replaceAll("[ $а-яa-z,]", "").trim();
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
		return this.parser.getTextContentFromNode(node, "/td[2]/a", "");
	}

	@Override
	protected boolean isRecord(Node node) {
		if(this.parser.getChildElementCount(node, "td")==4){
			return true;
		}else{
			return false;
		}
	}

	@Override
	protected boolean isSection(Node node) {
		if(this.parser.getChildElementCount(node, "td")==2){
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
		return "http://msr.kiev.ua/pricelist/";
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://msr.kiev.ua";
	}

	@Override
	protected String getXmlPathToMainBlock() {
		// FIXME проверить пути 
		return "/html/body/div/div/table/tbody/tr/td[2]/div/center/table/tbody";
	}

}
