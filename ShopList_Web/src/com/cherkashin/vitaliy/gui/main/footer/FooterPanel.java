package com.cherkashin.vitaliy.gui.main.footer;

import org.apache.wicket.markup.html.panel.Panel;

import wicket_utility.mapper_pattern.Mapper;

/** панель нижнего колонтитура */
public class FooterPanel extends Panel{
	private final static long serialVersionUID=1L;
	private Mapper mapper;
	
	/** панель нижнего колонтитура  
	 * @param name - имя панели 
	 * @param mapper - объект-шаблон Mapper
	 */
	public FooterPanel(String name, Mapper mapper){
		super(name);
		this.mapper=mapper;
	}
	
	
}
