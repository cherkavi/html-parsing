package shop_list.html.parser.engine.multi_page.section_finder.element_extractor;

import java.util.ArrayList;

public class UniqueUrlController {
	private ArrayList<String> uniqueList=new ArrayList<String>();
	
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
		if(uniqueList.indexOf(url)<0){
			uniqueList.add(url);
			return true;
		}else{
			return false;
		}
	}
	
}
