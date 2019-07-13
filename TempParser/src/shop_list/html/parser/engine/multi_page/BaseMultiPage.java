package shop_list.html.parser.engine.multi_page;

import java.text.SimpleDateFormat;



import java.util.ArrayList;
import java.util.Date;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.EParseState;
import shop_list.html.parser.engine.ESessionResult;
import shop_list.html.parser.engine.IDetectEndOfParsing;
import shop_list.html.parser.engine.IManager;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.exception.EParseExceptionItIsNotRecord;
import shop_list.html.parser.engine.exception.EParseExceptionNotInStore;
import shop_list.html.parser.engine.exception.EParseExceptionRecordListEmpty;
import shop_list.html.parser.engine.exception.EParseExceptionRecordListLoadError;
import shop_list.html.parser.engine.logger.ILogger;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.DirectFinder;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.RecursiveFinder;
import shop_list.html.parser.engine.multi_page.section_finder.two_level_finder.TwoLevelFinder;
import shop_list.html.parser.engine.parser.IParser;
import shop_list.html.parser.engine.parser.MozillaAlternativeParser;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.saver.ISaver;
import shop_list.html.parser.engine.multi_page.debug.PrintElement;


/** ������� �������� ��� ����������� ����������� �� �������� ������ � ������-���������� ����������
 * <br> 
 * 
 * <br>
 * <table border=1>
 * 	<tr>
 * 		<td colspan="3" align="center">������� ��������������/������� ������:</td>
 * 	</tr>
 * 	<tr>
 * 		<td>1</td><td>{@link #getSectionEndConditions()} </td> <td>������ ������� ��������� ������  </td>
 * 	</tr>
 * 	<tr>
 * 		<td>2</td><td>{@link #getCharset()} </td> <td> ��������� �������� {@link EParseState}  ( �� ��������� UTF-8)</td>
 * 	</tr>
 * 	<tr>
 * 		<td>3</td><td>{@link #getShopUrlStartPage()} </td> <td> ��������� �������� ������� �������</td>
 * 	</tr>
 * 	<tr>
 * 		<td>4</td><td>{@link #getTimeoutForReadNextPage()} </td> <td> ��������� �������� ����� ������� ��������� �������� (human emulation) </td>
 * 	</tr>
 * 	<tr>
 * 		<td>5</td><td>{@link #getTimeoutForReadNextSection()} </td> <td> ��������� �������� ����� ������� ��������� ������ (human emulation) </td>
 *	</tr>
 * 	<tr>
 * 		<td>6</td><td>{@link #getSection()} </td> <td> �������� ������ ���� ������, ������� ����� ���� � ������ �������� </td>
 *  </tr> 
 * 	<tr>
 * 		<td>7</td><td>class CurrentSession extends {@link NextSection}</td> <td> ������� <b>����������</b> �����, ������� ����� ���������������� �������� ������ ������</td>
 *  </tr> 
 * 	<tr>
 * 		<td>8</td><td>{@link #getXmlPathToDataBlock()} </td> <td> XPath � ����� ������, ������� �������� ��� ������ (��� �������� �� ��������) </td>
 *  </tr> 
 *  <tr>
 *  	<td>9</td><td>�������� ������ �� ����� ������:</td>
 *  	<td>
 *  		<table border=1>
 *  			<tr>
 *  				<td>1</td>
 *  				<td>
 *  					<ul>
 *  						<li>{@link #getRecordTagNameInBlock()}</li>
 *  						<li>{@link #getRecordFromElement(Element element)}</li>
 *  					</ul>	
 * 					</td>
 * 					<td>
 * 						<ul>
 * 							<li>�������� �� ������ ������� ������ �� ��������� ��� ( ������� ),</li>
 * 							<li>����� �� ����� �������� �������� ������ ��� ����������</li>
 * 						</ul>
 * 					</td>
 *  			<tr>
 *  			<tr>
 *  				<td>2</td>
 *  				<td>{@link #getRecordsFromBlock(Node node)} </td>
 *  				<td> �������� ������ ������� �� ������ �� ������� ���������  {@link #getXmlPathToDataBlock()}</td>
 *  			<tr>
 *  		</table>
 *  	</td>
 *  </tr>
 * </table>
 * */
