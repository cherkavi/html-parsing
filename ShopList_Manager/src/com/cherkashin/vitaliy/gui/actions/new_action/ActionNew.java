package com.cherkashin.vitaliy.gui.actions.new_action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import manager.utility.DirectoryClassLoader;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.Model;

import com.cherkashin.vitaliy.application.ShopListApplication;
import com.cherkashin.vitaliy.dao.DatabaseFacade;
import com.cherkashin.vitaliy.gui.actions.ActionsList;

import database.ConnectWrap;
import database.StaticConnector;

/** Создание нового элемента */
public class ActionNew extends WebPage{
	private Form<?> formMain;
	private Model<ArrayList<String>> modelListSource=new Model<ArrayList<String>>(new ArrayList<String>());
	private Model<ArrayList<String>> modelListSourceChoices=new Model<ArrayList<String>>(new ArrayList<String>());
	private Model<ArrayList<String>> modelListDestination=new Model<ArrayList<String>>(new ArrayList<String>());
	private Model<ArrayList<String>> modelListDestinationChoices=new Model<ArrayList<String>>(new ArrayList<String>());
	private HashMap<String, String> originalMap=null;
	
	/** Создание нового элемента */
	public ActionNew() {
		initComponents();
	}
	
	/** получить старторвую страницу магазина на основании уникального имени класса */
	private String getShopStartPageByKey(String key){
		return originalMap.get(key);
	}
	
	/** получить все имена классов */
	@SuppressWarnings("unchecked")
	private ArrayList<String> getAllClassNames(){
		ArrayList<String> returnValue=new ArrayList<String>(this.originalMap.keySet());
		Collections.sort(returnValue, new Comparator(){
			public int compare(Object o1, Object o2)
		    {
				try{
			        String u1 = (String) o1;
			        String u2 = (String) o2;
			        return u1.compareTo(u2);
				}catch(Exception ex){
					return 0;
				}
		    }			
		});
		return returnValue;
	}

	class ShopListRenderer implements IChoiceRenderer<String>{
		private final static long serialVersionUID=1L;
		
		@Override
		public Object getDisplayValue(String arg) {
			return getShopStartPageByKey(arg);
		}

		@Override
		public String getIdValue(String arg0, int arg1) {
			return arg0;
		}
		
	}
	
	public void initComponents(){
		originalMap=DirectoryClassLoader
		.getAvailableShopListStartPage(((ShopListApplication)this.getApplication()).getShopListParsersFolder());
		
		modelListSourceChoices.setObject(this.getAllClassNames());
		
		formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		ListMultipleChoice<String> listSource=new ListMultipleChoice<String>("list_source",
																			 modelListSource, 
																			 modelListSourceChoices, 
																			 new ShopListRenderer());
		formMain.add(listSource);
		
		ListMultipleChoice<String> listDestination=new ListMultipleChoice<String>("list_destination",
																				  modelListDestination, 
																				  modelListDestinationChoices, 
																				  new ShopListRenderer());
		formMain.add(listDestination);

		formMain.add(new Button("list_add"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonListAdd();
			}
		});
		formMain.add(new Button("list_remove"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonListRemove();
			}
		});
		
		formMain.add(new Button("button_ok"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonOk();
			}
		});
		formMain.add(new Button("button_cancel"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonCancel();
			}
		});
		formMain.add(new ComponentFeedbackPanel("form_feedback",formMain));
	}
/*
	private void printArrayList(String header, ArrayList<String> list){
		System.out.println("-------"+header+"-------");
		for(int counter=0;counter<list.size();counter++){
			System.out.println(counter+"  "+list.get(counter));
		}
		System.out.println("------------------------");
		// printArrayList("Destination",this.modelListDestination.getObject());
		// printArrayList("DestinationChoices",this.modelListDestinationChoices.getObject());
		// printArrayList("Source",this.modelListSource.getObject());
		// printArrayList("SourceChoices",this.modelListSourceChoices.getObject());
	}
*/
	
	private void onButtonListAdd(){
		if(this.modelListSource.getObject().size()>0){
			for(int counter=0;counter<this.modelListSource.getObject().size();counter++){
				this.modelListDestinationChoices.getObject().add(this.modelListSource.getObject().get(counter));
				this.modelListSourceChoices.getObject().remove(this.modelListSource.getObject().get(counter));
			}
		}
	}
	
	private void onButtonListRemove(){
		// System.out.println("Remove");
		if(this.modelListDestination.getObject().size()>0){
			for(int counter=0;counter<this.modelListDestination.getObject().size();counter++){
				this.modelListSourceChoices.getObject().add(this.modelListDestination.getObject().get(counter));
				this.modelListDestinationChoices.getObject().remove(this.modelListDestination.getObject().get(counter));
			}
		}
	}
	
	@SuppressWarnings("unused")
	private ArrayList<String> convertClassNameToStartPages(ArrayList<String> classNames){
		HashSet<String> set=new HashSet<String>();
		ArrayList<String> startPages=this.convertClassNameToStartPages(classNames);
		for(int counter=0;counter<startPages.size();counter++){
			set.add(startPages.get(counter));
		}
		return new ArrayList<String>(set);
	}
	
	/** создать   */
	private void onButtonOk(){
		if(this.modelListDestinationChoices.getObject().size()>0){
			ConnectWrap connectWrap=StaticConnector.getConnectWrap();
			try{
				DatabaseFacade facade=new DatabaseFacade();
				Integer actionId=facade.createAction(connectWrap, this.getHashMapChoices(this.modelListDestinationChoices.getObject()));
				if(actionId==null){
					formMain.error("Database save Error");
				}else{
					this.setResponsePage(new ActionsList());
				}
			}catch(Exception ex){
				System.err.println("ActionNew#onButtonOk Exception:"+ex.getMessage());
			}finally{
				connectWrap.close();
			}
		}else{
			this.onButtonCancel();
		}
	}
	
	/** создать на основании переданного массива имен классов HashMap<String,String> (<имя класса>, <стартовая страница>) */
	private HashMap<String, String> getHashMapChoices(ArrayList<String> list) {
		HashMap<String, String> returnValue=new HashMap<String, String>();
		for(int counter=0;counter<list.size();counter++){
			returnValue.put(list.get(counter), this.getShopStartPageByKey(list.get(counter)));
		}
		return returnValue;
	}

	/** отменить */
	private void onButtonCancel(){
		this.setResponsePage(new ActionsList());
	}
}
