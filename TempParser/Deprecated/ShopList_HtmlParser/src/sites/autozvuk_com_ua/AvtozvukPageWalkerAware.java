package sites.autozvuk_com_ua;

import html_parser.page.PageWalkerAware;

public class AvtozvukPageWalkerAware extends PageWalkerAware{
	private String url;
	
	
	public AvtozvukPageWalkerAware(){
	}
	
	public void reset(String baseUrl){
		this.url=baseUrl;
	}
	
	@Override
	public String getUrl(int number) {
		return this.url+"&page="+number;
	}

}
