package shop_list.html.parser.engine.multi_page.section.url_next_section;

import shop_list.html.parser.engine.multi_page.section.url_next_section.single_change.AddPageChange;
import shop_list.html.parser.engine.multi_page.section.url_next_section.single_change.DirectPageChange;


/* Анализатор для секций - интуитивный построитель секций на основании предоставленных первоначальных данных */
public class Analisator implements IGetPageByNumber{
	private IGetPageByNumber proxy;
	private String originalFirstSection;
	/**
	 * Анализатор для секций - интуитивный построитель секций на основании предоставленных первоначальных данных
	 * @param firstSource
	 * @param secondSource
	 * @param thirdSource
	 * @throws Exception
	 */
	public Analisator(String firstSource, 
					  String secondSource, 
					  String thirdSource) throws Exception {
		this.originalFirstSection=firstSource;
		this.analisator(
						secondSource, 
						thirdSource);
	}
	
	private String getPreambula(String first, String second){
		String s1=(first.length()>second.length())?second:first;
		int counter=0;
		for(;counter<s1.length();counter++){
			if(first.charAt(counter)!=second.charAt(counter)){
				break;
			}
		}
		return first.substring(0,counter);
	}
	
	private String getPostambula(String first, String second){
		String s1=(first.length()>second.length())?second:first;
		int counter=0;
		for(;counter<s1.length();counter++){
			if(first.charAt(first.length()-counter-1)!=second.charAt(second.length()-counter-1))break;
		}
		return first.substring(first.length()-counter,first.length());
	}
	
	private int getKoef(String source, String left, String right){
		return Integer.parseInt(source.substring(left.length(), source.length()-right.length()));
	}
	
	private void analisator(String secondSource, 
							String thirdSource) throws Exception{
		
		// получить преамбулу
		String preambula=this.getPreambula(secondSource, thirdSource);
		// получить постамбулу
		String postambule=this.getPostambula(secondSource, thirdSource);
		// проанализировать прирост
		int k1=this.getKoef(secondSource, preambula, postambule);
		int k2=this.getKoef(thirdSource, preambula, postambule);
		if(k1==0||k2==0)throw new Exception("analise error");
		if(k1==2&&k2==3){
			this.proxy=new DirectPageChange(preambula, postambule);
		}else if(k1==1&&k2==2){
			this.proxy=new DirectPageChange(preambula, postambule, true);
		}else{
			this.proxy=new AddPageChange(preambula, postambule, k1, k2);
		}
	}
	
	@Override
	public String getPageByNumber(String firstPageInSection, int page) {
		if(firstPageInSection.equals(this.originalFirstSection)){
			//  искомая секция является оригинальной			
			return this.proxy.getPageByNumber(firstPageInSection, page);
		}
		String destinationSectionNumber=getSectionNumberFromPage(firstPageInSection);
		String pageWithOriginal=this.replaceToOriginalSection(firstPageInSection);
		String returnValue=this.proxy.getPageByNumber(pageWithOriginal, page);
		return this.replaceToDestinationSection(destinationSectionNumber,returnValue);
	}

	/**
	 * заменить оригинальный номер секции на указанный целевой
	 * @param destinationSectionNumber ( целевой номер секции )
	 * @param returnValue строка(содержащая оригинальный номер ), в которой должна произойти замена и которая должа вернуться как результат
	 * @return строка с замененным целевым номером 
	 */
	private String replaceToDestinationSection(String destinationSectionNumber, String returnValue) {
		/*if(this.getRepeatCount(returnValue, this.originalSectionValue)>1){
			String tempString=this.originalSectionPreambula.substring(this.originalSectionPreambula.length()-1);
			if(this.getRepeatCount(returnValue,tempString+this.originalSectionValue)==1){
				int index=returnValue.indexOf(tempString+this.originalSectionValue);
				return returnValue.substring(0, index+tempString.length())+destinationSectionNumber+returnValue.substring(index+this.originalSectionValue.length());
			}
			return null;
		}else{
			int index=returnValue.indexOf(this.originalSectionValue);
			return returnValue.substring(0, index)+destinationSectionNumber+returnValue.substring(index+this.originalSectionValue.length());
		}*/
		int index=returnValue.indexOf(this.originalSectionValue, this.originalSectionPreambula.length());
		return returnValue.substring(0, index)+destinationSectionNumber+returnValue.substring(index+this.originalSectionValue.length());
	}
	
	@SuppressWarnings("unused")
	private int getRepeatCount(String value, String tempValue){
		int repeatCount=0;
		int currentPosition=-1;
		while( (currentPosition=value.indexOf(tempValue, currentPosition+1))>=0){
			repeatCount++;
		}
		return repeatCount;
	}

	/**  в переданной строке найти уникальный номер секции и заменить его на оригинальный, вернуть полученный результат */
	private String replaceToOriginalSection(String firstPageInSection) {
		return this.originalFirstSection;
	}

