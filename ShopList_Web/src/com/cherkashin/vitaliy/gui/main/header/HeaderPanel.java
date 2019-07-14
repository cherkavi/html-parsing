package com.cherkashin.vitaliy.gui.main.header;

import org.apache.wicket.markup.html.panel.Panel;

import wicket_utility.mapper_pattern.Mapper;

/** панель верхнего колонтитура */
public class HeaderPanel extends Panel{
	private final static long serialVersionUID=1L;
	private Mapper mapper;
	
	/** панель верхнего колонтитура  
	 * @param name - имя панели 
	 * @param mapper - объект-шаблон Mapper
	 */
	public HeaderPanel(String name, Mapper mapper){
		super(name);
		this.mapper=mapper;
	}
	
	
}
