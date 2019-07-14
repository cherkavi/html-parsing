package com.cherkashin.vitaliy.gui.actions.data_reset;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;

import com.cherkashin.vitaliy.dao.DatabaseFacade;
import com.cherkashin.vitaliy.gui.actions.ActionsList;

import database.ConnectWrap;
import database.StaticConnector;

/** Удаление элементов */
public class DataReset extends WebPage{
	private Form<?> formMain;
	public DataReset() {
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
		});
		formMain.add(new Button("button_cancel"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonCancel();
			}
		});
	}

	/** удалить  */
	private void onButtonDelete(){
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			DatabaseFacade database=new DatabaseFacade();
			database.resetData(connector);
		}catch(Exception ex){
			System.out.println("#onButtonDelete: "+ex.getMessage());
			formMain.error("remove record Exception:"+ex.getMessage());
		}finally{
			connector.close();
		}
		this.setResponsePage(new ActionsList());
	}
	
	/** отменить */
	private void onButtonCancel(){
		this.setResponsePage(new ActionsList());
	}
}
