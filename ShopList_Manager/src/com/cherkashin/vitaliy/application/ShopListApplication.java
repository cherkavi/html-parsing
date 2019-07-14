package com.cherkashin.vitaliy.application;

import it.sauronsoftware.ftp4j.FTPClient;

import java.io.File;
import java.text.SimpleDateFormat;

import java.util.Random;

import manager.Processor;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;

import org.apache.wicket.protocol.http.WebApplication;

import shop_list.html.parser.engine.EngineSettings;


import com.cherkashin.vitaliy.gui.actions.ActionsList;
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

	private String shopListParserFolder;
	private String pathToHttpShutdown;
	@Override
	protected void init() {
		try{
			StaticConnector.driver=DatabaseDrivers.valueOf(this.getInitParameter(initPrefix+"database_driver").trim());
		}catch(Exception ex){
			System.err.println("ShopListApplication database_driver Exception: "+ex.getMessage());
		}
		try{
			StaticConnector.url=StaticConnector.driver.getUrl(this.getInitParameter(initPrefix+"database_host"),"shop_list");
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
		
		try{
			pathToHttpShutdown=this.getInitParameter(initPrefix+"http_shutdown").trim();
		}catch(Exception ex){
			System.err.println("ShopListApplication http_shutdown Exception: "+ex.getMessage());
		}
		
		int maxParseCount=10;
		try{
			maxParseCount=Integer.parseInt(this.getInitParameter("parser_thread_count").trim());
		}catch(Exception ex){
			System.err.println("ShopListApplication database_password Exception: "+ex.getMessage());
		}
		try{
			this.shopListParserFolder=this.getInitParameter(initPrefix+"shop_list_folder").trim();
			Processor.parserClassesDirectory=this.shopListParserFolder;
			Processor.parserThreadCount=maxParseCount;
			Processor.getInstance();
			
		}catch(Exception ex){
			System.err.println("ShopListApplication database_password Exception: "+ex.getMessage());
		}
		
		try{
			EngineSettings.pathToTemp=this.getInitParameter(initPrefix+"parser_temp_folder").trim();
		}catch(Exception ex){
			System.err.println("ShopListApplication parser_temp_folder Exception: "+ex.getMessage());
		}
		
		getApplicationSettings().setPageExpiredErrorPage(ActionsList.class);
		getApplicationSettings().setAccessDeniedPage(ActionsList.class);
		// getApplicationSettings().setInternalErrorPage(MyInternalErrorPage.class);
		BasicConfigurator.configure();
		Logger.getLogger("org.apache.wicket").setLevel(Level.ERROR);
		Logger.getLogger("org.hibernate").setLevel(Level.WARN);
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
		return ActionsList.class;
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
	
	/** получить каталог в котором находятся парсеры */
	public String getShopListParsersFolder(){
		return this.shopListParserFolder;
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
	
	/** запустить указанный Action */
	public void startAction(Integer idAction) {
		Processor.getInstance().addActions(idAction);
		
	}
	
	/** остановить указанный Action */
	public void stopAction(Integer idAction) {
		Processor.getInstance().removeActions(idAction);
	}
	
	/** полностью остановить процесс */
	public void stopActionAll(){
		Processor.getInstance().stopThread();
	}
	
	/** запустить команду "выключения" сервера  
	 * @return null - если все прошло успешно 
	 * */
	public String shutdownServer() throws Exception {
		try{
			ShellExecute.execute(this.pathToHttpShutdown);
			return null;
		}catch(Exception ex){
			return ex.getMessage();
		}
		
	}
	
	/** получить случайным образом сгенерированный путь к дампу */
	private String getRandomPathToSqlDump(){
		Random random=new Random();
		StringBuffer returnValue=new StringBuffer();
		for(int counter=0;counter<10;counter++){
			returnValue.append(Integer.toHexString(random.nextInt(16)));
		}
		return EngineSettings.pathToTemp+returnValue.toString()+".sql";
	}
	
	/** создать дамп базы данных MySQL */
	public String createDumpMySqlDump() {
		String dumpFile=this.getRandomPathToSqlDump();
		String executeCommand="mysqldump --user="+StaticConnector.userName+" --password="+StaticConnector.password+" shop_list currency_value parse_record parse_result parse_session section shop_list  > "+dumpFile;
		try{
			if(ShellExecute.execute(executeCommand)!=0){
				return null;
			}else{
				return dumpFile;
			}
		}catch(Exception ex){
			System.err.println("createDumpMySqlDump Exception:"+ex.getMessage());
			return null;
		}
	}
	
	/** копировать файлы на FTP сервер 
	 * @param pathToDump - полный путь к файлу
	 * @param ftpServer - сервер
	 * @param port - порт
	 * @param ftpLogin - логин 
	 * @param ftpPassword - пароль 
	 * @return
	 */
	public boolean copyFileToFtp(String pathToDump, 
								 String ftpServer, 
								 int port, 
								 String ftpLogin, 
								 String ftpPassword) {
		try{
			FTPClient client = new FTPClient();
			client.connect(ftpServer, port);
			client.login(ftpLogin, ftpPassword);
			client.upload(new File(pathToDump));
			client.disconnect(true);
			return true;
		}catch(Exception ex){
			System.err.println("Exception: #copyFileToFtp:"+ex.getMessage());
			return false;
		}
	}
	
	
	/** удалить указанный файл */
	public void removeFile(String pathToFile){
		try{
			File f=new File(pathToFile);
			if(f.delete()==false){
				f.deleteOnExit();
			}
		}catch(Exception ex){
			return ;
		}
	}
}
