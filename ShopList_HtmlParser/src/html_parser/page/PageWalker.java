package html_parser.page;

import html_parser.Parser;
import html_parser.record.Record;
import java.util.ArrayList;
import java.util.Enumeration;
import org.w3c.dom.Node;

/** объект, который собирает данные со страницы, <br>
 * сначала получает блок данных со всеми записями ({@link #getNodeFromPage(String)}), <br>
 * потом получает список записей ({@link #getRecordsFromNode}), на основании этого блока  */
public abstract class PageWalker implements Enumeration<ArrayList<Record>>{
	private Parser parser=null;
	private PageWalkerAware pageAware=null;
	private int currentPage=0;
	private boolean callHasMoreElement=false;
	/** блок, который будет выдан по запросу, после вызова метода .nextElement()*/
	private ArrayList<Record> nextBlock=null;
	/** блок, который был выдан после вызова метода .nextElement()*/
	private ArrayList<Record> currentBlock=null;
	/** последний обработанный URL */
	private String lastUrl;
	/** класс, который передвигается по страницам выделенного раздела */
	public PageWalker(){
		this.parser=new Parser();
	}
	
	protected PageWalkerAware getPageAware(){
		return this.pageAware;
	}
	
	public void updatePageWalkerAware(PageWalkerAware pageAware){
		this.pageAware=pageAware;
		currentPage=0;
		callHasMoreElement=false;
		nextBlock=null;
		currentBlock=null;
	}

	/** получить из страницы Node, который содержит блок с данными */
	protected abstract Node getNodeFromPage(String url);
	
	/** получить из родительского Node всех записей блок из записей */
	protected abstract ArrayList<Record> getRecordsFromNode(Node node);
	
	@Override
	public boolean hasMoreElements(){
		this.callHasMoreElement=false;
		boolean returnValue=false;

		// попробовать получить следующую страницу
		lastUrl=this.pageAware.getUrl(currentPage+1);
		try{
			// получить следующий блок данных
			nextBlock=getRecordsFromNode(getNodeFromPage(lastUrl));
			// проверить на наличие данных
			if((this.nextBlock==null)||(this.nextBlock.size()==0)){
				return false;
			}
			// проверить данные на идентичность предыдущего блока  
			if(isListEquals(this.currentBlock, this.nextBlock)){
				return false;
			}
			this.callHasMoreElement=true;
			returnValue=true;
		}catch(Exception ex){
			System.err.println(this.getClass().getName()+"#hasMoreElements: "+ex.getMessage());
		}
		return returnValue;
	}

	/** получить последний URL по которому делался запрос */
	public String getLastUrl(){
		return this.lastUrl;
	}
	
	private boolean isListEquals(ArrayList<Record> first, ArrayList<Record> second){
		if((first!=null)&&(second!=null)){
			// проверить на равенство списков 
			if(first.size()!=second.size()){
				return false;
			}else{
				boolean returnValue=true;
				for(int counter=0;counter<first.size();counter++){
					returnValue=returnValue&&(first.get(counter).equals(second.get(counter)));
					if(returnValue==false){
						break;
					}
				}
				return returnValue;
			}
			
		}else{
			if((first==null)&&(second==null)){
				return true;
			};
			if(((first==null)&&(second!=null))||((first!=null)&&(second==null))){
				return false;
			}
			return false;
		}
	}
	
	@Override
	public ArrayList<Record> nextElement(){
		if(callHasMoreElement==false){
			// метод не вызывался - вызвать
			if(this.hasMoreElements()==true){
				currentPage++;
				// следующий элемент прочитан - вернуть его
				this.currentBlock=nextBlock;
				nextBlock=null;
				callHasMoreElement=false;
				return this.currentBlock;
			}else{
				// нет больше элементов
				return null;
			}
		}else{
			// метод вызывался - вернуть результат
			currentPage++;
			this.currentBlock=nextBlock;
			nextBlock=null;
			callHasMoreElement=false;
			return this.currentBlock;
		}
	}
	
	/** получить Node элемент из удалённого источника элемент в виде строки  
	 * @param urlPath - путь к удаленному 
	 * @param charsetName - кодировка удаленной страницы 
	 * @param xpath - путь в формате XPath
	 * @return
	 */
	protected Node getXmlNodeFromUrl(String urlPath, 
									 String charsetName, 
									 String xpath){
		return this.parser.getNodeFromUrl(urlPath, charsetName, xpath);
	}
	
	
}
