package com.cherkashin.vitaliy.gui.main;

import com.cherkashin.vitaliy.gui.BasePage;
import com.cherkashin.vitaliy.gui.main.find.FindPanel;
import com.cherkashin.vitaliy.gui.main.result.ResultPanelEmpty;
import com.cherkashin.vitaliy.gui.main.result.commodity_list.ResultPanel;

import wicket_utility.mapper_pattern.IInterceptor;
import wicket_utility.mapper_pattern.Mapper;
import wicket_utility.mapper_pattern.MapperInterceptor;
import wicket_utility.mapper_pattern.MapperMessage;

/** ��������� ��������  */
public class MainPage extends BasePage implements IInterceptor{
	/** ������-������ Mapper */
	private Mapper mapper;
	/** ����� ������ ������ */
	private final static String findOption="find_option";
	/** ����� �������� �� ��������� �������� */
	// private final static String gotoCommodity="goto_commodity";
	
	private String idPanelFind="panel_find";
	private String idPanelResult="panel_result";
	
	/** ��������� ��������  */
	public MainPage(){
		this.createMapper();
		this.initLocalComponents();
		// ����������� ������� ����� ���� ������������� 
		this.initComponents();
	}
	
	private void createMapper(){
		this.mapper=new Mapper();
		this.mapper.addInterceptor(new MapperInterceptor(this));
	}
	
	private void initLocalComponents(){
		this.add(new FindPanel(idPanelFind, mapper, this.getClass().getName(),findOption));
		this.add(new ResultPanelEmpty(idPanelResult));
	}

	@Override
	public void getOutsideMessage(MapperMessage message) {
		if(message.getOptionName()!=null){
			// findOption
			if(message.getOptionName().equals(findOption)){
				// ������������ ����� �� ����� ������
				// ������������� ������ �� �������������� ������
				String findString=(String)message.getParameters();
				this.remove(idPanelResult);
				this.add(new ResultPanel(idPanelResult, 
										 this.mapper, 
										 findString, 
										 20, 
										 1));
			}
			/*
			// ������ ������ �� ����� 
			if(message.getOptionName().equals(gotoCommodity)){
				// ������� ������ � ����� ���� 
			}
			*/
		}
	}

	
	@Override
	public String getInterceptorId() {
		return this.getClass().getName();
	}
}
