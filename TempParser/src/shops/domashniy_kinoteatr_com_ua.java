package shops;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.single_page.TableSinglePage;

public class domashniy_kinoteatr_com_ua extends TableSinglePage{

	@Override
	protected Record getRecordFromRow(Node node) {
		//if(((Element)this.parser.getNodeFromNodeAlternative(node, "/td[2]/nobr")).getTextContent().indexOf("На складе")>=0){
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
				name=nodeName.getTextContent().trim();
				
				Element priceElement=(Element)this.parser.getNodeFromNode(node, "/td[2]");
				// System.out.println("Source   price String: "+priceElement.getTextContent().trim());
				// System.out.println("Replaced price String: "+priceElement.getTextContent().trim().replaceAll("[\\$ ,]", ""));
				String priceElementText=priceElement.getTextContent().trim().toUpperCase().replaceAll("[$]", "");
				priceElementText=priceElementText.replaceAll(",", ".");
				//while(priceElementText.endsWith(".")){priceElementText=priceElementText.substring(0, priceElementText.length()-1);}
				priceUSD=Float.parseFloat(priceElementText);
				
				returnValue=new Record(name,description,url, price, priceUSD, priceEURO);
			}catch(Exception ex){
				returnValue=null;
				logger.error(this, "#getRecordFromRow Exception:"+ex.getMessage());
			}
			return returnValue;
		//}else{return null;}
	}

	@Override
	protected String getSectionNameFromRow(Node node) {
		return this.parser.getTextContentFromNode(node, "/td/a", "");
	}

	@Override
	protected boolean isRecord(Node node) {
		try{
			return ((Element)this.parser.getNodeFromNode(node, "td[1]")).getAttribute("class").equals("product");
		}catch(Exception ex){
			return false;
		}
	}

	@Override
	protected boolean isSection(Node node) {
		try{
			return ((Element)this.parser.getNodeFromNode(node, "td[1]")).getAttribute("class").equals("group");
		}catch(Exception ex){
			return false;
		}
	}

	@Override
	protected String getCharset() {
		return "utf-8";
	}

	@Override
	protected String getFullHttpUrlToPrice() {
		return "http://domashniy-kinoteatr.com.ua/pricelist.aspx";
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://domashniy-kinoteatr.com.ua";
	}

	@Override
	protected String getXmlPathToMainBlock() {
		return "/html/body/div/div[3]/div[2]/table/tbody";
	}

	
	public static void main(String[] args){
		System.out.println("begin");
		String priceElementText="$846,00".replaceAll("[ $]", "");
		System.out.println(priceElementText);
		System.out.println("-end-");
	}
}
