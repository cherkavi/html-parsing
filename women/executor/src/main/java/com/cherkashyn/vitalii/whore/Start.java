package com.cherkashyn.vitalii.whore;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.gogo.commands.*;
import org.slf4j.*;

import com.cherkashyn.vitalii.whore.interfaces.reestr.Reestr;
import com.cherkashyn.vitalii.whore.interfaces.spider.*;

@Command(scope="spider", name="start", description=" start spider(s) ")
public class Start extends ReestrAware {
	Logger LOGGER=LoggerFactory.getLogger(getClass());
	
	@Argument(index = 0, name = "spiderName", description = "name/number of the spider", required = true, multiValued = false)
    String name = null;
	
	@Override
	protected Object doExecute() throws Exception {
		if(this.reestr==null || this.reestr.getList()==null || this.reestr.getList().size()==0){
			System.out.println("ReturnCode:0");
			return null;
		}
		if(name==null){
			System.out.println("ReturnCode:0");
			System.out.println("need to have number or name of spider");
			return null;
		}
		
		Spider spiderForExecute=getTargetSpider(this.reestr, this.name);
		if(spiderForExecute==null){
			System.out.println("ReturnCode:0");
			System.out.println("spider was not found: "+name);
			return null;
		}
		if(Spider.Status.WORKING.equals(spiderForExecute.getCurrentStatus())){
			System.out.println("ReturnCode:0");
			System.out.println("spider is running now: "+name);
			return null;
		}
		
		spiderForExecute.execute();
		System.out.println("ReturnCode:1");
		System.out.println("spider was executed: "+name);
		return null;
	}

	protected Spider getTargetSpider(Reestr reestr, String argument) {
		Integer number=parseInteger(argument);
		if(number!=null){
			for(Map.Entry<Spider, Integer> eachSpider:reestr.getList().entrySet()){
				if(number.equals(eachSpider.getValue())){
					return eachSpider.getKey();
				}
			}
		}else{
			String clearName=StringUtils.trimToEmpty(argument);
			for(Map.Entry<Spider, Integer> eachSpider:reestr.getList().entrySet()){
				if(clearName.equals(eachSpider.getKey().getId())){
					return eachSpider.getKey();
				}
			}
		}
		return null;
	}

	private Integer parseInteger(String argument) {
		try{
			return Integer.parseInt(StringUtils.trimToEmpty(argument));
		}catch(NumberFormatException ne){
			return null;
		}
	}
	
}
