package shop_list.html.parser.engine.multi_page.section_finder;

import java.util.ArrayList;

import shop_list.html.parser.engine.multi_page.section.INextSection;

/** ���������, ������� ���������� ��� ������ ��� ������ �����  */
public interface ISectionFinder {
	/** �������� ��� ������  */
	public ArrayList<INextSection> getSection();
}
