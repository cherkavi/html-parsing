package shop_list.html.parser.engine.multi_page.standart;

import java.util.ArrayList;


import org.w3c.dom.Element;
import org.w3c.dom.Node;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionItIsNotRecord;
import shop_list.html.parser.engine.exception.EParseExceptionNotInStore;
import shop_list.html.parser.engine.exception.EParseExceptionRecordListEmpty;
import shop_list.html.parser.engine.exception.EParseExceptionRecordListLoadError;
import shop_list.html.parser.engine.multi_page.BaseMultiPage;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.section_finder.TestStubFinder;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.DirectFinder;
import shop_list.html.parser.engine.multi_page.section_finder.multi_level_finder.MultiLevelFinder;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.RecursiveFinder;
import shop_list.html.parser.engine.multi_page.section_finder.two_level_finder.TwoLevelFinder;
import shop_list.html.parser.engine.parser.IParser;
import shop_list.html.parser.engine.record.Record;

public abstract class ListBaseMultiPage extends BaseMultiPage{
	
	/** �������� �� ��������� Node �� ���� Xml Path  ���� ���������� Node (�������� ������ ������� �� ArrayList<Node> ��� ��������� � ������� �� ������� ��������� )*/
	protected Element getElementByAttributeName(Node rootNode, String xpath){
		try{
			ArrayList<Node> list=this.parser.getNodeListFromNode(rootNode, xpath);
			return (Element)list.get(0);
		}catch(Exception ex){
			logger.info(this,"check algorithm for valid elements:"+xpath+"   Exception:"+ex.getMessage());
			return null;
		}
	}
	
	
	@Override
	protected ArrayList<INextSection> getSection() {
		try{
			return this.getSectionFinder().getSection();
		}catch(Exception ex){
			System.err.println("#getSection Exception:"+ex.getMessage());
			return null;
		}
	}

	/** ���� � ����� ������ ����, ��������, ���������, ������� ����� ����� �� �������� ��� � ��� �����- ����� �������������� ������ ����� */
	protected int getFirstPositionInRecordsBlock(){
		return 0;
	}
	
	@Override
	protected ArrayList<Record> getRecordsFromBlock(Node node) throws EParseException{
		ArrayList<Record> returnValue=new ArrayList<Record>();
		ArrayList<Node> nodeList=null;
		try{
			logger.debug(this, "get Nodes from DataBlock by XPath:"+getXmlPathToRecordListFromDataBlock());
			nodeList=this.parser.getNodeListFromNode(node,getXmlPathToRecordListFromDataBlock());
		}catch(Exception ex){
			throw new EParseExceptionRecordListLoadError();
		}
		if((nodeList==null))throw new EParseExceptionRecordListLoadError();
		if(nodeList.size()==0) throw new EParseExceptionRecordListEmpty();
		boolean anyDataExists=false;
		for(int index=getFirstPositionInRecordsBlock();index<nodeList.size();index++){
			Node currentNode=nodeList.get(index);
			try{
				Record record=this.prepareRecordBeforeSave(this.getRecordFromNode(currentNode));
				anyDataExists=true;
				if(record!=null)returnValue.add(record);
			}catch(EParseExceptionNotInStore ex){
				anyDataExists=true;
				returnValue.add(new Record());
				logger.debug(this, "������ �� �� ������");
			}catch(EParseExceptionItIsNotRecord ex){
				logger.debug(this, "��������� Node �� �������� Record");
			}catch(Exception ex){
				logger.error(this, "#getRecordsFromBlock - check Elements");
			}
		}
		logger.debug(this, "#getRecordsFromBlock: "+returnValue.size());
		// ���� ��� ������ �� ������������ � ������ 
		if(anyDataExists==false)throw new EParseExceptionRecordListEmpty();
		return returnValue;
	}

	/** �������� ������, ������� ������ ��� ������
	 * <ul>
	 * 	<li>{@link DirectFinder}</li>
	 * 	<li>{@link TwoLevelFinder}</li>
	 * 	<li>{@link MultiLevelFinder}</li>
	 * 	<li>{@link RecursiveFinder}</li>
	 * 	<br /><br />
	 * 	<li>{@link TestStubFinder}</li>
	 * </ul>  
	 * */
	protected abstract ISectionFinder getSectionFinder() throws Exception;
	

	/** XPath �� ������ ����� ������, �� ������� ��������� {@link ListBaseMultiPage#getXmlPathToDataBlock()} 
	 * <br/>
	 * ����������� ������ ��������� �� ������ ������ ��� ���������� � {@link IParser#getNodeListFromNode(Node, String)}
	 * */
	protected abstract String getXmlPathToRecordListFromDataBlock();
	
	/** �� �������� �� ������� ���������: {@link #getXmlPathToDataBlock()} + {@link #getXmlPathToRecordFromDataBlock()} �������� ������� ��� ���������� � ���� ������ 
	 * <br/>
	 * @return null - ������ ������ �� ����� ��������� � ������
	 * <br/>
	 * for debug use method {@link #debugPrintNode(String, Node)} 
	 * */
	protected abstract Record getRecordFromNode(Node node) throws EParseException;
	
	
}
