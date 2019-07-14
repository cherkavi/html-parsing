package com.cherkashin.vitaliy.gui.actions.statistics.logger_view;

import java.util.ArrayList;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import com.cherkashin.vitaliy.dao.DatabaseFacade;
import com.cherkashin.vitaliy.dao.DatabaseFacade.LoggerInfo;

import database.ConnectWrap;
import database.StaticConnector;

public class LoggerView extends WebPage{
	public LoggerView(Integer sessionId){
		initComponents(sessionId);
	}
	
	private void initComponents(Integer sessionId){
		ConnectWrap connector=null;
		try{
			connector=StaticConnector.getConnectWrap();
			DatabaseFacade facade=new DatabaseFacade();
			this.add(new Label("title", this.getString("title")+facade.getShopStartPageBySessionId(connector, sessionId)));
			ArrayList<DatabaseFacade.LoggerInfo> listOfLogger=facade.getStatisticsLoggerDetail(connector, sessionId,1);
			this.add(new ListView<DatabaseFacade.LoggerInfo>("list", listOfLogger){
				private final static long serialVersionUID=1L;
				@Override
				protected void populateItem(ListItem<LoggerInfo> item) {
					DatabaseFacade.LoggerInfo object=item.getModelObject();
					item.add(new Label("level",object.getLevelName()));
					item.add(new Label("message",object.getMessage()));
				}
			});
		}finally{
			connector.close();
		}
	}
}
