package shop_list.html.parser.engine.multi_page.section_finder.multi_level_finder;

import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;

/** ���� ������� ����������� ��� ������ ������  */
public class FindLevel {
	private NodeListFinderByUrl urlFinder;
	private IUrlFromElementExtractor urlExtractor;
	
	public FindLevel(NodeListFinderByUrl urlFinder,IUrlFromElementExtractor urlExtractor){
		this.urlExtractor=urlExtractor;
		this.urlFinder=urlFinder;
	}

	/** �������� ������, ������� ����� �������� �������� �� ��������� ��������  */
	public NodeListFinderByUrl getUrlFinder() {
		return urlFinder;
	}

	/** �������� ������, ������� ����� �������� �� ��������� ������ �� ��������� �������� */
	public IUrlFromElementExtractor getUrlExtractor() {
		return urlExtractor;
	}
	
	
}
