package shop_list.html.parser.engine.multi_page.section_finder.element_extractor;

/** объект, который "умеет" добавлять к ссылке текущее положение паука во время парсинга  
 * <br />
 * обязательно добавлять новую ссылку методом : {@link #addNewUrl(String)} 
 */
public abstract class CurrentUrlFromElementExtractorAdapterUnique extends CurrentUrlFromElementExtractor{
	private String url=null;
	private UniqueUrlController controller=new UniqueUrlController();

	/** объект, который "умеет" добавлять к ссылке текущее положение паука во время парсинга  
	 * <br />
	 * обязательно добавлять новую ссылку методом : {@link #addNewUrl(String)} 
	 */
	public CurrentUrlFromElementExtractorAdapterUnique(){
	}
	
	/** установить текущий URL  */
	public void setCurrentUrl(String url){
		this.url=url;
	}
	
	/** получить текущий (установленный {@link #setCurrentUrl(String)} URL ) */
	protected String getCurrentUrl(){
		return this.url;
	}

	/**
	 *   добавить новый URL в список уникальных адресов 
	 * @param url - url для добавления 
	 * @return
	 * <ul>
	 * 	<li><b>true</b> - Добавление нового URL успешно произведено</li>
	 * 	<li><b>true</b> - Данный URL уже есть в списке </li>
	 * </ul>
	 */
	public boolean addNewUrl(String url){
		return this.controller.addNewUrl(url);
	}
}
