package com.cherkashyn.vitalii.tools.web.parser.action.pages;

import com.cherkashyn.vitalii.tools.web.parser.action.exception.ChangedStructureException;
import com.cherkashyn.vitalii.tools.web.parser.action.exception.ReadRemoteDataException;
import com.cherkashyn.vitalii.tools.web.parser.action.page.url.NextUrlGenerator;
import com.cherkashyn.vitalii.tools.web.parser.domain.element.GroupBlock;
import com.cherkashyn.vitalii.tools.web.parser.domain.element.Page;

public abstract class PagesParser<T> {
	
	/**
	 * @return
	 * <ul>
	 * 	<li> next block </li>
	 * 	<li> null - when no records found, or page was not found  </li>
	 * </ul>
	 * @throws ChangedStructureException 
	 * @throws ReadRemoteDataException 
	 * 
	 */
	public abstract GroupBlock<T> readNextBlock(Page page, NextUrlGenerator generator) throws ReadRemoteDataException, ChangedStructureException;
	
}
