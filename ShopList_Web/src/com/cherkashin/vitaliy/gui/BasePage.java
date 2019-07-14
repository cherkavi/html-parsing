package com.cherkashin.vitaliy.gui;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import wicket_utility.mapper_pattern.IInterceptor;
import wicket_utility.mapper_pattern.Mapper;
import wicket_utility.mapper_pattern.MapperInterceptor;
import wicket_utility.mapper_pattern.MapperMessage;

import com.cherkashin.vitaliy.gui.main.footer.FooterPanel;
import com.cherkashin.vitaliy.gui.main.header.HeaderPanel;

/** ������� ��������  */
public class BasePage extends WebPage implements IInterceptor{
	/** ���������� ������������� ������-��������� */
	protected String idPanelHeader="panel_header";
	/** ���������� ������������� ������-������� */
	protected String idPanelFooter="panel_footer";
	/** �����, ������� ��������������� � ���� ��� ������������  */
	private Mapper mapper;
	
	/** ������� �������� */
	public BasePage(){
		super();
		this.mapper=new Mapper();
		this.mapper.addInterceptor(new MapperInterceptor(this));
	}
	
	/** ������ ����� ������ ���� ����������� ������ � �������� */
	protected void initComponents(){
		this.add(new Label("title",this.getString("title")));
		this.add(this.getHeaderPanel());
		this.add(this.getFooterPanel());
	}
	
	/** �������� ������ ��� ��������� */
	protected Panel getHeaderPanel(){
		return new HeaderPanel(idPanelHeader,this.mapper);
	}

	/** �������� ������ ��� ��������� */
	protected Panel getFooterPanel(){
		return new FooterPanel(idPanelFooter,this.mapper);
	}

	@Override
	public String getInterceptorId() {
		return this.getClass().getName();
	}

	@Override
	public void getOutsideMessage(MapperMessage message) {
		// ������������� ���������
	}
}
