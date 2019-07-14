package com.cherkashin.vitaliy.gui.main.find;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import wicket_utility.mapper_pattern.Mapper;
import wicket_utility.mapper_pattern.MapperMessage;

/** панель поиска данных */
public class FindPanel extends Panel{
	private final static long serialVersionUID=1L;
	private Mapper mapper;
	private Model<String> modelFind=new Model<String>();
	private String findInterceptorId;
	private String findInterceptorOptionName;
	
	/** панель поиска данных 
	 * @param name - имя панели 
	 * @param mapper - объект-шаблон Mapper
	 * @param 
	 */
	public FindPanel(String name, 
					 Mapper mapper,
					 String findInterceptorId,
					 String findInterceptorOptionName
					 ){
		super(name);
		this.mapper=mapper;
		this.findInterceptorId=findInterceptorId;
		this.findInterceptorOptionName=findInterceptorOptionName;
		this.initComponents();
	}
	
	
	private void initComponents(){
		Form<?> formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		TextField<String> editText=new TextField<String>("find_text", modelFind);
		formMain.add(editText);
		editText.setRequired(false);
		
		Button buttonSubmit=new Button("button_submit"){
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {
				onButtonSubmit();
			}
		};
		formMain.add(buttonSubmit);
	}

	
	/** послать запрос на сервер  */
	private void onButtonSubmit(){
		String findText=this.modelFind.getObject();
		if(findText==null) return;
		findText=findText.trim();
		if(findText.length()<3)return;
		this.mapper.sendMessageToInterceptor(new MapperMessage(this.findInterceptorId, this.findInterceptorOptionName, findText));
	}
}
