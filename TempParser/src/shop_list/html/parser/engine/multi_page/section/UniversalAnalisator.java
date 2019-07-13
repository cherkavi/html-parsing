package shop_list.html.parser.engine.multi_page.section;

import shop_list.html.parser.engine.multi_page.section.url_next_section.Analisator;

/** ������������� ������ ��� ��������� ������� �� ��������� ������ �� ��������� ������� �������� ������  */
public class UniversalAnalisator extends NextSection{
	private Analisator analisator;
	
	/** ������������� ������ ��� ��������� ������� �� ��������� ������ �� ��������� ������� �������� ������  */
	public UniversalAnalisator(String name, 
							   String url,
							   Analisator analisator) {
		super(name, url);
		this.analisator=analisator;
	}

	private int pageCounter=0;
	@Override
	public String getUrlToNextPage() {
		pageCounter++;
		return analisator.getPageByNumber(getUrl(), pageCounter);
	}

}
