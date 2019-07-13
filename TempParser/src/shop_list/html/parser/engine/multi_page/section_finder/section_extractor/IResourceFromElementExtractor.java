package shop_list.html.parser.engine.multi_page.section_finder.section_extractor;

import org.w3c.dom.Element;

import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.NextSection;

/** ���������, ���������� {@link NextSection} �� ��������� �������� */
public interface IResourceFromElementExtractor {
	
	/** �������� {@link NextSection} �� ��������� �������� */
	public INextSection getResourceSection(Element element);
}
