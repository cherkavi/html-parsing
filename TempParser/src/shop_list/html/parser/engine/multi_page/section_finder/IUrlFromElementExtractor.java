package shop_list.html.parser.engine.multi_page.section_finder;

import org.w3c.dom.Element;

/** �������� �� ��������� �������� URL ������ ��� ����������� �������� "�����" */
public interface IUrlFromElementExtractor {
	/** �������� �� ��������� �������� URL ������ ��� ����������� �������� "�����" */
	public String getUrlFromElement(Element element);
}
