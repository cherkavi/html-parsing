package shop_list.html.parser.engine;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.SortedMap;

/** допустимые кодировки */
public enum ECharset {
	/** utf-8 */
	UTF_8("utf-8"), 
	/** windows-1251 */
	WIN_1251("windows-1251"),
	/** KOI8-U */
	KOI8_U("KOI8-U"),
	/** KOI8-R */
	KOI8_R("KOI8-R"),
	;
	
	private String charset;
	
	ECharset(String value){
		this.charset=value;
	}
	
	public String getName(){
		return this.charset;
	}
	
	public static void main(String[] args){
		SortedMap<String, Charset> map=Charset.availableCharsets();
		Iterator<String> iterator=map.keySet().iterator();
		while(iterator.hasNext()){
			String key=iterator.next();
			Charset value=map.get(key);
			System.out.println("Key:"+key+"    Charset:"+value.name());
		}
	}
}
