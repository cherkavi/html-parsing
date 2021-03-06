package shops;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.single_page.TableSinglePage;

public class svitbt_com_ua extends TableSinglePage{

	@Override
	protected Record getRecordFromRow(Node node) {
		// if(((Element)this.parser.getNodeFromNodeAlternative(node, "/td[2]/nobr")).getTextContent().indexOf("�� ������")>=0){
			Record returnValue=null;
			try{
				String name=null;
				String url=null;
				String description=null;
				Float price=null;
				Float priceUSD=null;
				Float priceEURO=null;
				
				Element nodeName=(Element)this.parser.getNodeFromNode(node, "/td[3]/a");
				url=nodeName.getAttribute("href");
				// int indexOfAnd=url.indexOf("&");if(indexOfAnd>0){url=url.substring(0,indexOfAnd);}
				// if(url.startsWith("."))url=url.substring(1);
				name=nodeName.getTextContent().trim();
				
				nodeName=(Element)this.parser.getNodeFromNode(node, "/td[2]");
				name=nodeName.getTextContent().trim()+" "+name;
				
				Element priceElement=(Element)this.parser.getNodeFromNode(node, "/td[4]");
				// System.out.println("Source   price String: "+priceElement.getTextContent().trim());
				// System.out.println("Replaced price String: "+priceElement.getTextContent().trim().replaceAll("[\\$ ,]", ""));
				String priceElementText=priceElement.getTextContent().trim().toUpperCase().replaceAll("[�-�A-Z $,]", "");
				// priceElementText=priceElementText.replaceAll(",", ".");
				// while(priceElementText.endsWith(".")){priceElementText=priceElementText.substring(0, priceElementText.length()-1);}
				int indexOfQuotes=priceElementText.indexOf("(");
				priceUSD=Float.parseFloat(priceElementText.substring(0, indexOfQuotes));
				
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
		return this.parser.getTextContentFromNode(node, "/td/b", "");
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
		if(this.parser.getChildElementCount(node, "td")==1){
			return true;
		}else{
			return false;
		}
	}

	@Override
	protected String getCharset() {
		return ECharset.WIN_1251.getName();
	}

	@Override
	protected String getFullHttpUrlToPrice() {
		return "http://www.svitbt.com.ua/price_list.htm";
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.svitbt.com.ua";
	}

	@Override
	protected String getXmlPathToMainBlock() {
		return "/html/body/body/table/tbody/tr/td/p[2]/table/tbody/tr/td[3]/table[2]/tbody";
	}

	@Override
	protected int getFirstLine() {
		return 1;
	}
}
