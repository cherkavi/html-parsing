package com.cherkashin.vitaliy.gui.actions.delete_action;

import java.text.SimpleDateFormat;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;

import com.cherkashin.vitaliy.dao.DatabaseFacade;
import com.cherkashin.vitaliy.gui.actions.ActionsList;

import database.ConnectWrap;
import database.StaticConnector;
import database.wrap_mysql.Actions;


/** Удаление элементов */
public class ActionDelete extends WebPage{
	private int actionId;
	private Form<?> formMain;
	public ActionDelete(Actions action) {
		this.initComponents(action);
		this.actionId=action.getId();
	}

	private SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy");
	
	public void initComponents(Actions currentAction){
		formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		formMain.add(new Label("actions_id", Integer.toString(currentAction.getId())));
		formMain.add(new Label("actions_date", sdf.format(currentAction.getDate_write())));
		formMain.add(new Label("actions_state", currentAction.getActionState().getName()));
		formMain.add(new Button("button_delete"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonDelete();
			}
		});
		formMain.add(new Button("button_cancel"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonCancel();
			}
		});
		formMain.add(new ComponentFeedbackPanel("form_feedback", formMain));
	}

	/** удалить  */
	private void onButtonDelete(){
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			DatabaseFacade database=new DatabaseFacade();
			database.removeAction(connector, this.actionId);
			this.setResponsePage(new ActionsList());
		}catch(Exception ex){
			System.out.println("#onButtonDelete: "+ex.getMessage());
			formMain.error("remove record Exception:"+ex.getMessage());
		}finally{
			connector.close();
		}
	}
	
	/** отменить */
	private void onButtonCancel(){
		this.setResponsePage(new ActionsList());
	}
}
