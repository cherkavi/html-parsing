package shop_list.html.parser.engine.multi_page.section.url_next_section.single_change;

import shop_list.html.parser.engine.multi_page.section.url_next_section.IGetPageByNumber;

public class DirectPageChange implements IGetPageByNumber{
	private String preambule;
	private String postambule;
	private boolean minusOne=false;
	
	public DirectPageChange(String preambule, String postambule){
		this(preambule, postambule, false);
	}
	
	public DirectPageChange(String preambule, String postambule, boolean minusOne){
		this.preambule=preambule;
		this.postambule=postambule;
		this.minusOne=minusOne;
	}
	
	@Override
	public String getPageByNumber(String sectionStartPage, int page) {
		if(page==1)return sectionStartPage;
		if(minusOne==true){
			return this.preambule+(page-1)+this.postambule;
		}else{
			return this.preambule+page+this.postambule;
		}
	}

}
