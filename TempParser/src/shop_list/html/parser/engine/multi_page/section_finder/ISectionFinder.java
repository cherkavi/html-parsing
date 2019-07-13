package shop_list.html.parser.engine.multi_page.section_finder;

import java.util.ArrayList;

import shop_list.html.parser.engine.multi_page.section.INextSection;

/** интерфейс, который возвращает все секции для поиска сайту  */
public interface ISectionFinder {
	/** получить все секции  */
	public ArrayList<INextSection> getSection();
}
