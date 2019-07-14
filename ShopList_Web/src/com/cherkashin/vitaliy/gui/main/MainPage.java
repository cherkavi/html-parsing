package com.cherkashin.vitaliy.gui.main;

import com.cherkashin.vitaliy.gui.BasePage;
import com.cherkashin.vitaliy.gui.main.find.FindPanel;
import com.cherkashin.vitaliy.gui.main.result.ResultPanelEmpty;
import com.cherkashin.vitaliy.gui.main.result.commodity_list.ResultPanel;

import wicket_utility.mapper_pattern.IInterceptor;
import wicket_utility.mapper_pattern.Mapper;
import wicket_utility.mapper_pattern.MapperInterceptor;
import wicket_utility.mapper_pattern.MapperMessage;

/** стартовая страница  */
public class MainPage extends BasePage implements IInterceptor{
	/** объект-шаблон Mapper */
	private Mapper mapper;
	/** опция поиска товара */
	private final static String findOption="find_option";
	/** опция перехода на следующую страницу */
	// private final static String gotoCommodity="goto_commodity";
	
	private String idPanelFind="panel_find";
	private String idPanelResult="panel_result";
	
	/** стартовая страница  */
	public MainPage(){
		this.createMapper();
		this.initLocalComponents();
		// обязательно вызвать после всех инициализаций 
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
				// активировать поиск по имени товара
				// перенаправить запрос на результирующую панель
				String findString=(String)message.getParameters();
				this.remove(idPanelResult);
				this.add(new ResultPanel(idPanelResult, 
										 this.mapper, 
										 findString, 
										 20, 
										 1));
			}
			/*
			// нажата ссылка на товар 
			if(message.getOptionName().equals(gotoCommodity)){
				// открыть запрос в новом окне 
			}
			*/
		}
	}

	
	@Override
	public String getInterceptorId() {
		return this.getClass().getName();
	}
}
