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

/** процессор для {@link AvaRecord} */
public class AvaRecordProcessor extends RecordProcessor{
	private Parser parser;
	/** кодовая таблица страницы */
	private String charset;
	/** соединение с базой данных */
	private Connector connector=null;
	
	/** процессор для {@link AvaRecord}
	 * @param connector коннектор с базой данных 
	 * @param charset - кодовая страница таблицы
	 * @param classBase - класс [Video.class, Memory.class, Mb.class, Processor.class]
	 * @param classBaseDescribe класс элементов
	 * @param classBaseDescribeName класс названия элементов
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
		// получить страницу на которой находится искомый продукт
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
	
	/** по названию секции определить уникальный номер */
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

	/** класс объекта Base */
	private Class<? extends Base> classBase;
	/** класс объекта BaseDescribe*/
	private Class<? extends BaseDescribe> classDescribe;
	/** класс объекта BaseDescribeName */
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
	
	/** сохранить полученные данные по объекту 
	 * @param mb - объект "материнская плата"
	 * @param listOfDescribe - список объектов, описывающих 
	 * @return
	 */
	private boolean saveData(Base mb, ArrayList<BaseDescribe> listOfDescribe){
		boolean returnValue=false;
		ConnectWrap connectWrap=this.connector.getConnector();
		Session session=null;
		try{
			session=connectWrap.getSession();
			session.beginTransaction();
			// сохранить материнскую плату и получить по ней значение из базы
			session.save(mb);
			for(int counter=0;counter<listOfDescribe.size();counter++){
				// проставить значение из listOfDescribe 
				listOfDescribe.get(counter).setIdMb(mb.getId());
				// сохранить значение из listOfDescribe 
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
	
	/** обработать переданную запись */
	private void processRecord(AvaRecord record){
		// получить из страницы Document
		Node node=this.parser.getNodeFromUrlAlternative(record.getUrl(), this.charset, "/html/body/div[2]/div[3]/div[4]");
		if(node!=null){
			try{
				// сохранить Node в базе данных
				// получить полное имя /h1 getText()
				Node objectName=this.parser.getNodeFromNodeAlternative(node, "/h1");
				Base mb=this.getBaseInstance();
				ArrayList<BaseDescribe> listOfDescribe=new ArrayList<BaseDescribe>();
				mb.setName(objectName.getTextContent());
				debug("Name: "+objectName.getTextContent());
					// получить таблицу с данными /div/table/tbody
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
