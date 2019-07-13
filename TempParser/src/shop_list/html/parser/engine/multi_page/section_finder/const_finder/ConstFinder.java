package shop_list.html.parser.engine.multi_page.section_finder.const_finder;

import java.util.ArrayList;

import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceSectionFactory;

/** заранее заданный список секций  */
public class ConstFinder implements ISectionFinder{
	private ArrayList<Pair<String,String>> sections;
	private IResourceSectionFactory resourceFactory;

	/** заранее заданный список секций  
	 * @param names - список пар: <имя секции>, <значение секции> 
	 * @param urls - список URL  
	 */
	public ConstFinder(ArrayList<Pair<String,String>> sections, 
					   IResourceSectionFactory factory){
		this.sections=sections;
		resourceFactory=factory;
	}
	
		
	@Override
	public ArrayList<INextSection> getSection() {
		ArrayList<INextSection> returnValue=new ArrayList<INextSection>();
		for(int counter=0;counter<this.sections.size();counter++){
			returnValue.add(resourceFactory.getResourceSection(sections.get(counter).getName(), sections.get(counter).getValue()));
		}
		return returnValue;
	}
}
