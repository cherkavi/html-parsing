package com.cherkashin.vitaliy.gui.main.result;

import org.apache.wicket.markup.html.basic.Label;

import org.apache.wicket.markup.html.panel.Panel;

/** панель верхнего колонтитура */
public class ResultPanelEmpty extends Panel{
	private final static long serialVersionUID=1L;
	
	/** панель отображения результатов поиска ( сообщение )
	 * в данном случае пустое сообщение  
	 * @param name - имя панели 
	 */
	public ResultPanelEmpty(String name){
		super(name);
		this.add(new Label("message",""));
	}
	
	/** панель отображения результатов поиска ( сообщение )
	 * @param name - имя панели
	 * @param message - сообщение, которое должно быть отражено 
	 */
	public ResultPanelEmpty(String name, String message){
		super(name);
		this.add(new Label("message", message));
	}
	
}
