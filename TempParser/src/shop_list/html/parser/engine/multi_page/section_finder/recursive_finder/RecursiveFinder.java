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
 * ����������� ����� ������ 
 */
public class RecursiveFinder implements ISectionFinder{
	public static boolean debug=false;
	private ArrayList<Node> mainList;
	private IUrlFromElementExtractor mainUrlExtractor;
	private NodeListFinderByUrl branchFinderByUrl;
	private IResourceFromElementExtractor extractor;
	private IUrlFromElementExtractor branchUrlExtractor;
	/** �����������-���������� ������ URL ������� �� ����� ������ */
	public int getMaxLenghtOfUrl(){
		return(400);
	}
	
	
	/**
	 * @param mainList - ������ ���������, �� ������� ������� ������ ������ �� ��������, ������� �������� ��������� ������� ��������
	 * @param mainUrlExtractor - ���������� �� ��������� ������ 
	 * @param branchFinderByUrl - ����� ��������� ����� � ��������
	 * @param branchUrlExtractor - ��������� �� ����� ������ �� ��������� ��������� ���� � �������� ( ��� �� ��������� �������� ) (<b> ��� ������������� ������ ����������� {@link CurrentUrlFromElementExtractor} ��� ������������ ������ � �������������� ������������ ����������� {@link CurrentUrlFromElementExtractorAdapterUnique} </b>) 
	 * @param extractor - �������� �� ��������� ����� {@link NextSection} (<b> ��� ������������� ������ ����������� {@link CurrentResourceFromElementExtractor} </b>)
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
	
	/** ������������� ArrayList[Node] � ArrayList[SectionElement] */
	private ArrayList<SectionElement> getSectionElementList(ArrayList<Node> nodeList){
		ArrayList<SectionElement> returnValue=new ArrayList<SectionElement>();
		if(nodeList!=null){
			for(int counter=0;counter<nodeList.size();counter++){
				returnValue.add(new SectionElement(nodeList.get(counter)));
			}
		}
		return returnValue;
	}
	
	/** ��������� ��������� ������ �� ������������� ( �� ���� "���� �� �������� ������ �� ����� ?") */
	private boolean checkControlNodeList(ArrayList<Node> nodeList){
		ArrayList<SectionElement> list=this.getSectionElementList(nodeList);
		for(int counter=0;counter<controlNodeList.size();counter++){
			if(nodesEquals(controlNodeList.get(counter), list)==true){
				// �������� ������ ��� ����
				return false;
			}
		}
		// ������ ��� 
		controlNodeList.add(list);
		return true;
	}
	
	/** ��������� �� ��������������� ��� ������ */
	private boolean nodesEquals(ArrayList<SectionElement> one, ArrayList<SectionElement> two){
		if(one==null&&two==null){
			// ��� �������� ����� null
			return true;
		}else{
			if(one==null||two==null){
				// ���� �� ��������� ����� null
				return false;
			}
			if(one.size()!=two.size()){
				// ����������� ���� ������� ������
				return false;
			}
			boolean returnValue=true;
			for(int counter=0;counter<one.size();counter++){
				if(!one.get(counter).equals(two.get(counter))){
				// if(!one.get(counter).equals(two.get(counter))){
					// ��������� �������� �� �����
					returnValue=false;
					break;
				}
			}
			return returnValue;
		}
	}
	
	/** �������� ���������� ������ ����������� ��������  */
	private void clearControlNodeList(){
		this.controlNodeList.clear();
	}
	
	/** ��������� ������ �� ��������� ������������ ������
	 * @param returnValue - ������, ������� ������ ���� �������� ��������� �������
	 * @param url - ������ �� ��������� �������� ��������� ��������
	 * @param sectionFinder - ��������� ��� ������
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
			// �� ������� �������� ��������� �����
			return false;
		}else{
			// ��������� ����� ��������
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
					// ������� ����� �������� ������ - ��������� 
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
			// �������� ������ ���� ������
			for(int counter=0;counter<mainList.size();counter++){
				if((mainList.get(counter)!=null)&&(mainList.get(counter) instanceof Element)){
					String url=this.mainUrlExtractor.getUrlFromElement((Element)mainList.get(counter));
					// ����� ��������� � ��������� ��������� � ���������
					if(fillResourceSectionByLeaf(returnValue, url, this.branchFinderByUrl,this.branchUrlExtractor, this.extractor)==false){
						// ������� ����� �������� ������ - ��������� 
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

/** ���� �������, ������� �������������� ������ */
class SectionElement implements Serializable{
	private final static long serialVersionUID=1L;
	private String value=null;
	
	/** ���� �������, ������� �������������� ������ */
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
				// ��� ����� null
				return true;
			}
			if(another.value==null){
				// ���� �� ��������� ����� null
				return false;
			}
			if(this.value==null){
				// ���� �� ��������� ����� null
				return false;
			}
			return another.value.equals(this.value);
		}else{
			return false;
		}
	}
}