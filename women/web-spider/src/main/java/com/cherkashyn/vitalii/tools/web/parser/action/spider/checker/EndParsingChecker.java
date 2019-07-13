package com.cherkashyn.vitalii.tools.web.parser.action.spider.checker;

import com.cherkashyn.vitalii.tools.web.parser.domain.element.GroupBlock;

public interface EndParsingChecker<T> {
	/**
	 * @param block - next block 
	 * @return
	 * <ul>
	 * 	<li><b>true</b> - process is ending </li>
	 * 	<li><b>false</b> - need to continue  </li>
	 * </ul>
	 */
	boolean isEnd(GroupBlock<T> block);
}
