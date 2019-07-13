package shop_list.html.parser.engine.multi_page.section_finder.element_extractor;

import java.util.ArrayList;

public class UniqueUrlController {
	private ArrayList<String> uniqueList=new ArrayList<String>();
	
	/**
	 *   �������� ����� URL � ������ ���������� ������� 
	 * @param url - url ��� ���������� 
	 * @return
	 * <ul>
	 * 	<li><b>true</b> - ���������� ������ URL ������� �����������</li>
	 * 	<li><b>true</b> - ������ URL ��� ���� � ������ </li>
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
