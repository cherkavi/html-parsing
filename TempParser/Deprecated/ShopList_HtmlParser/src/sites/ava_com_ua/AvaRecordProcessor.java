package sites.ava_com_ua;

import java.util.ArrayList;


import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import database.ConnectWrap;
import database.Connector;
import database.wrap.Base;
import database.wrap.BaseDescribe;
import database.wrap.BaseDescribeName;

import html_parser.Parser;
import html_parser.record.Record;
import html_parser.record_processor.RecordProcessor;

/** ��������� ��� {@link AvaRecord} */
public class AvaRecordProcessor extends RecordProcessor{
	private Parser parser;
	/** ������� ������� �������� */
	private String charset;
	/** ���������� � ����� ������ */
	private Connector connector=null;
	
	/** ��������� ��� {@link AvaRecord}
	 * @param connector ��������� � ����� ������ 
	 * @param charset - ������� �������� �������
	 * @param classBase - ����� [Video.class, Memory.class, Mb.class, Processor.class]
	 * @param classBaseDescribe ����� ���������
	 * @param classBaseDescribeName ����� �������� ���������
	 */
	public AvaRecordProcessor(Connector connector, 
							  String charset,
							  Class<? extends Base> classBase,
							  Class<? extends BaseDescribe> classBaseDescribe,
							  Class<? extends BaseDescribeName> classBaseDescribeName){
		this.parser=new Parser();
		this.charset=charset;
		this.connector=connector;
		this.classBase=classBase;
		this.classDescribe=classBaseDescribe;
		this.classDescribeName=classBaseDescribeName;
	}
	
	@Override
	public void afterSave(ArrayList<Record> block) {
	}

	@Override
	public void beforeSave(ArrayList<Record> list) {
		// �������� �������� �� ������� ��������� ������� �������
		if(list!=null){
			for(int counter=0;counter<list.size();counter++){
				if(list.get(counter) instanceof AvaRecord){
					this.processRecord((AvaRecord)list.get(counter));
				}
			}
		}
	}

	private void debug(Object object){
		System.out.println(object);
	}
	
	/** �� �������� ������ ���������� ���������� ����� */
	private Integer getDescribeNameId(String describeName){
		Integer returnValue=null;
		ConnectWrap connectWrap=this.connector.getConnector();
		try{
			Session session=connectWrap.getSession();
			BaseDescribeName mbDescribeName=(BaseDescribeName)session.createCriteria(classDescribeName).add(Restrictions.eq("name", describeName)).uniqueResult();
			if(mbDescribeName!=null){
				// value is exists
				returnValue=mbDescribeName.getId();
			}else{
				// new Value;
				session.beginTransaction();
				BaseDescribeName newObject=this.getBaseDescribeNameInstance();
				newObject.setName(describeName);
				session.save(newObject);
				session.getTransaction().commit();
				returnValue=newObject.getId();
			}
		}catch(Exception ex){
			System.err.println("getDescribeNameId#Exception: "+ex.getMessage());
		}finally{
			connectWrap.close();
		}
		return returnValue;
	}

	/** ����� ������� Base */
	private Class<? extends Base> classBase;
	/** ����� ������� BaseDescribe*/
	private Class<? extends BaseDescribe> classDescribe;
	/** ����� ������� BaseDescribeName */
	private Class<? extends BaseDescribeName> classDescribeName;
	
	
	private Base getBaseInstance(){
		try{
			return this.classBase.newInstance();
		}catch(Exception ex){
			System.err.println("AvaRecordProcessor#getBaseInstance Exception: "+ex.getMessage());
			return null;
		}
	}
	
	private BaseDescribe getBaseDescribeIntance(){
		try{
			return this.classDescribe.newInstance();
		}catch(Exception ex){
			System.err.println("AvaRecordProcessor#getClassDescribeInstance Exception: "+ex.getMessage());
			return null;
		}
	}
	
	private BaseDescribeName getBaseDescribeNameInstance(){
		try{
			return this.classDescribeName.newInstance();
		}catch(Exception ex){
			System.err.println("AvaRecordProcessor#getBaseClassDescribeNameInstance Exception: "+ex.getMessage());
			return null;
		}
	}
	
