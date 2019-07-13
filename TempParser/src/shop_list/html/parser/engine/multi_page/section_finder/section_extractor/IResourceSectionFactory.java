package shop_list.html.parser.engine.multi_page.section_finder.section_extractor;

import shop_list.html.parser.engine.multi_page.section.INextSection;

public interface IResourceSectionFactory {
	/** получить на основании
	 * @param name - им€ секции
	 * @param url - ссылка ( ѕќЋЌјя http:// )
	 * @return 
	 */
	public INextSection getResourceSection(String name, String url);
}
