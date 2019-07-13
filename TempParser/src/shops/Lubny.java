package shops;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.single_page.SinglePage;

/** парсер для сайта Лубны-Маркет  */
public class Lubny extends SinglePage{

	@Override
	protected String getFullHttpUrlToPrice() {
		return "http://lubnymarket.com/price.php";
		// return "file:///C:/price.php.htm";
	}

	@Override
	protected String getDescription() {
		return "http://lubnymarket.com";
	}

	/** 
	 * {@inheritDoc} 
	 */
	@Override
	protected String getCharset() {
		return "windows-1251";
	}

	@Override
	protected boolean work(Integer sessionId, Node table) throws Exception {
		logger.debug(this, "пробежка по элементам");
		
		if(table.hasChildNodes()){
			NodeList list=table.getChildNodes();
			Integer currentSection=null;
			for(int counter=0;counter<list.getLength();counter++){
				if(list.item(counter) instanceof Element){
					Element element=((Element)list.item(counter));
					String classAttr=element.getAttribute("class");
					if(classAttr.equalsIgnoreCase("PriceCat")){
						 
						Node sectionNode=this.parser.getNodeFromNode(element, "/td/a");
						if(sectionNode!=null){
							currentSection=saver.getSectionId(sectionNode.getTextContent());
							logger.info(this, "секция:"+sectionNode.getTextContent());
						}else{
							logger.info(this, "секция: NULL");
							currentSection=null;
						}
					}else if(classAttr.equalsIgnoreCase("str")||classAttr.equalsIgnoreCase("ttr")){
						String name=null;
						String url=null;
						try{
							Element anchorElement=(Element)this.parser.getNodeFromNode(element, "/td[1]/a");
							name=anchorElement.getTextContent();
							url=anchorElement.getAttribute("href");
							logger.debug(this, "Element: "+name);
						}catch(Exception ex){
							logger.error(this, "get Anchor Error");
						}
						Float priceUSD=null;
						try{
							String priceString=this.parser.getTextContentFromNode(element, "/td[3]", "");
							int indexOf=priceString.indexOf(" ");
							priceString=priceString.substring(0,indexOf);
							priceUSD=Float.parseFloat(priceString);
						}catch(Exception ex){
							priceUSD=0f;
						}
						if(priceUSD!=0){
							Record record=new Record(name, null, url, null, priceUSD, null);
							this.saver.saveRecord(sessionId, currentSection, record);
						}else{
							// price is null - maybe not recognized 
						}
					}
				}else{
					// this is not element
				}
			}
		}else{
			logger.error(this, "No records found");
		}
		System.out.println("END");
		return true;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://lubnymarket.com";
	}

	@Override
	protected String getXmlPathToMainBlock() {
		return "/html/body/table[2]/tbody";		
	}
	
}