	/** значение оригинального секционного номера */
	private String originalSectionValue=null;
	private String originalSectionPreambula=null;
	private String originalSectionPostambula=null;
	
	// private int originalSectionPosition=0;
	
	private String[] getPreambulaAndPostambula(String firstPageInSection){
		if(originalSectionValue==null){
			String preambula=this.getPreambula(originalFirstSection, firstPageInSection);
			String postambula=this.getPostambula(originalFirstSection, firstPageInSection);
			// проанализировать к какому типу определения секции относится данный тип:
				//                       http://xxx.yyy/section_id/zzzzzz
				// 		         http://xxx.yyy/section=section_id/zzzzz
				// http://xxx.yyy/another=valu1&section=section_id&another3=zzzzz
				// http://xxx.yyy/another=valu1&section=section_id/zzzzz
				//               http://xxx.yyy/section=section_id&another3=zzzzz
				//http://xxx.yyy/another3=zzzzz&section=section_id
			int tempLeft=this.getCharPositionFromEnd(preambula, '/','=');
			int tempRight=0;
			if(postambula.length()>0){
				tempRight=this.getCharPositionFromBegin(postambula, '/','&','.');
				if(tempRight==(-1)){
					tempRight=postambula.length();
				}
			}
			// получить истинное значение section_id ( без возможных вхождений одной секции в другую, например (46632 и 63 )
			preambula=originalFirstSection.substring(0, tempLeft+1);
			postambula=originalFirstSection.substring(originalFirstSection.length()-postambula.length()+tempRight);
			this.originalSectionPreambula=preambula;
			this.originalSectionPostambula=postambula;
			this.originalSectionValue=this.originalFirstSection.substring(preambula.length(), originalFirstSection.length()-postambula.length());
			// originalSectionPosition=preambula.length();
		}
		return new String[]{this.originalSectionPreambula, this.originalSectionPostambula};
	}
	
	/**  получить уникальный номер сессии на основании первой страницы другой сессии
	 * <br/>
	 * то есть нужно сравнить с сохраненным номером сессии и обнаружить различия
	 *  */
	private String getSectionNumberFromPage(String firstPageInSection) {
		// System.out.println("    original:"+firstPageInSection);
		// System.out.println("    preambula:"+preambula);
		// System.out.println("    postambula:"+postambula);
		String[] preambulaPostambula=this.getPreambulaAndPostambula(firstPageInSection);
		return firstPageInSection.substring(preambulaPostambula[0].length(),
											firstPageInSection.length()-preambulaPostambula[1].length());
	}
	
	/** получить первый найденный символ в указанной строке с конца из указанного массива 
	 * @return номер позиции в строке
	 * */
	private int getCharPositionFromEnd(String value, char ... array){
		for(int counter=value.length()-1;counter>=0;counter--){
			if(charInArrayOfChar(value.charAt(counter),array))return counter;
		}
		return -1;
	}
	
	private boolean charInArrayOfChar(char value, char ... array){
		for(int counter=0;counter<array.length;counter++){
			if(value==array[counter])return true;
		}
		return false;
	}
	
	/** получить первый найденный символ в указанной строке с начала из указанного массива 
	 * @return номер позиции в строке
	 * */
	private int getCharPositionFromBegin(String value, char ... array){
		for(int counter=0;counter<value.length();counter++){
			if(charInArrayOfChar(value.charAt(counter),array))return counter;
		}
		return -1;
	}
	
	public static void main(String[] args) throws Exception{
		System.out.println("--- begin ---");
		/*Analisator a=new Analisator(
				"http://www.5star.com.ua/category/gps-navigatory/",
				"http://www.5star.com.ua/category/gps-navigatory/offset8/",
				"http://www.5star.com.ua/category/gps-navigatory/offset16/"
				);*/
		
		Analisator a=new Analisator(
				// "http://www.coolmart.com.ua/index.php?cat=2", "http://www.coolmart.com.ua/index.php?cat=2&page=2", "http://www.coolmart.com.ua/index.php?cat=2&page=3"
				"http://citycom.ua/shop/notebook/14-77/",
				  "http://citycom.ua/shop/notebook/14-77/page2/",
				  "http://citycom.ua/shop/notebook/14-77/page3/"
				  );
		
		
		
		System.out.println(a.getPageByNumber("http://citycom.ua/shop/mobilephone/b31/",2));
		
		/*
		Analisator a=new Analisator(
				"http://www.all-shop.com.ua/?PAGE=list&CATEGORY=56952",
				"http://www.all-shop.com.ua/?PAGE=list&p=2&CATEGORY=56952",
				"http://www.all-shop.com.ua/?PAGE=list&p=3&CATEGORY=56952"
		);
		System.out.println(a.getSectionNumberFromPage("http://www.all-shop.com.ua/?PAGE=list&CATEGORY=552"));
		System.out.println(a.getPageByNumber("http://www.all-shop.com.ua/?PAGE=list&CATEGORY=33333",4));
		*/
		System.out.println("---  end  ---");
	}
	
}
