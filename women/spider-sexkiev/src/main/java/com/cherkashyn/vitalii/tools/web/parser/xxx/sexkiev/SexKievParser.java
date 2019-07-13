package com.cherkashyn.vitalii.tools.web.parser.xxx.sexkiev;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cherkashyn.vitalii.tools.web.parser.action.exception.ChangedStructureException;
import com.cherkashyn.vitalii.tools.web.parser.action.exception.ReadRemoteDataException;
import com.cherkashyn.vitalii.tools.web.parser.action.page.Parser;
import com.cherkashyn.vitalii.tools.web.parser.domain.element.GroupBlock;
import com.cherkashyn.vitalii.tools.web.parser.domain.element.Page;
import com.cherkashyn.vitalii.whore.domain.WhoreElement;
import com.cherkashyn.vitalii.whore.exceptions.StorageException;
import com.cherkashyn.vitalii.whore.interfaces.imagesaver.ImageSaver;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class SexKievParser extends Parser<WhoreElement>{
	static final String INIT="http://sexkiev.net";
	
	public SexKievParser(){
	}

	private final static String XPATH_ANCHOR="//*[@id=\"wrapper\"]/div[@class=\"body\"]/div[@class=\"index\"][1]/div[@class=\"line\"]/div[@class=\"dotted\"]/div[@class=\"description\"]/div[@class=\"phone\"]/a";
	
	
	@Override
	public GroupBlock<WhoreElement> readData(Page pageData, ImageSaver imageSaver) throws ReadRemoteDataException, ChangedStructureException {
		GroupBlock<WhoreElement> returnValue=new GroupBlock<WhoreElement>();
		List<String> elements=parseUrls(pageData);
		webClient.closeAllWindows();
		if(elements==null){
			return returnValue;
		}
		try{
			for(String eachUrl: elements){
				HtmlPage page=null;
				try {
					page=webClient.getPage(eachUrl);
				} catch (FailingHttpStatusCodeException | IOException e ) {
					throw new ReadRemoteDataException(e);
				}
				returnValue.getElements().add(readBlock(page, imageSaver));
			}
		}finally{
			webClient.closeAllWindows();
		}
		return returnValue;
	}

	
	/**
	 * parse details from page 
	 * @param page
	 * @param imageSaver 
	 * @return
	 * @throws ReadRemoteDataException 
	 */
	private WhoreElement readBlock(HtmlPage page, ImageSaver imageSaver) throws ReadRemoteDataException {
		WhoreElement block=new WhoreElement();
		block.setUrl(page.getUrl().toString());
		// ... main image
		@SuppressWarnings("unchecked")
		List<HtmlImage> imageDefault =(List<HtmlImage>) page.getByXPath("//*[@id=\"wrapper\"]/div[@class=\"body\"]/div[@class=\"index\"]/div[@class=\"form_left\"]/div[@class=\"form_left_bg\"]/img[1]");
		if(imageDefault!=null && imageDefault.size()>0){
			try {
				block.getImages().add(imageSaver.saveFromHttp(imageDefault.get(0).getSrcAttribute()));
			} catch (StorageException e) {
				throw new ReadRemoteDataException(e);
			}
		}
		
		// ... sub-images 
		@SuppressWarnings("unchecked")
		List<HtmlAnchor> imagesSub =(List<HtmlAnchor>) page.getByXPath("//*[@id=\"wrapper\"]/div[@class=\"body\"]/div[@class=\"index\"]/div[@class=\"form_left\"]/div[@class=\"form_left_bg\"]/a[@rel=\"nofollow\"]");
		if(imagesSub!=null){
			for(HtmlAnchor eachAnchor:imagesSub){
				try {
					block.getImages().add(imageSaver.saveFromHttp(eachAnchor.getHrefAttribute()));
				} catch (StorageException e) {
					throw new ReadRemoteDataException(e);
				}
			}
		}
		
		// get information:
		block.setName(getText(page, "//*[@id=\"wrapper\"]/div[@class=\"body\"]/div[@class=\"index\"]/div[2]/div[@class=\"form_top_layout\"]/div[1]/span[2]/b"));
		block.getPhones().add(getText(page, "//*[@id=\"wrapper\"]/div[@class=\"body\"]/div[@class=\"index\"]/div[2]/div[@class=\"form_top_layout\"]/div[2]/span[2]/div[1]/span[1]"));
		
		List<String> prices=getTexts(page, "//*[@id=\"wrapper\"]/div[@class=\"body\"]/div[@class=\"index\"]/div[2]/div[@class=\"form_top_layout\"]/div[3]/span");
		block.getPrices().putAll(convert(split(prices, ":"), new IntegerConverter(), new StringConverter()));
		List<String> descriptions=getTexts(page, "//*[@id=\"wrapper\"]/div[@class=\"body\"]/div[@class=\"index\"]/div[2]/div[@class=\"all\"]");
		block.getDescriptions().putAll(filterByKey(split(descriptions, ":"),DESCRIPTIONS));
		List<String> 
		services=getTexts(page, "//*[@id=\"wrapper\"]/div[@class=\"body\"]/div[@class=\"index\"]/div[2]/div[@class=\"dotted\"]/div[@class=\"all\"]/span[1]/div[@class=\"services\"][1]/ul/li[@class=\"tick\"]/a");
		block.getServices().addAll(services);
		services=getTexts(page, "//*[@id=\"wrapper\"]/div[@class=\"body\"]/div[@class=\"index\"]/div[2]/div[@class=\"dotted\"]/div[@class=\"all\"]/span[1]/div[@class=\"services\"][2]/ul/li[@class=\"tick\"]/a");
		block.getServices().addAll(services);
		return block;
	}
	
	private final static List<String> DESCRIPTIONS=Arrays.asList("Город","Возраст", "Рост", "Грудь", "Вес", "Выезд");
	
	<TK, TV> Map<TK, TV> convert(Map<String, String> map, Converter<TK> converterKey, Converter<TV> converterValue){
		Map<TK, TV> returnValue=new LinkedHashMap<TK, TV>();
		for(Map.Entry<String, String> eachEntry:map.entrySet()){
			returnValue.put(converterKey.convert(eachEntry.getKey()), converterValue.convert(eachEntry.getValue()));
		}
		return returnValue;
	}
	
	
	/**
	 * parse all url from main page 
	 * @param pageData
	 * @return Nullable
	 * @throws ReadRemoteDataException
	 * @throws ChangedStructureException
	 */
	@SuppressWarnings("unchecked")
	private List<String> parseUrls(Page pageData) throws ReadRemoteDataException, ChangedStructureException{
		List<HtmlAnchor> groupNode = null;
		try{
			groupNode = (List<HtmlAnchor>) readHtmlPage(pageData).getByXPath(XPATH_ANCHOR);
		}catch(Exception ex){
			throw new ChangedStructureException(pageData.getUrlCurrent(),ex);
		}
		
		if(groupNode==null || groupNode.size()==0){
			return null;
		}
		List<String> returnValue=new ArrayList<String>();
		for(HtmlAnchor eachAnchor: groupNode){
			if(eachAnchor!=null && StringUtils.trimToNull(eachAnchor.getHrefAttribute())!=null){
				String href=eachAnchor.getHrefAttribute();
				if(StringUtils.startsWith(href, INIT)){
					returnValue.add(href);
				}else{
					returnValue.add(INIT+href);
				}
			}
		}
		return returnValue;
	}


	@Override
	public String getStartPage() {
		return  INIT;
	}

}
