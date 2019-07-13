package shop_list.html.parser.engine.single_page.list_element;

import java.util.ArrayList;


import org.w3c.dom.Node;

import shop_list.html.parser.engine.record.Record;

/** ������ ��� ������ ������ ����� ��������, ������� �������� ��� �������� � ���� �����-����� */
public abstract class TableSinglePage2 extends SinglePage2{
	/** ����� ������, �� ������� ���������� ���������� ������ � ������� ������  */
	private int currentSection=0;
	

	/** �������� �� ������ ������ */
	protected boolean isStringEmpty(String value){
		return (value==null)||(value.trim().equals(""));
	}
	
	
	/** �������� Float �� ��������� ������  */
	protected Float getFloatFromString(String value){
		if(value==null)return null;
		String tempValue=value.trim();
		try{
			return Float.parseFloat(tempValue);
		}catch(Exception ex){
			// ��������, ����� �������� ����� �� �������
			try{
				return Float.parseFloat(tempValue.replaceAll(",", "."));
			}catch(Exception exInner){
				return null;
			}
			
		}
	}
	
	/** 
	 * ��������� �� ������������ ������������� �������� ������ �� ������������� 
	 * @param original - ������������ ��������
	 * @param controlValues - ��������, ������� ����� ��������� �� ������������ 
	 * */
	protected boolean isStringEqualsAny(String original, String ... controlValues) {
		if(original==null)return false;
		if((controlValues==null)||(controlValues.length==0))return true;
		boolean returnValue=false;
		String newValue=original.trim();
		for(int counter=0;counter<controlValues.length;counter++){
			if(controlValues[counter]!=null)
			if(newValue.equalsIgnoreCase(controlValues[counter].trim())){
				returnValue=true;
				break;
			}
		}
		return returnValue;
	}
	
	
	/** ������� �� ������ ����� ���������� ������� ��� ��������, ������� ��� �� */
	protected String removeAfterSymbolIncludeIt(String url, char c) {
		int index=url.indexOf(c);
		if(index>0){
			return url.substring(0,index); 
		}else{
			return url;
		}
	}

	/** ������� �� ������ ������ (�������� ��� �������, ������, 160 ) */
	protected String removeCharFromString(String value, int charKod){
		StringBuffer returnValue=new StringBuffer();
		for(int counter=0;counter<value.length();counter++){
			if(value.codePointAt(counter)!=charKod){
				returnValue.append(value.charAt(counter));
			}
		}
		return returnValue.toString();
	}
	
	
	/** ������� �� ������ URL ��������� �������� */
	protected String removeStartPage(String url){
		String source=this.getShopUrlStartPage().replaceAll("^(http://)", "");
		source=source.replaceAll("^(www.)", "");
		int index=url.indexOf(source);
		if(index>=0){
			try{
				return url.substring(index+source.length());
			}catch(Exception ex){
				return url;
			}
		}else{
			return url;
		}
	}
	
	
	/** ������� ��������� ������� �� ������  */
	protected String removeLastSymbol(String priceElementText, int count) {
		if(priceElementText==null)return null;
		if(priceElementText.length()<count)return "";
		return priceElementText.substring(0, priceElementText.length()-count);
	}

	
	
	/** ���������� ���������� SectionName �� Node */
	public String sectionPostProcessor(String sectionName){
		return sectionName;
	}
	
	@Override
	protected boolean work(Integer sessionId, ArrayList<Node> nodeList) throws Exception {
		logger.info(this, "Elements count:"+nodeList.size());
		for(int counter=this.getFirstLine();counter<nodeList.size();counter++){
			// ���� ����� ��� ���������� ������� ������ stop
			if(flagRun==false){
				// ���������� ����� 
				return false;
			}
			Node currentNode=nodeList.get(counter);
			if(isSection(currentNode)){
				String sectionName=this.sectionPostProcessor(this.getSectionNameFromRow(currentNode));
				if(sectionName!=null){
					logger.info(this, "Section:"+sectionName.trim());
					currentSection=this.saver.getSectionId(sectionName.trim());
				}else{
					currentSection=0;
				}
				
			}else if(isRecord(currentNode)){
				Record record=this.checkForEmptyPrice(this.getRecordFromRow(currentNode));
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
	}
	
	private Record checkForEmptyPrice(Record record){
		if(record==null)return null;
		if(  ((record.getPrice()==null)||record.getPrice().floatValue()==0)
		   &&((record.getPriceUsd()==null)||record.getPriceUsd().floatValue()==0)
		   &&((record.getPriceEuro()==null)||record.getPriceEuro().floatValue()==0)){
			return null;
		}else{
			return record;
		}
	}
	
	/** �������� ������ �����, � ������� ���������� ������ �������  0.. */
	protected int getFirstLine(){
		return 0;
	}

	/** �������� �� ���������� Node ��������� TR, ������� �������� � ���� �������� ������  */
	protected abstract boolean isSection(Node node);

	/** �������� �������� ������ �� �������� TR  */
	protected abstract String getSectionNameFromRow(Node node);
	
	/** �������� �� ���������� Node ��������� TR, ������� �������� � ���� ������ ��� ���������� */
	protected abstract boolean isRecord(Node node);

	/** �������� ������ ��� ���������� � ���� ������ �� �������� TR  
	 * <br>
	 * ������� null, ���� ������ ��������, ���� �� �������� ������ ����, ���� �����������
	 * */
	protected abstract Record getRecordFromRow(Node node);

}
