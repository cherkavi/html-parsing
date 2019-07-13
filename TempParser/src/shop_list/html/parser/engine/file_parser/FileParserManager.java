package shop_list.html.parser.engine.file_parser;

import java.io.File;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;



import shop_list.html.parser.engine.EParseState;
import shop_list.html.parser.engine.ESessionResult;
import shop_list.html.parser.engine.EngineSettings;
import shop_list.html.parser.engine.IDetectEndOfParsing;
import shop_list.html.parser.engine.IManager;
import shop_list.html.parser.engine.file_parser.extractor.Extractor;
import shop_list.html.parser.engine.file_parser.html_analisator.DynamicLinkAnalisator;
import shop_list.html.parser.engine.logger.ILogger;
import shop_list.html.parser.engine.saver.ISaver;

public abstract class FileParserManager implements IManager, Runnable, IFileNameGenerator{
	/** объект, который отвечает за раскрытие архивных файлов */
	protected Extractor archivExtractor=new Extractor();
	
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
	
	private volatile EParseState parseState=EParseState.READY;
	@Override
	public EParseState getParseState() {
		return this.parseState;
	}

	@Override
	public void setParseState(EParseState state) {
		this.parseState=state;
	}

	@Override
	public Integer getShopId() {
		return this.saver.getShopId(this.getShopUrlStartPage());
	}
	
	protected ILogger logger;
	@Override
	public void setLogger(ILogger logger) {
		this.logger=logger;
	}
	
	protected ISaver saver;
	@Override
	public void setSaver(ISaver saver) {
		this.saver=saver;
	}
	
	@Override
	public abstract String getShopUrlStartPage();

	@Override
	public String pause() {
		return "NOT ALLOW";
	}

	@Override
	public Integer getSessionId() {
		return this.sessionId;
	}

	/** уникальный номер сессии по которой происходит парсинг */
	private Integer sessionId=null;
	/** флаг состояния запущенного потока в данный момент */
	private volatile boolean flagRun=false;
	/** поток, в контексте которого запущена задача */
	private Thread thread=null;
	
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

	/** получить описание данного парсера для записи в файл */
	protected String getDescription(){
		return this.getShopUrlStartPage();
	}
	
