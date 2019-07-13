package html_parser.page;

import html_parser.Parser;
import html_parser.record.Record;
import java.util.ArrayList;
import java.util.Enumeration;
import org.w3c.dom.Node;

/** ������, ������� �������� ������ �� ��������, <br>
 * ������� �������� ���� ������ �� ����� �������� ({@link #getNodeFromPage(String)}), <br>
 * ����� �������� ������ ������� ({@link #getRecordsFromNode}), �� ��������� ����� �����  */
public abstract class PageWalker implements Enumeration<ArrayList<Record>>{
	private Parser parser=null;
	private PageWalkerAware pageAware=null;
	private int currentPage=0;
	private boolean callHasMoreElement=false;
	/** ����, ������� ����� ����� �� �������, ����� ������ ������ .nextElement()*/
	private ArrayList<Record> nextBlock=null;
	/** ����, ������� ��� ����� ����� ������ ������ .nextElement()*/
	private ArrayList<Record> currentBlock=null;
	/** ��������� ������������ URL */
	private String lastUrl;
	/** �����, ������� ������������� �� ��������� ����������� ������� */
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

	/** �������� �� �������� Node, ������� �������� ���� � ������� */
	protected abstract Node getNodeFromPage(String url);
	
	/** �������� �� ������������� Node ���� ������� ���� �� ������� */
	protected abstract ArrayList<Record> getRecordsFromNode(Node node);
	
	@Override
	public boolean hasMoreElements(){
		this.callHasMoreElement=false;
		boolean returnValue=false;

		// ����������� �������� ��������� ��������
		lastUrl=this.pageAware.getUrl(currentPage+1);
		try{
			// �������� ��������� ���� ������
			nextBlock=getRecordsFromNode(getNodeFromPage(lastUrl));
			// ��������� �� ������� ������
			if((this.nextBlock==null)||(this.nextBlock.size()==0)){
				return false;
			}
			// ��������� ������ �� ������������ ����������� �����  
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

	/** �������� ��������� URL �� �������� ������� ������ */
	public String getLastUrl(){
		return this.lastUrl;
	}
	
	private boolean isListEquals(ArrayList<Record> first, ArrayList<Record> second){
		if((first!=null)&&(second!=null)){
			// ��������� �� ��������� ������� 
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
			// ����� �� ��������� - �������
			if(this.hasMoreElements()==true){
				currentPage++;
				// ��������� ������� �������� - ������� ���
				this.currentBlock=nextBlock;
				nextBlock=null;
				callHasMoreElement=false;
				return this.currentBlock;
			}else{
				// ��� ������ ���������
				return null;
			}
		}else{
			// ����� ��������� - ������� ���������
			currentPage++;
			this.currentBlock=nextBlock;
			nextBlock=null;
			callHasMoreElement=false;
			return this.currentBlock;
		}
	}
	
	/** �������� Node ������� �� ��������� ��������� ������� � ���� ������  
	 * @param urlPath - ���� � ���������� 
	 * @param charsetName - ��������� ��������� �������� 
	 * @param xpath - ���� � ������� XPath
	 * @return
	 */
	protected Node getXmlNodeFromUrl(String urlPath, 
									 String charsetName, 
									 String xpath){
		return this.parser.getNodeFromUrl(urlPath, charsetName, xpath);
	}
	
	
}
