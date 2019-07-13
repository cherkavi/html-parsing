package shop_list.html.parser.engine.multi_page.standart;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionItIsNotRecord;
import shop_list.html.parser.engine.exception.EParseExceptionNotInStore;
import shop_list.html.parser.engine.multi_page.section.url_next_section.Analisator;
import shop_list.html.parser.engine.multi_page.section.url_next_section.IAnalisatorAware;
import shop_list.html.parser.engine.record.Record;

public abstract class AnalisatorRecordListBaseMultiPage extends ListBaseMultiPage implements IAnalisatorAware{
	
	/** �������� ������������� (������������ Record) XPath ���� � ������������, ����� ����������� {@link Node#getTextContent()} */
	protected abstract String recordFromNodeInRecordToName();

	/** �������� ������������� (������������ Record) XPath ���� � URL, ����� ������� � ������� ������ ��������� ��������� {@link Element#getAttribute(String)} �� ������:*/
	protected abstract String recordFromNodeInRecordToUrl();

	/** �������� ������������� (������������ Record) XPath ���� � ���� � ������ ��� ��������� ������� {}
	 * @return null, ���� ��� � ������� 
	 * */
	protected abstract String recordFromNodeInRecordToPrice();
	
	/**  �������� ����� ������� ����� "��������" � Float ��� Price 
	 * <br>
	 * ���� ���������� �� ��������� - ������ ������� ���� �������� 
	 * */
	protected abstract String recordFromNodeInRecordToPriceBeforeConvert(String priceText);

	/** �������� ������������� (������������ Record) XPath ���� � ���� � USD ��� ��������� ������� {}
	 * @return null, ���� ��� � ������� 
	 */
	protected abstract String recordFromNodeInRecordToPriceUsd();

	/**  �������� ����� ������� ����� "��������" � Float ��� PriceUsd 
	 * <br>
	 * ���� ���������� �� ��������� - ������ ������� ���� �������� 
	 * */
	protected abstract String recordFromNodeInRecordToPriceUsdBeforeConvert(String priceText);

	/** �������� ������������� (������������ Record) XPath ���� � ���� � HRN ��� ��������� ������� {}
	 * @return null, ���� ��� � ������� 
	 * */
	protected abstract String recordFromNodeInRecordToPriceEuro();

	/**  �������� ����� ������� ����� "��������" � Float ��� PriceEuro
	 * <br>
	 * ���� ���������� �� ��������� - ������ ������� ���� �������� 
	 *  */
	protected abstract String recordFromNodeInRecordToPriceEuroBeforeConvert(String priceText);
	
	/** �������� ��� ��������� ��� ���������� �� ��������, �� ������� ����������� URL (default = "href") */
	protected String getAttributeForExtractRecordUrl(){
		return "href";
	}
	/** ����� �� ������� �� ������� ���� � Record ������ ��� ��������� �������� ? */
	protected abstract boolean recordIsRemoveStartPageFromUrl();
	
	/** ����� �� ��������� �� ������� ������ ( �� ���� ���� �� ������ �� �����, ������� �������������� ������� ������ ),
	 * ���������� XPath �� �������, ������� ������ ���� ������� � ���� ������  
	 * @return null - ���� �������� �� ���������
	 * ���� �������� ��������� - �������������� 
	 * */
	protected abstract String recordFromNodeIsPresent();
	
	/** �����, ������� ������ ���� � ������� � ������  �� ������� ��������� {{@link #recordFromNodeIsPresent()}} 
	 * @return null - ���������� ������ ������� ��������, �� ������� ��������� {@link #recordFromNodeIsPresent()}
	 * @return text - ����� ����� � ������, �� ������� ��������� {@link #recordFromNodeIsPresent()} ����������� ���������� ������ ����� (CaseSensetive) 
	 * */
	protected abstract String recordFromNodeIsPresentText();
	
	private Analisator analisator=null;
	
	/** �������� � ���� ������� �� ���� ����� ������ ������ �� ���� ������ ( ���� ������ ) ��� ��������
	 * <ul>
	 * 	<li><b>0</b> - ������ �������� ������� </li>
	 * 	<li><b>1</b> - ������ �������� ������� </li>
	 * 	<li><b>2</b> - ������ �������� ������� </li>
	 * </ul>
	 *   */
	protected abstract String[] getThreePageForAnalisator(); 
	
	/** */
	public Analisator getAnalisator(){
		if(analisator==null){
			String[] pages=getThreePageForAnalisator();
			try{
				this.analisator=new Analisator(pages[0], pages[1], pages[2]);
			}catch(Exception ex){
				logger.error(this, "#getAnalisator Exception: "+ex.getMessage());
			}
		}
		return this.analisator;
	}
	
	/** ����������� URL ����� ��������������� ��������� ������� */
	protected String prepareUrlBeforeSave(String url){
		return url;
	}
	
	/** �������� ������ �� �������������, ���� ����� ���� � ������������ Node {@link #recordFromNodeIsPresent},
	 *  � ��� �� ����� �����, ������� ������ ���� � ���� Node {@link #recordFromNodeIsPresentText()},
	 *  <br>
	 *  <ul>
	 *  	<li><b>true</b> - ����� ������ <b>��������������</b> (���� �� ��������, ����� ������ ��������� "�� �� ������" </li>
	 *  	<li><b>false</b> - ����� ������ <b>�������������</b> (���� �� ��������, ����� ������ ��������� "�� �� ������" )</li>
	 *  </ul>
	 *  <br>
	 *  �� �����������, � ������ ���� {@link #recordFromNodeIsPresentText()} ���������� null
	 */
	protected boolean isPresentTextExistsMustPresent(){
		return true;
	}
	
