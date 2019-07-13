package shop_list.html.parser.engine.multi_page.section_finder.multi_level_finder;

import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;

/** один уровень вложенности для поиска секций  */
public class FindLevel {
	private NodeListFinderByUrl urlFinder;
	private IUrlFromElementExtractor urlExtractor;
	
	public FindLevel(NodeListFinderByUrl urlFinder,IUrlFromElementExtractor urlExtractor){
		this.urlExtractor=urlExtractor;
		this.urlFinder=urlFinder;
	}

	/** получить объект, который будет вынимать элементы из очередной страницы  */
	public NodeListFinderByUrl getUrlFinder() {
		return urlFinder;
	}

	/** получить объект, который будет вынимать из элементов ссылки на следующие страницы */
	public IUrlFromElementExtractor getUrlExtractor() {
		return urlExtractor;
	}
	
	
}
