package com.cherkashyn.vitalii.whore.interfaces.executor;

import com.cherkashyn.vitalii.whore.exceptions.LogicException;
import com.cherkashyn.vitalii.whore.interfaces.spider.Spider;

public interface Executor {
	
	void start(Spider spider) throws LogicException;
	void terminate(Spider spider) throws LogicException;
	void pause(Spider spider) throws LogicException;
	String status(Spider spider) throws LogicException;
	
}
