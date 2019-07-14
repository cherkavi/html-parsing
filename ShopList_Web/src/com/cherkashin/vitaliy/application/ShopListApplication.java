package com.cherkashin.vitaliy.application;

import java.text.SimpleDateFormat;

import java.util.Random;

import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;

import org.apache.wicket.protocol.http.WebApplication;

import com.cherkashin.vitaliy.gui.main.MainPage;
import com.cherkashin.vitaliy.session.ShopListSession;

import database.DatabaseDrivers;
import database.StaticConnector;


public class ShopListApplication extends WebApplication{
	
	private static final String initPrefix;
	static {
		if(isWindows()){
			initPrefix="windows_";
		}else{
			if(isMac()){
				initPrefix="mac_";
			}else{
				initPrefix="unix_";
			}
		}
	}
	
	private static boolean isWindows(){
		String os = System.getProperty("os.name").toLowerCase();
	    return (os.indexOf( "win" ) >= 0); 
	}
	private static boolean isMac(){
		String os = System.getProperty("os.name").toLowerCase();
	    return (os.indexOf( "mac" ) >= 0); 
	}
	/* private static boolean isUnix(){
		String os = System.getProperty("os.name").toLowerCase();
	    return (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0);
	}*/
	
	@Override
	protected void init() {
		try{
			StaticConnector.driver=DatabaseDrivers.valueOf(this.getInitParameter(initPrefix+"database_driver").trim());
		}catch(Exception ex){
			System.err.println("ShopListApplication database_driver Exception: "+ex.getMessage());
		}
		try{
			StaticConnector.url=StaticConnector.driver.getUrl(this.getInitParameter(initPrefix+"database_host"),this.getInitParameter(initPrefix+"database_name"));
		}catch(Exception ex){
			System.err.println("ShopListApplication database_url Exception: "+ex.getMessage());
		}
		try{
			StaticConnector.userName=this.getInitParameter(initPrefix+"database_login").trim();
		}catch(Exception ex){
			System.err.println("ShopListApplication database_login Exception: "+ex.getMessage());
		}
		try{
			StaticConnector.password=this.getInitParameter(initPrefix+"database_password").trim();
		}catch(Exception ex){
			System.err.println("ShopListApplication database_password Exception: "+ex.getMessage());
		}
		getApplicationSettings().setPageExpiredErrorPage(MainPage.class);
		getApplicationSettings().setAccessDeniedPage(MainPage.class);
		// getApplicationSettings().setInternalErrorPage(MyInternalErrorPage.class);		
	}

	/* 
	//  получить полный путь к каталогу с Шаблонами отчетов  
	public String getPathToPatternDirectory(){
		return this.pathToPatternDirectory;
	}
	
	// получть полный путь к каталогу с выходными отчетами  
	public String getPathToOutputReport(){
		return this.pathToOutputReport;
	}
	*/
	
	@Override
	public Class<? extends Page> getHomePage() {
		return MainPage.class;
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new ShopListSession(request);
	}
	
	private SimpleDateFormat sdf=new SimpleDateFormat("MMddHHmmss");
	private Random random=new Random();
	
	/** сгенерировать уникальный идентификатор */
	public String generateUniqueId(){
		return sdf.format(new java.util.Date())+this.getRandomString(7);
	}
	
	/** получить строку из случайной последовательности символов */
	private String getRandomString(int count){
		StringBuffer returnValue=new StringBuffer();
		for(int counter=0;counter<counter;counter++)returnValue.append(random.nextInt(16));
		return returnValue.toString();
	}
	
	/** максимальное время ожидания готового отчета */
	public long getMaxWaitReportTime() {
		return 60000;
	}
	
}
