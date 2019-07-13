package shops;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.single_page.TableSinglePage;

public class whitebox_com_ua extends TableSinglePage{

	@Override
	protected Record getRecordFromRow(Node node) {
		if(((Element)this.parser.getNodeFromNode(node, "/td[3]/nobr")).getTextContent().indexOf("На складе")>=0){
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
				// if(url.startsWith("."))url=url.substring(1);
				name=nodeName.getTextContent().trim();
				
				Element priceElement=(Element)this.parser.getNodeFromNode(node, "/td[4]/b/nobr");
				// System.out.println("Source   price String: "+priceElement.getTextContent().trim());
				// System.out.println("Replaced price String: "+priceElement.getTextContent().trim().replaceAll("[\\$ ,]", ""));
				String priceElementText=priceElement.getTextContent().trim().toUpperCase().replaceAll("[А-ЯA-Z $,]", "");
				// priceElementText=priceElementText.replaceAll(",", ".");
				// while(priceElementText.endsWith(".")){priceElementText=priceElementText.substring(0,priceElementText.length()-1);}
				priceUSD=Float.parseFloat(priceElementText);
				
				returnValue=new Record(name,description,url, price, priceUSD, priceEURO);
			}catch(Exception ex){
				returnValue=null;
				logger.error(this, "#getRecordFromRow Exception:"+ex.getMessage());
			}
			return returnValue;
		}else{return null;}
	}

	@Override
	protected String getSectionNameFromRow(Node node) {
		return this.parser.getTextContentFromNode(node, "/td[2]/a", "");
	}

	@Override
	protected boolean isRecord(Node node) {
		// System.out.println(node.getTextContent());
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
		return "http://whitebox.com.ua/pricelist/";
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://whitebox.com.ua";
	}

	@Override
	protected String getXmlPathToMainBlock() {
		return "/html/body/div[3]/table/tbody/tr/td[2]/div[2]/center/p[2]/table/tbody";
	}

}
