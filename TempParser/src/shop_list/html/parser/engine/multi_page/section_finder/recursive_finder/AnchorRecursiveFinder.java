package shop_list.html.parser.engine.multi_page.section_finder.recursive_finder;

import java.util.ArrayList;

import org.w3c.dom.Element;

import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.IUrlFromElementExtractor;
import shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;

/** ����������� ����� ������, �� ��������� �������� �� �������� A, ������� �������� �������� href */
public class AnchorRecursiveFinder implements ISectionFinder{
	private RecursiveFinder recursiveFinder=null;
	
	/** �������� ��� ���������, ������� ����� "����������" �� ������� ����� */
	protected String getDefaultAttribute(){
		return "href";
	}

	/** ����������� ����� ������, �� ��������� �������� �� �������� A, ������� �������� �������� href ({@link #getDefaultAttribute()}) 
	 * @param mainUrl - ����� ����� ��� ������� ������ ��������� ( ������ ���� ������ )
	 * @param mainFinderByUrl - ����� ����������� ���������  
	 * @param branchFinderByUrl - ����� ��������� ����� � ��������
	 * @param extractor - �������� �� ��������� ����� {@link NextSection}
	 * */	
	public AnchorRecursiveFinder(String mainUrl, 
			 NodeListFinderByUrl mainFinderByUrl, 
			 NodeListFinderByUrl branchFinderByUrl,
			 IResourceFromElementExtractor extractor) {
		this(mainUrl, mainFinderByUrl, branchFinderByUrl, extractor, null);
	}

	
	/** ����������� ����� ������, �� ��������� �������� �� �������� A, ������� �������� �������� href ({@link #getDefaultAttribute()}) 
	 * @param mainUrl - ����� ����� ��� ������� ������ ��������� ( ������ ���� ������ )
	 * @param mainFinderByUrl - ����� ����������� ���������  
	 * @param branchFinderByUrl - ����� ��������� ����� � ��������
	 * @param extractor - �������� �� ��������� ����� {@link NextSection}
	 * @param preambule - ������������ ������� ��� ���������� � ������ ��������� ������ �� ������ ( ���� ���� � ��������� ������� ������ ������������� )
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
	/** �������� �� ������ ������ */
	private boolean isStringEmpty(String value){
		return (value==null)||(value.trim().equals(""));
	}

	@Override
	public ArrayList<INextSection> getSection() {
		return recursiveFinder.getSection();
	}

}
