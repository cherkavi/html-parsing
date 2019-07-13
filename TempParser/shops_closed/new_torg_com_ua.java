package shops;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.single_page.TableSinglePage;

public class new_torg_com_ua extends TableSinglePage{

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
				
				Element nodeName=(Element)this.parser.getNodeFromNode(node, "/td[3]/a");
				url=this.removeStartPage(nodeName.getAttribute("href"));
				// if(url.startsWith("."))url=url.substring(1);
				name=nodeName.getTextContent().trim();
				
				Element priceElement=(Element)this.parser.getNodeFromNode(node, "/td[5]");
				// System.out.println("Source   price String: "+priceElement.getTextContent().trim());
				// System.out.println("Replaced price String: "+priceElement.getTextContent().trim().replaceAll("[\\$ ,]", ""));
				String priceElementText=priceElement.getTextContent().trim().toUpperCase().replaceAll("[А-ЯA-Z $.]", "");
				priceElementText=priceElementText.replaceAll(",", ".");
				// while(priceElementText.startsWith(".")){priceElementText=priceElementText.substring(1);}
				try{
					price=Float.parseFloat(priceElementText);
				}catch(Exception ex){
					price=Float.parseFloat(this.removeCharFromString(priceElementText, 160));
				}
				
				
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
		String returnValue=null;
		returnValue=this.parser.getTextContentFromNode(node, "/td/h2/a", null);
		if(returnValue==null){
			return this.parser.getTextContentFromNode(node, "/td/h3/a", "");
		}else{
			return returnValue;
		}
		
	}

	@Override
	protected boolean isRecord(Node node) {
		// System.out.println(node.getTextContent());
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
		return ECharset.WIN_1251.getName();
	}

	@Override
	protected String getFullHttpUrlToPrice() {
		return "http://new-torg.com.ua/price.php";
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://new-torg.com.ua";
	}

	@Override
	protected String getXmlPathToMainBlock() {
		return "/html/body/table/tbody/tr/td[2]/table[2]/tbody";
	}

}
