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

/** базовая страница  */
public class BasePage extends WebPage implements IInterceptor{
	/** уникальный идентификатор панели-заголовка */
	protected String idPanelHeader="panel_header";
	/** уникальный идентификатор панели-подвала */
	protected String idPanelFooter="panel_footer";
	/** класс, который сосредотачивает в себе все перехватчики  */
	private Mapper mapper;
	
	/** базовая страница */
	public BasePage(){
		super();
		this.mapper=new Mapper();
		this.mapper.addInterceptor(new MapperInterceptor(this));
	}
	
	/** данный метод должен быть обязательно вызван в потомках */
	protected void initComponents(){
		this.add(new Label("title",this.getString("title")));
		this.add(this.getHeaderPanel());
		this.add(this.getFooterPanel());
	}
	
	/** получить панель для заголовка */
	protected Panel getHeaderPanel(){
		return new HeaderPanel(idPanelHeader,this.mapper);
	}

	/** получить панель для заголовка */
	protected Panel getFooterPanel(){
		return new FooterPanel(idPanelFooter,this.mapper);
	}

	@Override
	public String getInterceptorId() {
		return this.getClass().getName();
	}

	@Override
	public void getOutsideMessage(MapperMessage message) {
		// перехваченные сообщения
	}
}
