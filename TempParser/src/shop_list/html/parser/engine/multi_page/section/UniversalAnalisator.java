package shop_list.html.parser.engine.multi_page.section;

import shop_list.html.parser.engine.multi_page.section.url_next_section.Analisator;

/** универсальный объект для получения страниц из указанных секций на основании заранее заданных правил  */
public class UniversalAnalisator extends NextSection{
	private Analisator analisator;
	
	/** универсальный объект для получения страниц из указанных секций на основании заранее заданных правил  */
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
