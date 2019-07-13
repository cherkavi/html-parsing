package shop_list.html.parser.engine.multi_page.standart;

import org.w3c.dom.Element;

import org.w3c.dom.Node;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionItIsNotRecord;
import shop_list.html.parser.engine.exception.EParseExceptionNotInStore;
import shop_list.html.parser.engine.record.Record;

public abstract class RecordListBaseMultiPage extends ListBaseMultiPage{
	
	/** получить относительный (относительно Record) XPath путь к Наименованию, будет использован {@link Node#getTextContent()} */
	protected abstract String recordFromNodeInRecordToName();

	/** получить относительный (относительно Record) XPath путь к URL, будет получен с помощью метода получения аттрибута {@link Element#getAttribute(String)} из метода:
	 * <br/>
	 * путь будет извлечен из аттрибута {@link #getAttributeForExtractRecordUrl}
	 * */
	protected abstract String recordFromNodeInRecordToUrl();

	/** получить относительный (относительно Record) XPath путь к Цене в гривне для получения методом {}
	 * @return null, если нет в наличии 
	 * */
	protected abstract String recordFromNodeInRecordToPrice();
	
	/**  получить текст который будет "приведен" к Float для Price 
	 * <br>
	 * если обработчик не требуется - просто вернуть само значение 
	 * */
	protected abstract String recordFromNodeInRecordToPriceBeforeConvert(String priceText);

	/** получить относительный (относительно Record) XPath путь к Цене в USD для получения методом {}
	 * @return null, если нет в наличии 
	 */
	protected abstract String recordFromNodeInRecordToPriceUsd();

	/**  получить текст который будет "приведен" к Float для PriceUsd 
	 * <br>
	 * если обработчик не требуется - просто вернуть само значение 
	 * */
	protected abstract String recordFromNodeInRecordToPriceUsdBeforeConvert(String priceText);

	/** получить относительный (относительно Record) XPath путь к Цене в HRN для получения методом {}
	 * @return null, если нет в наличии 
	 * */
	protected abstract String recordFromNodeInRecordToPriceEuro();

	/**  получить текст который будет "приведен" к Float для PriceEuro
	 * <br>
	 * если обработчик не требуется - просто вернуть само значение 
	 *  */
	protected abstract String recordFromNodeInRecordToPriceEuroBeforeConvert(String priceText);
	
	/** получить имя аттрибута для извлечения из элемента, на который указывается URL (default = "href") */
	protected String getAttributeForExtractRecordUrl(){
		return "href";
	}
	/** нужно ли удалять из полного пути к Record полное имя стартовой страницы ? */
	protected abstract boolean recordIsRemoveStartPageFromUrl();
	
	/** нужно ли проверять на наличие товара ( то есть есть ли ссылка на текст, который идентифицирует наличие товара ),
	 * возвращает XPath на элемент, который должен быть получен в виде текста  
	 * @return null - если проверка не требуется
	 * если проверка требуется - переопределите 
	 * */
	protected abstract String recordFromNodeIsPresent();
	
	/** текст, который должен быть в наличии в строке  на которую указывает {{@link #recordFromNodeIsPresent()}} 
	 * @return null - достаточно просто наличие элемента, на который указывает {@link #recordFromNodeIsPresent()}
	 * @return text - нужно чтобы в строке, на которую указывает {@link #recordFromNodeIsPresent()} ОБЯЗАТЕЛЬНО содержался данный текст (CaseSensetive) 
	 * */
	protected abstract String recordFromNodeIsPresentText();
	
