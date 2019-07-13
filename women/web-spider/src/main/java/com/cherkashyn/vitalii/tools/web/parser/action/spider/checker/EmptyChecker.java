package com.cherkashyn.vitalii.tools.web.parser.action.spider.checker;

import com.cherkashyn.vitalii.tools.web.parser.domain.element.GroupBlock;
import com.cherkashyn.vitalii.whore.domain.WhoreElement;

public class EmptyChecker implements EndParsingChecker<WhoreElement> {

	@Override
	public boolean isEnd(GroupBlock<WhoreElement> block) {
		return block==null || block.getElements()==null || block.getElements().size()==0;
	}

}
