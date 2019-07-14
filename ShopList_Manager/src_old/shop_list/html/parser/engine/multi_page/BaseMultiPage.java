package shop_list.html.parser.engine.multi_page;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import shop_list.html.parser.engine.EParseState;
import shop_list.html.parser.engine.ESessionResult;
import shop_list.html.parser.engine.IDetectEndOfParsing;
import shop_list.html.parser.engine.IManager;
import shop_list.html.parser.engine.logger.ILogger;
import shop_list.html.parser.engine.multi_page.section.ResourceSection;
import shop_list.html.parser.engine.parser.Parser;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.saver.ISaver;


/** ������� �������� ��� ����������� ����������� �� �������� ������ � ������-���������� ���������� */
public abstract class BaseMultiPage implements IManager,Runnable{
	/** ������ */
	protected ILogger logger=null;
	/** ����������� ������  */
	protected ISaver saver=null;
	/** ������ ���� � ������� Mozilla */
	private String mozillaParserPath=null;
	/** ����, ������� �������������� ������������� � ����������� ������ ������ */
	private volatile boolean flagRun=false;
	/** ������, ���������� �� ������� ������� */
	protected Parser parser=null;
	/** �����, ������� ����������� ������� {@link #start()} */
	private Thread thread;
	/** ������� ��������� ��������  */
	protected volatile EParseState parseState=EParseState.READY;
	/** ���������� ������������� �������� � �������� ���� ������ */
	private Integer shopId=null;
	
	/** ���������� ������������� ������, �� ������� ���������� ������� � ������ ������ */
	private Integer sessionId=null;
	


	@Override
	public void setSaver(ISaver saver) {
		this.saver=saver;
	}
	@Override
	public void setLogger(ILogger logger) {
		this.logger=logger;
	}
	

	@Override
	public void setMozillaParserPath(String path) {
		this.mozillaParserPath=path;
	}

	private IDetectEndOfParsing callback;
	
