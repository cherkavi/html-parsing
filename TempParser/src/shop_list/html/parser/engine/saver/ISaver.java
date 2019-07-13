package shop_list.html.parser.engine.saver;

import shop_list.html.parser.engine.ESessionResult;
import shop_list.html.parser.engine.record.Record;

/** интерфейс для сохранения полученных данных из удаленног ресурса */
public interface ISaver {
	/** получить уникальный код магазина  на основании адреса URL
	 * @param shopUrl - URL магазина
	 * @return код из базы данных по данному магазину 
	 * */
	public int getShopId(String shopUrl);
	
	/** старт нового витка парсинга
	 * @param idShop - уникальный код магазина, по которому просходит парсинг   
	 * @param description - описание данного парсинга
	 * @return 
	 * <li><b>value</b> - уникальный номер данной сессии </li>
	 * <li><b>null</b> - ошибка создания </li>
	 * */
	public Integer startNewSession(int shopId, String description);
	
	/** получить уникальный код секции на основании имени  
	 * @param sectionName - имя секции 
	 * */
	public Integer getSectionId(String sectionName);
	
	/** 
	 * сохранить запись по указанному номеру 
	 * @param sessionId - номер сессии 
	 * @param section - секция, которая содержит данную запись 
	 * @param record - запись, которая должна быть сохранена 
	 * @return 
	 * <li><b>true</b> - запись успешно создана </li>
	 * <li><b>false</b> - ошибка создания </li>
	 */
	public boolean saveRecord(Integer sessionId,Integer sectionId, Record record);
	
	/** записать флаг окончания чтения данных  
	 * @param sessionId - уникальный код сессионного номера
	 * @param result результат чтения данных 
	 * */
	public boolean endSession(Integer sessionId, ESessionResult result);
}
