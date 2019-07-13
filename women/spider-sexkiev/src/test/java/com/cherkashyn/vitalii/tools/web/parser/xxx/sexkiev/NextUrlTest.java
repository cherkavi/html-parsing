package com.cherkashyn.vitalii.tools.web.parser.xxx.sexkiev;

import junit.framework.Assert;

import org.junit.Test;

public class NextUrlTest {

	@Test
	public void checkNextPageGenerator(){
		// given
		String url="http://sexkiev.net ";
		// when
		String result=new SexKievNextUrl().getNextUrl(url);
		// then 
		Assert.assertEquals("http://sexkiev.net/page-2", result);
	}

	@Test
	public void checkNextPageGenerator2(){
		// given
		String url="http://sexkiev.net/";
		// when
		String result=new SexKievNextUrl().getNextUrl(url);
		// then 
		Assert.assertEquals("http://sexkiev.net/page-2", result);
	}

	@Test
	public void checkNextPageGenerator3(){
		// given
		String url="http://sexkiev.net/page-2";
		// when
		String result=new SexKievNextUrl().getNextUrl(url);
		// then 
		Assert.assertEquals("http://sexkiev.net/page-3", result);
	}

}
