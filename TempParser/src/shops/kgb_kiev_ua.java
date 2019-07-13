package shops;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.single_page.TableSinglePage;

public class kgb_kiev_ua extends TableSinglePage{

	@Override
	protected Record getRecordFromRow(Node node) {
		// if(((Element)this.parser.getNodeFromNodeAlternative(node, "/td[2]/nobr")).getTextContent().indexOf("На складе")>=0){
			Record returnValue=null;
			try{
				String name=null;
				String url=null;
				String description=null;
				Float price=null;
				Float priceUSD=null;
				Float priceEURO=null;
				
				Element nodeName=(Element)this.parser.getNodeFromNode(node, "/td[1]");
				url=nodeName.getAttribute("href");
				// if(url.startsWith("."))url=url.substring(1);
				name=nodeName.getTextContent().trim();
				
				Element priceElement=(Element)this.parser.getNodeFromNode(node, "/td[2]");
				// System.out.println("Source   price String: "+priceElement.getTextContent().trim());
				// System.out.println("Replaced price String: "+priceElement.getTextContent().trim().replaceAll("[\\$ ,]", ""));
				String priceElementText=priceElement.getTextContent().trim().toUpperCase().replaceAll("[^0-9]", "");
				// priceElementText=priceElementText.replaceAll(",", ".");
				// while(priceElementText.startsWith(".")){priceElementText=priceElementText.substring(1);}
				priceUSD=Float.parseFloat(priceElementText);
				if(priceUSD.floatValue()==0)return null;
				returnValue=new Record(name,description,url, price, priceUSD, priceEURO);
			}catch(Exception ex){
				returnValue=null;
				logger.error(this, "#getRecordFromRow Exception:"+ex.getMessage());
			}
			return returnValue;
		// }else{return null;}
	}

	@Override
	protected String getSectionNameFromRow(Node node) {
		return null;
		// return this.parser.getTextContentFromNodeAlternative(node, "/td/a", "");
	}

	@Override
	protected boolean isRecord(Node node) {
		// System.out.println(node.getTextContent());
		if(this.parser.getChildElementCount(node, "td")==2){
			return true;
		}else{
			return false;
		}
	}

	@Override
	protected boolean isSection(Node node) {
		return false;
	}

	@Override
	protected String getCharset() {
		return ECharset.UTF_8.getName();
	}

	@Override
	protected String getFullHttpUrlToPrice() {
		return "http://www.kgb.kiev.ua/price/";
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.kgb.kiev.ua";
	}

	@Override
	protected String getXmlPathToMainBlock() {
		return "/html/body/table/tbody/tr/td/div[3]/table/tbody";
	}

}