	protected Record getRecordFromNode(Node node) throws EParseException{
		try{
			// element exists
			if(recordFromNodeIsPresent()!=null){
				// �������� �� ������� ������ 
				Node presentNode=this.parser.getNodeFromNode(node, this.recordFromNodeIsPresent());
				if(presentNode==null){
					logger.debug(this, "�� ������ ������������ NODE ");
					throw new EParseExceptionItIsNotRecord();
				}
				// �������� �� ������� ������������ ������, ���� ������� ���������
				if(recordFromNodeIsPresentText()!=null){
					// ��������� �� ������� ������ � ������, ���� ������ ��� - ������� null
					if(isPresentTextExistsMustPresent()){
						// ����� ������ ������������
						if(presentNode.getTextContent().indexOf(this.recordFromNodeIsPresentText())<0){
							// ����� �� ������ - ������ "�� �� ������"
							logger.debug(this, "������������ NODE ������, ����� �� �������������: "+presentNode.getTextContent());
							throw new EParseExceptionItIsNotRecord();
						}
					}else{
						// ����� �� ������ ������������
						if(presentNode.getTextContent().indexOf(this.recordFromNodeIsPresentText())>=0){
							// ����� ������ - ������ "�� �� ������"
							logger.debug(this, "������������ NODE ������, ����� ������������� (not) - ������ �� �� ������: "+presentNode.getTextContent());
							throw new EParseExceptionItIsNotRecord();
						}
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
				url=this.removeStartPage(prepareUrlBeforeSave(elementUrl.getAttribute(this.getAttributeForExtractRecordUrl())));
			}else{
				url=prepareUrlBeforeSave(elementUrl.getAttribute(this.getAttributeForExtractRecordUrl()));
			}
			// price
			// priceUsd
			// priceEuro
			Float price=null;
			Float priceUsd=null;
			Float priceEuro=null;
			if(this.recordFromNodeInRecordToPrice()!=null){
				// ���� ���� � price
				Node element=getPriceNodeFromLeafNode(node);
				if(element!=null){
					price=getFloatFromString(this.recordFromNodeInRecordToPriceBeforeConvert(recordFromNodeInRecordToPriceGetTextFromElement(element)));
				}else{
					logger.info(this, "���� � ��� �������, ������� �� ������ ");
				}
			}
			if(this.recordFromNodeInRecordToPriceUsd()!=null){
				// ���� ���� � price
				Node element=getPriceUsdNodeFromLeafNode(node);
				if(element!=null){
					// ������� ������ 
					priceUsd=getFloatFromString(this.recordFromNodeInRecordToPriceUsdBeforeConvert(recordFromNodeInRecordToPriceUsdGetTextFromElement(element)));
				}else{
					logger.info(this, "���� � USD �������, ������� �� ������ ");
				}
			}
			if(this.recordFromNodeInRecordToPriceEuro()!=null){
				// ���� ���� � price
				Node element=getPriceEuroNodeFromLeafNode(node);
				if(element!=null){
					// ������� ������ 
					priceEuro=getFloatFromString(this.recordFromNodeInRecordToPriceEuroBeforeConvert(recordFromNodeInRecordToPriceEuroGetTextFromElement(element)));
				}else{
					logger.info(this, "���� � USD �������, ������� �� ������ ");
				}
			}
			// �������� �� ������� �����-���� ����  
			if(isFloatEmpty(price)&&isFloatEmpty(priceUsd)&&isFloatEmpty(priceEuro)){
				this.logger.debug(this, "��� ���� � ������");
				throw new EParseExceptionNotInStore();
			}
			return new Record(name, null, url, price, priceUsd, priceEuro);
		}catch(NullPointerException npe){
			this.logger.debug(this, "AnalisatorRecordListBaseMultiPage ������ ������ �� ���� Record");
			throw new EParseExceptionItIsNotRecord();
		}
	};
	
	/** �������� ���� �� ��������, ������� �������� ��� ������ � ������� ������� � �����   */
	protected Node getPriceNodeFromLeafNode(Node node){
		return this.parser.getNodeFromNode(node, this.recordFromNodeInRecordToPrice());		
	}
	
	/** �������� ���� USD �� ��������, ������� �������� ��� ������ � ������� ������� � �����   */
	protected Node getPriceUsdNodeFromLeafNode(Node node){
		return this.parser.getNodeFromNode(node, this.recordFromNodeInRecordToPriceUsd());
	}

	/** �������� ���� EURO �� ��������, ������� �������� ��� ������ � ������� ������� � �����   */
	protected Node getPriceEuroNodeFromLeafNode(Node node){
		return this.parser.getNodeFromNode(node, this.recordFromNodeInRecordToPriceEuro());
	}
	
	/** �������� �� ��������� �������� �����, �� �������� ����� �������� ���� ��� ( � ������, ����� ���� "��������" � ��������� ��������) */
	protected String recordFromNodeInRecordToPriceGetTextFromElement(Node element) {
		return element.getTextContent();
	}

	/** �������� �� ��������� �������� �����, �� �������� ����� �������� ���� USD ( � ������, ����� ���� "��������" � ��������� ��������) */
	protected String recordFromNodeInRecordToPriceUsdGetTextFromElement(Node element) {
		return element.getTextContent();
	}

	/** �������� �� ��������� �������� �����, �� �������� ����� �������� ���� EURO ( � ������, ����� ���� "��������" � ��������� ��������) */
	protected String recordFromNodeInRecordToPriceEuroGetTextFromElement(Node element) {
		return element.getTextContent();
	}

	/** �������� �� Float �������� null ��� 0 */
	private boolean isFloatEmpty(Float value){
		if(value==null)return true;
		if(value.floatValue()==0)return true;
		return false;
	}
}
