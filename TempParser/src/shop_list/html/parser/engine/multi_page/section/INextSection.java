package shop_list.html.parser.engine.multi_page.section;

/** интерфейс для получения следующей страницы  */
public interface INextSection {
	/** получить html-адрес следующей страницы  */
	public String getUrlToNextPage();
	
	/** Получить полное имя секции  */
	public String getName();
	
	/** получить точку входа в секцию - первую страницу  */
	public String getUrl();
}
