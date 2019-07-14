package shop_list.html.parser.engine;

import shop_list.html.parser.engine.logger.ILogger;
import shop_list.html.parser.engine.saver.ISaver;

/** интерфейс управления для получения данных от удаленного клиента  */
public interface IManager {
	/** старт парсинга 
	 * @param callback - метод, который следует вызвать после окончания парсинга
	 * @return
	 * <li> <b>null</b> - ошибка старта </li>
	 * <li> <b>PARSE_SESSION.ID</b> - Сессионный номер </li>
	 * */
	public Integer start(IDetectEndOfParsing callback);

	/** пауза парсинга 
	 * @return
	 * <li> <b>null</b> - пауза успешно произведена  </li>
	 * <li> <b>text</b> - описание ошибки, из-за которой пауза не установлена </li>
	 * */
	public String pause();

	/** остановка парсинга 
	 * @return
	 * <li> <b>null</b> - остановка потока </li>
	 * <li> <b>text</b> - описание ошибки остановки </li>
	 * */
	public String stop();
	
	/** получить значение состояния парсинга в данный момент ( по отдельно взятому парсеру ) 
	 * @return {@link EParseState} 
	 * */
	public EParseState getParseState();
	
	/** установить состояние парсинга ( по отдельно взятому парсеру ) 
	 * @param {@link EParseState} 
	 * */
	public void setParseState(EParseState state);
	
	/** установить сохраняющего записи */
	public void setSaver(ISaver saver);
	
	/** установить логгер  */
	public void setLogger(ILogger logger);
	
	/** установить полный путь к Mozilla парсеру  */
	public void setMozillaParserPath(String path);
	
	/** получить страницу ресурса ( по которой будет происходить дальнейшее "опознание" данного ресурса  */
	public String getShopUrlStartPage();
	
	/** получить уникальный идентификатор магазина, по которому происходит парсинг */
	public Integer getShopId();
	
	/** получить уникальный идентификатор сессии парсинга  */
	public Integer getSessionId();
}
