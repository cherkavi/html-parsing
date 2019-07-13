package image_parser;

import java.io.InputStream;

/** объект, который владеет информацией о получении ссылки на следующее изображение */
public abstract class ImagePathAware {
	/** возвращает ссылку на следующее изображение, которое нужно получить 
	 * @return 
	 * <li> <b>null</b> -  больше нет данных </li>
	 * <li> <b>String</b> -  ссылка на очередную картинку  </li>
	 * */
	public abstract String getNextUrl();
	/** сохранить последний полученный URL */
	public abstract boolean saveLastGetUrl(InputStream inputStream);

	/** сохранить последний полученный URL как ошибку записи */
	public abstract boolean saveLastGetUrlAsError();
	
	/** начало процесса сохранения */
	public abstract  boolean begin();
	
	/** конец процесса сохранения */
	public abstract boolean end();
}
