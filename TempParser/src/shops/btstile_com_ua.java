package shops;

import org.w3c.dom.Element;

import org.w3c.dom.Node;

import shop_list.html.parser.engine.parser.IParser;
import shop_list.html.parser.engine.parser.NekoParser;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.single_page.list_element.TableSinglePage2;

public class btstile_com_ua extends TableSinglePage2{

	@Override
	protected Record getRecordFromRow(Node node) {
		if(((Element)this.parser.getNodeFromNode(node, "/td[2]/nobr")).getTextContent().indexOf("�� ������")>=0){
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
				
				Element priceElement=(Element)this.parser.getNodeFromNode(node, "/td[3]/b/nobr");
				// System.out.println("Source   price String: "+priceElement.getTextContent().trim());
				// System.out.println("Replaced price String: "+priceElement.getTextContent().trim().replaceAll("[\\$ ,]", ""));
				String priceElementText=priceElement.getTextContent().trim().toUpperCase().replaceAll("[�-� $,]", "");
				if(priceElementText.endsWith(".")){priceElementText=priceElementText.substring(0, priceElementText.length()-1);}
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
		return this.parser.getTextContentFromNode(node, "/td/a", "");
	}

	@Override
	protected boolean isRecord(Node node) {
		// System.out.println(node.getTextContent());
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
		return "http://btstile.com.ua/pricelist/";
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://btstile.com.ua";
	}

	@Override
	protected String getXmlPathToMainBlock() {
		return "/html/body/div/table/tbody/tr[2]/td[2]/div[3]/center/p[2]/table/tbody/tr[*]";
	}

	@Override
	protected IParser getParserInstance() {
		return new NekoParser();
	}
}
