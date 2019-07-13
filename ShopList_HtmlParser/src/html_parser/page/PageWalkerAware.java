package html_parser.page;

/** класс, который "знает" как составить ссылку для получения следующей страницы, без контроля существования составленной страницы  */
public abstract class PageWalkerAware {
	/** ссылка на страницу, которая имеет номер в разделе
	 * @param number - номер страницы, который должен быть 
	 * @return полную ссылку на интересующий номер 
	 */
	public abstract String getUrl(int number);
	
	/** устновить новый корневой Url для поиска данных 
	 * @param baseHref - новый корневой URL 
	 * */
	public abstract void reset(String baseHref);
}
