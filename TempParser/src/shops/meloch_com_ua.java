package shops;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.single_page.TableSinglePage;

public class meloch_com_ua extends TableSinglePage{

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
				// if(url.startsWith("."))url=url.substring(1);
				name=nodeName.getTextContent().trim();
				
				Element priceElement=(Element)this.parser.getNodeFromNode(node, "/td[3]/nobr/b");
				// System.out.println("Source   price String: "+priceElement.getTextContent().trim());
				// System.out.println("Replaced price String: "+priceElement.getTextContent().trim().replaceAll("[\\$ ,]", ""));
				String priceElementText=priceElement.getTextContent().trim().toUpperCase().replaceAll("[�-�A-Z $,]", "");
				// while(priceElementText.endsWith(".")){priceElementText=priceElementText.substring(0, priceElementText.length()-1);}
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
		return "windows-1251";
	}

	@Override
	protected String getFullHttpUrlToPrice() {
		return "http://meloch.com.ua/index.php?show_price=yes";
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://meloch.com.ua";
	}

	@Override
	protected String getXmlPathToMainBlock() {
		return "/HTML/BODY/TABLE[4]/TBODY/TR[2]/TD[@id=\"center\"]/TABLE[2]/TBODY/TR/TD[@class=\"tgubg\"]/TABLE[2]/TBODY";
	}

}