public abstract class BaseMultiPage implements IManager,Runnable{
	/** ������ */
	protected ILogger logger=null;
	/** ����������� ������  */
	protected ISaver saver=null;
	/** ����, ������� �������������� ������������� � ����������� ������ ������ */
	private volatile boolean flagRun=false;
	/** ������, ���������� �� ������� ������� */
	protected IParser parser=null;
	/** �����, ������� ����������� ������� {@link #start()} */
	private Thread thread;
	/** ������� ��������� ��������  */
	protected volatile EParseState parseState=EParseState.READY;
	/** ���������� ������������� �������� � �������� ���� ������ */
	private Integer shopId=null;
	
	/** ���������� ������������� ������, �� ������� ���������� ������� � ������ ������ */
	private Integer sessionId=null;
	
	protected void debugPrintNode(String pathToFile, Node node){
		PrintElement printer=new PrintElement(pathToFile); 
		printer.outputAsTree(node);		
	}
	
	/** �������� ������-������
	 * <ul>
	 * 	<li>new MozillaAlternativeParser</li>
	 * 	<li>new NekoParser</li>
	 * </ul>
	 * */
	protected IParser getParserInstance(){
		return new MozillaAlternativeParser(null);
	}

	@Override
	public void setSaver(ISaver saver) {
		this.saver=saver;
	}
	@Override
	public void setLogger(ILogger logger) {
		this.logger=logger;
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
				// parser=new MozillaAlternativeParser(this.mozillaParserPath);
				// parser=new NekoParser();
				this.parser=this.getParserInstance();
				
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
		}else if ((parser!=null)&&(flagRun)){
			try{
				logger.info(this, "������ ��������� ���� ������� ");
				ArrayList<INextSection> sections=this.getSection();
				walkSection:for(int counter=0;counter<sections.size();counter++){
					/** ������ �� ������ �������� ������  */
					ArrayList<Record> firstRecordSet=new ArrayList<Record>();
					boolean isFirstRecordSet=true;
					/** ������ �� ��������� ����������� �������� ������ */
					ArrayList<Record> lastRecordSet=new ArrayList<Record>();
					// ��������� ������ 
					INextSection currentSection=sections.get(counter);
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
							continue;
						}else{
							logger.debug(this, "���� ������ �� ��������: "+nextRecordSet.size());
							for(int index=0;index<nextRecordSet.size();index++){
								// FIXME ����� ���������� ������ 
								if((nextRecordSet.get(index)!=null)&&(!priceIsEmpty(nextRecordSet.get(index))))
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
					if(sections.size()==0){
						this.logger.error(this, "check get section interface");
						this.saver.endSession(sessionId, ESessionResult.error);
						this.parseState=EParseState.DONE_ERROR;
					}else{
						this.saver.endSession(sessionId, ESessionResult.ok);
						this.parseState=EParseState.DONE_OK;
					}
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
		}
		if(this.callback!=null){
			this.callback.endParsing(this, this.parseState);
		}
	}
	
	/** "�����" �������� � ������(��� ����� ������) ������ ��������� (http://), ���� ��� ���������� (���� ��� �� �������� ) 
	 * @param startPage - ��������� �������� 
	 * @param href - ������ 
	 * <ul>
	 * 	<li> http:// </li>
	 * 	<li> / </li>
	 * 	<li> ./ </li>
	 * 	<li> ../ </li>
	 * 	<li>  </li>
	 * </ul>
	 * @return
	 */
	protected String addHttpPreambula(String startPage, String href){
		startPage=startPage.trim();
		if(startPage.endsWith("/")){
			startPage=startPage.substring(0, startPage.length()-1);
		}
		href=href.trim();
		if(href.startsWith("http://")){
			return href;
		}
		if(href.startsWith("/")){
			return startPage+href;
		}
		if(href.startsWith("./")){
			return startPage+href.substring(1);
		}
		if(href.startsWith("../")){
			return startPage+href.substring(2);
		}
		return startPage+"/"+href;
	}
	
	
	/** �������� �� ���� "������"  */
	private boolean priceIsEmpty(Record record){
		if(record==null) return true;
		if((record.getPrice()==null)&&(record.getPriceUsd()==null)&&(record.getPriceEuro()==null))return true;
		boolean returnValue=false;
		// ����� ����� ����, ���� ������� ���� 
		while(true){
			if(record.getPrice()!=null){
				if(record.getPrice().floatValue()!=0)break;
			}
			if(record.getPriceUsd()!=null){
				if(record.getPriceUsd().floatValue()!=0)break;
			}
			if(record.getPriceEuro()!=null){
				if(record.getPriceEuro().floatValue()!=0)break;
			}
			returnValue=true;
			break;
		}
		return returnValue;
	}
	
	/** �������� ��������� �������� � ������������ ����� ������� ���������� ������� */
	protected long getTimeoutForReadNextSection(){
		return 200;		
	}

	/** �������� ��������� �������� � ������������ ����� ������� ��������� �������� �������  */
	protected long getTimeoutForReadNextPage(){
		return 2000;		
	}
	
	
	/** �������� Float �� ��������� ������  */
	protected Float getFloatFromString(String value){
		if(value==null)return null;
		String tempValue=value.trim();
		try{
			return Float.parseFloat(tempValue);
		}catch(Exception ex){
			// ��������, ����� �������� ����� �� �������
			try{
				return Float.parseFloat(tempValue.replaceAll(",", "."));
			}catch(Exception exInner){
				return null;
			}
			
		}
	}
	
	/** �������� ��� �������� �� destination ���������� �� source 
	 * @param source - �������� ������
	 * @param destination - �������� ������ ( ��� �������� ����� �������� �� ��������� ) nullable==false
	 * */
	private void replaceDataFromSourceToDestination(ArrayList<Record> source, ArrayList<Record> destination){
		if(destination==null)assert false:"#replaceDataFromSourceToDestination destination is null";
		destination.clear();
		if((source!=null)&&(source.size()>0)){
			for(int counter=0;counter<source.size();counter++){
				destination.add(source.get(counter));
			}
			
		}
	}
	
	/** �������� ��������� ���-�� ���������� ��� ���������� "������������" - ��� ���������� ��������� ������ ������ (default=50)*/
	protected int getWatchDogEmptyPageLimit(){
		return 20;
	}
	
	/** ��� ���������� ������ ���������� ERROR ���������  */
	protected boolean getWatchDogEmptyPageLimitShowError(){
		return true;
	}
	
	private int errorCounter=0;
	
	/** �������� �� ��������� {@link #getXmlPathToDataBlock()} Node �� ��������   
	 * @param  nextUrl - �������� ��� ��������� ������ 
	 * @param  charset - ��������� ��������  
	 * */
	protected Node getDataBlockFromUrl(String nextUrl, String charset) throws Exception{
		return this.parser.getNodeFromUrl(nextUrl, this.getCharset(), this.getXmlPathToDataBlock());
	}
	
	/** ������� �������� */
	private String currentUrl;
	/** �������� ������ �� ������� ��������, � ������� ������������ ����� */
	
	/** �������� ������� �������� */
	protected String getCurrentUrl(){
		return this.currentUrl;
	}
	
	/** ���������� ������� ��������  */
	private void setCurrentUrl(String url){
		this.currentUrl=url;
	}
	
	/** �� ��������� ������ �������� ��������� ������ ������ ��� ����������, ���� �� ������� null 
	 * ( ��� ������� ��������� ������ ) ���� �� ������� ArrayList ��� ���������  
	 */
	private ArrayList<Record> getNextRecordSet(INextSection section, ArrayList<Record> firstPageBlock, ArrayList<Record> lastPageBlock){
		String nextUrl=null;
		try{
			ArrayList<Record> returnValue=new ArrayList<Record>();
			logger.debug(this, "������� ��������� ��������� ��������:");
			nextUrl=section.getUrlToNextPage();
			this.setCurrentUrl(nextUrl);
			logger.debug(this, "��������� �������� ��������: "+nextUrl);
			logger.debug(this, "��������� �� �������� XPath:"+this.getXmlPathToDataBlock());
			// if(nextUrl.equals("http://pikmarket.kiev.ua/?path=0/2/774/1695/1782/1713/1753")){System.out.println("control");}
			Node node=this.getDataBlockFromUrl(nextUrl, this.getCharset());//  
			if(node!=null){
				logger.debug(this, "#getNextRecordSet ������� ���� c �������");
				try{
					returnValue=this.getRecordsFromBlock(node);
				}catch(EParseExceptionEmptyRecord ep){
					// ��� ������� � ����� ������  
					if(this.isConditionPresent(ESectionEnd.NEXT_RECORDS_ZERO_SIZE)){
						logger.debug(this, "#getNextRecordSet ������ ������ ��������� "+ESectionEnd.NEXT_RECORDS_ZERO_SIZE.toString());
						return null;
					}else{
						logger.error(this, "#getNextRecordSet ������ ��������� �� ����� ������ ���� : "+nextUrl);
					}
				}catch( EParseExceptionRecordListLoadError ep){
					logger.debug(this, "������ �������� �������� ");
					returnValue=null;
				}
				// ������ ����������� ����������
				if(returnValue==null){
					if(this.isConditionPresent(ESectionEnd.NEXT_RECORDS_LOAD_ERROR)){
						logger.debug(this, "#getNextRecordSet ������ ������ ��������� "+ESectionEnd.NEXT_RECORDS_LOAD_ERROR.toString());
						return null;
					}else{
						logger.error(this, "#getNextRecordSet �� ������� �������� List � ������� �� ��������: "+nextUrl);
					}
				}else{
					if(returnValue.size()==0){
						// � ������ ����� �� ����� �������� �� NEXT_RECORDS_ZERO_SIZE - � ��� ����� �� ��������, �.�. � ������ ������� ����� ���������� ������ ���������� EParseExceptionEmptyRecord 
						errorCounter++;
						if(errorCounter>getWatchDogEmptyPageLimit()){
							if(getWatchDogEmptyPageLimitShowError()){
								logger.error(this, "BaseMultiPage WatchDog empty page !!! watchdog stop parse section check engine !!! ");
							}else{
								logger.info(this, "BaseMultiPage WatchDog watchdog stop parse ");
							}
							return null;
						}else{
							logger.debug(this, "#getNextRecordSet �������� ��� ������ �� ������ �� ����(=0) ���� �� ��� �� ������ ");
							return returnValue;
						}
					}else{
						errorCounter=0;
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
							if(isListEquals(returnValue, lastPageBlock)){
								logger.debug(this, "���� ���������� ��������� �������� ");
								return null;
							}
						}
						logger.debug(this, "���� ������ ������" );
						return returnValue;
					}
				}
				// ���� �� �������� ��������� 0 ������� � ���� ������� ����������� �������� ������ ����� ���������� ����������� ������ �������� - ���������� ������� ������, ����� - infinity loop 
// >>>   ������, ��������, ����� ��������� � ������ ����� 
				// if((this.isConditionPresent(ESectionEnd.NEXT_RECORDS_SHOW_FIRST)&&(firstPageBlock!=null)&&(firstPageBlock.size()==0))){return null;}
				return returnValue;
			}else{
				logger.info(this, "�� ������� Node, ������� �� �������� ��� �������� - ��������� ������, �.�. ����� ���� �������� ������, ���� �� ����"); 
				/*if(this.isConditionPresent(ESectionEnd.NEXT_PAGE_LOAD_ERROR)){
					logger.debug(this, "#getNextRecordSet ������ ������ ��������� "+ESectionEnd.NEXT_PAGE_LOAD_ERROR.toString());
				}else{
					logger.error(this, "#getNextRecordSet �� ������� �������� ���� ������ �� ��������: "+nextUrl);
				}*/
				return null;
			}
		}catch(Exception ex){
			System.err.println("if section created of Analisator - check it:"+ex.getMessage());
			logger.info(this,"#getNextRecordSet checkUrl("+nextUrl+") Exception:"+ex.getMessage());
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
	
	/** Post-���������� ��� record, ����� ���� �����������, ���� ��������� ��������� ��������� ����� ������ ����� ��������� �� ������ ��������  */
	protected Record prepareRecordBeforeSave(Record record) throws EParseException{
		return record;
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
	 * @throws EParseExceptionRecordListEmpty - ���� �� ����� ������ �� ������� � ����� ������ ( �� �� ������������ ������� ������������� ������ )
	 * <br />
	 * ��� ������ ���������� ������ ����������� ���������� {@link #recordPostProcessor()}
	  */
	protected ArrayList<Record> getRecordsFromBlock(Node node) throws EParseException{
		if((node!=null)&&(node.hasChildNodes())){
			NodeList list=node.getChildNodes();
			ArrayList<Record> returnValue=new ArrayList<Record>();
			boolean anyRecordPresent=false;
			for(int counter=0;counter<list.getLength();counter++){
				if(  ( list.item(counter) instanceof Element)
				   &&( ((Element)list.item(counter)).getTagName().equalsIgnoreCase(getRecordTagNameInBlock()))){
					try{
						Record record=this.prepareRecordBeforeSave(this.getRecordFromElement((Element)list.item(counter)));
						anyRecordPresent=true;
						if(record!=null){
							returnValue.add(record);
						}
					}catch(EParseExceptionNotInStore ep){
						// ��������� ������ �� �� ������ 
						anyRecordPresent=true;
						returnValue.add(new Record());
						logger.debug(this, "������ �� �� ������");
					}catch(EParseExceptionItIsNotRecord ep){
						// ��������� ������ �� ����� ���� ������������ ��� Record
					}catch(Exception ex){
						this.logger.error(this, "#getRecordsFromBlock:"+ex.getMessage());
						// return null;
					}
				}
			}
			if(anyRecordPresent==false)throw new EParseExceptionRecordListEmpty();
			return returnValue;
		}else{
			// ������ �� ������� �� ���������� ���� - ������ ���������� �������
			throw new EParseExceptionRecordListLoadError();
		}
	}
	
	/** �������� ������� {@link Record} �� ������ ��������, �� ������� ������ �� ����� ������ ({@link #getXmlPathToDataBlock()}) ���  {@link #getRecordTagNameInBlock()}
	 * <br>
	 * � ������ ���� �������� ����������� �� �������, � ����� � ����� ������������ ����,���� � ����� �������, 
	 * 	������� �������������� ����� {@link #readRecordsFromBlock} � �� ������� �������������� ({@link #getRecordFromElement(Element)}) � {@link #getRecordTagNameInBlock()}
	 * @throws 
	 *  EParseExceptionItIsNotRecord - ���� ���������� ������� �� ����� ���� ��������������� ��� ������
	 *  @return null - ���� �� ������� �������� ������ ��-�� ��������� ����, ���� �� �� �� ���������� ��������
	 *  ( ���� �� ������� �������� �������, ������� ��������������� �� ���� ���� ������������ - ������� ����������� {@link EParseExceptionItIsNotRecord}) 
	 * */
	protected Record getRecordFromElement(Element element) throws EParseException{
		throw new RuntimeException("replace method BaseMultiPage#getRecordFromElement or (BaseMultiPage#readRecordsFromBlock)");
	}
	
	/** �������� ��� HTML ����, ������� �������������� � ����� HTML (������������ {@link #getXmlPathToDataBlock()}) ���� ������� ��� ������ ������ (Record) 
	 * <br>
	 * � ������ ���� �������� ����������� �� �������, � ����� � ����� ������������ ����,���� � ����� �������, 
	 * 	������� �������������� ����� {@link #readRecordsFromBlock} � �� ������� �������������� ({@link #getRecordFromElement(Element)}) � {@link #getRecordTagNameInBlock()}	 
	 * */
	protected String getRecordTagNameInBlock(){
		throw new RuntimeException("replace method BaseMultiPage#getRecordTagNameInBlock or (BaseMultiPage#readRecordsFromBlock)");
	}

	/** ������ ���� � ����� HTML ������� */
	@Override
	public abstract String getShopUrlStartPage();
	
	/** �������� ��������� �������� � ������� {@link java.nio.charset.Charset} ��� �� ����� ����� �� {@link ECharset}
	 * ���������� ��������� (<b> ������� ����� �������� - � ��������� ����� </b>)  
	 * <ul>
	 * 	<li>utf-8</li>
	 * 	<li>windows-1251</li>
	 * </ul>
	 * <br>
	 * 
	 * �������� ��-���������: "utf-8"
	 * */
	protected String getCharset(){
		return ECharset.UTF_8.getName();		
	}
	
	/** �������� ������ ���� ������ �� ������� �������  
	 * <br>
	 * {@link DirectFinder}
	 * <br>
	 * {@link TwoLevelFinder}
	 * <br>
	 * {@link RecursiveFinder}
	 * */
	protected abstract ArrayList<INextSection> getSection();
	
	/** ������ ���� � ����� ������ ( � �������� �� �������� ), ������� �������� ������ ��� ���������� 
	 * <br>
	 * ����������� ������ ���������� ������ ���� �������, ���� ������� �������
	 * */
	protected abstract String getXmlPathToDataBlock();
	
	
	/** � ����������� Node ��������� ������� ��������� � ��� ��������������� �� ���������� ��������  
	 * @param item - Element
	 * @param attributeName - ��� ��������� 
	 * @param attributeValue - �������� ���������
	 * @return
	 */
	protected boolean isNodeAttributeEquals(Node item, String attributeName, String attributeValue) {
		boolean returnValue=false;
		if(item instanceof Element){
			String value=((Element)item).getAttribute(attributeName);
			if(value!=null){
				returnValue=value.trim().equalsIgnoreCase(attributeValue);
			}
		}
		return returnValue;
	}

	/**
	 * ���������� �� ��������� ��������� 
	 * @param presentNode - ������� 
	 * @param recordFromNodeIsPresentAttr - 
	 * @return
	 */
	protected boolean isNodeAttributeExists(Node item, String attributeName) {
		boolean returnValue=false;
		if(item instanceof Element){
			return ((Element)item).getAttribute(attributeName)!=null;
		}
		return returnValue;
	}

	

	/** �� ���������� Node �������� �������� �� ��� �����
	 * @param node - ������� Node
	 * @param attrName - ��� ���������
	 * @return
	 */
	protected String getNodeAttribute(Node node, String attrName) {
		String returnValue=null;
		if(node instanceof Element){
			returnValue=((Element)node).getAttribute(attrName);
			if(isStringEmpty(returnValue))returnValue=null;
		}
		return returnValue;
	}

	
	/** 
	 * ��������� �� ������������ ������������� �������� ������ �� ������������� 
	 * @param original - ������������ ��������
	 * @param controlValues - ��������, ������� ����� ��������� �� ������������ 
	 * */
	protected boolean isStringEqualsAny(String original, String ... controlValues) {
		if(original==null)return false;
		if((controlValues==null)||(controlValues.length==0))return true;
		boolean returnValue=false;
		String newValue=original.trim();
		for(int counter=0;counter<controlValues.length;counter++){
			if(controlValues[counter]!=null)
			if(newValue.equalsIgnoreCase(controlValues[counter].trim())){
				returnValue=true;
				break;
			}
		}
		return returnValue;
	}
	

	/** �������� �� ������ ������ */
	protected boolean isStringEmpty(String value){
		return (value==null)||(value.trim().equals(""));
	}

	/** ������� �� ������ ����� ���������� ������� ��� ��������, ������� ��� �� */
	protected String removeAfterSymbolIncludeIt(String url, char c) {
		int index=url.indexOf(c);
		if(index>0){
			return url.substring(0,index); 
		}else{
			return url;
		}
	}
	/** ������� �� ������ ����� ���������� (����������) ������� ��� ��������, ������� ��� �� */
	protected String removeAfterSymbolLastIncludeIt(String url, char c) {
		int index=url.lastIndexOf(c);
		if(index>0){
			return url.substring(0,index); 
		}else{
			return url;
		}
	}

	/** ������� �� ������ ������ (�������� ��� �������, ������, 160 ) */
	protected String removeCharFromString(String value, int charKod){
		StringBuffer returnValue=new StringBuffer();
		for(int counter=0;counter<value.length();counter++){
			if(value.codePointAt(counter)!=charKod){
				returnValue.append(value.charAt(counter));
			}
		}
		return returnValue.toString();
	}
	
	
	/** ������� �� ��������� ������, ������� �� */
	protected String removeBeforeStringIncludeIt(String url, String findString){
		int index=url.indexOf(findString);
		if(index>=0){
			return url.substring(index+findString.length());
		}else{
			return url;
		}
	}
	
	/** ������� �� ������ URL ������ */
	protected String removeStartPage(String url){
		String source=this.getShopUrlStartPage().replaceAll("^(http://)", "");
		source=source.replaceAll("^(www.)", "");
		int index=url.indexOf(source);
		if(index>=0){
			try{
				return url.substring(index+source.length());
			}catch(Exception ex){
				return url;
			}
		}else{
			return url;
		}
	}


	/** ���� �� � ���������� ������ ����������� ��������
	 * @param values - ��������� ��������
	 * @param controlValue - ����������� �������� 
	 * @return
	 */
	private boolean valueInArray(String[] values, String controlValue){
		for(int counter=0;counter<values.length;counter++){
			if(values[counter].trim().equalsIgnoreCase(controlValue)){
				return true;
			}
		}
		return false;
	}
	
	/** �������� ������ ������� ��������� � ������ �� Node ������ �� �������� ������� 
	 * @param list - ������ Node
	 * @param values - ������ ��������� �������� 
	 * @return
	 */
	private int getIndex(ArrayList<Node> list, String[] values){
		for(int counter=0;counter<list.size();counter++){
			String controlValue=list.get(counter).getTextContent().trim();
			if(valueInArray(values, controlValue.trim())){
				return counter;
			}
		}
		return -1;
	}
	 
	/** ������� �� ������ Node ��������� �������� �� �������  
	 * @param list - ������, �� �������� ����� ������� ��������, ��������������� ����������� ���������
	 * @param excludeValues - ��������, ������� ������� ������� 
	 */
	protected void removeNodeFromListByTextContent(ArrayList<Node> list, String[] excludeValues){
		int removeIndex=(-1);
		while((removeIndex=getIndex(list, excludeValues))>=0){
			list.remove(removeIndex);
		}
	}

	
	/** ������� �� ���������� ������ Element ��������, ������� ����� �������� ���������� ��������� ����� ������  */
	protected void removeNodeWithRepeatAttributes(ArrayList<Node> list,String attributeName){
		int index=0;
		while(index<list.size()){
			String attributeValue=getAttributeFromNode(list.get(index), attributeName);
			if(attributeValue==null){
				index++;
				continue;
			}
			int repeatCount=getAttributeCount(list, attributeName, attributeValue);
			if(repeatCount>1){
				list.remove(index);
			}else{
				index++;
			}
		}
	}
	
	/** �������� �� ���������� ������ ���-�� ������������� ���������� �� ���������� ������
	 * @param list - ������ ��������� 
	 * @param attributeName - ��� ���������
	 * @param attributeValue - �������� ���������
	 * @return
	 */
	private int getAttributeCount(ArrayList<Node> list, String attributeName,
			String attributeValue) {
		int returnValue=0;
		for(int counter=0;counter<list.size();counter++){
			if(attributeValue.equalsIgnoreCase(getAttributeFromNode(list.get(counter), attributeName))){
				returnValue++;
			}
		}
		return returnValue;
	}

	/** �������� �� ���������� Node  */
	private String getAttributeFromNode(Node node, String attributeName) {
		try{
			return ((Element)node).getAttribute(attributeName).trim();
		}catch(Exception ex){
			return null;
		}
	}
	
}
