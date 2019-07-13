package shop_list.html.parser.engine.multi_page.section.url_next_section.single_change;

import shop_list.html.parser.engine.multi_page.section.url_next_section.IGetPageByNumber;


public class AddPageChange implements IGetPageByNumber {
	private String preambula;
	private String postambule;
	private int page2Start;
	private int step;
	
	/** 
	 * Прирост страницы на основании постоянного числа
	 * @param preambula - начало
	 * @param postambule - окончание
	 * @param k2 - коэфициент для первой страницы
	 * @param k3 - коэфициент второй страницы
	 */
	public AddPageChange(String preambula, String postambule, int k2, int k3) {
		this.preambula=preambula;
		this.postambule=postambule;
		this.page2Start=k2;
		this.step=k3-k2;
	}

	private int getNumber(int pageNumber){
		int returnValue=page2Start;
		for(int counter=3;counter<=pageNumber;counter++){
			returnValue+=this.step;
		}
		return returnValue;
	}
	
	@Override
	public String getPageByNumber(String sectionStartPage, int page) {
		if(page==1)return sectionStartPage;
		return this.preambula+this.getNumber(page)+this.postambule;
	}

}