	@Override
	public Integer start(IDetectEndOfParsing callback) {
		this.callback=callback;
		if(this.parseState.getKod()>0){
			logger.info(this, "already started");
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
	public String pause() {
		// TODO - ������������� ��������� ��������� 
		return "NOT ALLOWED";
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
	
	/** �������������� �������� */
	protected String getDescription(){
		return getShopUrlStartPage();
	}
	
	
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
		}else if ((parser!=null)&&(flagRun)){
			try{
				logger.info(this, "������ ��������� ���� ������� ");
				ArrayList<ResourceSection> sections=this.getSection();
				walkSection:for(int counter=0;counter<sections.size();counter++){
					/** ������ �� ������ �������� ������  */
					ArrayList<Record> firstRecordSet=null;
					boolean isFirstRecordSet=true;
					/** ������ �� ��������� ����������� �������� ������ */
					ArrayList<Record> lastRecordSet=null;
					// ��������� ������ 
					ResourceSection currentSection=sections.get(counter);
					Integer idSection=this.saver.getSectionId(currentSection.getName());
					logger.info(this, "��������� ��������� ������: "+currentSection.getName()+"   IdSection:"+idSection);
					/** ������, ���������� �� ������� ��������  */
					ArrayList<Record> nextRecordSet=null;
					walkPage: while( (nextRecordSet=this.getNextRecordSet(currentSection, firstRecordSet, lastRecordSet))!=null){
						if(isFirstRecordSet==true){
							// ����������� ���������� ������ �� ������ ��� ����������� � ������ �����������
							replaceDataFromSourceToDestination(nextRecordSet, firstRecordSet);
							isFirstRecordSet=false;
						}
						// ����������� ���������� ������ �� ������ ��� ����������� � ��������� �����������
						replaceDataFromSourceToDestination(nextRecordSet, lastRecordSet);
						
						if(nextRecordSet.size()==0){
							logger.debug(this, "��� ������ ��� ��������");
							break;
						}else{
							logger.debug(this, "���� ������ �� ��������: "+nextRecordSet.size());
							for(int index=0;index<nextRecordSet.size();index++){
								this.saver.saveRecord(sessionId, idSection, nextRecordSet.get(index)) ;
							}
						}
						try{
							Thread.sleep(this.getTimeoutForReadNextPage());
						}catch(InterruptedException ex){};
						if(flagRun==false){
							break walkPage;
						}
					}
					if(flagRun==false){
						break walkSection;
					}
					try{
						Thread.sleep(this.getTimeoutForReadNextSection());
					}catch(InterruptedException ex){};
					if(flagRun==false){
						break walkSection;
					}
				}
				if(flagRun==false){
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
		}
		if(this.callback!=null){
			this.callback.endParsing(this, this.parseState);
		}
	}
	
	/** �������� ��������� �������� � ������������ ����� ������� ���������� ������� */
	protected abstract long getTimeoutForReadNextSection();

	/** �������� ��������� �������� � ������������ ����� ������� ��������� �������� �������  */
	protected abstract long getTimeoutForReadNextPage();
	
	/** �������� ��� �������� �� destination ���������� �� source 
	 * @param source - �������� ������
	 * @param destination - �������� ������ ( ��� �������� ����� �������� �� ��������� )
	 * */
	private void replaceDataFromSourceToDestination(ArrayList<Record> source, ArrayList<Record> destination){
		if(source==null){
			destination=null;
		}else{
			if(destination==null){
				destination=new ArrayList<Record>(source.size());
			}else{
				destination.clear();
			}
			for(int counter=0;counter<source.size();counter++){
				destination.add(source.get(counter));
			}
		}
	}
	
	/** �� ��������� ������ �������� ��������� ������ ������ ��� ����������, ���� �� ������� null 
	 * ( ��� ������� ��������� ������ ) ���� �� ������� ArrayList ��� ���������  
	 */
	private ArrayList<Record> getNextRecordSet(ResourceSection section, ArrayList<Record> firstPageBlock, ArrayList<Record> lastPageBlock){
		try{
			ArrayList<Record> returnValue=new ArrayList<Record>();
			String nextUrl=section.getUrlToNextPage();
			logger.debug(this, "������ ��������� ��������: "+nextUrl);
			Node node=this.parser.getNodeFromUrlAlternative(nextUrl, this.getCharset(), this.getXmlPathToDataBlock());
			if(node!=null){
				logger.debug(this, "#getNextRecordSet ������� ���� c �������"); 
				returnValue=this.readRecordsFromBlock(node);
				if(returnValue==null){
					if(this.isConditionPresent(ESectionEnd.NEXT_RECORDS_LOAD_ERROR)){
						logger.debug(this, "#getNextRecordSet ������ ������ ��������� "+ESectionEnd.NEXT_RECORDS_LOAD_ERROR.toString());
						return null;
					}else{
						logger.error(this, "#getNextRecordSet �� ������� �������� List � ������� �� ��������: "+nextUrl);
					}
				}else{
					if(returnValue.size()==0){
						if(this.isConditionPresent(ESectionEnd.NEXT_RECORDS_ZERO_SIZE)){
							logger.debug(this, "#getNextRecordSet ������ ������ ��������� "+ESectionEnd.NEXT_RECORDS_ZERO_SIZE.toString());
							return null;
						}else{
							logger.error(this, "#getNextRecordSet ������ ��������� �� ����� ������ ���� : "+nextUrl);
						}
					}else{
						logger.debug(this, "�������� ���� � �������: "+returnValue.size());
						if(this.isConditionPresent(ESectionEnd.NEXT_RECORDS_SHOW_FIRST)){
							// �������� �� ����������� ������ ��������
							if(isListEquals(returnValue, firstPageBlock)){
								logger.debug(this, "���� ���������� ������ �������� ");
								return null;
							}
						}
						if(this.isConditionPresent(ESectionEnd.NEXT_RECORDS_REPEAT_LAST)){
							// �������� �� ����������� ���������� ��������  
							if(isListEquals(returnValue, firstPageBlock)){
								logger.debug(this, "���� ���������� ��������� �������� ");
								return null;
							}
						}
						logger.debug(this, "���� ������ ������" );
						return returnValue;
					}
				}
			}else{
				// �� ������� Node, ������� �� �������� ��� �������� 
				if(this.isConditionPresent(ESectionEnd.NEXT_PAGE_LOAD_ERROR)){
					logger.debug(this, "#getNextRecordSet ������ ������ ��������� "+ESectionEnd.NEXT_PAGE_LOAD_ERROR.toString());
					return null;
				}else{
					logger.error(this, "#getNextRecordSet �� ������� �������� ���� ������ �� ��������: "+nextUrl);
				}
			}
			return returnValue;
		}catch(Exception ex){
			logger.warn(this,"#getNextRecordSet Exception:"+ex.getMessage());
			return null;
		}
	}

	/** ����������� ������� ������� ���������� ������ ������ �� ������������ ����������  */
	private boolean isConditionPresent(ESectionEnd controlCondition){
		ESectionEnd[] sections=this.getSectionEndConditions();
		if((sections!=null)&&(sections.length>0)){
			for(int counter=0;counter<sections.length;counter++){
				if(sections[counter].equals(controlCondition)){
					return true;
				}
			}
			return false;
		}else{
			return false;
		}
	}
	
	/** �������� ������� ����������� �������� �� ������ �������� � ������ */
	protected abstract ESectionEnd[] getSectionEndConditions();
	
	
	/** �������� �� ��������������� ���� ������� � ��������� */
	private boolean isListEquals(ArrayList<Record> first, ArrayList<Record> second){
		if((first==null)&&(second==null)){
			// ��� ������� - null 
			return true;
		}else{
			if((first!=null)&&(second!=null)){
				if(first.size()==second.size()){
					boolean returnValue=true;
					for(int counter=0;counter<first.size();counter++){
						if(second.indexOf(first.get(counter))<0){
							returnValue=false;
							break;
						}
					}
					return returnValue;
				}else{
					// ������ ������� � �������� 
					return false;
				}
			}else{
				// ���� �� �������� ����� null
				return false;
			}
		}
	}
	
	/** �������� ��� ������ �� ����� ������, ������� �� �������� �� �������� 
	 * <br>
	 * � ��������� �� ��� {@link #getXmlPathToDataBlock()}
	 * @return 
	 * <ul>
	 * 	<li> <b> null </b> ���� ��������� ������ ������ ����� ������ </li>
	 * 	<li> <b> ArrayList.size()==0 </b> ��� ������ � ����� </li>
	 * 	<li> <b> ArrayList.size()>0 </b> ������ �� ����� ������ </li>
	 * </ul>
	  */
	protected ArrayList<Record> readRecordsFromBlock(Node node){
		if((node!=null)&&(node.hasChildNodes())){
			NodeList list=node.getChildNodes();
			ArrayList<Record> returnValue=new ArrayList<Record>();
			for(int counter=0;counter<list.getLength();counter++){
				if(  ( list.item(counter) instanceof Element)
				   &&( ((Element)list.item(counter)).getTagName().equalsIgnoreCase(getRecordTagNameInBlock()))){
					try{
						Record record=this.getRecordFromElement((Element)list.item(counter));
						if(record!=null){
							returnValue.add(record);
						}
					}catch(Exception ex){
						this.logger.error(this, "#getRecordsFromBlock:"+ex.getMessage());
						return null;
					}
				}
			}
			return returnValue;
		}else{
			return null;
		}
	}
	
	/** �������� ������� {@link Record} �� ������ ��������, �� ������� ������ �� ����� ������ ({@link #getXmlPathToDataBlock()}) ���  {@link #getRecordTagNameInBlock()}*/
	protected abstract Record getRecordFromElement(Element element) throws Exception;
	
	/** �������� ��� HTML ����, ������� �������������� � ����� HTML (������������ {@link #getXmlPathToDataBlock()}) ���� ������� ��� ������ ������ (Record) */
	protected abstract String getRecordTagNameInBlock();

	/** ������ ���� � ����� HTML ������� */
	@Override
	public abstract String getShopUrlStartPage();
	
	/** �������� ��������� �������� � ������� {@link java.nio.charset.Charset} 
	 * <ul>utf-8</ul>
	 * <ul>windows-1251</ul>
	 * <br>
	 * <b> ������� ����� �������� - � ��������� ����� </b>
	 * */
	protected abstract String getCharset();
	
	/** �������� ������ ���� ������ �� ������� �������  */
	protected abstract ArrayList<ResourceSection> getSection();
	
	/** ������ ���� � ����� ������ ( � �������� �� �������� ), ������� �������� ������ ��� ���������� */
	protected abstract String getXmlPathToDataBlock();
	
	
}
