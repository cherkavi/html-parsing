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


/** базовая страница для определения функционала по парсингу сайтов с мульти-страничной структурой
 * <br> 
 * 
 * <br>
 * <table border=1>
 * 	<tr>
 * 		<td colspan="3" align="center">следует переопределить/создать методы:</td>
 * 	</tr>
 * 	<tr>
 * 		<td>1</td><td>{@link #getSectionEndConditions()} </td> <td>Список условий окончания секции  </td>
 * 	</tr>
 * 	<tr>
 * 		<td>2</td><td>{@link #getCharset()} </td> <td> кодировка страницы {@link EParseState}  ( по умолчанию UTF-8)</td>
 * 	</tr>
 * 	<tr>
 * 		<td>3</td><td>{@link #getShopUrlStartPage()} </td> <td> стартовая страница данного ресурса</td>
 * 	</tr>
 * 	<tr>
 * 		<td>4</td><td>{@link #getTimeoutForReadNextPage()} </td> <td> временная задержка перед чтением следующей страницы (human emulation) </td>
 * 	</tr>
 * 	<tr>
 * 		<td>5</td><td>{@link #getTimeoutForReadNextSection()} </td> <td> временная задержка перед чтением следующей секции (human emulation) </td>
 *	</tr>
 * 	<tr>
 * 		<td>6</td><td>{@link #getSection()} </td> <td> получить список всех секций, которые могут быть в данном парсинге </td>
 *  </tr> 
 * 	<tr>
 * 		<td>7</td><td>class CurrentSession extends {@link NextSection}</td> <td> создать <b>внутренний</b> класс, который будет идентифицировать движение внутри секции</td>
 *  </tr> 
 * 	<tr>
 * 		<td>8</td><td>{@link #getXmlPathToDataBlock()} </td> <td> XPath к блоку данных, который содержит все записи (все элементы на странице) </td>
 *  </tr> 
 *  <tr>
 *  	<td>9</td><td>Получить записи из блока данных:</td>
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
 * 							<li>получить из списка записей ссылку на следующий тэг ( элемент ),</li>
 * 							<li>потом из этого элемента получить запись для сохранения</li>
 * 						</ul>
 * 					</td>
 *  			<tr>
 *  			<tr>
 *  				<td>2</td>
 *  				<td>{@link #getRecordsFromBlock(Node node)} </td>
 *  				<td> получить список записей из секции на которую указывает  {@link #getXmlPathToDataBlock()}</td>
 *  			<tr>
 *  		</table>
 *  	</td>
 *  </tr>
 * </table>
 * */
public abstract class BaseMultiPage implements IManager,Runnable{
	/** логгер */
	protected ILogger logger=null;
	/** сохраняющий данные  */
	protected ISaver saver=null;
	/** флаг, который идентифицирует необходимость в продолжении работы потока */
	private volatile boolean flagRun=false;
	/** парсер, отвечающий за парсинг страниц */
	protected IParser parser=null;
	/** поток, который запускается методом {@link #start()} */
	private Thread thread;
	/** текущее состояние парсинга  */
	protected volatile EParseState parseState=EParseState.READY;
	/** уникальный идентификатор магазина в масштабе базы данных */
	private Integer shopId=null;
	
	/** уникальный идентификатор сессии, по которой происходит парсинг в данный момент */
	private Integer sessionId=null;
	
	protected void debugPrintNode(String pathToFile, Node node){
		PrintElement printer=new PrintElement(pathToFile); 
		printer.outputAsTree(node);		
	}
	
