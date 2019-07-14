package shop_list.html.parser.engine.single_page;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import shop_list.html.parser.engine.record.Record;

/** шаблон для чтения только одной страницы, которая содержит все элементы в виде прайс-листа */
public abstract class TableSinglePage extends SinglePage{
	/** номер секции, по которой происходит сохранение данных в текущий момент  */
	private int currentSection=0;
	

	@Override
	protected boolean work(Integer sessionId, Node node) throws Exception {
		if((node!=null)&&(node.hasChildNodes())){
			NodeList list=node.getChildNodes();
			for(int counter=this.getFirstLine();counter<list.getLength();counter++){
				// если поток был остановлен вызовом метода stop
				if(flagRun==false){
					// остановить поток 
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
						// возможно запись содержит пустые значения цены, и не может быть сохранена
					}
				}
			}
			return true;
		}else{
			logger.error(this, "TableSinglePage#work Table not consists children ");
			return true;
		}
	}
	
	/** получить первую линию, с которой необходимо начать парсинг  */
	protected int getFirstLine(){
		return 0;
	}

	/** является ли переданный Node элементом TR, который содержит в себе название секции  */
	protected abstract boolean isSection(Node node);

	/** получить название секции из элемента TR  */
	protected abstract String getSectionNameFromRow(Node node);
	
	/** является ли переданный Node элементом TR, который содержит в себе данные для сохранения */
	protected abstract boolean isRecord(Node node);

	/** получить запись для сохранения в базе данных из элемента TR  */
	protected abstract Record getRecordFromRow(Node node);

}
