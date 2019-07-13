package shop_list.html.parser.engine.multi_page.section_finder;

import org.w3c.dom.Element;

/** получить на основании элемента URL ссылку для дальнейшего движения "паука" */
public interface IUrlFromElementExtractor {
	/** получить на основании элемента URL ссылку для дальнейшего движения "паука" */
	public String getUrlFromElement(Element element);
}