	/** получить объект-парсер
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
		// TODO - предусмотреть временную остановку 
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
	
	/** дополнительное описание */
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
				logger.info(this, "запуск основного тела парсера ");
				ArrayList<INextSection> sections=this.getSection();
				walkSection:for(int counter=0;counter<sections.size();counter++){
					/** данные из первой страницы секции  */
					ArrayList<Record> firstRecordSet=new ArrayList<Record>();
					boolean isFirstRecordSet=true;
					/** данные из последней прочитанной страницы секции */
					ArrayList<Record> lastRecordSet=new ArrayList<Record>();
					// очередная секция 
					INextSection currentSection=sections.get(counter);
					Integer idSection=this.saver.getSectionId(currentSection.getName());
					logger.info(this, "получение очередной секции: "+currentSection.getName()+"   IdSection:"+idSection);
					/** данные, полученные из текущий страницы  */
					ArrayList<Record> nextRecordSet=null;
					walkPage: while( (nextRecordSet=this.getNextRecordSet(currentSection, firstRecordSet, lastRecordSet))!=null){
						if(isFirstRecordSet==true){
							// скопировать полученные данных из только что прочитанных в первые прочитанные
							replaceDataFromSourceToDestination(nextRecordSet, firstRecordSet);
							isFirstRecordSet=false;
						}
						// скопировать полученные данных из только что прочитанных в последние прочитанные
						replaceDataFromSourceToDestination(nextRecordSet, lastRecordSet);
						
						if(nextRecordSet.size()==0){
							logger.debug(this, "нет данных для парсинга");
							continue;
						}else{
							logger.debug(this, "есть записи на странице: "+nextRecordSet.size());
							for(int index=0;index<nextRecordSet.size();index++){
								// FIXME место сохранения данных 
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
	
	/** "мягко" добавить к ссылке(или части ссылки) полный заголовок (http://), если это необходимо (если уже не добавлен ) 
	 * @param startPage - стартовая страница 
	 * @param href - ссылка 
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
	
	
	/** является ли цена "пустой"  */
	private boolean priceIsEmpty(Record record){
		if(record==null) return true;
		if((record.getPrice()==null)&&(record.getPriceUsd()==null)&&(record.getPriceEuro()==null))return true;
		boolean returnValue=false;
		// поиск любой цены, если таковая есть 
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
	
	/** получить временную задержку в милисекундах перед чтением очередного раздела */
	protected long getTimeoutForReadNextSection(){
		return 200;		
	}

	/** получить временную задержку в милисекундах перед чтением очередной страницы раздела  */
	protected long getTimeoutForReadNextPage(){
		return 2000;		
	}
	
	
	/** получить Float на основании строки  */
	protected Float getFloatFromString(String value){
		if(value==null)return null;
		String tempValue=value.trim();
		try{
			return Float.parseFloat(tempValue);
		}catch(Exception ex){
			// возможно, нужно заменить точку на запятую
			try{
				return Float.parseFloat(tempValue.replaceAll(",", "."));
			}catch(Exception exInner){
				return null;
			}
			
		}
	}
	
	/** заменить все элементы из destination элементами из source 
	 * @param source - источник данных
	 * @param destination - приемник данных ( все элементы будут заменены из приемника ) nullable==false
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
	
	/** получить критичное кол-во повторений для возможного "зацикливания" - при превышении завершает данную секцию (default=50)*/
	protected int getWatchDogEmptyPageLimit(){
		return 20;
	}
	
	/** при превышении лимита отображать ERROR сообщение  */
	protected boolean getWatchDogEmptyPageLimitShowError(){
		return true;
	}
	
	private int errorCounter=0;
	
	/** получить на основании {@link #getXmlPathToDataBlock()} Node из страницы   
	 * @param  nextUrl - страница для получения данных 
	 * @param  charset - кодировка страницы  
	 * */
	protected Node getDataBlockFromUrl(String nextUrl, String charset) throws Exception{
		return this.parser.getNodeFromUrl(nextUrl, this.getCharset(), this.getXmlPathToDataBlock());
	}
	
	/** текущая страница */
	private String currentUrl;
	/** получить ссылку на текущую страницу, в которой производится поиск */
	
	/** получить текущую страницу */
	protected String getCurrentUrl(){
		return this.currentUrl;
	}
	
	/** установить текущую страницу  */
	private void setCurrentUrl(String url){
		this.currentUrl=url;
	}
	
	/** на основании секции получить очередную порцию данных для сохранения, либо же вернуть null 
	 * ( как признак окончания секции ) либо же вернуть ArrayList без элементов  
	 */
	private ArrayList<Record> getNextRecordSet(INextSection section, ArrayList<Record> firstPageBlock, ArrayList<Record> lastPageBlock){
		String nextUrl=null;
		try{
			ArrayList<Record> returnValue=new ArrayList<Record>();
			logger.debug(this, "Попытка получения следующей страницы:");
			nextUrl=section.getUrlToNextPage();
			this.setCurrentUrl(nextUrl);
			logger.debug(this, "Следующая страница получена: "+nextUrl);
			logger.debug(this, "Получение из страницы XPath:"+this.getXmlPathToDataBlock());
			// if(nextUrl.equals("http://pikmarket.kiev.ua/?path=0/2/774/1695/1782/1713/1753")){System.out.println("control");}
			Node node=this.getDataBlockFromUrl(nextUrl, this.getCharset());//  
			if(node!=null){
				logger.debug(this, "#getNextRecordSet получен блок c данными");
				try{
					returnValue=this.getRecordsFromBlock(node);
				}catch(EParseExceptionEmptyRecord ep){
					// нет записей в блоке данных  
					if(this.isConditionPresent(ESectionEnd.NEXT_RECORDS_ZERO_SIZE)){
						logger.debug(this, "#getNextRecordSet чтение секции завершено "+ESectionEnd.NEXT_RECORDS_ZERO_SIZE.toString());
						return null;
					}else{
						logger.error(this, "#getNextRecordSet список элементов из блока данных пуст : "+nextUrl);
					}
				}catch( EParseExceptionRecordListLoadError ep){
					logger.debug(this, "ошибка загрузки страницы ");
					returnValue=null;
				}
				// анализ полученного результата
				if(returnValue==null){
					if(this.isConditionPresent(ESectionEnd.NEXT_RECORDS_LOAD_ERROR)){
						logger.debug(this, "#getNextRecordSet чтение секции завершено "+ESectionEnd.NEXT_RECORDS_LOAD_ERROR.toString());
						return null;
					}else{
						logger.error(this, "#getNextRecordSet не удалось получить List с данными из страницы: "+nextUrl);
					}
				}else{
					if(returnValue.size()==0){
						// в данном месте не нужна проверка на NEXT_RECORDS_ZERO_SIZE - в эту ветку не попадает, т.к. в случае пустого блока происходит выброс исключения EParseExceptionEmptyRecord 
						errorCounter++;
						if(errorCounter>getWatchDogEmptyPageLimit()){
							if(getWatchDogEmptyPageLimitShowError()){
								logger.error(this, "BaseMultiPage WatchDog empty page !!! watchdog stop parse section check engine !!! ");
							}else{
								logger.info(this, "BaseMultiPage WatchDog watchdog stop parse ");
							}
							return null;
						}else{
							logger.debug(this, "#getNextRecordSet возможно все данные не прошли по цене(=0) либо же нет на складе ");
							return returnValue;
						}
					}else{
						errorCounter=0;
						logger.debug(this, "прочитан блок с данными: "+returnValue.size());
						if(this.isConditionPresent(ESectionEnd.NEXT_RECORDS_SHOW_FIRST)){
							// проверка на отображение первой страницы
							if(isListEquals(returnValue, firstPageBlock)){
								logger.debug(this, "была отображена первая страница ");
								return null;
							}
						}
						if(this.isConditionPresent(ESectionEnd.NEXT_RECORDS_REPEAT_LAST)){
							// проверка на отображение предыдущей страницы  
							if(isListEquals(returnValue, lastPageBlock)){
								logger.debug(this, "была отображена последняя страница ");
								return null;
							}
						}
						logger.debug(this, "блок данных послан" );
						return returnValue;
					}
				}
				// если на странице прочитано 0 записей и есть условие прекращения парсинга секции после повторения отображения первой страницы - прекратить парсинг секции, иначе - infinity loop 
// >>>   глючит, возможно, нужно перенести в другое место 
				// if((this.isConditionPresent(ESectionEnd.NEXT_RECORDS_SHOW_FIRST)&&(firstPageBlock!=null)&&(firstPageBlock.size()==0))){return null;}
				return returnValue;
			}else{
				logger.info(this, "не получен Node, который бы содержал все элементы - завершить работу, т.к. может быть отключен сервер, либо же сбой"); 
				/*if(this.isConditionPresent(ESectionEnd.NEXT_PAGE_LOAD_ERROR)){
					logger.debug(this, "#getNextRecordSet чтение секции завершено "+ESectionEnd.NEXT_PAGE_LOAD_ERROR.toString());
				}else{
					logger.error(this, "#getNextRecordSet не удалось получить блок данных из страницы: "+nextUrl);
				}*/
				return null;
			}
		}catch(Exception ex){
			System.err.println("if section created of Analisator - check it:"+ex.getMessage());
			logger.info(this,"#getNextRecordSet checkUrl("+nextUrl+") Exception:"+ex.getMessage());
			return null;
		}
	}

	/** просмотреть текущие условия завершения чтения секции на срабатывание указанного  */
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
	
	/** получить условия прекращения перехода на другую страницу в секции */
	protected abstract ESectionEnd[] getSectionEndConditions();
	
	
	/** проверка на эквивалентность двух списков с объектами */
	private boolean isListEquals(ArrayList<Record> first, ArrayList<Record> second){
		if((first==null)&&(second==null)){
			// оба объекта - null 
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
					// разные размеры у объектов 
					return false;
				}
			}else{
				// один из объектов равен null
				return false;
			}
		}
	}
	
	/** Post-обработчик для record, может быть использован, если требуется финальная обработка одной записи перед отправкой на уровни хранения  */
	protected Record prepareRecordBeforeSave(Record record) throws EParseException{
		return record;
	}
	
	/** прочесть все записи из блока данных, который их содержит на странице 
	 * <br>
	 * и указывает на них {@link #getXmlPathToDataBlock()}
	 * @return 
	 * <ul>
	 * 	<li> <b> null </b> если произошла ошибка чтения блока данных </li>
	 * 	<li> <b> ArrayList.size()==0 </b> нет данных в блоке </li>
	 * 	<li> <b> ArrayList.size()>0 </b> записи из блока данных </li>
	 * </ul>
	 * @throws EParseExceptionRecordListEmpty - если ни одной записи не найдено в блоке данных ( но не эквивалентно пустому возвращаемому списку )
	 * <br />
	 * для каждой полученной записи обязательно применение {@link #recordPostProcessor()}
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
						// очередная запись не на складе 
						anyRecordPresent=true;
						returnValue.add(new Record());
						logger.debug(this, "запись не на складе");
					}catch(EParseExceptionItIsNotRecord ep){
						// очередная запись не может быть представлена как Record
					}catch(Exception ex){
						this.logger.error(this, "#getRecordsFromBlock:"+ex.getMessage());
						// return null;
					}
				}
			}
			if(anyRecordPresent==false)throw new EParseExceptionRecordListEmpty();
			return returnValue;
		}else{
			// записи не найдены по указанному пути - ошибка нахождения записей
			throw new EParseExceptionRecordListLoadError();
		}
	}
	
	/** получить элемент {@link Record} из одного элемента, на который указал из блока данных ({@link #getXmlPathToDataBlock()}) тэг  {@link #getRecordTagNameInBlock()}
	 * <br>
	 * в случае если элементы расположены не списком, а имеют в блоке соответствие двум,трем и более записям, 
	 * 	следует переопределить метод {@link #readRecordsFromBlock} и не следует переопределять ({@link #getRecordFromElement(Element)}) и {@link #getRecordTagNameInBlock()}
	 * @throws 
	 *  EParseExceptionItIsNotRecord - если переданный элемент не может быть интерпретирован как запись
	 *  @return null - если не удалось получить запись из-за отсуствия цены, либо же из за отсутствия названия
	 *  ( если не удалось получить элемент, который идентифицировал бы цену либо наименование - следует выбрасывать {@link EParseExceptionItIsNotRecord}) 
	 * */
	protected Record getRecordFromElement(Element element) throws EParseException{
		throw new RuntimeException("replace method BaseMultiPage#getRecordFromElement or (BaseMultiPage#readRecordsFromBlock)");
	}
	
	/** получить имя HTML тэга, который идентифицирует в блоке HTML (обозначенный {@link #getXmlPathToDataBlock()}) один элемент для чтения записи (Record) 
	 * <br>
	 * в случае если элементы расположены не списком, а имеют в блоке соответствие двум,трем и более записям, 
	 * 	следует переопределить метод {@link #readRecordsFromBlock} и не следует переопределять ({@link #getRecordFromElement(Element)}) и {@link #getRecordTagNameInBlock()}	 
	 * */
	protected String getRecordTagNameInBlock(){
		throw new RuntimeException("replace method BaseMultiPage#getRecordTagNameInBlock or (BaseMultiPage#readRecordsFromBlock)");
	}

	/** полный путь к корню HTML ресурса */
	@Override
	public abstract String getShopUrlStartPage();
	
	/** получить кодировку страницы в формате {@link java.nio.charset.Charset} или же можно брать из {@link ECharset}
	 * Допустимые кодировки (<b> регистр имеет значение - с маленькой буквы </b>)  
	 * <ul>
	 * 	<li>utf-8</li>
	 * 	<li>windows-1251</li>
	 * </ul>
	 * <br>
	 * 
	 * значение по-умолчанию: "utf-8"
	 * */
	protected String getCharset(){
		return ECharset.UTF_8.getName();		
	}
	
	/** получить список всех секция по данному ресурсу  
	 * <br>
	 * {@link DirectFinder}
	 * <br>
	 * {@link TwoLevelFinder}
	 * <br>
	 * {@link RecursiveFinder}
	 * */
	protected abstract ArrayList<INextSection> getSection();
	
	/** полный путь к блоку данных ( к элементу на странице ), который содержит записи для сохранения 
	 * <br>
	 * ОБЯЗАТЕЛЬНО должен возвращать только ОДИН элемент, если таковой имеется
	 * */
	protected abstract String getXmlPathToDataBlock();
	
	
	/** у переданного Node проверить наличие аттрибута и его эквивалентность на переданное значение  
	 * @param item - Element
	 * @param attributeName - имя аттрибута 
	 * @param attributeValue - значение аттрибута
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
	 * существует ли указанный аттрибуте 
	 * @param presentNode - элемент 
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

	

	/** из указанного Node получить аттрибут по его имени
	 * @param node - искомый Node
	 * @param attrName - имя аттрибута
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
	 * проверить на соответствие оригинального значения любому из перечисленных 
	 * @param original - оригинальное значение
	 * @param controlValues - значения, которые нужно проверить на соответствие 
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
	

	/** является ли строка пустой */
	protected boolean isStringEmpty(String value){
		return (value==null)||(value.trim().equals(""));
	}

	/** удалить из строки после указанного символа все элементы, включая его же */
	protected String removeAfterSymbolIncludeIt(String url, char c) {
		int index=url.indexOf(c);
		if(index>0){
			return url.substring(0,index); 
		}else{
			return url;
		}
	}
	/** удалить из строки после указанного (последнего) символа все элементы, включая его же */
	protected String removeAfterSymbolLastIncludeIt(String url, char c) {
		int index=url.lastIndexOf(c);
		if(index>0){
			return url.substring(0,index); 
		}else{
			return url;
		}
	}

	/** удалить из строки символ (например код пробела, иногда, 160 ) */
	protected String removeCharFromString(String value, int charKod){
		StringBuffer returnValue=new StringBuffer();
		for(int counter=0;counter<value.length();counter++){
			if(value.codePointAt(counter)!=charKod){
				returnValue.append(value.charAt(counter));
			}
		}
		return returnValue.toString();
	}
	
	
	/** удалить до указанной строки, включая ее */
	protected String removeBeforeStringIncludeIt(String url, String findString){
		int index=url.indexOf(findString);
		if(index>=0){
			return url.substring(index+findString.length());
		}else{
			return url;
		}
	}
	
	/** удалить из адреса URL старто */
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


	/** есть ли в переданном масиве контрольное значение
	 * @param values - множество значений
	 * @param controlValue - контрольное значение 
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
	
	/** получить индекс первого вхождения в списке из Node одного из значений массива 
	 * @param list - список Node
	 * @param values - список текстовых значений 
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
	 
	/** удалить из списка Node указанные названия из массива  
	 * @param list - список, из которого будут удалены названия, соответствующие контрольным значениям
	 * @param excludeValues - значения, которые следует удалить 
	 */
	protected void removeNodeFromListByTextContent(ArrayList<Node> list, String[] excludeValues){
		int removeIndex=(-1);
		while((removeIndex=getIndex(list, excludeValues))>=0){
			list.remove(removeIndex);
		}
	}

	
	/** удалить из указанного списка Element элементы, которые имеют значения указанного аттрибута более одного  */
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
	
	/** получить из указанного списка кол-во повторяющихся аттрибутов из указанного списка
	 * @param list - список элементов 
	 * @param attributeName - имя аттрибута
	 * @param attributeValue - значение аттрибута
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

	/** получить из указанного Node  */
	private String getAttributeFromNode(Node node, String attributeName) {
		try{
			return ((Element)node).getAttribute(attributeName).trim();
		}catch(Exception ex){
			return null;
		}
	}
	
}
