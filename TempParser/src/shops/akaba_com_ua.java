package shops;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.single_page.TableSinglePage;

public class akaba_com_ua extends TableSinglePage{

	@Override
	protected String getCharset() {
		return "utf-8";
	}

	@Override
	protected String getDescription() {
		return "http://akaba.com.ua";
	}

	@Override
	protected String getFullHttpUrlToPrice() {
		return "http://akaba.com.ua/pricelist/";
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://akaba.com.ua";
	}

	@Override
	protected String getXmlPathToMainBlock() {
		return "/html/body/div/div/table[2]/tbody/tr/td[2]/div[3]/center/p[2]/table/tbody";
	}

	@Override
	protected Record getRecordFromRow(Node node) {
		Record returnValue=null;
		try{
			if(((Element)this.parser.getNodeFromNode(node, "/td[2]/nobr")).getTextContent().indexOf("На складе")>=0){
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
				String priceElementText=priceElement.getTextContent().trim().replaceAll("[\\$ ,а-яa-z\\.]", "");
				price=Float.parseFloat(priceElementText);
				
				returnValue=new Record(name,description,url, price, priceUSD, priceEURO);
			}else{
				// Нет на складе
			}
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
		if(this.parser.getChildElementCount(node,"td")==3){
			return true;
		}else{
			return false;
		}
	}

	@Override
	protected boolean isSection(Node node) {
		if(this.parser.getChildElementCount(node,"td")==1){
			return true;
		}else{
			return false;
		}
	}

	public static void main(String[] args){
		System.out.println("begin");
		String value="1,245грн.";
		System.out.println(value.replaceAll("[\\$ ,а-яa-z\\.]", "") );
		System.out.println("-end-");
	}
	
}
