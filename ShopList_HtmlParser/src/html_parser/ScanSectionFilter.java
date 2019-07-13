package html_parser;

import org.w3c.dom.Element;

/** ������, ������� �������������� � �������� ��������� � {@link WalkSection} � �������, ����� �� ����������� ������ ������ ��� ��� */
public class ScanSectionFilter {
	/** ��� ��������� ��������, ������� �������� ������ */
	protected static String captionHref="href";
	/** ��� ��������� ��������, ������� �������� ��������� */
	protected static String captionCaption="caption";
	/** ��������� �� ������ ��������� ����������� ��������� ������� 
	 * <li> true - ����� ����������� ������� </li>
	 * <li> false - ���������� ������� </li>
	 * @param element - �������, �� �������� ����������� �������
	 * ��� ������� ��������, ���������� ���������� �������� ����� �������� � {@link SectionXml#getElement}
	 * <br>
	 * ( attributes "href" � "caption" ) 
	 * */
	public boolean isFilter(Element element){
		//String href=element.getAttribute("href");
		//String caption=element.getAttribute("caption");
		return true;
	}

	/** �������� �� �������� �������� ��������� */
	protected String getCaption(Element element){
		return element.getAttribute(captionCaption);
	}
	
	/** �������� �� �������� �������� ������ */
	protected String getHref(Element element){
		return element.getAttribute(captionHref);
	}
}
