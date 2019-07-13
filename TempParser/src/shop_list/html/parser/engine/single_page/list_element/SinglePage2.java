package shop_list.html.parser.engine.single_page.list_element;

import java.text.SimpleDateFormat;



import java.util.ArrayList;
import java.util.Date;

import org.w3c.dom.Node;


import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.EParseState;
import shop_list.html.parser.engine.ESessionResult;
import shop_list.html.parser.engine.IDetectEndOfParsing;
import shop_list.html.parser.engine.IManager;
import shop_list.html.parser.engine.logger.ILogger;
import shop_list.html.parser.engine.parser.IParser;
import shop_list.html.parser.engine.parser.MozillaAlternativeParser;
import shop_list.html.parser.engine.saver.ISaver;

/** шаблон для чтения только одной страницы, которая содержит все элементы в виде прайс-листа */
public abstract class SinglePage2 implements IManager, Runnable{
	/** логгер для сохранения данных  */
	protected ILogger logger;
	/** интерфейс по сохранению данных  */
	protected ISaver saver;
	/** полный путь к Mozilla scanner */
	protected String mozillaParserPath=null;
	/** парсер для получения данных из HTTP ресурса */
	protected IParser parser;
	
	protected Thread thread;
	protected boolean flagRun=false;
	
	protected IDetectEndOfParsing callback;

	/** уникальный идентификатор магазина в масштабе базы данных */
	private Integer shopId=null;
	
	/** уникальный идентификатор сессии, по которой происходит парсинг в данный момент */
	private Integer sessionId=null;
	

	/** текущее состояние парсинга  */
	protected volatile EParseState parseState=EParseState.READY;

	@Override
	public Integer start(IDetectEndOfParsing callback) {
		this.callback=callback;
		if(this.parseState.getKod()>0){
			System.out.println("already started");
			return null;
		}else if(this.logger==null){
			System.err.println("LOGGER WAS NOT SET ");
			thread=new Thread(this);
			this.flagRun=true;
			thread.start();
			return null;
		}else if(this.saver==null){
			logger.error(this, "SAVER WAS NOT SET ");
			thread=new Thread(this);
			this.flagRun=true;
			thread.start();
			return null;
		}else{
			// this.parseState.getKod()==0
			try{
				this.parser=this.getParserInstance();
				thread=new Thread(this);
				this.flagRun=true;
				// инициализация парсера
				shopId=this.saver.getShopId(this.getShopUrlStartPage());
				sessionId=this.saver.startNewSession(shopId, this.getDescription()+sdf.format(new Date()));
				thread.start();
				return sessionId;
			}catch(Exception ex){
				String message="Start Exception:"+ex.getMessage();
				System.err.println(message);
				logger.error(this, message);
				return null;
			}
		}
	}

	/** получить ссылку на парсер  */
	protected IParser getParserInstance(){
		return new MozillaAlternativeParser(null);
	}
	
	
	@Override
	public String pause() {
		return "NOT ALLOW";
	}
	
	@Override
	public String stop() {
		if(this.parseState.equals(EParseState.PROCESS)){
			this.parseState=EParseState.STOPPED;
			try{
				this.flagRun=false;
				this.thread.interrupt();
				return null;
			}catch(Exception ex){
				String message="ERROR: "+ex.getMessage();
				this.logger.error(this, message);
				return message;
			}
		}else{
			return null;
		}
	}

	@Override
	public void setLogger(ILogger logger) {
		this.logger=logger;
	}

	@Override
	public void setSaver(ISaver saver) {
		this.saver=saver;
	}

	@Override
	public EParseState getParseState() {
		return this.parseState;
	}
	
	@Override
	public void setParseState(EParseState state){
		this.parseState=state;
	}

	private final static SimpleDateFormat sdf=new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	
	/** получить полный XPathпуть к блоку данных XML, который содержит основные данные, указывает на ГРУППУ объектов {@link IParser#getNodeListFromUrl(Node, String)}   */
	protected abstract String getXmlPathToMainBlock();
	
	@Override
	public Integer getShopId(){
		if(this.shopId==null){
			shopId=this.saver.getShopId(this.getShopUrlStartPage());
		}
		return this.shopId;
	}
	
	@Override
	public Integer getSessionId(){
		return this.sessionId;
	}
	
	@Override
	public void run() {
		this.parseState=EParseState.PROCESS;
		if(sessionId==null){
			if(logger!=null){
				logger.warn(this, "ID session was not received");
			}
			this.parseState=EParseState.DONE_ERROR;
		}else if ((parser!=null)&&(flagRun)){
			logger.info(this, "чтение данных из удаленного URL");
			try{
				if(work(sessionId, parser.getNodeListFromUrl(this.getFullHttpUrlToPrice(), this.getCharset(), getXmlPathToMainBlock()))==false){
					this.saver.endSession(sessionId, ESessionResult.stopped);
					this.parseState=EParseState.STOPPED;
				}else{
					this.saver.endSession(sessionId, ESessionResult.ok);
					this.parseState=EParseState.DONE_OK;
				}
			}catch(Exception ex){
				logger.error(this, "#work Exception:"+ex.getMessage());
				// ex.printStackTrace(System.err);
				this.saver.endSession(sessionId, ESessionResult.error);
				this.parseState=EParseState.DONE_ERROR;
			}
		}else{
			if(logger!=null){
				logger.warn(this, "Check parameters");
			}
			this.parseState=EParseState.DONE_ERROR;
		}
		if(this.callback!=null){
			this.callback.endParsing(this, this.parseState);
		}
	}

	/** обработать полученный текст на предмет наличия данных  
	 * @param sessionId - уникальный идентификатор сессии 
	 * @param text - текст, который содержит полностью страницу с данными для парсинга
	 * @return 
	 * <ul>
	 * 	<li>true - успешно завершен </li>
	 * 	<li>false - остановлен </li>
	 * </ul> 
	 * @throws - вызвал ошибку парсинга 
	 */
	protected abstract boolean work(Integer sessionId, ArrayList<Node> listOfNode) throws Exception;

	/** полный HTTP путь к прайс-листу  */
	protected abstract String getFullHttpUrlToPrice();
	
	/** получить описание данного парсера для записи в файл */
	protected String getDescription(){
		return this.getShopUrlStartPage();
	}
	
	
	/** получить кодировку страницы в формате {@link java.nio.charset.Charset} основные данные находятся в enum {@link ECharset} 
	 * <ul>utf-8</ul>
	 * <ul>windows-1251</ul>
	 * <br>
	 * <b> регистр имеет значение - с маленькой буквы </b>
	 * */
	protected abstract String getCharset();
	
	/** получить страницу ресурса ( по которой будет происходить дальнейшее "опознание" данного ресурса  */
	@Override
	public abstract String getShopUrlStartPage();
}
