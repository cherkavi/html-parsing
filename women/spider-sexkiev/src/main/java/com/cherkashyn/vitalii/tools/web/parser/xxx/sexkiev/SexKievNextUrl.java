package com.cherkashyn.vitalii.tools.web.parser.xxx.sexkiev;

import org.apache.commons.lang3.StringUtils;

import com.cherkashyn.vitalii.tools.web.parser.action.page.Constants;
import com.cherkashyn.vitalii.tools.web.parser.action.page.url.NextUrlGenerator;

public class SexKievNextUrl implements NextUrlGenerator{
	static final String INIT="http://sexkiev.net";
	private static final String PAGE="page"; 
	private static final String PAGE_DELIMITER="-"; 
	
	/**
	 * examples:
	 * http://sexkiev.net
	 * http://sexkiev.net/page-2
	 * http://sexkiev.net/page-3
	 * 
	 */
	@Override
	public String getNextUrl(String previousUrl) {
		if(StringUtils.trimToNull(previousUrl)==null){
			return INIT;
		}
		// parse first 
		String nextPage=StringUtils.trimToNull(StringUtils.substringAfterLast(previousUrl, Constants.URL_DELIMITER));
		if(nextPage==null || StringUtils.contains(nextPage, PAGE)==false){
			return generateSecondPage(previousUrl);
		}
		
		String header=StringUtils.substringBefore(previousUrl, PAGE_DELIMITER);
		String tail=StringUtils.substringAfter(previousUrl, PAGE_DELIMITER);
		return header+PAGE_DELIMITER+(Integer.parseInt(tail)+1);
	}

	private String generateSecondPage(String previousUrl) {
		String clear=StringUtils.trim(previousUrl);
		if(!StringUtils.endsWith(clear, Constants.URL_DELIMITER)){
			clear=clear+Constants.URL_DELIMITER;
		}
		return clear+PAGE+PAGE_DELIMITER+"2";
	}

}
