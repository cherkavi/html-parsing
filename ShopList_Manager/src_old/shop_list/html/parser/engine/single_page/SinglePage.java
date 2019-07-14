package shop_list.html.parser.engine.single_page;

import java.text.SimpleDateFormat;

import java.util.Date;

import org.w3c.dom.Node;

import shop_list.html.parser.engine.EParseState;
import shop_list.html.parser.engine.ESessionResult;
import shop_list.html.parser.engine.IDetectEndOfParsing;
import shop_list.html.parser.engine.IManager;
import shop_list.html.parser.engine.logger.ILogger;
import shop_list.html.parser.engine.parser.Parser;
import shop_list.html.parser.engine.saver.ISaver;

/** ������ ��� ������ ������ ����� ��������, ������� �������� ��� �������� � ���� �����-����� */
public abstract class SinglePage implements IManager, Runnable{
	/** ������ ��� ���������� ������  */
	protected ILogger logger;
	/** ��������� �� ���������� ������  */
	protected ISaver saver;
	/** ������ ���� � Mozilla scanner */
	protected String mozillaParserPath=null;
	/** ������ ��� ��������� ������ �� HTTP ������� */
	protected Parser parser;
	
	protected Thread thread;
	protected boolean flagRun=false;
	
	protected IDetectEndOfParsing callback;

	/** ���������� ������������� �������� � �������� ���� ������ */
	private Integer shopId=null;
	
	/** ���������� ������������� ������, �� ������� ���������� ������� � ������ ������ */
	private Integer sessionId=null;
	

	/** ������� ��������� ��������  */
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
				parser=new Parser(this.mozillaParserPath);
				thread=new Thread(this);
				this.flagRun=true;
				// ������������� �������
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
	
	/** �������� ������ XPath���� � ����� ������ XML, ������� �������� �������� ������  */
	protected abstract String getXmlPathToMainBlock();
	
	@Override
	public Integer getShopId(){
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
			logger.info(this, "������ ������ �� ���������� URL");
			try{
				if(work(sessionId, parser.getNodeFromUrl(this.getFullHttpUrlToPrice(), this.getCharset(), getXmlPathToMainBlock()))==false){
					this.saver.endSession(sessionId, ESessionResult.stopped);
					this.parseState=EParseState.STOPPED;
				}else{
					this.saver.endSession(sessionId, ESessionResult.ok);
					this.parseState=EParseState.DONE_OK;
				}
			}catch(Exception ex){
				logger.error(this, "#work Exception:"+ex.getMessage());
				ex.printStackTrace(System.err);
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

	@Override
	public void setMozillaParserPath(String path) {
		this.mozillaParserPath=path;
	}
	
	/** ���������� ���������� ����� �� ������� ������� ������  
	 * @param sessionId - ���������� ������������� ������ 
	 * @param text - �����, ������� �������� ��������� �������� � ������� ��� ��������
	 * @return 
	 * <ul>
	 * 	<li>true - ������� �������� </li>
	 * 	<li>false - ���������� </li>
	 * </ul> 
	 * @throws - ������ ������ �������� 
	 */
	protected abstract boolean work(Integer sessionId, Node node) throws Exception;

	/** ������ HTTP ���� � �����-�����  */
	protected abstract String getFullHttpUrlToPrice();
	
	/** �������� �������� ������� ������� ��� ������ � ���� */
	protected String getDescription(){
		return this.getShopUrlStartPage();
	}
	
	
	/** �������� ��������� �������� � ������� {@link java.nio.charset.Charset} 
	 * <ul>utf-8</ul>
	 * <ul>windows-1251</ul>
	 * <br>
	 * <b> ������� ����� �������� - � ��������� ����� </b>
	 * */
	protected abstract String getCharset();
	
	/** �������� �������� ������� ( �� ������� ����� ����������� ���������� "���������" ������� �������  */
	@Override
	public abstract String getShopUrlStartPage();
}
