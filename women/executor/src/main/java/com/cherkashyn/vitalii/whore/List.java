package com.cherkashyn.vitalii.whore;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.gogo.commands.*;
import org.slf4j.*;

import com.cherkashyn.vitalii.whore.interfaces.spider.*;

@Command(scope="spider", name="list", description=" print all accessible spiders ")
public class List extends ReestrAware {
	Logger LOGGER=LoggerFactory.getLogger(getClass());
	
	@Override
	protected Object doExecute() throws Exception {
		if(this.reestr==null || this.reestr.getList()==null || this.reestr.getList().size()==0){
			System.out.println("ReturnCode:0");
			return null;
		}
		System.out.println("ReturnCode:"+this.reestr.getList().size());
		
		for(Map.Entry<Spider, Integer> eachSpider:this.reestr.getList().entrySet()){
			printLine(eachSpider);
		}
		return null;
	}

	private void printLine(Entry<Spider, Integer> eachSpider) {
		StringBuilder returnValue=new StringBuilder();
		returnValue.append("[");
		returnValue.append(StringUtils.leftPad(eachSpider.getValue().toString(), 5));
		returnValue.append("] [");
		returnValue.append(StringUtils.leftPad(eachSpider.getKey().getCurrentStatus().toString(), 12));
		returnValue.append("] ");
		returnValue.append(eachSpider.getKey().getId());
		System.out.println(returnValue.toString());
	}
}
