package shop_list.html.parser.engine.multi_page.section.url_next_section;

/** интерфейс по получению номера страницы на основании первой страницы и номера возвращаемой */
public interface IGetPageByNumber {
	public String getPageByNumber(String startPageSection, int page);
}
