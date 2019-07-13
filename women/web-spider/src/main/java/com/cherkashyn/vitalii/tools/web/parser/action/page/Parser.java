package com.cherkashyn.vitalii.tools.web.parser.action.page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.cherkashyn.vitalii.tools.web.parser.action.exception.ChangedStructureException;
import com.cherkashyn.vitalii.tools.web.parser.action.exception.ReadRemoteDataException;
import com.cherkashyn.vitalii.tools.web.parser.domain.element.GroupBlock;
import com.cherkashyn.vitalii.tools.web.parser.domain.element.Page;
import com.cherkashyn.vitalii.whore.interfaces.imagesaver.ImageSaver;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * read data from certain HTML page
 */
public abstract class Parser<T> {
	
	protected final WebClient webClient = new WebClient();

	/**
	 * @return first page as ID of parser
	 */
	public abstract String getStartPage();
	
	/**
	 * @param page - page for read data
	 * @param imageSaver - destination point for images 
	 * @return elements
	 * @throws ChangedStructureException  - when data from remote source was changed 
	 * @throws ReadRemoteDataException - when can't read data from remote source
	 */
	public abstract GroupBlock<T> readData(Page page, ImageSaver imageSaver) throws ReadRemoteDataException, ChangedStructureException;
	
	protected Map<? extends String, ? extends String> filterByKey(Map<String, String> source, List<String> descriptions) {
		Map<String, String> returnValue=new LinkedHashMap<String, String>(source);
		Iterator<Entry<String, String>> iterator=returnValue.entrySet().iterator();
		while(iterator.hasNext()){
			String key=StringUtils.trim(iterator.next().getKey());
			if(!descriptions.contains(key)){
				iterator.remove();
			}
		}
		return returnValue;
	}


	
	protected Map<String, String> split(List<String> lines, String separator){
		Map<String, String> returnValue=new LinkedHashMap<String, String>();
		for(String eachLine:lines){
			String key=StringUtils.substringBefore(eachLine, separator);
			String value=StringUtils.substringAfter(eachLine, separator);
			returnValue.put(key, value);
		}
		return returnValue;
	}
	
	protected List<String> getTexts(HtmlPage page, String xpath) {
		@SuppressWarnings("unchecked")
		List<HtmlElement> elements=(List<HtmlElement>)page.getByXPath(xpath);
		List<String> returnValue=new ArrayList<String>();
		if(elements!=null && elements.size()>0){
			for(HtmlElement eachElement:elements){
				returnValue.add(eachElement.getTextContent());
			}
		}
		return returnValue;
	}


	protected String getText(HtmlPage page, String xpath){
		List<String> elements=getTexts(page, xpath);
		if(elements.size()==0){
			return null;
		}
		return elements.get(0);
	}


	/**
	 * read HTML data from next main page  
	 * @param pageData
	 * @return
	 * @throws ReadRemoteDataException
	 */
	protected HtmlPage readHtmlPage(Page pageData) throws ReadRemoteDataException{
	    try {
			return webClient.getPage(pageData.getUrlCurrent());
		} catch (FailingHttpStatusCodeException | IOException e) {
			throw new ReadRemoteDataException(e);
		}
	}

	
	// -------------------- utility classes -------------------------
	
	
	public static abstract class Converter<T>{
		public abstract T convert(String inputValue);
	}

	
	public static class StringConverter extends Converter<String>{

		@Override
		public String convert(String inputValue) {
			return inputValue;
		}
	}

	public static class IntegerConverter extends Converter<Integer>{

		@Override
		public Integer convert(String inputValue) {
			try{
				return Integer.parseInt(inputValue.replaceAll("[^0-9]", ""));
			}catch(RuntimeException ex){
				return null;
			}
		}
		
	}
}



