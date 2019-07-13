package shop_list.html.parser.engine.multi_page.section;

/** секци€/раздел дл€ сайта, котора€ ссылаетс€ на группу страниц, которые содержат элементы (которые, в свою очередь, содержат записи)
 * <br><b>главна€ цель объекта - получение следующей группы элементов ( которые содержат записи )</b>
 *  */
public abstract class NextSection implements INextSection {
	/** коренной уровень HTML страницы */
	private String basePage;
	/** полное им€ секции */
	private String name;
	/** ссылка на первую страницу секции */
	private String url;
	
	/** секци€/раздел дл€ сайта, котора€ ссылаетс€ на группу страниц, которые содержат элементы (которые, в свою очередь, содержат записи)
	 * <br><b>главна€ цель объекта - получение следующей группы элементов ( которые содержат записи )</b>
	 * @param name - наименование раздела ( секции )
	 * @param url - полный адрес первой страницы дл€ перехода 
	 *  */
	public NextSection(String name, String url ){
		this.name=name;
		if(this.name!=null)this.name=this.name.trim();
		this.url=url;
	}

	/** секци€/раздел дл€ сайта, котора€ ссылаетс€ на группу страниц, которые содержат элементы (которые, в свою очередь, содержат записи)
	 * @param basePage - коренной уровень HTML страницы 
	 * @param name - наименование раздела ( секции )
	 * @param url - полный адрес первой страницы дл€ перехода 
	 *  */
	public NextSection(String basePage, String name, String url ){
		this.name=name;
		this.url=url;
	}
	
	/** получить им€ раздела */
	public String getName() {
		return name;
	}

	/** получить полную ссылку на раздел  */
	public String getUrl() {
		return url;
	}
	
	/** установить базовую ссылку на страницу */
	public void setUrl(String url){
		this.url=url;
	}
	
	/** получить HTML-адрес базовой страницы */
	public String getBasePage(){
		return this.basePage;
	}
	
	/** получить html-адрес следующей страницы  */
	@Override
	public abstract String getUrlToNextPage();
}
