package shops;

import java.util.ArrayList;
import org.w3c.dom.Element;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionItIsNotRecord;
import shop_list.html.parser.engine.multi_page.BaseMultiPage;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.RecursiveFinder;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.record.Record;

public class attis_ua extends BaseMultiPage{

	@Override
	protected ArrayList<INextSection> getSection() {
		// 
		//  new RecursiveFinder(this.parser, this.getCharset(), "/html/body/table/tbody/tr[3]/td/table/tbody/tr/td[2]/table/tbody/tr[2]/td/ul/li[*]/div/a")
		// "/html/body/table/tbody/tr[3]/td/table/tbody/tr/td[2]/table/tbody/tr[2]/td/ul/li[3]/div/a";
		// "/html/body/table/tbody/tr[3]/td/table/tbody/tr/td[2]/table/tbody/tr[2]/td/ul"
		try{
			ISectionFinder finder=new RecursiveFinder(this.parser.getNodeListFromUrl(this.getShopUrlStartPage(), getCharset(),"/html/body/table/tbody/tr[2]/td/ul/li[*]/div/a" ),
													  new IUrlFromElementExtractor() {
														@Override
														public String getUrlFromElement(Element element) {
															String url=element.getAttribute("href");
															if(isStringEmpty(url))return null;
															return getShopUrlStartPage()+url;
														}
													  },
													  
													  new NodeListFinderByUrl(this.parser, getCharset(), "/html/body/table/tbody/tr[3]/td/table/tbody/tr/td[2]/table/tbody/tr[2]/td/ul/li[*]/div/a"),
													  new IUrlFromElementExtractor() {
															@Override
															public String getUrlFromElement(Element element) {
																String url=element.getAttribute("href");
																if(isStringEmpty(url))return null;
																return getShopUrlStartPage()+url;
															}
														  },
													   new IResourceFromElementExtractor() {
															 @Override
															 public NextSection getResourceSection(Element element) {
																 return new UserSection(element.getTextContent(), getShopUrlStartPage()+element.getAttribute("href"));
															 }
														 }
													  );
			return finder.getSection();
		}catch(Exception ex){
			this.logger.error(this, "#getSection Exception:"+ex.getMessage());
			return null;
		}
		
	}

	class UserSection extends NextSection{

		public UserSection(String name, String url) {
			super(name, url);
		}
		private int counter=0;
		@Override
		public String getUrlToNextPage() {
			counter++;
			if(counter>1){
				return this.getUrl()+"page/"+counter+"/";
			}else{
				return this.getUrl();
			}
		}
		
	}
	
	@Override
	protected String getCharset() {
		return ECharset.WIN_1251.getName();
	}
	
	private ESectionEnd[] condition=new ESectionEnd[]{ESectionEnd.NEXT_RECORDS_ZERO_SIZE}; 
	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return condition;
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://attis.ua";
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/html/body/table/tbody/tr[3]/td/table/tbody/tr/td[2]";
	}

	@Override
	protected Record getRecordFromElement(Element element) throws EParseException{
		if((element!=null)&&(element.getAttribute("class").equals("one_prod"))){
			Element nameElement=(Element)this.parser.getNodeFromNode(element, "/tbody/tr/td[2]/a");
			Element urlElement=(Element)this.parser.getNodeFromNode(element, "/tbody/tr/td[2]/div[2]/a");
			Element priceElement=(Element)this.parser.getNodeFromNode(element, "/tbody/tr/td[3]/b");
			if((nameElement!=null)&&(urlElement!=null)&&(priceElement!=null)){
				 String name=nameElement.getTextContent();
				 String url=urlElement.getAttribute("href");
				 Float price=this.getFloatFromString(priceElement.getTextContent().replaceAll("[^0-9.]", "").replaceAll(".$",""));
				 if(price!=null&&price.floatValue()!=0)return new Record(name, null, url, price, null, null);
				 return null;
			}else{
				throw new EParseExceptionItIsNotRecord();
			}
		}else{
			throw new EParseExceptionItIsNotRecord();
		}
	}
	
	@Override
	protected String getRecordTagNameInBlock() {
		return "table";
	}
}
