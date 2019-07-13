package shop_list.html.parser.engine.multi_page.section_finder.element_extractor;

import org.w3c.dom.Element;

import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;

/** получение из элемента A ( anchor ) href аттрибута  */
public class AnchorElementExtractor implements IUrlFromElementExtractor{

	/** получение из элемента A ( anchor ) href аттрибута  */
	public AnchorElementExtractor(){
		this(null);
	}
	
	/** получение из элемента A ( anchor ) href аттрибута  
	 * @param preambula - нужно ли прибавлять к элементу ( в начало ) строку ( если получаем из элемента относительный путь )
	 * */
	public AnchorElementExtractor(String preambula){
		this.preambula=null;
	}
	
	private String preambula=null;
	
	
	/** является ли строка пустой */
	protected boolean isStringEmpty(String value){
		return (value==null)||(value.trim().equals(""));
	}
	
	@Override
	public String getUrlFromElement(Element element) {
		String url=element.getAttribute("href");
		if(isStringEmpty(url))return null;
		if(preambula!=null){
			return preambula+url;
		}else{
			return url;
		}
	}
	
}
