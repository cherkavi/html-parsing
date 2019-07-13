package shop_list.html.parser.engine.multi_page.section_finder.recursive_finder;

import java.util.ArrayList;

import org.w3c.dom.Element;

import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;

/** рекурсивный поиск секций, на основании указаний на элементы A, которые содержат аттрибут href */
public class AnchorRecursiveFinder implements ISectionFinder{
	private RecursiveFinder recursiveFinder=null;
	
	/** получить имя аттрибута, который будет "выниматься" из каждого листа */
	protected String getDefaultAttribute(){
		return "href";
	}

	/** рекурсивный поиск секций, на основании указаний на элементы A, которые содержат аттрибут href ({@link #getDefaultAttribute()}) 
	 * @param mainUrl - точка входа для первого поиска элементов ( должны быть всегда )
	 * @param mainFinderByUrl - поиск центральных элементов  
	 * @param branchFinderByUrl - поиск очередной ветви в рекурсии
	 * @param extractor - получить из конечного листа {@link NextSection}
	 * */	
	public AnchorRecursiveFinder(String mainUrl, 
			 NodeListFinderByUrl mainFinderByUrl, 
			 NodeListFinderByUrl branchFinderByUrl,
			 IResourceFromElementExtractor extractor) {
		this(mainUrl, mainFinderByUrl, branchFinderByUrl, extractor, null);
	}

	
	/** рекурсивный поиск секций, на основании указаний на элементы A, которые содержат аттрибут href ({@link #getDefaultAttribute()}) 
	 * @param mainUrl - точка входа для первого поиска элементов ( должны быть всегда )
	 * @param mainFinderByUrl - поиск центральных элементов  
	 * @param branchFinderByUrl - поиск очередной ветви в рекурсии
	 * @param extractor - получить из конечного листа {@link NextSection}
	 * @param preambule - обязательный префикс для добавления в каждую найденную ссылку на секцию ( если путь в найденных секциях указан относительный )
	 * */	
	public AnchorRecursiveFinder(String mainUrl, 
								 NodeListFinderByUrl mainFinderByUrl, 
								 NodeListFinderByUrl branchFinderByUrl,
								 IResourceFromElementExtractor extractor,
								 final String preambule) {
		IUrlFromElementExtractor extractorUrl=new IUrlFromElementExtractor() {
			@Override
			public String getUrlFromElement(Element element) {
				String url=element.getAttribute(getDefaultAttribute());
				if(isStringEmpty(url))return null;
				if(preambule!=null){
					return preambule+url;
				}else{
					return url;
				}
			}
		};

		recursiveFinder=new RecursiveFinder(mainFinderByUrl.getNodeListByUrl(mainUrl),
										    extractorUrl,
											branchFinderByUrl,
											extractorUrl,
											extractor
											);
	}
	/** является ли строка пустой */
	private boolean isStringEmpty(String value){
		return (value==null)||(value.trim().equals(""));
	}

	@Override
	public ArrayList<INextSection> getSection() {
		return recursiveFinder.getSection();
	}

}
