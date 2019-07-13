package shops;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionItIsNotRecord;
import shop_list.html.parser.engine.multi_page.BaseMultiPage;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.record.Record;

public class all_trade_in_ua extends BaseMultiPage{

	@Override
	protected ArrayList<INextSection> getSection() {
		try{
			ArrayList<INextSection> returnValue=new ArrayList<INextSection>();
			ArrayList<Node> nodeList=this.parser.getNodeListFromUrl(this.getShopUrlStartPage(), this.getCharset(), "/html/body/table/tbody/tr[2]/td/table/tbody/tr/td/table[2]/tbody/tr[*]/td/table/tbody/tr[2]/td[2]/a");
			for(int counter=0;counter<nodeList.size();counter++){
				String mainSectionUrl=this.getShopUrlStartPage()+"/"+((Element)nodeList.get(counter)).getAttribute("href");
				ArrayList<Node> subList=this.parser.getNodeListFromUrl(mainSectionUrl, 
																		this.getCharset(), 
																		"/html/body/table/tbody/tr[2]/td/table/tbody/tr/td[2]/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[*]/td[@height=\"40\"]/a");
				if(subList!=null){
					for(int index=0;index<subList.size();index++){
						Element element=((Element)subList.get(index));
						String urlSection=this.getShopUrlStartPage()+"/"+element.getAttribute("href");
						String name=element.getTextContent();
						if((urlSection!=null)&&(name!=null)){
							returnValue.add(new UserSection(name.trim(), urlSection));
						}else{
							logger.error(this, "get section from page:"+mainSectionUrl);
						}
					}
				}else{
					this.logger.error(this, "SubList Error: "+mainSectionUrl);
				}
			}
			return returnValue;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getCharset() {
		return ECharset.WIN_1251.getName();
	}
	
	class UserSection extends NextSection{

		public UserSection(String name, String url) {
			super(name, url);
		}

		private int counter=0;
		@Override
		public String getUrlToNextPage() {
			counter++;
			if(counter==1){
				return this.getUrl();
			}else{
				return this.getUrl()+"&page_num="+counter+"&ipp=10&sort=&sv=&lm_mode=";
			}
		}
		
	}
	
	private ESectionEnd[] conditions=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_ZERO_SIZE};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://all-trade.in.ua/ru";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table/tbody/tr[2]/td/table/tbody/tr/td[2]/table/tbody/tr[2]/td/table/tbody/tr/td";
	}

	@Override
	protected String getRecordTagNameInBlock() {
		return "table";
	}
	
	@Override
	protected Record getRecordFromElement(Element element) throws EParseException {
		if(this.isStringEmpty(element.getAttribute("bgcolor"))){
			Element nodeName=(Element)this.parser.getNodeFromNode(element, "/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/b");
			Element nodeUrl=(Element)this.parser.getNodeFromNode(element, "/tbody/tr[2]/td/table/tbody/tr[2]/td/a");
			ArrayList<Node> spanList=this.parser.getNodeListFromNode(element, "/tbody/tr[2]/td/table/tbody/tr/td[3]/span[*]");
			if(spanList.size()>=3){
				if(spanList.get(2).getTextContent().indexOf("В наличии")>=0){
					String text=spanList.get(0).getTextContent().replaceAll("[^0-9.]", "").replaceAll(".$", "").replaceAll(".$", "");
					Float priceUsd=this.getFloatFromString(text);
					text=spanList.get(1).getTextContent().replaceAll("[^0-9]", "");
					Float price=this.getFloatFromString(text);
					Record returnValue=new Record(nodeName.getTextContent(), null, nodeUrl.getAttribute("href"), price, priceUsd, null);
					return returnValue;
				}else{
					return null;
				}
				
			}else{
				throw new EParseExceptionItIsNotRecord();
			}
		}else{
			throw new EParseExceptionItIsNotRecord();
		}
	}
}
