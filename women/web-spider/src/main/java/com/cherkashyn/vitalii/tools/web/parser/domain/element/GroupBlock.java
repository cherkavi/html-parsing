package com.cherkashyn.vitalii.tools.web.parser.domain.element;

import java.util.ArrayList;
import java.util.List;

/**
 * container for elements from page 
 */
public class GroupBlock<T> {

	List<T> elements=new ArrayList<T>();

	public List<T> getElements() {
		return elements;
	}

}
