package shop_list.html.parser.engine.multi_page.section_finder.element_extractor;

import org.w3c.dom.Element;

import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;

/** получение из аттрибута */
public class AttributeElementExtractor implements IUrlFromElementExtractor{

	/** получение из аттрибута
	 * @param attributeName - имя аттрибута 
	 * @param preambula - (!=null) нужно ли прибавлять к элементу ( в начало ) строку ( если получаем из элемента относительный путь )
	 * или не нужно прибавлять - null
	 * */
	public AttributeElementExtractor(String attributeName, String preambula){
		this.attributeName=null;
		this.preambula=null;
	}
	
	private String preambula=null;
	private String attributeName; 
	
	/** является ли строка пустой */
	protected boolean isStringEmpty(String value){
		return (value==null)||(value.trim().equals(""));
	}
	
	@Override
	public String getUrlFromElement(Element element) {
		String url=element.getAttribute(this.attributeName);
		if(isStringEmpty(url))return null;
		if(preambula!=null){
			return preambula+url;
		}else{
			return url;
		}
	}
	
}
