package shop_list.html.parser.engine.multi_page.section_finder.recursive_finder;

import java.io.Serializable;
import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.element_extractor.CurrentUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.element_extractor.CurrentUrlFromElementExtractorAdapterUnique;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.CurrentResourceFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;

/**
 * рекурсивный поиск секций 
 */
public class RecursiveFinder implements ISectionFinder{
	public static boolean debug=false;
	private ArrayList<Node> mainList;
	private IUrlFromElementExtractor mainUrlExtractor;
	private NodeListFinderByUrl branchFinderByUrl;
	private IResourceFromElementExtractor extractor;
	private IUrlFromElementExtractor branchUrlExtractor;
	/** максимально-допустимая длинна URL запроса на поиск секции */
	public int getMaxLenghtOfUrl(){
		return(400);
	}
	
	
	/**
	 * @param mainList - список элементов, из которых следует изъять ссылки на страницы, которые являются возможным началом рекурсии
	 * @param mainUrlExtractor - экстрактор из элементов ссылок 
	 * @param branchFinderByUrl - поиск очередной ветви в рекурсии
	 * @param branchUrlExtractor - получение из ветви ссылки на очередной возможный лист в рекурсии ( или на следующую подветвь ) (<b> для относительных ссылок используйте {@link CurrentUrlFromElementExtractor} для уникальности ссылок и предотвращения зацикливания используйте {@link CurrentUrlFromElementExtractorAdapterUnique} </b>) 
	 * @param extractor - получить из конечного листа {@link NextSection} (<b> для относительных ссылок используйте {@link CurrentResourceFromElementExtractor} </b>)
	 */
	public RecursiveFinder(ArrayList<Node> mainList,  
						   IUrlFromElementExtractor mainUrlExtractor,
						   NodeListFinderByUrl branchFinderByUrl, 
						   IUrlFromElementExtractor branchUrlExtractor,
						   IResourceFromElementExtractor extractor){
		this.mainList=mainList;
		this.mainUrlExtractor=mainUrlExtractor;
		this.branchFinderByUrl=branchFinderByUrl;
		this.branchUrlExtractor=branchUrlExtractor;
		this.extractor=extractor;
	}
	
	private ArrayList<ArrayList<SectionElement>> controlNodeList=new ArrayList<ArrayList<SectionElement>>();
	
	/** преобразовать ArrayList[Node] в ArrayList[SectionElement] */
	private ArrayList<SectionElement> getSectionElementList(ArrayList<Node> nodeList){
		ArrayList<SectionElement> returnValue=new ArrayList<SectionElement>();
		if(nodeList!=null){
			for(int counter=0;counter<nodeList.size();counter++){
				returnValue.add(new SectionElement(nodeList.get(counter)));
			}
		}
		return returnValue;
	}
	
	/** проверить очередную секцию на повторяемость ( то есть "была ли подобная секция до этого ?") */
	private boolean checkControlNodeList(ArrayList<Node> nodeList){
		ArrayList<SectionElement> list=this.getSectionElementList(nodeList);
		for(int counter=0;counter<controlNodeList.size();counter++){
			if(nodesEquals(controlNodeList.get(counter), list)==true){
				// подобный список уже есть
				return false;
			}
		}
		// списка нет 
		controlNodeList.add(list);
		return true;
	}
	
	/** проверить на эквивалентность два списка */
	private boolean nodesEquals(ArrayList<SectionElement> one, ArrayList<SectionElement> two){
		if(one==null&&two==null){
			// оба элемента равны null
			return true;
		}else{
			if(one==null||two==null){
				// один из элементов равен null
				return false;
			}
			if(one.size()!=two.size()){
				// размерность двух списков разная
				return false;
			}
			boolean returnValue=true;
			for(int counter=0;counter<one.size();counter++){
				if(!one.get(counter).equals(two.get(counter))){
				// if(!one.get(counter).equals(two.get(counter))){
					// очередные элементы не равны
					returnValue=false;
					break;
				}
			}
			return returnValue;
		}
	}
	
	/** очистить внутренний реестр заполненных значений  */
	private void clearControlNodeList(){
		this.controlNodeList.clear();
	}
	
	/** наполнить секцию на основании рекурсивного вызова
	 * @param returnValue - объект, который должен быть наполнен конечными листами
	 * @param url - ссылка на стартовую страницу возможной рекурсии
	 * @param sectionFinder - поисковик для ссылок
	 * @return 
	 */
	private boolean fillResourceSectionByLeaf(ArrayList<INextSection> returnValue, 
										   String url, 
										   NodeListFinderByUrl subListFinder,
										   IUrlFromElementExtractor subListExtractor,
										   IResourceFromElementExtractor resourceExtractor
										   ){
		if(debug)System.out.println(url);
		if(url==null||url.length()>this.getMaxLenghtOfUrl())return false;
		ArrayList<Node> list=subListFinder.getNodeListByUrl(url);
		if((list==null)||(list.size()==0)){
			// не удалось получить очередную ветку
			return false;
		}else{
			// очередная ветка получена
			if(checkControlNodeList(list)==false){
				System.out.println("Collision");
				return false;
			}
			for(int counter=0;counter<list.size();counter++){
				if(subListExtractor instanceof CurrentUrlFromElementExtractor){
					((CurrentUrlFromElementExtractor)subListExtractor).setCurrentUrl(url);
				}
				String subListUrl=subListExtractor.getUrlFromElement((Element)list.get(counter));
				if(fillResourceSectionByLeaf(returnValue, 
											 subListUrl, 
											 subListFinder, 
											 subListExtractor,
											 resourceExtractor)==false){
					// текущая ветка является листом - заполнить 
					if(resourceExtractor instanceof CurrentResourceFromElementExtractor){
						((CurrentResourceFromElementExtractor)resourceExtractor).setCurrentUrl(url);
					}
					returnValue.add(resourceExtractor.getResourceSection((Element)list.get(counter)));
				}
			}
			return true;
		}
	}
	
	@Override
	public ArrayList<INextSection> getSection() {
		try{
			ArrayList<INextSection> returnValue=new ArrayList<INextSection>();
			// получить первый блок данных
			for(int counter=0;counter<mainList.size();counter++){
				if((mainList.get(counter)!=null)&&(mainList.get(counter) instanceof Element)){
					String url=this.mainUrlExtractor.getUrlFromElement((Element)mainList.get(counter));
					// поиск элементов в указанных страницах с рекурсией
					if(fillResourceSectionByLeaf(returnValue, url, this.branchFinderByUrl,this.branchUrlExtractor, this.extractor)==false){
						// текущая ветка является листом - заполнить 
						returnValue.add(this.extractor.getResourceSection((Element)mainList.get(counter)));
					}
				}
			}
			clearControlNodeList();
			return returnValue;
		}catch(Exception ex){
			System.err.println("RecursiveFinder#getSection Exception:"+ex.getMessage());
			return null;
		}
	}

}

/** один элемент, который идентифицирует секцию */
class SectionElement implements Serializable{
	private final static long serialVersionUID=1L;
	private String value=null;
	
	/** один элемент, который идентифицирует секцию */
	public SectionElement(Node node){
		if(node!=null){
			value=node.getTextContent();
		}else{
			value=null;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof SectionElement){
			SectionElement another=(SectionElement)obj;
			if((another.value==null)&&(this.value==null)){
				// оба равны null
				return true;
			}
			if(another.value==null){
				// один из элементов равен null
				return false;
			}
			if(this.value==null){
				// один из элементов равен null
				return false;
			}
			return another.value.equals(this.value);
		}else{
			return false;
		}
	}
}