package com.cherkashyn.vitalii.tools.web.parser.xxx.sexkiev;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherkashyn.vitalii.tools.web.parser.action.page.Parser;
import com.cherkashyn.vitalii.tools.web.parser.action.page.url.NextUrlGenerator;
import com.cherkashyn.vitalii.tools.web.parser.action.spider.SpiderRunner;
import com.cherkashyn.vitalii.tools.web.parser.action.spider.checker.EmptyChecker;
import com.cherkashyn.vitalii.whore.domain.WhoreElement;
import com.cherkashyn.vitalii.whore.interfaces.elementsaver.ElementSaver;
import com.cherkashyn.vitalii.whore.interfaces.imagesaver.ImageSaver;
import com.cherkashyn.vitalii.whore.interfaces.parsercontroller.ParserController;
import com.cherkashyn.vitalii.whore.interfaces.spider.Spider;

public class SexKievSpider implements Spider{
	
	private Logger logger;
	private Parser<WhoreElement> parser; 
	private NextUrlGenerator urlGenerator;
	private SpiderRunner runner=null;

	
	public SexKievSpider() {
		this.parser=new SexKievParser();
		this.urlGenerator=new SexKievNextUrl();
		this.logger=LoggerFactory.getLogger(this.getClass());
	}

	@Override
	public void execute() {
		if(Spider.Status.WORKING.equals(this.getCurrentStatus())){
			logger.warn("in process, need to terminate "+this.getId());
			return;
		}
		if(this.runner!=null && this.runner.isAlive()){
			logger.warn("in process, try to terminate "+this.getId());
			return;
		}
		this.runner=new SpiderRunner(this.parser, this.urlGenerator, this.imageSaver, this.elementSaver, this.controller, this.logger, new EmptyChecker());
		this.runner.start();
	}

	@Override
	public String getId() {
		return this.parser.getStartPage();
	}

	@Override
	public void terminate() {
		this.runner.terminate();
	}

	@Override
	public Status getCurrentStatus() {
		if(this.runner==null){
			return Spider.Status.READY;
		}
		return this.runner.getCurrentStatus();
	}
	
	// ----------- IoC responsibilities (OSGi blueprint ) ----------
	protected ImageSaver imageSaver;
	protected ElementSaver elementSaver;
	protected ParserController controller;
	
	public ImageSaver getImageSaver() {
		return imageSaver;
	}

	public void setImageSaver(ImageSaver imageSaver) {
		this.imageSaver = imageSaver;
	}

	public ElementSaver getElementSaver() {
		return elementSaver;
	}

	public void setElementSaver(ElementSaver elementSaver) {
		this.elementSaver = elementSaver;
	}

	public ParserController getController() {
		return controller;
	}

	public void setController(ParserController controller) {
		this.controller = controller;
	}

}
