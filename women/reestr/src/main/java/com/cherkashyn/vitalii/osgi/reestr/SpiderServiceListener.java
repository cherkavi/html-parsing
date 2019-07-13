/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.cherkashyn.vitalii.osgi.reestr;

import java.text.MessageFormat;
import java.util.*;

import org.osgi.framework.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherkashyn.vitalii.whore.interfaces.reestr.Reestr;
import com.cherkashyn.vitalii.whore.interfaces.spider.Spider;

/**
 * collect all Spiders to reestr, and provide access to list of reestr
 */
public class SpiderServiceListener implements BundleActivator, Reestr{
	
	private static Logger LOGGER = LoggerFactory.getLogger(SpiderServiceListener.class);
	private final static Integer INIT=100;
	
	private Map<Spider, Integer> reestr=new HashMap<Spider, Integer>();


	public void addSpider(Spider spider){
		int index=getMaxIndex()+1;
		LOGGER.info(MessageFormat.format("add spider({0}):{1}",index,spider.toString()));
		this.reestr.put(spider, index);
	}

	/**
	 * @return - max index of 
	 */
	private int getMaxIndex() {
		if(this.reestr.size()==0){
			return INIT;
		}
		
		int returnValue=INIT;
		for(Map.Entry<Spider, Integer> eachEntry:this.reestr.entrySet()){
			if(eachEntry.getValue()>returnValue){
				returnValue=eachEntry.getValue();
			}
		}
		return returnValue;
	}

	public void removeSpider(Spider spider){
		LOGGER.info("remove spider:"+spider.toString());
		this.reestr.remove(spider);
	}
	
	/**
	 * @return list of all accessible services 
	 */
	@Override
	public Map<Spider, Integer> getList() {
		return new HashMap<Spider, Integer>(this.reestr);
	}

	@Override
	public void start(BundleContext context) throws Exception {
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}
}
