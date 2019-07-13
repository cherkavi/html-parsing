package sites.ava_com_ua;

import html_parser.Delay;
import html_parser.ScanSectionFilter;

import html_parser.WalkSection;
import html_parser.page.PageWalker;
import html_parser.record_processor.RecordProcessor;
import html_parser.section.SectionXml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import database.Connector;

public class EnterPoint {
	public static void main(String[] args){
		System.out.println("begin");
		
		// ������, ������� ����� XML �� ��������� �����������/������ ���������  
		SectionXml section=new AvaSectionXml();
		Document treeXml=section.getXmlDocument();
		
		// ������, ������� ��� ����� ��������� ��������
		AvaPageWalkerAware pageWalkerAware=new AvaPageWalkerAware();
		pageWalkerAware.setStartNumber(159);
		
		// ������, ������� ������ ��������
		PageWalker pageWalker=new AvaPageWalker("utf-8");
		
		// ������-�������� ��� �������� ������ ������ ���������, � �� �������� 
		Delay delay=new Delay(5,5);
		
		
		// ������, �������� ���������� ������ ��� ���������
		Connector connector=null;
		try{
			connector=new Connector("V:\\eclipse_workspace\\ShopList_HtmlParser\\shop_list.gdb");
		}catch(Exception ex){
			System.out.println("Connector Exception: "+ex.getMessage());
		};
		
		
		/** ������, ������� "�����������" �� ���� ������� */
		WalkSection walk=new WalkSection();
			// ���������� ������ �� ������, �� ������� ����� "��������" 
/*		
  		RecordProcessor recordProcessor=new AvaRecordProcessor(connector,
															   "utf-8",
															   Video.class,
															   VideoDescribe.class,
															   VideoDescribeName.class
															   );
*/															   
   		walk.addAllowListener(new ScanSectionFilter(){
   			@Override
   			public boolean isFilter(Element element) {
				boolean returnValue=false;
				String caption=this.getCaption(element);
				if(caption!=null){
					//element=this.getElement(returnValue, "http://ava.com.ua/category/16/106/150/", "motherboard");
					//element=this.getElement(returnValue, "http://ava.com.ua/category/16/106/143/", "video");
					//element=this.getElement(returnValue, "http://ava.com.ua/category/16/106/152/", "processor");
					//element=this.getElement(returnValue, "http://ava.com.ua/category/16/106/146/", "hdd");
					//element=this.getElement(returnValue, "http://ava.com.ua/category/16/106/151/", "memory");
					//if(caption.indexOf("hdd")>=0){returnValue=true;}
					if(caption.indexOf("memory")>=0){returnValue=true;}
				}
				return returnValue;
   			}
		});

		RecordProcessor recordProcessorImageSaver=new AvaRecordProcessorImageSaver(connector,"utf-8","C:\\computer_shop_photo\\");
		walk.walk(treeXml, 
				  "", 
				  true, 
				  true,
				  pageWalker, 
				  pageWalkerAware, 
				  delay,
				  recordProcessorImageSaver, // recordProcessor,
				  null
				  );
		
		/*
		
		// ������ ��� ���������� ������ 
		Connection connection=connector.get_connection_to_firebird("", 
																   "V:/eclipse_workspace/ShopList_HtmlParser/shop_list.gdb", 
																   0, 
																   "SYSDBA", 
																   "masterkey");
		AvtozvukSaver saver=new AvtozvukSaver(connection);
		//saver.resetAllRecord();
		
		WalkSection walk=new WalkSection();
		// ���������� ������ �� ������� 
		walk.addAllowListener(new ScanSectionFilter(){
			private boolean parse=false;
			@Override
			public boolean isFilter(Element element) {
				if(element.getAttribute("caption").indexOf("����������")>=0){
					parse=true;
				}
				return this.parse;
			}
		});
		walk.walk(treeXml, 
				  "http://avtozvuk.ua", 
				  true, 
				  true,
				  pageWalker, 
				  pageWalkerAware, 
				  delay,
				  saver
				  );
		try{
			connection.close();
		}catch(Exception ex){};
		*/
		System.out.println("end");
	}
}
