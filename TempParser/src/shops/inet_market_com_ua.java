package shops;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.multi_page.BaseMultiPage;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.DirectFinder;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.record.Record;

public class inet_market_com_ua extends BaseMultiPage{

	@Override
	protected ArrayList<INextSection> getSection() {
		try{
			DirectFinder finder=new DirectFinder(this.parser.getNodeListFromUrl(this.getShopUrlStartPage(),this.getCharset(),"/html/body/table[2]/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr[*]/td/a"),
												 new IResourceFromElementExtractor(){
													@Override
													public NextSection getResourceSection(Element element) {
														return new UserSection(element.getTextContent(), removeAfterSymbolIncludeIt(element.getAttribute("href"),'&') );
													}
												});
			return finder.getSection();
		}catch(Exception ex){
			System.err.println("getSection Exception: "+ex.getMessage());
			return null;
		}
		/* 
		ArrayList<ResourceSection> returnValue=new ArrayList<ResourceSection>();
		
		returnValue.add( new UserSection("Цифровые видеокамеры","http://inet-market.com.ua/index.php?cPath=253"));
		returnValue.add(new UserSection("Цифровые фотокамеры","http://inet-market.com.ua/index.php?cPath=1"));
		returnValue.add(new UserSection("Телевизоры и крепления","http://inet-market.com.ua/index.php?cPath=124"));
		returnValue.add(new UserSection("Проекционное оборудование","http://inet-market.com.ua/index.php?cPath=117"));
		returnValue.add(new UserSection("Мониторы","http://inet-market.com.ua/index.php?cPath=516"));
		returnValue.add(new UserSection("Blu-Ray и HD медиаплееры","http://inet-market.com.ua/index.php?cPath=599"));
		returnValue.add(new UserSection("GPS-навигаторы","http://inet-market.com.ua/index.php?cPath=579"));
		returnValue.add(new UserSection("Бытовая техника","http://inet-market.com.ua/index.php?cPath=30"));
		returnValue.add(new UserSection("Климатическая техника","http://inet-market.com.ua/index.php?cPath=552"));
		return returnValue;
		*/
	}
	
	class UserSection extends NextSection{

		public UserSection(String name, String url) {
			super(name, url);
		}

		private int counter;
		@Override
		public String getUrlToNextPage() {
			counter++;
			if(counter==1)return this.getUrl();
			return this.getUrl()+"&page="+counter;
		}
		
	}

	ESectionEnd[] conditions=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_REPEAT_LAST};
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://inet-market.com.ua";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table[2]/tbody/tr/td/table/tbody/tr/td[4]/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr[3]/td/table[2]/tbody/tr[2]/td/table/tbody";
	}

	@Override
	protected String getCharset() {
		return ECharset.WIN_1251.getName();
	}
	
	@Override
	protected ArrayList<Record> getRecordsFromBlock(Node node) {
		ArrayList<Record> returnValue=new ArrayList<Record>();
		try{
			ArrayList<Node> nodeList=this.parser.getNodeListFromNode(node, "/tr[*]/td/table/tbody/tr");
			for(Node currentNode: nodeList){
				Element nameElement=(Element)this.parser.getNodeFromNode(currentNode, "/td[2]/a");
				Element priceElement=(Element)this.parser.getNodeFromNode(currentNode, "/td[3]/b/font");
				try{
					String name=nameElement.getTextContent();
					System.out.println(name);
					String url=nameElement.getAttribute("href");
					Float priceUsd=this.getFloatFromString(priceElement.getTextContent().replaceAll("[^0-9^,]", ""));
					returnValue.add(new Record(name, null, url, null, priceUsd, null));
				}catch(Exception ex){
					// System.err.println("#getRecordsFromBlock get element Exception:"+ex.getMessage());
				}
			}
			return returnValue;
		}catch(Exception ex){
			System.err.println("getRecordsFromBlock Exception:"+ex.getMessage());
			return null;
		}
	}
	
}
