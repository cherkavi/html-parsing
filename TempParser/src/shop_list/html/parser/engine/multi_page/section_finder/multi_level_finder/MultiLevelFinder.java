package shop_list.html.parser.engine.multi_page.section_finder.multi_level_finder;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.RecursiveFinder;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;

/** ��������������� "�����������" ������ - ����� ���� ������� ���-�� ������� ����������� ������ ( � ��� ��� ������, ���� ���������� - ��������� {@link RecursiveFinder}) */
public class MultiLevelFinder implements ISectionFinder{

	private IResourceFromElementExtractor extractor;
	private final FindLevel[] levels;
	private String startPage;
	
	/** ���������� ��� ���� ������� ����������� �� �������, <br>
	 * �� ���� ���� ��������� �������� � �������� � ��������� ��� ������ ������ 
	 * <br> ������ ���� ������� �����������
	 * @param startPage - ��������� �������� ��� ������� 
	 * @param extractor - ���������� ��������� ��������� �� ��������� ������� ������ ����������� ���������
	 * @param levels - ������ �������� ������� ( � ��������� ������ �� ����� �������� {@link IUrlFromElementExtractor}, �� ���� ����� ���� null ), ����� �������� {@link IResourceFromElementExtractor}
	 */
	public MultiLevelFinder(String startPage, 
							IResourceFromElementExtractor extractor, 
							FindLevel ... levels ){
		this.levels = levels;
		this.extractor=extractor;
		this.startPage=startPage;
	}
	
	
	private void recursiveFinderCall(ArrayList<INextSection> returnValue, int findLevel, String url){
		if(findLevel==(levels.length-1)){
			// this is last level - fill it
			ArrayList<Node> nodeList=this.levels[findLevel].getUrlFinder().getNodeListByUrl(url);
			if(nodeList!=null)
			for(int counter=0;counter<nodeList.size();counter++){
				try{
					returnValue.add(this.extractor.getResourceSection((Element)nodeList.get(counter)));
				}catch(Exception ex){
					System.err.println("MultiLevelFinder#recursiveFinderCall Exception:"+ex.getMessage());
				}
			}
		}else{
			// recursive find
			ArrayList<Node> nodeList=this.levels[findLevel].getUrlFinder().getNodeListByUrl(url);
			for(int counter=0;counter<nodeList.size();counter++){
				recursiveFinderCall(returnValue, findLevel+1, this.levels[findLevel].getUrlExtractor().getUrlFromElement((Element)nodeList.get(counter)));
			}
		}
	}
	
	
	@Override
	public ArrayList<INextSection> getSection() {
		if((levels==null)||(levels.length==0)){
			return null;
		}
		ArrayList<INextSection> returnValue=new ArrayList<INextSection>();
		recursiveFinderCall(returnValue, 0, startPage);
		
		return returnValue;
	}

}
