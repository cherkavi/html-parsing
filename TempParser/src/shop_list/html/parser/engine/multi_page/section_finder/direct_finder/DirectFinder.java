package shop_list.html.parser.engine.multi_page.section_finder.direct_finder;

import java.util.ArrayList;


import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;

/** прямое получение секций на основании списка элементов и экстрактора */
public class DirectFinder implements ISectionFinder{
	private ArrayList<Node> elements=null;
	private IResourceFromElementExtractor extractor=null;
	
	/** прямое получение секций на основании списка элементов и экстрактора
	 * @param elements - список элементов
	 * @param extractor - экстрактор для сессий из элементов ( может возвращать null, тогда "раздел" не будет добавлен ) 
	 */
	public DirectFinder(ArrayList<Node> elements, 
				  		IResourceFromElementExtractor extractor){
		this.elements=elements;
		this.extractor=extractor;
	}
	@Override
	public ArrayList<INextSection> getSection() {
		if((elements!=null)&&(elements.size()>0)){
			ArrayList<INextSection> returnValue=new ArrayList<INextSection>();
			for(int counter=0;counter<this.elements.size();counter++){
				INextSection section=null;
				try{
					section=this.extractor.getResourceSection((Element)this.elements.get(counter));
					// System.out.println(section.getName()+"  "+section.getUrl());
					if(section!=null)returnValue.add(section);
				}catch(Exception ex){
					System.err.println("#getSection Exception:"+ex.getMessage());
				}
			}
			return returnValue;
		}else{
			System.err.println("!!! Check main list node !!!");
			return null;
		}
	}
	
}
