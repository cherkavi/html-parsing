package shops;

import org.w3c.dom.Element;

import org.w3c.dom.Node;
import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.single_page.TableSinglePage;

public class telemarket_com_ua extends TableSinglePage{

	@Override
	protected Record getRecordFromRow(Node node) {
		if(((Element)this.parser.getNodeFromNode(node, "/td[2]/nobr")).getTextContent().indexOf("На складе")>=0){
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
				String priceElementText=priceElement.getTextContent();
				/*int indexSquare=priceElementText.indexOf('(');
				if(indexSquare>0){
					priceElementText=priceElementText.substring(0,indexSquare);
				}*/
				priceElementText=priceElementText.trim().toUpperCase().replaceAll("[А-ЯA-Z $,)]", "");
				
				// priceElementText=priceElementText.replaceAll(",", ".");
				while(priceElementText.endsWith(".")){priceElementText=priceElementText.substring(0,priceElementText.length()-1);}
				
				int breakPosition=priceElementText.indexOf('(');
				if(breakPosition>0){
					priceUSD=Float.parseFloat(priceElementText.substring(0, breakPosition));
					price=Float.parseFloat(priceElementText.substring(breakPosition+1));
				}else{
					priceUSD=Float.parseFloat(priceElementText);
				}
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
		return ECharset.WIN_1251.getName();
	}

	@Override
	protected String getFullHttpUrlToPrice() {
		return "http://telemarket.com.ua/index.php?show_price=yes";
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://telemarket.com.ua";
	}

	@Override
	protected String getXmlPathToMainBlock() {
		return "/HTML/BODY/DIV[@class=\"container\"]/TABLE/TBODY/TR[3]/TD[2]/TABLE/TBODY/TR/TD/CENTER/TABLE[2]/TBODY";
	}
}
