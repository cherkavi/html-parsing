package com.cherkashyn.vitalii.tools.web.parser.action.spider;

import org.slf4j.Logger;

import com.cherkashyn.vitalii.tools.web.parser.action.exception.ChangedStructureException;
import com.cherkashyn.vitalii.tools.web.parser.action.exception.ReadRemoteDataException;
import com.cherkashyn.vitalii.tools.web.parser.action.page.Parser;
import com.cherkashyn.vitalii.tools.web.parser.action.page.url.NextUrlGenerator;
import com.cherkashyn.vitalii.tools.web.parser.action.spider.checker.EndParsingChecker;
import com.cherkashyn.vitalii.tools.web.parser.domain.element.GroupBlock;
import com.cherkashyn.vitalii.tools.web.parser.domain.element.Page;
import com.cherkashyn.vitalii.whore.domain.WhoreElement;
import com.cherkashyn.vitalii.whore.exceptions.StorageException;
import com.cherkashyn.vitalii.whore.exceptions.ValidationException;
import com.cherkashyn.vitalii.whore.interfaces.elementsaver.ElementSaver;
import com.cherkashyn.vitalii.whore.interfaces.imagesaver.ImageSaver;
import com.cherkashyn.vitalii.whore.interfaces.parsercontroller.ParserController;
import com.cherkashyn.vitalii.whore.interfaces.spider.Spider;
import com.cherkashyn.vitalii.whore.interfaces.spider.Spider.Status;

/**
 * dedicated Thread for run parsing process
 */
public class SpiderRunner extends Thread {
	private Parser<WhoreElement> parser;
	private NextUrlGenerator urlGenerator; 
	private ImageSaver imageSaver;
	private ElementSaver elementSaver; 
	private ParserController controller;
	private Logger logger;
	private volatile Spider.Status status=Spider.Status.READY;
	private EndParsingChecker<WhoreElement> checker;
	private boolean flagStop=false;
	
	
	public SpiderRunner(Parser<WhoreElement> parser,
			NextUrlGenerator urlGenerator, ImageSaver imageSaver,
			ElementSaver elementSaver, ParserController controller,
			Logger logger, EndParsingChecker<WhoreElement> checker) {
		super();
		this.parser = parser;
		this.urlGenerator = urlGenerator;
		this.imageSaver = imageSaver;
		this.elementSaver = elementSaver;
		this.controller = controller;
		this.logger = logger;
		this.checker=checker;
	}

	public void terminate(){
		this.flagStop=true;
	}
	
	@Override
	public void run() {
		int sessionId=0;
		try{
			sessionId=controller.createSessionNumber(this.parser.getStartPage());
			logger.debug("parse begin: "+sessionId);
			
			Page page=new Page(this.urlGenerator);
			 
			controller.notifyBegin(sessionId);
			status=Spider.Status.WORKING;
			
			while(true){
				
				if(this.flagStop){
					status=Spider.Status.TERMINATED;
					controller.notifyTerminate(sessionId, "stopped by User");
					return;
				}
				
				// move to next page
				String url=page.getUrlNext();
				logger.debug("parse page: "+url);
				 
				// parse next page 
				GroupBlock<WhoreElement> block=parser.readData(page, imageSaver);
				
				// check for data exists
				if(this.checker.isEnd(block)){
					status=Spider.Status.FINISHED;
					controller.notifyEnd(sessionId);
					break;
				}
				logger.debug("parse OK: "+url);
				
				// save elements
				for(WhoreElement eachBlock: block.getElements()){
					if(this.flagStop){
						status=Spider.Status.TERMINATED;
						controller.notifyTerminate(sessionId, "stopped by User");
						return;
					}
					eachBlock.setSessionId(sessionId);
					elementSaver.saveElement(eachBlock);
				}
				
			} // working cycle 
			status=Spider.Status.FINISHED;
			controller.notifyEnd(sessionId);
		}catch(StorageException se){
			logger.error("parse terminate: "+sessionId);
			status=Spider.Status.BROKEN;
			try {
				controller.notifyTerminate(sessionId, "StorageException: "+se.getMessage());
			} catch (StorageException e) {
				logger.error(e.getMessage());
			}
		}catch(ValidationException ve){
			logger.error("parse terminate: "+sessionId);
			status=Spider.Status.BROKEN;
			try {
				controller.notifyTerminate(sessionId, "ValidationException: "+ve.getMessage());
			} catch (StorageException e) {
				logger.error(e.getMessage());
			}
		}catch(ReadRemoteDataException ex){
			logger.error("parse terminate: "+sessionId);
			status=Spider.Status.BROKEN;
			try {
				controller.notifyTerminate(sessionId, "ReadRemoteDataException: "+ex.getMessage());
			} catch (StorageException e) {
				logger.error(e.getMessage());				
			}
		}catch(ChangedStructureException ex){
			logger.error("parse terminate: "+sessionId);
			status=Spider.Status.BROKEN;
			try {
				controller.notifyTerminate(sessionId, "ChangeStructureException: "+ex.getMessage());
			} catch (StorageException e) {
				logger.error(e.getMessage());
			}
		}catch(RuntimeException ex){
			logger.error("parse terminate: "+sessionId);
			status=Spider.Status.BROKEN;
			try {
				controller.notifyTerminate(sessionId, "RuntimeException: "+ex.getMessage());
			} catch (StorageException e) {
				logger.error(e.getMessage());
			}
		}
		logger.debug("parse end: "+sessionId);
	}


	public Status getCurrentStatus() {
		return this.status;
	}

}