	/** Проверка записи на существование, если задан путь к проверяющему Node {@link #recordFromNodeIsPresent},
	 *  а так же задан текст, который должен быть в этом Node {@link #recordFromNodeIsPresentText()},
	 *  <br>
	 *  <ul>
	 *  	<li><b>true</b> - текст должен <b>присутствовать</b> (хотя бы частично, иначе запись считается "не на складе" </li>
	 *  	<li><b>false</b> - текст должен <b>отсутствовать</b> (хотя бы частично, иначе запись считается "не на складе" )</li>
	 *  </ul>
	 *  <br>
	 *  не учитывается, в случае если {@link #recordFromNodeIsPresentText()} возвращает null
	 */
	protected boolean isPresentTextExistsMustPresent(){
		return true;
	}
	
	
	protected Record getRecordFromNode(Node node) throws EParseException{
		try{
			// Node elementNameDebug=this.parser.getNodeFromNode(node, recordFromNodeInRecordToName());if(elementNameDebug!=null){System.out.println("Element finded: "+elementNameDebug.getTextContent());}else{System.out.println("Element does not finded ");}
			// element exists
			if(recordFromNodeIsPresent()!=null){
				// проверка на наличие записи 
				Node presentNode=this.parser.getNodeFromNode(node, this.recordFromNodeIsPresent());
				if(presentNode==null){
					logger.debug(this, "Не найден обязательный NODE ");
					throw new EParseExceptionItIsNotRecord();
				}
				// проверка на наличие необходимого текста, если таковой необходим
				if(recordFromNodeIsPresentText()!=null){
					// проверить на наличие текста в строке, если текста нет - вернуть null
					if(isPresentTextExistsMustPresent()){
						// текст должен существовать
						if(presentNode.getTextContent().indexOf(this.recordFromNodeIsPresentText())<0){
							// текст не найден - запись "не на складе"
							logger.debug(this, "обязательный NODE найден, текст не соответствует: "+presentNode.getTextContent());
							throw new EParseExceptionNotInStore();
						}
					}else{
						// текст не должен существовать
						// System.out.println(presentNode.getTextContent());
						if(presentNode.getTextContent().indexOf(this.recordFromNodeIsPresentText())>=0){
							// текст найден - запись "не на складе"
							logger.debug(this, "обязательный NODE найден, текст соответствует (not) - запись не на складе: "+presentNode.getTextContent());
							throw new EParseExceptionNotInStore();
						}
					}
				}
				// требуется ли проверка на наличие в элементе аттрибута 
				if(recordFromNodeIsPresentAttr()!=null){
					// logger.debug(this, "требуется наличие аттрибута ("+recordFromNodeIsPresentAttr()+") в элементе");
					if(this.isNodeAttributeExists(presentNode,recordFromNodeIsPresentAttr())==true){
						// требуется ли проверка на текстовое соответствие аттрибута
						if(this.recordFromNodeIsPresentAttrValue()!=null){
							// требуется проверка 
							if(this.isNodeAttributeEquals(presentNode, 
														  recordFromNodeIsPresentAttr(), 
														  recordFromNodeIsPresentAttrValue())==false){
								// аттрибут не соответствует
								throw new EParseExceptionNotInStore();
							}else{
								// аттрибут соответствует 
							}
						}else{
							// проверка не требуется 
						}
					}else{
						logger.debug(this, "аттрибут ("+recordFromNodeIsPresentAttr()+") в элементе НЕ найден - нет на складе ");
						throw new EParseExceptionNotInStore();
					}
				}
			}
			// name
			Node elementName=this.parser.getNodeFromNode(node, recordFromNodeInRecordToName());
			if(elementName==null){
				logger.info(this, "name element does not found:"+recordFromNodeInRecordToName());
				throw new EParseExceptionItIsNotRecord();
			}
			String name=elementName.getTextContent().trim();
			// url
			Element elementUrl=(Element)this.parser.getNodeFromNode(node, recordFromNodeInRecordToUrl());
			if(elementUrl==null){
				logger.info(this, "url element does not found:"+recordFromNodeInRecordToUrl());
				throw new EParseExceptionItIsNotRecord();
			}
			String url=null;
			if(this.recordIsRemoveStartPageFromUrl()){
				url=this.removeStartPage(elementUrl.getAttribute(this.getAttributeForExtractRecordUrl()));
			}else{
				url=elementUrl.getAttribute(this.getAttributeForExtractRecordUrl());
			}
			// price
			// priceUsd
			// priceEuro
			Float price=null;
			Float priceUsd=null;
			Float priceEuro=null;
			if(this.recordFromNodeInRecordToPrice()!=null){
				// есть путь к price
				// Node element=this.parser.getNodeFromNode(node, this.recordFromNodeInRecordToPrice());
				Node element=getPriceNodeFromLeafNode(node);
				if(element!=null){
					// элемент найден 
					price=getFloatFromString(this.recordFromNodeInRecordToPriceBeforeConvert(element.getTextContent()));
				}
			}
			if(this.recordFromNodeInRecordToPriceUsd()!=null){
				// есть путь к price
				// Node element=this.parser.getNodeFromNode(node, this.recordFromNodeInRecordToPriceUsd());
				Node element=getPriceUsdNodeFromLeafNode(node);
				if(element!=null){
					// элемент найден 
					priceUsd=getFloatFromString(this.recordFromNodeInRecordToPriceUsdBeforeConvert(element.getTextContent()));
				}
			}
			if(this.recordFromNodeInRecordToPriceEuro()!=null){
				// есть путь к price
				// Node element=this.parser.getNodeFromNode(node, this.recordFromNodeInRecordToPriceEuro());
				Node element=getPriceEuroNodeFromLeafNode(node);
				if(element!=null){
					// элемент найден 
					priceEuro=getFloatFromString(this.recordFromNodeInRecordToPriceEuroBeforeConvert(element.getTextContent()));
				}
			}
			// проверка на наличие какой-либо цены  
			if(isFloatEmpty(price)&&isFloatEmpty(priceUsd)&&isFloatEmpty(priceEuro)){
				this.logger.debug(this, "нет цены в записи");
				throw new EParseExceptionNotInStore();
			}
			return new Record(name, null, url, price, priceUsd, priceEuro);
		}catch(NullPointerException npe){
			throw new EParseExceptionItIsNotRecord();
		}
	};
	
