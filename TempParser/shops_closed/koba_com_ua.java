package shops;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.single_page.TableSinglePage;

public class koba_com_ua extends TableSinglePage{

	@Override
	protected Record getRecordFromRow(Node node) {
		// if(((Element)this.parser.getNodeFromNodeAlternative(node, "/td[2]/nobr")).getTextContent().indexOf("Íà ñêëàäå")>=0){
			Record returnValue=null;
			try{
				String name=null;
				String url=null;
				String description=null;
				Float price=null;
				Float priceUSD=null;
				Float priceEURO=null;
				
				Element nodeName=(Element)this.parser.getNodeFromNodeAlternative(node, "/td[1]/a");
				url=this.removeStartPage(nodeName.getAttribute("href"));
				// if(url.startsWith("."))url=url.substring(1);
				name=nodeName.getTextContent().trim();
				
				Element priceElement=(Element)this.parser.getNodeFromNodeAlternative(node, "/td[2]");
				String dirtyPrice=priceElement.getTextContent();
				int indexDelimeter=dirtyPrice.indexOf('/');
				if(indexDelimeter>0){
					String dirtyUsd=dirtyPrice.substring(0, indexDelimeter).replaceAll("[a-zA-Zà-ÿÀ-ß.]","");
					priceUSD=Float.parseFloat(dirtyUsd);
					
					String dirtyHrn=dirtyPrice.substring(indexDelimeter+1).replaceAll("[a-zA-Zà-ÿÀ-ß]","").trim();
					while(dirtyHrn.endsWith(".")){dirtyHrn=dirtyHrn.substring(0,dirtyHrn.length()-1);}
					price=this.getFloatFromString(dirtyHrn);
				}else{
					return null;
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
		return this.parser.getTextContentFromNodeAlternative(node, "/td/a", "");
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
		return "http://www.koba.com.ua/price/html/";
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.koba.com.ua";
	}

	@Override
	protected String getXmlPathToMainBlock() {
		return "/html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr[2]/td[2]/table/tbody/tr[2]/td/table/tbody/tr[2]/td/table/tbody";
	}

}