	/** объект, который нужно оповестить что  */
	private IDetectEndOfParsing callback=null;
	
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
			// проверки пройдены
			// this.parseState.getKod()==0
			try{
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
	
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	private Integer shopId=null;

	public void run(){
		this.parseState=EParseState.PROCESS;
		if(sessionId==null){
			if(logger!=null){
				logger.warn(this, "ID session was not received");
			}
			this.parseState=EParseState.DONE_ERROR;
		}else if (flagRun){
			logger.info(this, "чтение данных из удаленного URL");
			try{
				if(work(sessionId)==false){
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
	
	/** получить полный путь к файлам(если это HTTP - обязательно указать http://), 
	 * в которых находится прайс  
	 * <br>
	 * если ссылка создается динамически, необходимо прибегнуть к {@link DynamicLinkAnalisator}
	 * <br>
	 * <i> для локальной отладки следует применять следующую форму записи пути к файлу, например: </i>
	 * "file:///C:/temp_parser/gb19_10.zip"
	 * */
	protected abstract String[] getShopPriceFileUrl();
	
	/** получить тип файла, который будет закачан из сети */
	protected abstract EFileTypes getFileType();
	
	/**
	 * @param pathToUrlPrice - все URL, которые могут быть задействованы в прайс-листе ( в случае надобности - открывает архив )
	 * @return - полные имена полученных файлов 
	 * @throws - исключение, в случае невозможности закачки данных
	 */
	protected String[] getFullPathToFile(String[] pathToUrlPrice) throws Exception{
		ArrayList<String> returnValue=new ArrayList<String>();
		for(int counter=0;counter<pathToUrlPrice.length;counter++){
			String loadedFile=this.loadFromUrlAndGetName(pathToUrlPrice[counter], EngineSettings.pathToTemp);
			// FIXME добавить возможность разархивирование RAR архива
			if( (this.getFileType().equals(EFileTypes.zip_csv))||
				(this.getFileType().equals(EFileTypes.zip_xls))||
				(this.getFileType().equals(EFileTypes.zip_txt))
				)
			{
				String[] unzipFiles=this.extractArchiv(EngineSettings.pathToTemp, loadedFile);
				if(unzipFiles!=null){
					for(int zipCounter=0;zipCounter<unzipFiles.length;zipCounter++)returnValue.add(unzipFiles[zipCounter]);
				}
				if((unzipFiles!=null)&&(unzipFiles.length>0)&&(!unzipFiles[0].equals(loadedFile))){
					try{
						if((new File(loadedFile)).delete()==true){
							(new File(loadedFile)).deleteOnExit();
						}
					}catch(Exception ex){};
				}else{
					// возможно обработка не была произведена
				}
			}else{
				returnValue.add(loadedFile);
			}
		}
		return returnValue.toArray(new String[]{});
	}
	
	
	private static final SimpleDateFormat timeStamp=new SimpleDateFormat("MM_dd_HH_mm_ss");
	private Random random=new Random();
	/** получить штамп случайных чисел заданной длинны  */
	private String getRandomStamp(int length){
		StringBuffer returnValue=new StringBuffer();
		for(int counter=0;counter<length;counter++){
			returnValue.append(Integer.toHexString(random.nextInt(16)));
		}
		return returnValue.toString();
	}
	
	/** скачать файл из указанного места в сети и сохранить файл на диске в указанном каталоге ( со случайным имененем, имя вернуть ) 
	 * @param pathToUrl - полный путь к Path
	 * @param localTempPath - полный путь ко временному каталогу
	 * */
	private String loadFromUrlAndGetName(String pathToUrl, String localTempPath) throws Exception{
		if(pathToUrl==null)return null;
		String destinationName=localTempPath+this.generateUniqueFileName();
		URL url=new URL(pathToUrl);
		URLConnection connection=url.openConnection();
		InputStream input=null;
		OutputStream output=null;
		int bufferLength=4096;
		byte[] buffer=new byte[bufferLength];
		int readedByteCount=0;
		try{
			input=connection.getInputStream();
			output=new FileOutputStream(new File(destinationName));
			while( (readedByteCount=input.read(buffer))>=0){
				output.write(buffer,0,readedByteCount);
				output.flush();
			}
		}finally{
			try{
				input.close();
			}catch(Exception ex){};
			try{
				output.close();
			}catch(Exception ex){};
		}
		//System.out.println("price destination: "+destinationName);
		return destinationName;
	}
	
	
	/** распарсить файл
	 * @param sessionId 
	 * @param fullPathToFile - полный путь к файлам в локальном хранилище 
	 * @param priceFileType - тип прайса
	 * @return
	 * @throws Exception
	 */
	protected abstract boolean parseFiles(Integer sessionId, 
							  String[] fullPathToFile, 
							  EFileTypes priceFileType) throws Exception;
	
	/** процесс парсинга для файла  */
	protected boolean work(Integer sessionId) throws Exception{
		// загрузить файл из удаленного источника в хранилище
		String[] fullPathToFile=this.getFullPathToFile(this.getShopPriceFileUrl());
		// распарсить файл 
		return parseFiles(sessionId, fullPathToFile, this.getFileType());
	}

	/** разархивировать указанный файл  
	 * @param pathToTemp - полный путь к временному каталогу 
	 * @param fileZip - полное имя файла 
	 * @return список файлов, который получены после разархивирования
	 */
	public String[] extractArchiv(String pathToTemp, String fileZip){
		switch(this.getFileType()){
			case csv:{
				return new String[]{fileZip};
			}
			case txt:{
				return new String[]{fileZip};
			}
			case xls:{
				return new String[]{fileZip};
			}
			case zip_csv:{
				return this.archivExtractor.extractZip(pathToTemp, fileZip, this);
			}
			case zip_txt:{
				return this.archivExtractor.extractZip(pathToTemp, fileZip, this);
			}
			case zip_xls:{
				return this.archivExtractor.extractZip(pathToTemp, fileZip, this);
			}
			case rar_csv:{
				return new String[]{fileZip};
			}
			case rar_txt:{
				return new String[]{fileZip};
			}
			case rar_xls:{
				return new String[]{fileZip};
			}
			default: return new String[]{fileZip};
		}
	}

	public String generateUniqueFileName(){
		return timeStamp.format(new Date())+this.getRandomStamp(5)+".tmp";
	}
	
	
	public static void main(String[] args){
		/*
		System.out.println(" -begin- ");
		String[] files=unzipFile("d:\\temp\\","d:\\words.zip");
		for(int counter=0;counter<files.length;counter++){
			System.out.println(">>> "+files[counter]);
		}
		System.out.println(" --end-- ");
		*/
	}

	
	
}
