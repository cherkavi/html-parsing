package shop_list.html.parser.engine.multi_page.section_finder.section_extractor;

/** объект, который "умеет" добавлять к ссылке текущее положение паука во время парсинга*/
public abstract class CurrentResourceFromElementExtractor implements IResourceFromElementExtractor{

	private String url=null;
	
	/** установить текущий URL  */
	public void setCurrentUrl(String url){
		this.url=url;
	}
	
	/** получить текущий (установленный {@link #setCurrentUrl(String)} URL ) */
	protected String getCurrentUrl(){
		return this.url;
	}

}
