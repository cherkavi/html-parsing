package html_parser;

import java.sql.Connection;

import java.util.ArrayList;
import html_parser.page.PageWalker;
import html_parser.page.PageWalkerAware;
import html_parser.record.Record;
import html_parser.record_processor.RecordProcessor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import database.Connector;

import sites.autozvuk_com_ua.AvtozvukPageWalker;
import sites.autozvuk_com_ua.AvtozvukPageWalkerAware;
import sites.autozvuk_com_ua.AvtozvukSaver;
import sites.autozvuk_com_ua.AvtozvukSectionXml;

/** �����, ������� �� ��������� XML ��������� ��������� "�����������" �� ���� ������� � ��������� ������, ������������ ��������� ������� ���� - "href","caption" */
public class WalkSection {
	/** ���������, ������� ��������� ������� �� ������ ������������ ��������� ������ */
	private ArrayList<ScanSectionFilter> allowListeners=new ArrayList<ScanSectionFilter>();
	
	/** �������� ���������, ������� ��������� ������� �� ������ �������� ��������� �������� */
	public void addAllowListener(ScanSectionFilter listener){
		this.allowListeners.add(listener);
	}
	/** ������� ���������, ������� ��������� ������� �� ������� ��������� ������ */
	public void removeAllowListener(ScanSectionFilter listener){
		this.allowListeners.remove(listener);
	}

	/** �������, ����� �� ���������� ������� ��������� ������� ������ */
	private boolean isProcessAllow(Element element){
		boolean returnValue=true;
		for(int counter=0;counter<this.allowListeners.size();counter++){
			if(this.allowListeners.get(counter).isFilter(element)==false){
				returnValue=false;
				break;
			}
		}
		return returnValue;
	}
	