	/** ��������� ���������� ������ �� ������� 
	 * @param mb - ������ "����������� �����"
	 * @param listOfDescribe - ������ ��������, ����������� 
	 * @return
	 */
	private boolean saveData(Base mb, ArrayList<BaseDescribe> listOfDescribe){
		boolean returnValue=false;
		ConnectWrap connectWrap=this.connector.getConnector();
		Session session=null;
		try{
			session=connectWrap.getSession();
			session.beginTransaction();
			// ��������� ����������� ����� � �������� �� ��� �������� �� ����
			session.save(mb);
			for(int counter=0;counter<listOfDescribe.size();counter++){
				// ���������� �������� �� listOfDescribe 
				listOfDescribe.get(counter).setIdMb(mb.getId());
				// ��������� �������� �� listOfDescribe 
				session.save(listOfDescribe.get(counter));
			}
			session.getTransaction().commit();
			returnValue=true;
		}catch(Exception ex){
			try{
				session.getTransaction().rollback();
			}catch(Exception exIn){};
			System.err.println("AvaRecordProcessor#saveData: "+ex.getMessage());
		}finally{
			connectWrap.close();
		}
		return returnValue;
	}
	
	/** ���������� ���������� ������ */
	private void processRecord(AvaRecord record){
		// �������� �� �������� Document
		Node node=this.parser.getNodeFromUrlAlternative(record.getUrl(), this.charset, "/html/body/div[2]/div[3]/div[4]");
		if(node!=null){
			try{
				// ��������� Node � ���� ������
				// �������� ������ ��� /h1 getText()
				Node objectName=this.parser.getNodeFromNodeAlternative(node, "/h1");
				Base mb=this.getBaseInstance();
				ArrayList<BaseDescribe> listOfDescribe=new ArrayList<BaseDescribe>();
				mb.setName(objectName.getTextContent());
				debug("Name: "+objectName.getTextContent());
					// �������� ������� � ������� /div/table/tbody
				Node description=this.parser.getNodeFromNodeAlternative(node, "/div[2]/table/tbody");
				NodeList nodeListOfTR=description.getChildNodes();
				for(int counter=0;counter<nodeListOfTR.getLength();counter++){
					NodeList listOfTD=nodeListOfTR.item(counter).getChildNodes();
					int tdIndex=1;
					Integer mbDescribeNameId=null;
					for(int index=0;index<listOfTD.getLength();index++){
						Node currentTD=listOfTD.item(index);
						if(currentTD.getNodeName().equalsIgnoreCase("td")){
							if(tdIndex==1){
								String describeName=currentTD.getTextContent().trim();
								debug("Section: "+describeName);
								mbDescribeNameId=this.getDescribeNameId(describeName);
							}
							if(tdIndex==2){
								Node ulNode=this.parser.getNodeFromNodeAlternative(currentTD, "/ul");
								if(ulNode!=null){
									NodeList li=ulNode.getChildNodes();
									for(int ulIndex=0;ulIndex<li.getLength();ulIndex++){
										if(li.item(ulIndex).getNodeName().equalsIgnoreCase("li")){
											String describeName=li.item(ulIndex).getTextContent().trim();
											BaseDescribe mbDescribe=this.getBaseDescribeIntance();
											mbDescribe.setIdMb(null);// need to set value
											mbDescribe.setIdDescribeName(mbDescribeNameId);
											mbDescribe.setName(describeName);
											listOfDescribe.add(mbDescribe);
										}
									}
								}else{
									String describeName=currentTD.getTextContent().trim();
									BaseDescribe mbDescribe=this.getBaseDescribeIntance();
									mbDescribe.setIdMb(null);// need to set value
									mbDescribe.setIdDescribeName(mbDescribeNameId);
									mbDescribe.setName(describeName);
									listOfDescribe.add(mbDescribe);
								}
							}
							tdIndex++;
						}
					}
				}
				this.saveData(mb, listOfDescribe);
			}catch(Exception ex){
				System.err.println("Error in read data - empty");
			}
		}else{
			System.err.println("AvaRecordProcessor#processRecord data not find ");
		}
	}
	
	
}
