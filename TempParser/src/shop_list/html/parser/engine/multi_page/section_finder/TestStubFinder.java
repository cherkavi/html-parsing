package shop_list.html.parser.engine.multi_page.section_finder;

import java.util.ArrayList;

import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.NextSection;

/** тестовая заглушка  */
public class TestStubFinder implements ISectionFinder{
	private NextSection section=null;
	
	/** тестовая заглушка  */
	public TestStubFinder(NextSection section){
		this.section=section;
	}
	
	@Override
	public ArrayList<INextSection> getSection() {
		return new ArrayList<INextSection>(){
			private final static long serialVersionUID=1L;
			{
				this.add(section);
			}
		};
	}

}
