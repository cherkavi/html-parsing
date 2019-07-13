package shop_list.html.parser.engine.multi_page.standart;

import org.w3c.dom.Element;

import org.w3c.dom.Node;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionItIsNotRecord;
import shop_list.html.parser.engine.exception.EParseExceptionNotInStore;
import shop_list.html.parser.engine.record.Record;

public abstract class RecordListBaseMultiPage extends ListBaseMultiPage{
	
	/** �������� ������������� (������������ Record) XPath ���� � ������������, ����� ����������� {@link Node#getTextContent()} */
	protected abstract String recordFromNodeInRecordToName();

	/** �������� ������������� (������������ Record) XPath ���� � URL, ����� ������� � ������� ������ ��������� ��������� {@link Element#getAttribute(String)} �� ������:
	 * <br/>
	 * ���� ����� �������� �� ��������� {@link #getAttributeForExtractRecordUrl}
	 * */
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
			// Node elementNameDebug=this.parser.getNodeFromNode(node, recordFromNodeInRecordToName());if(elementNameDebug!=null){System.out.println("Element finded: "+elementNameDebug.getTextContent());}else{System.out.println("Element does not finded ");}
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
							throw new EParseExceptionNotInStore();
						}
					}else{
						// ����� �� ������ ������������
						// System.out.println(presentNode.getTextContent());
						if(presentNode.getTextContent().indexOf(this.recordFromNodeIsPresentText())>=0){
							// ����� ������ - ������ "�� �� ������"
							logger.debug(this, "������������ NODE ������, ����� ������������� (not) - ������ �� �� ������: "+presentNode.getTextContent());
							throw new EParseExceptionNotInStore();
						}
					}
				}
				// ��������� �� �������� �� ������� � �������� ��������� 
				if(recordFromNodeIsPresentAttr()!=null){
					// logger.debug(this, "��������� ������� ��������� ("+recordFromNodeIsPresentAttr()+") � ��������");
					if(this.isNodeAttributeExists(presentNode,recordFromNodeIsPresentAttr())==true){
						// ��������� �� �������� �� ��������� ������������ ���������
						if(this.recordFromNodeIsPresentAttrValue()!=null){
							// ��������� �������� 
							if(this.isNodeAttributeEquals(presentNode, 
														  recordFromNodeIsPresentAttr(), 
														  recordFromNodeIsPresentAttrValue())==false){
								// �������� �� �������������
								throw new EParseExceptionNotInStore();
							}else{
								// �������� ������������� 
							}
						}else{
							// �������� �� ��������� 
						}
					}else{
						logger.debug(this, "�������� ("+recordFromNodeIsPresentAttr()+") � �������� �� ������ - ��� �� ������ ");
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
				// ���� ���� � price
				// Node element=this.parser.getNodeFromNode(node, this.recordFromNodeInRecordToPrice());
				Node element=getPriceNodeFromLeafNode(node);
				if(element!=null){
					// ������� ������ 
					price=getFloatFromString(this.recordFromNodeInRecordToPriceBeforeConvert(element.getTextContent()));
				}
			}
			if(this.recordFromNodeInRecordToPriceUsd()!=null){
				// ���� ���� � price
				// Node element=this.parser.getNodeFromNode(node, this.recordFromNodeInRecordToPriceUsd());
				Node element=getPriceUsdNodeFromLeafNode(node);
				if(element!=null){
					// ������� ������ 
					priceUsd=getFloatFromString(this.recordFromNodeInRecordToPriceUsdBeforeConvert(element.getTextContent()));
				}
			}
			if(this.recordFromNodeInRecordToPriceEuro()!=null){
				// ���� ���� � price
				// Node element=this.parser.getNodeFromNode(node, this.recordFromNodeInRecordToPriceEuro());
				Node element=getPriceEuroNodeFromLeafNode(node);
				if(element!=null){
					// ������� ������ 
					priceEuro=getFloatFromString(this.recordFromNodeInRecordToPriceEuroBeforeConvert(element.getTextContent()));
				}
			}
			// �������� �� ������� �����-���� ����  
			if(isFloatEmpty(price)&&isFloatEmpty(priceUsd)&&isFloatEmpty(priceEuro)){
				this.logger.debug(this, "��� ���� � ������");
				throw new EParseExceptionNotInStore();
			}
			return new Record(name, null, url, price, priceUsd, priceEuro);
		}catch(NullPointerException npe){
			throw new EParseExceptionItIsNotRecord();
		}
	};
	
	/** ����������� ���� �������� � Element, ������� �������� ������ � ������� ������ 
	 * @return
	 * <ul>
	 * 	<li><b>null</b> - ��� ��������� ��� ������� </li>
	 * 	<li><b>value</b> - ���� �������� ��� ������� </li>
	 * </ul>
	 * */
	protected String recordFromNodeIsPresentAttr() {
		return null;
	}
	
	/**
	 * ��������� �������� ���������, ������� �������������� ������� ������
	 * @return
	 * <ul>
	 * 	<li><b>null</b> - ��� ������ ��� ������������� ��������� </li>
	 * 	<li><b>value</b> - ���� ��������� �������� ��������� ��� ������������� </li>
	 * </ul>
	 */
	protected String recordFromNodeIsPresentAttrValue(){
		return null;
	}

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
	
	/** �������� �� Float �������� null ��� 0 */
	private boolean isFloatEmpty(Float value){
		if(value==null)return true;
		if(value.floatValue()==0)return true;
		return false;
	}
	
	
}
