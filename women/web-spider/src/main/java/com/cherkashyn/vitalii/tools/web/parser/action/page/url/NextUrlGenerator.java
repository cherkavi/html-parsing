package com.cherkashyn.vitalii.tools.web.parser.action.page.url;

/**
 * generator of the next step 
 */
public interface NextUrlGenerator {
	
	/**
	 * generate next step, based on previous url 
	 * @param previousUrl
	 * @return
	 */
	String getNextUrl(String previousUrl);

	
}
