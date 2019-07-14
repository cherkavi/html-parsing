package com.cherkashin.vitaliy.gui.actions.statistics.logger_view.server_reset;

import org.apache.wicket.markup.html.WebPage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;

import com.cherkashin.vitaliy.application.ShopListApplication;
import com.cherkashin.vitaliy.gui.actions.ActionsList;

/** Перезагрузка севера */
public class ServerReset extends WebPage{
	private Form<?> formMain;
	private Model<String> modelFeedback=new Model<String>();
	private Model<Boolean> showFeedback=new Model<Boolean>(false);
	
	public ServerReset() {
		this.initComponents();
	}

	public void initComponents(){
		formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		formMain.add(new Button("button_delete"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonDelete();
			}
			@Override
			public boolean isVisible() {
				return !showFeedback.getObject();
			}
		});
		
		formMain.add(new Button("button_cancel"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonCancel();
			}
			@Override
			public boolean isVisible() {
				return !showFeedback.getObject();
			}
		});
		
		formMain.add(new Label("feedback_form", modelFeedback){
			private final static long serialVersionUID=1L;
			@Override
			public boolean isVisible() {
				return showFeedback.getObject();
			}
		});
	}

	/** удалить  */
	private void onButtonDelete(){
		ShopListApplication application=(ShopListApplication)this.getApplication();
		String returnValue="";
		try{
			returnValue=application.shutdownServer();
		}catch(Exception ex){};
		if((returnValue==null)||(returnValue.trim().length()==0)){
			this.modelFeedback.setObject("Ok");
		}else{
			this.modelFeedback.setObject("Error:"+returnValue);
		}
		this.showFeedback.setObject(true);
	}
	
	/** отменить */
	private void onButtonCancel(){
		this.setResponsePage(new ActionsList());
	}
}
