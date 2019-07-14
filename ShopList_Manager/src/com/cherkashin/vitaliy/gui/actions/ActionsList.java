package com.cherkashin.vitaliy.gui.actions;

import java.text.SimpleDateFormat;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;

import wicket_utility.IActionExecutor;
import wicket_utility.panel_with_button.PanelWithButton;

import com.cherkashin.vitaliy.application.ShopListApplication;
import com.cherkashin.vitaliy.dao.DatabaseFacade;
import com.cherkashin.vitaliy.gui.actions.data_reset.DataReset;
import com.cherkashin.vitaliy.gui.actions.delete_action.ActionDelete;
import com.cherkashin.vitaliy.gui.actions.dump_creator.DumpCreator;
import com.cherkashin.vitaliy.gui.actions.new_action.ActionNew;
import com.cherkashin.vitaliy.gui.actions.statistics.Statistics;

import database.ConnectWrap;
import database.StaticConnector;
import database.wrap_mysql.Actions;
import database.wrap_mysql.EActionState;


/** страница отображения списка ACTIONS*/
public class ActionsList extends WebPage implements IActionExecutor{
	private final static String idButtonStart="START";
	private final static String idButtonStop="STOP";
	
	/** страница отображения списка ACTIONS*/
	public ActionsList(){
		this.initComponents();
	}
	
	private SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy HH:mm");
	
	
	public void initComponents(){
		Link<?> linkDumpCreate=new Link<Object>("dump_creator"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onButtonDumpCreate();
			}
		};
		this.add(linkDumpCreate);
		// прочесть все текущий Actions
		Form<?> formMain=new Form<Object>("form_main");
		this.add(formMain);
		formMain.add(new ListView<Actions>("actions_list", new IModel<List<Actions>>(){
			private final static long serialVersionUID=1L;
			private List<Actions> list=null;
			@Override
			public List<Actions> getObject() {
				if(list==null)list=getLastActions();
				return list;
			}

			@Override
			public void setObject(List<Actions> arg0) {
			}

			@Override
			public void detach() {
				list=null;
			}
		}){
			private final static long serialVersionUID=1L;
			@Override
			protected void populateItem(ListItem<Actions> item) {
				final Actions currentAction=item.getModelObject();
				item.add(new Label("actions_id", Integer.toString(currentAction.getId())));
				item.add(new Label("actions_date", sdf.format(currentAction.getDate_write())));
				item.add(new Label("actions_state", currentAction.getActionState().getName()));
				item.add(new Button("button_delete"){
					private final static long serialVersionUID=1L;
					@Override
					public void onSubmit() {
						onButtonDelete(currentAction);
					}
				});
				if(currentAction.getId_action_state()==EActionState.NEW.getKod()){
					item.add(new PanelWithButton("panel_manage", 
												 ActionsList.this, 
												 idButtonStart, 
												 currentAction.getId(), 
												 null, 
												 (String)null, 
												 "start"));
				}else{
					if(currentAction.getId_action_state()==EActionState.IN_PROCESS.getKod()){
						item.add(new PanelWithButton("panel_manage", 
								 ActionsList.this, 
								 idButtonStop, 
								 currentAction.getId(), 
								 "", 
								 "" , 
								 "stop"));
					}else{
						item.add(new EmptyPanel("panel_manage"));
					}
				};
				item.add(new Link<Object>("link_statistics"){
					private final static long serialVersionUID=1L;
					@Override
					public void onClick() {
						onLinkStatistics(currentAction.getId());
					}
				});
				
			}
		});
		
		formMain.add(new Button("button_new"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonNew();
			}
		});
		
		formMain.add(new Button("button_reset"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonReset();
			}
		});
	}
	
	private void onButtonDelete(Actions action){
		setResponsePage(new ActionDelete(action));
	}
	
	private void onButtonReset(){
		setResponsePage(new DataReset());
	}
	
	private void onButtonNew(){
		setResponsePage(new ActionNew());
	}
	
	
	private List<Actions> getLastActions(){
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			DatabaseFacade facade=new DatabaseFacade();
			return facade.getActions(connector, 10);
		}catch(Exception ex){
			return null;
		}finally{
			connector.close();
		}
	}

	@Override
	public void action(String actionName, Object argument) {
		if(actionName.equals(idButtonStart)){
			System.out.println("Start");
			Integer idAction=(Integer)argument;
			((ShopListApplication)this.getApplication()).startAction(idAction);
			return;
		}
		if(actionName.equals(idButtonStop)){
			System.out.println("Stop");
			Integer idAction=(Integer)argument;
			((ShopListApplication)this.getApplication()).stopAction(idAction);
			return;
		}
	}
	
	
	/** реакция на нажатие ссылки на статистику */
	private void onLinkStatistics(Integer currentActionId){
		this.setResponsePage(new Statistics(currentActionId));
	}
	
	/** создать Dump базы данных  */
	private void onButtonDumpCreate(){
		this.setResponsePage(new DumpCreator());
	}
}
