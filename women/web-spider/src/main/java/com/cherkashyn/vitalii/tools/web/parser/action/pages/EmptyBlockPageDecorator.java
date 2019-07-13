package com.cherkashyn.vitalii.tools.web.parser.action.pages;

import com.cherkashyn.vitalii.tools.web.parser.action.exception.ChangedStructureException;
import com.cherkashyn.vitalii.tools.web.parser.action.exception.ReadRemoteDataException;
import com.cherkashyn.vitalii.tools.web.parser.action.page.Parser;
import com.cherkashyn.vitalii.tools.web.parser.action.page.url.NextUrlGenerator;
import com.cherkashyn.vitalii.tools.web.parser.domain.element.GroupBlock;
import com.cherkashyn.vitalii.tools.web.parser.domain.element.Page;
import com.cherkashyn.vitalii.whore.interfaces.imagesaver.ImageSaver;

/**
 * parse page until GroupBlock from next page will be empty 
 */
public class EmptyBlockPageDecorator<T> extends PagesParser<T>{
	private final Parser<T> parser;
	private final ImageSaver imageSaver;
	
	public EmptyBlockPageDecorator(Parser<T> parser, ImageSaver imageSaver){
		this.parser=parser;
		this.imageSaver=imageSaver;
	}
	
	/**
	 * {@inheritDoc}
	 * @throws ChangedStructureException 
	 * @throws ReadRemoteDataException 
	 */
	@Override
	public GroupBlock<T> readNextBlock(Page page, NextUrlGenerator generator) throws ReadRemoteDataException, ChangedStructureException {
		
		GroupBlock<T> returnValue=parsePage(page);
		if(returnValue==null){
			return null;
		}
		if(returnValue.getElements()==null){
			return null;
		}
		if(returnValue.getElements().size()==0){
			return null;
		}
		return returnValue;
	}

	private GroupBlock<T> parsePage(Page page) throws ReadRemoteDataException, ChangedStructureException {
		return this.parser.readData(page, this.imageSaver);
	}

}
