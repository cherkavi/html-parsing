package com.cherkashyn.vitalii.tools.web.parser.domain.element;

import com.cherkashyn.vitalii.tools.web.parser.action.page.url.NextUrlGenerator;

public class Page {
	
	private NextUrlGenerator generator;
	private String currentUrl;
	
	public Page(NextUrlGenerator nextUrlGenerator){
		this.generator=nextUrlGenerator;
		this.reset();
	}
	
	public String getUrlCurrent(){
		return this.currentUrl;
	}
	
	public String getUrlNext(){
		this.currentUrl=this.generator.getNextUrl(currentUrl);
		return getUrlCurrent();
	}

	public void reset(){
		this.currentUrl=null;
	}
}
