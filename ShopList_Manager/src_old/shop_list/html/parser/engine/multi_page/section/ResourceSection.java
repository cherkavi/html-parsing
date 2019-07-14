package shop_list.html.parser.engine.multi_page.section;

/** секция/раздел для сайта, которая ссылается на группу страниц, которые содержат элементы  */
public abstract class ResourceSection {
	/** коренной уровень HTML страницы */
	private String basePage;
	/** полное имя секции */
	private String name;
	/** ссылка на первую страницу секции */
	private String url;
	
	/** секция/раздел для сайта, которая ссылается на группу страниц, которые содержат элементы (которые, в свою очередь, содержат записи)
	 * @param name - наименование раздела ( секции )
	 * @param url - полный адрес первой страницы для перехода 
	 *  */
	public ResourceSection(String name, String url ){
		this.name=name;
		this.url=url;
	}

	/** секция/раздел для сайта, которая ссылается на группу страниц, которые содержат элементы (которые, в свою очередь, содержат записи)
	 * @param basePage - коренной уровень HTML страницы 
	 * @param name - наименование раздела ( секции )
	 * @param url - полный адрес первой страницы для перехода 
	 *  */
	public ResourceSection(String basePage, String name, String url ){
		this.name=name;
		this.url=url;
	}
	
	/** получить имя раздела */
	public String getName() {
		return name;
	}

	/** получить полную ссылку на раздел  */
	public String getUrl() {
		return url;
	}
	
	/** получить HTML-адрес базовой страницы */
	public String getBasePage(){
		return this.basePage;
	}
	
	/** получить html-адрес следующей страницы  */
	public abstract String getUrlToNextPage();
}
