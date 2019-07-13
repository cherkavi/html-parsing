package aromat_kiev_ua;

import org.w3c.dom.NodeList;
import html_parser.element.base.HtmlPage;
import html_parser.element.base.HtmlRecord;
import html_parser.engine.base.WalkRecord;

public class AromatWalkRecord extends WalkRecord{
	/** ����, ���������� ��� ������ �� ������ �������� */
	private NodeList list;
	
	public AromatWalkRecord(HtmlPage page,String charset, String xpathData) throws Exception {
		super(page,charset,xpathData,true);
		this.list=this.rootNode.getChildNodes();
	}
	
	private int childCounter=0;
	
	/** �������� ��������� ������ �� �����, ��� ������� null, ���� ������� ��� */
	@Override
	public HtmlRecord getNextRecord() {
		HtmlRecord returnValue=null;
		// ����� ���������� �������� �� ����� ������
			// ����� valign="top"
		while(childCounter<this.list.getLength()){
			if(this.list.item(childCounter).hasAttributes()){
				// ���� �������� - ��������� - ������
				try{
					returnValue=new AromatHtmlRecord(
													 this.list.item(childCounter),
													 this.list.item(childCounter+1),
													 this.list.item(childCounter+2),
													 this.list.item(childCounter+3),
													 this.list.item(childCounter+4),
													 this.list.item(childCounter+5),
													 this.list.item(childCounter+6),
													 this.list.item(childCounter+7)
												     );
					childCounter++;
					break;
				}catch(Exception ex){
					// ������� � ��������� ��������
					System.err.println("get element error:"+ex.getMessage());
				}
			}
			childCounter++;
		}
		return returnValue;
	}
	
}