	/** обязательно есть аттрибут в Element, который содержит данные о наличии товара 
	 * @return
	 * <ul>
	 * 	<li><b>null</b> - нет аттрибута для анализа </li>
	 * 	<li><b>value</b> - есть аттрибут для анализа </li>
	 * </ul>
	 * */
	protected String recordFromNodeIsPresentAttr() {
		return null;
	}
	
	/**
	 * текстовое значение аттрибута, который идентифицирует наличие товара
	 * @return
	 * <ul>
	 * 	<li><b>null</b> - нет текста для идентификации аттрибута </li>
	 * 	<li><b>value</b> - есть текстовое значение аттрибута для идентификации </li>
	 * </ul>
	 */
	protected String recordFromNodeIsPresentAttrValue(){
		return null;
	}

	/** получить цену из элемента, который содержит все данные о текущей позиции в целом   */
	protected Node getPriceNodeFromLeafNode(Node node){
		return this.parser.getNodeFromNode(node, this.recordFromNodeInRecordToPrice());		
	}
	
	/** получить цену USD из элемента, который содержит все данные о текущей позиции в целом   */
	protected Node getPriceUsdNodeFromLeafNode(Node node){
		return this.parser.getNodeFromNode(node, this.recordFromNodeInRecordToPriceUsd());
	}

	/** получить цену EURO из элемента, который содержит все данные о текущей позиции в целом   */
	protected Node getPriceEuroNodeFromLeafNode(Node node){
		return this.parser.getNodeFromNode(node, this.recordFromNodeInRecordToPriceEuro());
	}
	
	/** является ли Float значение null или 0 */
	private boolean isFloatEmpty(Float value){
		if(value==null)return true;
		if(value.floatValue()==0)return true;
		return false;
	}
	
	
}
