package aromat_kiev_ua;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

import html_parser.element.base.HtmlPage;
import html_parser.element.base.HtmlRecord;
import html_parser.engine.base.WalkPage;
import html_parser.engine.utility.StringConverter;
import html_parser.reader.HttpReader;

/** класс для возврата всех страниц */
public class AromatWalkPage extends WalkPage{
	private HashMap<String,String> catalogList;

// --------------- begin test -----------------	
	public static void main(String[] args){
		AromatWalkPage aromat=new AromatWalkPage("D:\\eclipse_workspace\\ShopList_HtmlParser\\src\\description_cut.csv");
		//printHashMap(aromat.catalogList);
		HtmlPage page=null;
		while((page=aromat.getNextPage())!=null){
			System.out.println(">>> "+page.getUrl());
			try{
				AromatWalkRecord walkRecord=new AromatWalkRecord(page,"windows-1251","//html/body/table/tbody/tr[1]/td[1]/table[3]/tbody/tr[1]/td[2]/table[1]/tbody/tr[1]/td[1]/table[1]/tbody");
				HtmlRecord currentRecord=null;
				while( (currentRecord=walkRecord.getNextRecord())!=null){
					System.out.println(currentRecord);
				}
			}catch(Exception ex){
				System.out.println("WalkRecord: "+ex.getMessage());
			}
			page.close();
		}
	}
/*	private static void printHashMap(HashMap<String,String> map){
		Iterator<String> keys=map.keySet().iterator();
		while(keys.hasNext()){
			String key=keys.next();
			String value=map.get(key);
			System.out.println("Key: "+key+"    Value:"+value);
		}
	}*/
// --------------- end test -----------------
	
	public AromatWalkPage(String pathToCatalogList){
		try{
			catalogList=this.getElementsFromCsvToHashMap(pathToCatalogList, 
					 ';', 
					 "", 
					 0, 
					 new ConvertKey(), 
					 1, 
					 new ConvertValue());
		}catch(IOException ex){
			err("#constructor Exception: "+ex.getMessage());
		};
		first();
	}
	
	/** префикс для добавления к странице */
	private String urlPrefix="http://aromat.kiev.ua";
	/** итератор, который содержит текущий раздел */
	private Iterator<String> sectionIterator;
	/** текущий раздел */
	private String currentSection;
	/** номер текущего раздела */
	private int sectionCount;
	/** HttpReader from Aromat */
	HttpReader reader=new HttpReader();
	
	
	@Override
	public void first(){
		sectionIterator=this.catalogList.keySet().iterator();
		currentSection=sectionIterator.next();
		sectionCount=0;
	}
	
	@Override
	public String getCurrentSection(){
		return this.currentSection;
	}
	
	@Override
	public HtmlPage getNextPage() {
		// прочесть страницу по GET запросу
			// инкрементировать страницу
		String pageUrl=null;
		Reader currentReader=null;
		while(currentReader==null){
			if(sectionCount==0){
				pageUrl=urlPrefix+this.currentSection+"/index.html";
			}else{
				pageUrl=urlPrefix+this.currentSection+"/index"+sectionCount+".html";
			}
			currentReader=reader.getReader(pageUrl);
			if(currentReader==null){
				// инкрементировать раздел
				try{
					this.currentSection=this.sectionIterator.next();
				}catch(NoSuchElementException nsex){
					// список закончен
					return null;
				}
				this.sectionCount=0;
				continue;
			}else{
				// вернуть раздел
				break;
			}
		}
		sectionCount++;
		return new HtmlPage(pageUrl, currentReader);
	}
	
	
}

class ConvertKey implements StringConverter{

	@Override
	public String convertString(String value) {
		return value.trim();
	}
	
}

class ConvertValue implements StringConverter{

	@Override
	public String convertString(String value) {
		int bracketPosition=value.indexOf('[');
		if(bracketPosition>=0){
			return value.substring(0,bracketPosition);
		}else{
			return value;
		}
	}
	
}

