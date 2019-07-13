package shops;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.single_page.TableSinglePage;

public class etehnika_com_ua extends TableSinglePage{

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
				
				Element nodeName=(Element)this.parser.getNodeFromNode(node, "/td[1]/a");
				url=this.removeStartPage(nodeName.getAttribute("href"));
				// if(url.startsWith("."))url=url.substring(1);
				name=nodeName.getTextContent().trim();
				
				Element priceElement=(Element)this.parser.getNodeFromNode(node, "/td[2]");
				// System.out.println("Source   price String: "+priceElement.getTextContent().trim());
				// System.out.println("Replaced price String: "+priceElement.getTextContent().trim().replaceAll("[\\$ ,]", ""));
				int index=priceElement.getTextContent().indexOf('(');
				String priceElementText=priceElement.getTextContent().substring(0,index).replaceAll("[^0-9]", "");
				// priceElementText=priceElementText.replaceAll(",", ".");
				// while(priceElementText.startsWith(".")){priceElementText=priceElementText.substring(1);}
				priceUSD=Float.parseFloat(priceElementText);
				
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
		return this.parser.getTextContentFromNode(node, "/td/font", "").trim();
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
	protected int getFirstLine() {
		return 1;
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
		return "http://www.etehnika.com.ua/price.php";
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.etehnika.com.ua";
	}

	@Override
	protected String getXmlPathToMainBlock() {
		return "/HTML/BODY/TABLE[4]/TBODY/TR/TD[2]/TABLE/TBODY/TR[2]/TD/TABLE[@class=\"templateinfoBox\"]/TBODY/TR/TD[2]/TABLE[@class=\"infoBoxContents\"]/TBODY/TR/TD[@class=\"main\"]/TABLE/TBODY/TR/TD/table/tbody";
	}

}
