package shop_list.html.parser.engine.multi_page.section_finder.section_extractor;

import org.w3c.dom.Element;

import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.NextSection;

/** интерфейс, получающий {@link NextSection} на основании элемента */
public interface IResourceFromElementExtractor {
	
	/** получить {@link NextSection} на основании элемента */
	public INextSection getResourceSection(Element element);
}
