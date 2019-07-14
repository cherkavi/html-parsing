package com.cherkashin.vitaliy.gui.actions.statistics;

import java.util.ArrayList;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

import com.cherkashin.vitaliy.dao.DatabaseFacade;
import com.cherkashin.vitaliy.dao.DatabaseFacade.WrapErrorParsers;
import com.cherkashin.vitaliy.dao.DatabaseFacade.WrapParseResult;
import com.cherkashin.vitaliy.gui.actions.ActionsList;
import com.cherkashin.vitaliy.gui.actions.statistics.logger_view.LoggerView;

import database.ConnectWrap;
import database.StaticConnector;

/**  страница отображени€ статистики по указанному Current_action */
public class Statistics extends WebPage{
	private Integer currentActionId=null;
	private Model<Integer> modelRecordCount=new Model<Integer>();
	private Model<Integer> modelParserPosition=new Model<Integer>();
	private Model<Integer> modelParserAll=new Model<Integer>();
	private Model<ArrayList<DatabaseFacade.WrapErrorParsers>> modelListOfError=new Model<ArrayList<DatabaseFacade.WrapErrorParsers>>();
	private Model<ArrayList<DatabaseFacade.WrapErrorLogger>> modelListOfLoggerError=new Model<ArrayList<DatabaseFacade.WrapErrorLogger>>();
	private Model<ArrayList<DatabaseFacade.WrapParseResult>> modelListOfParseResult=new Model<ArrayList<DatabaseFacade.WrapParseResult>>();
	
	/**  страница отображени€ статистики по указанному Current_action 
	 * @param currentActionId
	 * */
	public Statistics(Integer currentActionId){
		this.currentActionId=currentActionId;
		initComponents();
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		this.fillValues();
	}
	
	private void fillValues(){
		ConnectWrap connector=null;
		try{
			connector=StaticConnector.getConnectWrap();
			DatabaseFacade dao=new DatabaseFacade();
			this.modelRecordCount.setObject(dao.getStatisticsRecordCount(connector, this.currentActionId));
			this.modelParserPosition.setObject(dao.getStatisticsParsetPosition(connector, this.currentActionId));
			this.modelParserAll.setObject(dao.getStatisticsParserAll(connector, this.currentActionId));
			this.modelListOfError.setObject(dao.getStatisticsErrorParsers(connector, this.currentActionId));
			this.modelListOfLoggerError.setObject(dao.getStatisticsLogger(connector, this.currentActionId,4));
			this.modelListOfParseResult.setObject(dao.getStatisticsParseResult(connector, this.currentActionId));
		}catch(Exception ex){
			System.err.println("Statistics#onBeforeRender Exception:"+ex.getMessage());
		}finally{
			try{
				connector.close();
			}catch(Exception ex){};
		}
	}
	
	
	/** первоначальна€ инициализаци€ компонентов  */
	private void initComponents(){
		Form<?> formBack=new Form<Object>("form_back");
		this.add(formBack);
		formBack.add(new Button("button_back"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				this.setResponsePage(new ActionsList());
			}
		});
		
		this.fillValues();
		//  ол-во записей, ( распарсенные ) в текущий момент в базе
		this.add(new Label("record_count",modelRecordCount));
		// –аспарсено магазинов в данный момент 
		this.add(new Label("parser_position",this.modelParserPosition));
		// ¬сего парсеров в текущей задаче 
		this.add(new Label("parser_all",this.modelParserAll));

		//  ол-во магазинов, которые распарсены с ошибками
		this.add(new ListView<DatabaseFacade.WrapErrorParsers>("list_error_parsers", this.modelListOfError){
			private final static long serialVersionUID=1L;
			@Override
			protected void populateItem(ListItem<WrapErrorParsers> item) {
				DatabaseFacade.WrapErrorParsers object=item.getModelObject();
				item.add(new Label("error_parser_start_page", object.getStartPage()));
				item.add(new Label("error_parser_count", Integer.toString(object.getCount())));
			}
			
		});
		
		// ћагазины, которые имеют в логе сообщени€ об ошибках
		this.add(new ListView<DatabaseFacade.WrapErrorLogger>("list_logger_error", this.modelListOfLoggerError){
			private final static long serialVersionUID=1L;
			@Override
			protected void populateItem(ListItem<DatabaseFacade.WrapErrorLogger> item) {
				final DatabaseFacade.WrapErrorLogger object=item.getModelObject();
				Link<Object> link=new Link<Object>("logger_error_link"){
					private final static long serialVersionUID=1L;
					@Override
					public void onClick() {
						onLinkShowLoggerErrorBySessionId(object.getIdParseSession());
					}
				};
				item.add(link);
				link.add(new Label("logger_error_start_page",object.getStartPage()));
				item.add(new Label("record_count", Integer.toString(object.getCountRecord())));
				item.add(new Label("logger_error_count", Integer.toString(object.getCountLogger())));
			}
		});
		
		this.add(new ListView<DatabaseFacade.WrapParseResult>("result_list",modelListOfParseResult){
			private final static long serialVersionUID=1L;
			@Override
			protected void populateItem(ListItem<WrapParseResult> item) {
				DatabaseFacade.WrapParseResult object=item.getModelObject();
				item.add(new Label("parse_result_data_1", object.getStartPage()));
				item.add(new Label("parse_result_data_2", object.getResult()));
				item.add(new Label("parse_result_data_3", Integer.toString(object.getRecordCount())));
			}
		});
	}
	
	/** отобразить по SessionId список логгера с ошибками  */
	private void onLinkShowLoggerErrorBySessionId(Integer sessionId){
		this.setResponsePage(new LoggerView(sessionId));
	}
	
}
