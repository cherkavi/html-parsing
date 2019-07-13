package shop_list.html.parser.engine.parser;

import java.util.ArrayList;

import org.w3c.dom.Node;

/** интерфейс парсинга страниц HTML  */
public interface IParser {
	/** получить org.w3c.Node из Url
	 * @param urlPath - адрес
	 * @param charsetName - кодировка
	 * @param xpath - полный XPath
	 * @return - null - если произошла какого-либо рода ошибка
	 * @throws - если произошла ошибка парсинга
	 */
	public Node getNodeFromUrl(String urlPath, String charsetName, String xpath) throws Exception;
	
	/** получить из Node еще подэлемент, согласно указанному Xpath - исправление глюков стандартного API */
	public Node getNodeFromNode(Node node, String xpath);
	
	/** получить org.w3c.Node из Url
	 * @param urlPath - адрес
	 * @param charsetName - кодировка
	 * @param xpath - полный XPath
	 * @return - null - если произошла какого-либо рода ошибка
	 * @throws - если произошла ошибка парсинга
	 */
	public ArrayList<Node> getNodeListFromUrl(String urlPath, String charsetName, String xpath) throws Exception;
	
	/** получить из Node еще подэлемент, согласно указанному Xpath - исправление глюков стандартного API */
	public ArrayList<Node> getNodeListFromNode(Node node, String xpath);

	
	/** получить текстовое представление данных для под-узла указанного узла
	 * @param node - узел XML
	 * @param xpath - путь к под-узлу 
	 * @param defaultValue - значение по-умолчанию, в случае не нахождения под-узла 
	 * @return
	 */
	public String getTextContentFromNode(Node node, String xpath, String defaultValue);
	
	/** получить кол-во дочерних элементов, которые имеют указанное имя тэга  
	 * @param element - элемент, который проверяется на вхождение дочерних элементов 
	 * @param childTagName - имя дочернего элемента, на которое проверяется данное значение 
	 * */
	public int getChildElementCount(Node node, String childTagName);
	
}
