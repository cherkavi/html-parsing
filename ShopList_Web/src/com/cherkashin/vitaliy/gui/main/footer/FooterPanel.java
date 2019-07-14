package com.cherkashin.vitaliy.gui.main.footer;

import org.apache.wicket.markup.html.panel.Panel;

import wicket_utility.mapper_pattern.Mapper;

/** ������ ������� ����������� */
public class FooterPanel extends Panel{
	private final static long serialVersionUID=1L;
	private Mapper mapper;
	
	/** ������ ������� �����������  
	 * @param name - ��� ������ 
	 * @param mapper - ������-������ Mapper
	 */
	public FooterPanel(String name, Mapper mapper){
		super(name);
		this.mapper=mapper;
	}
	
	
}
