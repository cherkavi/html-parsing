package shops;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.single_page.TableSinglePage;

public class _4elements_in_ua extends TableSinglePage{

	@Override
	protected String getCharset() {
		return "windows-1251";
	}

	@Override
	protected String getDescription() {
		return "http://4elements.in.ua/index.php";
	}

	@Override
	protected String getFullHttpUrlToPrice() {
		return "http://4elements.in.ua/index.php?show_price=yes";
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://4elements.in.ua";
	}

	@Override
	protected String getXmlPathToMainBlock() {
		return "/html/body/center/table/tbody/tr[3]/td[2]/center/table/tbody";
	}

	@Override
	protected Record getRecordFromRow(Node node) {
		Record returnValue=null;
		try{
			// if(((Element)this.parser.getNodeFromNodeAlternative(node, "/td[2]/nobr")).getTextContent().indexOf("Нет ")<0){
				String name=null;
				String url=null;
				String description=null;
				Float price=null;
				Float priceUSD=null;
				Float priceEURO=null;
				
				Element nodeName=(Element)this.parser.getNodeFromNode(node, "/td[1]/a");
				url=nodeName.getAttribute("href");
				name=nodeName.getTextContent().trim();
				
				Element priceElement=(Element)this.parser.getNodeFromNode(node, "/td[2]/nobr");
				// System.out.println("Source   price String: "+priceElement.getTextContent().trim());
				// System.out.println("Replaced price String: "+priceElement.getTextContent().trim().replaceAll("[\\$ ,]", ""));
				String priceElementText=priceElement.getTextContent().trim().replaceAll("[\\$ ,a-zа-я]", "");
				while(priceElementText.endsWith(".")){
					priceElementText=priceElementText.substring(0,priceElementText.length()-1);
				}
				priceUSD=Float.parseFloat(priceElementText);
				
				returnValue=new Record(name,description,url, price, priceUSD, priceEURO);
			// }else{// Нет на складе}
		}catch(Exception ex){
			returnValue=null;
			logger.error(this, "#getRecordFromRow Exception:"+ex.getMessage());
		}
		return returnValue;
	}

	@Override
	protected String getSectionNameFromRow(Node node) {
		try{
			return ((Element)this.parser.getNodeFromNode(node, "/td/a")).getTextContent();
		}catch(Exception ex){
			logger.error(this, "#getSectionNameFromRow Exception: "+ex.getMessage());
			return null;
		}
	}

	@Override
	protected boolean isRecord(Node node) {
		if(this.parser.getChildElementCount(node,"td")==2){
			return true;
		}else{
			return false;
		}
	}

	@Override
	protected boolean isSection(Node node) {
		String colspan=null;
		try{
			colspan=((Element)this.parser.getNodeFromNode(node, "/td")).getAttribute("colspan");
		}catch(Exception ex){
		}
		return (colspan!=null)&&(!colspan.equals(""));
	}

	public static void main(String[] args){
		System.out.println("begin");
		String value="1,245.00 $";
		System.out.println(value.replaceAll("[\\$ ,]", "") );
		System.out.println("-end-");
	}
	
}
