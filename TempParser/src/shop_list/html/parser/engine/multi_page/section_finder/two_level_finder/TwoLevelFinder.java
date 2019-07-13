package shop_list.html.parser.engine.multi_page.section_finder.two_level_finder;

import java.util.ArrayList;


import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;

public class TwoLevelFinder implements ISectionFinder{

	private ArrayList<Node> mainList=null;
	private IUrlFromElementExtractor mainUrlExtractor;
	
	private ArrayList<String> urlList=null;
	
	private NodeListFinderByUrl subFinder;
	private IResourceFromElementExtractor extractor;	
	
	/** Ёкстрактор дл€ двух уровней вложенности по секци€м, <br>
	 * то есть есть основана€ страница с секци€ми и подсекции дл€ каждой секции 
	 * <br> только один уровень вложенности 
	 * @param mainList - список элементов из основной страницы 
	 * @param mainUrlExtractor - URL Extractor дл€ страницы 
	 * @param subFinder - получатель элементов из подсекций 
	 * @param extractor - получатель ресурсных элементов на основании второго уровн€ вложенности элементов
	 */
	public TwoLevelFinder(ArrayList<Node> mainList,
						  IUrlFromElementExtractor mainUrlExtractor,
						  NodeListFinderByUrl subFinder, 
						  IResourceFromElementExtractor extractor){
		this.mainList=mainList;
		this.mainUrlExtractor=mainUrlExtractor;
		this.subFinder=subFinder;
		this.extractor=extractor;
	}

	
	/** Ёкстрактор дл€ двух уровней вложенности по секци€м, <br>
	 * то есть есть основана€ страница с секци€ми и подсекции дл€ каждой секции 
	 * <br> только один уровень вложенности 
	 * @param urlList - список URL, которые содержат необходимые элементы 
	 * @param subFinder - получатель элементов из подсекций 
	 * @param extractor - получатель ресурсных элементов на основании второго уровн€ вложенности элементов
	 */
	public TwoLevelFinder(ArrayList<String> urlList,
						  NodeListFinderByUrl subFinder, 
						  IResourceFromElementExtractor extractor){
		this.urlList=urlList;
		
		this.subFinder=subFinder;
		this.extractor=extractor;
	}
	
	@Override
	public ArrayList<INextSection> getSection() {
		if(this.urlList==null){
			ArrayList<INextSection> returnValue=new ArrayList<INextSection>();
			if((this.mainList==null)||(this.mainList.size()==0)){
				System.err.println("!!! MainList Empty !!! - check it");
				return null;
			}
			
			for(int counter=0;counter<this.mainList.size();counter++){
				try{
					String url=this.mainUrlExtractor.getUrlFromElement((Element)this.mainList.get(counter));
					ArrayList<Node> list=subFinder.getNodeListByUrl(url);
					for(int subIndex=0;subIndex<list.size();subIndex++){
						INextSection rs=this.extractor.getResourceSection((Element)list.get(subIndex));
						if((rs.getUrl()==null)||(rs.getUrl().equals(""))){
							System.out.println("TwoLevelFinder#getSection: section is empty:");
						}else{
							returnValue.add(rs);
						}
					}
				}catch(Exception ex){
					System.err.println("TwoLevelFinder#getSection Exception:"+ex.getMessage());
				}
			}
			return returnValue;
		}else{
			if(this.urlList.size()!=0){
				ArrayList<INextSection> returnValue=new ArrayList<INextSection>();
				for(int counter=0;counter<this.urlList.size();counter++){
					String url=this.urlList.get(counter);
					ArrayList<Node> list=subFinder.getNodeListByUrl(url);
					for(int subIndex=0;subIndex<list.size();subIndex++){
						INextSection rs=this.extractor.getResourceSection((Element)list.get(subIndex));
						if((rs.getUrl()==null)||(rs.getUrl().equals(""))){
							System.out.println("TwoLevelFinder#getSection: section is empty:");
						}else{
							returnValue.add(rs);
						}
					}
				}
				return returnValue;
			}else{
				return null;
			}
		}
	}

}
