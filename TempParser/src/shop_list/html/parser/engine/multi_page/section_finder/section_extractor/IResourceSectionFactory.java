package shop_list.html.parser.engine.multi_page.section_finder.section_extractor;

import shop_list.html.parser.engine.multi_page.section.INextSection;

public interface IResourceSectionFactory {
	/** �������� �� ���������
	 * @param name - ��� ������
	 * @param url - ������ ( ������ http:// )
	 * @return 
	 */
	public INextSection getResourceSection(String name, String url);
}
