package com.cherkashin.vitaliy.gui.main.result;

import org.apache.wicket.markup.html.basic.Label;

import org.apache.wicket.markup.html.panel.Panel;

/** ������ �������� ����������� */
public class ResultPanelEmpty extends Panel{
	private final static long serialVersionUID=1L;
	
	/** ������ ����������� ����������� ������ ( ��������� )
	 * � ������ ������ ������ ���������  
	 * @param name - ��� ������ 
	 */
	public ResultPanelEmpty(String name){
		super(name);
		this.add(new Label("message",""));
	}
	
	/** ������ ����������� ����������� ������ ( ��������� )
	 * @param name - ��� ������
	 * @param message - ���������, ������� ������ ���� �������� 
	 */
	public ResultPanelEmpty(String name, String message){
		super(name);
		this.add(new Label("message", message));
	}
	
}
