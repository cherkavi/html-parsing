package shop_list.html.parser.engine;

///** Окончание парсинга */
public interface IDetectEndOfParsing {
	/** вызов функции окончания парсинга 
	 * @param manager - объект, который производил парсинг
	 * @param parseEndEvent - результат окончания парсинга
	 * */
	public void endParsing(IManager manager, EParseState parseEndEvent);
}
