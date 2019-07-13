package com.cherkashyn.vitalii.whore.interfaces.reestr;

import java.util.Map;

import com.cherkashyn.vitalii.whore.interfaces.spider.Spider;

public interface Reestr {
	/**
	 * @return all accessible spiders and corresponds number for them 
	 */
	Map<Spider, Integer> getList();
}