	/** �������� �� ���� �������, ������� ������� � XML ��������� 
	 * @param xmlDocument - ��������, ������� �������� ��� �������� {@literal"<leaf caption="" href="">"} 
	 * @param urlPrefix - �������, ������� ����� ��������� ��� ��������� ������ ������ (href)
	 * @param processParent - ����� �� "�������" ����, ������� �������� �������������, �.�. �������� ������ ���� (true) ��� �� ����� ������� ������ "�����" ��� ������������ ����������� (false)
	 * @param processLeaf - ����� �� "�������" �������� ���� ������ - ������
	 * @param pageWalker - ������ ��������, ������� "��������" �� �������� ����������� ������
	 * @param pageWalkerAware - ������, ������� �������� ������ �� ��������� ��������
	 * @param delay - ��������, ������� ���������� ������ ��� �����������(�������� ������������) ������ ������ �� ����
	 * @param postProcessor - ��������� ���������� ������� ����� ����������  
	 */
	public void walk(Document xmlDocument, 
					 String urlPrefix, 
					 boolean processParent,
					 boolean processLeaf, 
					 PageWalker pageWalker,
					 PageWalkerAware pageWalkerAware,
					 Delay delay,
					 RecordProcessor preProcessor,
					 Saver saver){
		if(saver!=null)saver.begin();
		// ��������� ��� �����, ������� �������� � xmlDocument
		NodeList nodes=xmlDocument.getChildNodes().item(0).getChildNodes();
		for(int counter=0;counter<nodes.getLength();counter++){
			//System.out.println(this.getStringFromXmlDocument(nodes.item(counter)));
			walkNode(nodes.item(counter),
					 urlPrefix, 
					 processParent,
					 processLeaf, 
					 pageWalker, 
					 pageWalkerAware, 
					 delay,
					 preProcessor,
					 saver);
		}
		if(saver!=null)saver.finish();
	}
	/*
	private String getStringFromXmlDocument(Node document){
		Writer out=null;
		try{
			javax.xml.transform.TransformerFactory transformer_factory = javax.xml.transform.TransformerFactory.newInstance();  
			javax.xml.transform.Transformer transformer = transformer_factory.newTransformer();  
			javax.xml.transform.dom.DOMSource dom_source = new javax.xml.transform.dom.DOMSource(document); // Pass in your document object here  
			out=new StringWriter();
			//string_writer = new Packages.java.io.StringWriter();  
			javax.xml.transform.stream.StreamResult stream_result = new javax.xml.transform.stream.StreamResult(out);  
			transformer.transform(dom_source, stream_result);  
		}catch(Exception ex){
			System.err.println("getStringFromXmlDocument:"+ex.getMessage());
		}
		return (out==null)?"":out.toString();
	}*/

	
	/** �������� �� ���� ������, �� ���� �������  */
	private void walkNode(Node node,
						  String urlPrefix,
						  boolean processParent,
						  boolean processLeaf,
						  PageWalker pageWalker, 
						  PageWalkerAware pageWalkerAware, 
						  Delay delay,
						  RecordProcessor preProcessor,
						  Saver saver){
		if(node.hasChildNodes()){
			//System.out.println("has child");
			// ���� �������� ��������
			NodeList list=node.getChildNodes();
			for(int counter=0;counter<list.getLength();counter++){
				this.walkNode(list.item(counter),
							  urlPrefix,
							  processParent,
							  processLeaf,
							  pageWalker,
							  pageWalkerAware, 
							  delay,
							  preProcessor,
							  saver);
			}
			if(processParent){
				this.processNode(node,urlPrefix, pageWalker,pageWalkerAware,delay,preProcessor, saver);
			};
		}else{
			//System.out.println("this is leaf");
			// ������ ������� �������� ������, �.�. �� ����� �������� ���������
			// node
			if(processLeaf){
				this.processNode(node,urlPrefix,pageWalker,pageWalkerAware, delay,preProcessor, saver);
			}
		}
	}
	
	
	/** ���������� ������ ������� �� �������� ������, ������� �������� ��� ������
	 * @param node - ����, ������� �����
	 * @param urlPrefix - ������� URL � �������� ����� ��������� ������ 
	 * @param pageWalker - ������ ��� ������ �������
	 * @param pageWalkerAware - ������ ��� ����������� ����� ���������� 
	 * @param delay - �������� ��� ������
	 * @param saver - ������, ������� ������������ ��� ���������� Records 
	 */
	private void processNode(Node node,
							 String urlPrefix,
							 PageWalker pageWalker,
							 PageWalkerAware pageWalkerAware, 
							 Delay delay,
							 RecordProcessor preProcessor,
							 Saver saver){
		if(node instanceof Element){
			Element element=(Element)node;
			String href=element.getAttribute("href");
			String caption=element.getAttribute("caption");
			System.out.println(">>> Section:"+caption+"   Href: "+href);
			if(this.isProcessAllow(element)){
				pageWalker.updatePageWalkerAware(pageWalkerAware);
				pageWalkerAware.reset(urlPrefix+href);
				// �������� ��� ������ ��������
				while(pageWalker.hasMoreElements()){
					// ���������� ������� ����������
					ArrayList<Record> block=pageWalker.nextElement();
					if(preProcessor!=null){
						preProcessor.beforeSave(block);
					}
					// ����������� �� Saver-�
					if(saver!=null){
						for(int counter=0;counter<block.size();counter++){
							//System.out.println(block.get(counter).toString());
							if(saver.save(caption, block.get(counter))==false){
								System.err.println("WalkSection save Error:");
							}
						}
					}
					if(preProcessor!=null){
						preProcessor.afterSave(block);
					}
					System.out.println(">>> NextPage ( after "+pageWalker.getLastUrl()+") ");
					// ������, ���� �����, ��� ���������� �������� �������� - ����� ��������� � ������ � ���� �� �������
					try{
						Thread.sleep(delay.getDelayReadPage());
					}catch(Exception ex){};
				}
				try{
					Thread.sleep(delay.getDelayReadSection());
				}catch(Exception ex){};
			}else{
				// ������ �������� ���������� �������� - ������ ����������  
			}
		}else{
			// ������������� �������
		}
	}
	
	
	public static void main(String[] args){
/*		System.out.println("begin");
		// ������, ������� ������ �������� 
		PageWalker pageWalker=new AvtozvukPageWalker();
		// ������, ������� ��� ����� ��������� �������� 
		PageWalkerAware pageWalkerAware=new AvtozvukPageWalkerAware();
		// ������, ������� ����� XML �� ��������� �����������/������ ���������  
		AvtozvukSectionXml avtozvuk=new AvtozvukSectionXml();
		Document treeXml=avtozvuk.getXmlDocument();
		// ������-�������� ��� �������� ������ ������ ���������, � �� �������� 
		Delay delay=new Delay(5,2);
		// ������ ��� ���������� ������ 
		Connection connection=null;
		try{
			Connector connector=new Connector("V:/eclipse_workspace/ShopList_HtmlParser/shop_list.gdb");
			connection=connector.getConnector().getConnection();
		}catch(Exception ex){
			System.out.println("WalkSection#main getConnection Exception: "+ex.getMessage());
		}
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
				  null,
				  saver
				  );
		try{
			connection.close();
		}catch(Exception ex){};
		System.out.println("end");
*/		
	}
}
