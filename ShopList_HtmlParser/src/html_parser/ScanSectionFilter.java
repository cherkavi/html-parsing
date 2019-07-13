package html_parser;

import org.w3c.dom.Element;

/** объект, который присоединяется в качестве слушателя к {@link WalkSection} и говорит, можно ли сканировать данный объект или нет */
public class ScanSectionFilter {
	/** имя аттрибута элемента, который содержит ссылку */
	protected static String captionHref="href";
	/** имя аттрибута элемента, который содержит заголовок */
	protected static String captionCaption="caption";
	/** позволяет ли данный слушатель сканировать указанный элемент 
	 * <li> true - нужно сканировать элемент </li>
	 * <li> false - пропустить элемент </li>
	 * @param element - элемент, по которому принимается решение
	 * для анализа элемента, необходимо посмотреть создание этого элемента в {@link SectionXml#getElement}
	 * <br>
	 * ( attributes "href" и "caption" ) 
	 * */
	public boolean isFilter(Element element){
		//String href=element.getAttribute("href");
		//String caption=element.getAttribute("caption");
		return true;
	}

	/** получить из элемента значение заголовка */
	protected String getCaption(Element element){
		return element.getAttribute(captionCaption);
	}
	
	/** получить из элемента значение ссылки */
	protected String getHref(Element element){
		return element.getAttribute(captionHref);
	}
}
