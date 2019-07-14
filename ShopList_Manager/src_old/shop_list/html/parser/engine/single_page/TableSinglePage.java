package shop_list.html.parser.engine.single_page;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import shop_list.html.parser.engine.record.Record;

/** ������ ��� ������ ������ ����� ��������, ������� �������� ��� �������� � ���� �����-����� */
public abstract class TableSinglePage extends SinglePage{
	/** ����� ������, �� ������� ���������� ���������� ������ � ������� ������  */
	private int currentSection=0;
	

	@Override
	protected boolean work(Integer sessionId, Node node) throws Exception {
		if((node!=null)&&(node.hasChildNodes())){
			NodeList list=node.getChildNodes();
			for(int counter=this.getFirstLine();counter<list.getLength();counter++){
				// ���� ����� ��� ���������� ������� ������ stop
				if(flagRun==false){
					// ���������� ����� 
					return false;
				}
				Node currentNode=list.item(counter);
				if(isSection(currentNode)){
					String sectionName=this.getSectionNameFromRow(currentNode);
					if(sectionName!=null){
						logger.info(this, "Section:"+sectionName.trim());
						currentSection=this.saver.getSectionId(sectionName.trim());
					}else{
						currentSection=0;
					}
					
				}else if(isRecord(currentNode)){
					Record record=this.getRecordFromRow(currentNode);
					if(record!=null){
						logger.debug(this, record.toString());
						if(this.saver.saveRecord(sessionId, currentSection, record)==false){
							logger.error(this, "Save Error");
						}
					}else{
						// �������� ������ �������� ������ �������� ����, � �� ����� ���� ���������
					}
				}
			}
			return true;
		}else{
			logger.error(this, "TableSinglePage#work Table not consists children ");
			return true;
		}
	}
	
	/** �������� ������ �����, � ������� ���������� ������ �������  */
	protected int getFirstLine(){
		return 0;
	}

	/** �������� �� ���������� Node ��������� TR, ������� �������� � ���� �������� ������  */
	protected abstract boolean isSection(Node node);

	/** �������� �������� ������ �� �������� TR  */
	protected abstract String getSectionNameFromRow(Node node);
	
	/** �������� �� ���������� Node ��������� TR, ������� �������� � ���� ������ ��� ���������� */
	protected abstract boolean isRecord(Node node);

	/** �������� ������ ��� ���������� � ���� ������ �� �������� TR  */
	protected abstract Record getRecordFromRow(Node node);

}
