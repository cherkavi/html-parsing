package shops;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.single_page.TableSinglePage;

public class vkladovke_com_ua extends TableSinglePage{

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
				
				Element nodeName=(Element)this.parser.getNodeFromNode(node, "/td[2]/a");
				url=nodeName.getAttribute("href");
				// int indexOfAnd=url.indexOf("&");if(indexOfAnd>0){url=url.substring(0,indexOfAnd);}
				// if(url.startsWith("."))url=url.substring(1);
				name=nodeName.getTextContent().trim();
				
				Element priceElement=(Element)this.parser.getNodeFromNode(node, "/td[4]/b/nobr");
				// System.out.println("Source   price String: "+priceElement.getTextContent().trim());
				// System.out.println("Replaced price String: "+priceElement.getTextContent().trim().replaceAll("[\\$ ,]", ""));
				String priceElementText=priceElement.getTextContent().trim().toUpperCase().replaceAll("[А-ЯA-Z $.]", "");
				priceElementText=priceElementText.replaceAll(",", ".");
				while(priceElementText.endsWith(".")){priceElementText=priceElementText.substring(0, priceElementText.length()-1);}
				price=Float.parseFloat(priceElementText);
				
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
		return ECharset.UTF_8.getName();
	}

	@Override
	protected String getFullHttpUrlToPrice() {
		return "http://vkladovke.com.ua/pricelist/";
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://vkladovke.com.ua";
	}

	@Override
	protected String getXmlPathToMainBlock() {
		return "/html/body/div/div/div/div/div/table[2]/tbody/tr/td[2]/div[3]/center/p[2]/table/tbody";
	}

}
