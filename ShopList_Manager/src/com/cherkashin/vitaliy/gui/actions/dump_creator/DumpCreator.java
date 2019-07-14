package com.cherkashin.vitaliy.gui.actions.dump_creator;

import org.apache.wicket.markup.html.WebPage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.Model;

import com.cherkashin.vitaliy.application.ShopListApplication;
import com.cherkashin.vitaliy.gui.actions.ActionsList;

/** Удаление элементов */
public class DumpCreator extends WebPage{
	private Form<?> formMain;
	private Model<Boolean> modelShowButtonCreateDump=new Model<Boolean>(true);
	private Model<String> modelFtpServer=new Model<String>("");
	private Model<String> modelFtpLogin=new Model<String>("");
	private Model<String> modelFtpPassword=new Model<String>("");
	public DumpCreator() {
		this.initComponents();
	}

	public void initComponents(){
		formMain=new Form<Object>("form_main");
		this.add(formMain);
		formMain.add(new ComponentFeedbackPanel("form_feedback", formMain));
		
		formMain.add(new Button("button_create_dump"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonCreateDump();
			}
			@Override
			public boolean isVisible() {
				return modelShowButtonCreateDump.getObject();
			}
		});
		
		formMain.add(new Label("dump_was_created",this.getString("dump_was_created")){
			private final static long serialVersionUID=1L;
			@Override
			public boolean isVisible() {
				return !modelShowButtonCreateDump.getObject();
			}
		});

		
		formMain.add(new Label("ftp_server_label",this.getString("ftp_server_label")){
			private final static long serialVersionUID=1L;
			@Override
			public boolean isVisible() {
				return !modelShowButtonCreateDump.getObject();
			}
		});
		formMain.add(new TextField<String>("ftp_server", modelFtpServer){
			private final static long serialVersionUID=1L;
			{
				this.setRequired(false);
			}
			@Override
			public boolean isVisible() {
				return !modelShowButtonCreateDump.getObject();
			}
		});
		
		formMain.add(new Label("ftp_user_label",this.getString("ftp_user_label")){
			private final static long serialVersionUID=1L;
			@Override
			public boolean isVisible() {
				return !modelShowButtonCreateDump.getObject();
			}
		});
		formMain.add(new TextField<String>("ftp_user", modelFtpLogin){
			private final static long serialVersionUID=1L;
			{
				this.setRequired(false);
			}
			@Override
			public boolean isVisible() {
				return !modelShowButtonCreateDump.getObject();
			}
		});
		
		formMain.add(new Label("ftp_password_label",this.getString("ftp_password_label")){
			private final static long serialVersionUID=1L;
			@Override
			public boolean isVisible() {
				return !modelShowButtonCreateDump.getObject();
			}
		});
		formMain.add(new PasswordTextField("ftp_password", modelFtpPassword){
			private final static long serialVersionUID=1L;
			{
				this.setRequired(false);
			}
			@Override
			public boolean isVisible() {
				return !modelShowButtonCreateDump.getObject();
			}
		});
		
		formMain.add(new Button("button_copy"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonCreate();
			}
			public boolean isVisible() {
				return !modelShowButtonCreateDump.getObject();
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

	/** полный путь к дампу базы данных  */
	private String pathToDump=null;
	
	/** реакция нажатия на кнопку Dump */
	private void onButtonCreateDump(){
		ShopListApplication application=(ShopListApplication)this.getApplication();
		String fullFileName=application.createDumpMySqlDump();
		if(fullFileName!=null){
			this.pathToDump=fullFileName;
			this.modelShowButtonCreateDump.setObject(false);
		}else{
			// ошибка создания файла
			this.formMain.error("Create dump error");
		}
	}
	
	
	/** удалить  */
	private void onButtonCreate(){
		if((this.modelFtpServer.getObject()==null)||(this.modelFtpServer.getObject().trim().length()==0)){
			formMain.error("Enter Server");
			return;
		}
		if((this.modelFtpLogin.getObject()==null)||(this.modelFtpLogin.getObject().trim().length()==0)){
			formMain.error("Enter Login");
			return;
		}
		if((this.modelFtpPassword.getObject()==null)||(this.modelFtpPassword.getObject().trim().length()==0)){
			formMain.error("Enter Password");
			return;
		}
		
		ShopListApplication application=(ShopListApplication)this.getApplication();
		if(application.copyFileToFtp(this.pathToDump, modelFtpServer.getObject(), 21, modelFtpLogin.getObject(), modelFtpPassword.getObject())){
			application.removeFile(this.pathToDump);
			setResponsePage(new ActionsList());
		}else{
			formMain.error("copy to destination server error ");
		}
	}
	
	/** отменить */
	private void onButtonCancel(){
		ShopListApplication application=(ShopListApplication)this.getApplication();
		if(this.pathToDump!=null)application.removeFile(this.pathToDump);
		this.setResponsePage(new ActionsList());
	}
}
