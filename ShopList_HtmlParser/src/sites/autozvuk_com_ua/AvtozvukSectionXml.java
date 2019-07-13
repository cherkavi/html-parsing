package sites.autozvuk_com_ua;

import java.io.BufferedReader;
import java.io.StringReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import html_parser.section.SectionXml;

public class AvtozvukSectionXml extends SectionXml{
	private static String markerSectionNameBegin="8pt;\">";
	private static String markerSectionNameEnd="</a>";
	private static String markerSectionUrlBegin="<b><a href=\"";
	private static String markerSectionUrlEnd="\" style=";

	private static String markerSubSectionNameBegin="8pt;\">";
	private static String markerSubSectionNameEnd="</a>";
	private static String markerSubSectionUrlBegin="<a href=\"";
	private static String markerSubSectionUrlEnd="\" style=";

	private String getSubString(String value, String markerBegin, String markerEnd){
		int positionBegin=value.indexOf(markerBegin);
		if(positionBegin>=0){
			int positionEnd=value.indexOf(markerEnd, positionBegin);
			if(positionEnd>=0){
				return value.substring(positionBegin+markerBegin.length(),positionEnd).trim();
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	private String getSectionName(String value){
		if(isSection(value)){
			return getSubString(value, markerSectionNameBegin, markerSectionNameEnd);
		}else{
			return null;
		}
	}
	private String getSectionUrl(String value){
		if(isSection(value)){
			return getSubString(value, markerSectionUrlBegin, markerSectionUrlEnd);
		}else{
			return null;
		}
	}
	private String getSubSectionName(String value){
		if(isSection(value)){
			return null;
		}else{
			return getSubString(value, markerSubSectionNameBegin, markerSubSectionNameEnd);
		}
	}
	private String getSubSectionUrl(String value){
		if(isSection(value)){
			return null;
		}else{
			return getSubString(value, markerSubSectionUrlBegin, markerSubSectionUrlEnd);
		}
	}
	
	/** содержит ли прочитанная строка необходимую информацию для парсинга */
	private static boolean isValidString(String value){
		return (value.contains("href="));
	}

	/** является ли прочитанная строка Секцией или же является Подсекцией */
	private static boolean isSection(String value){
		return value.contains("<b>");
	}
	
	@Override
	public Document getXmlDocument(){
		Document returnValue=null;
		try{
			String bodyString=this.getSectionAsString("http://avtozvuk.ua", 
													  "windows-1251", 
													  "//html//body//table//tbody//tr[4]//td//table//tbody//tr[2]//td//div");
			BufferedReader reader=new BufferedReader(new StringReader(bodyString));
			returnValue=this.getNewEmptyDocument();
			Element root=this.getRootElement(returnValue);
			returnValue.appendChild(root);
			
			String currentLine=null;
			String sectionName=null;
			String sectionUrl=null;
			String subSectionName=null;
			String subSectionUrl=null;
			Element section=null;
			String temp=null;
			//Document doc = parser.parse(data, charsetName); // windows-1251
			
			while((currentLine=reader.readLine())!=null){
				if(isValidString(currentLine)){
					temp=getSectionName(currentLine);
					if((temp!=null)&&(!temp.equals(""))){
						sectionName=temp;
						sectionUrl=getSectionUrl(currentLine);
						subSectionName=null;
						subSectionUrl=null;
					}
					temp=getSubSectionName(currentLine);
					if((temp!=null)&&(!temp.equals(""))){
						subSectionName=temp;
						subSectionUrl=getSubSectionUrl(currentLine);
					}
					
					if(subSectionName==null){
						temp=getSectionName(currentLine);
						if((temp!=null)&&(!temp.equals(""))){
							// this is Section
							section=this.getElement(returnValue, sectionUrl, sectionName);
							root.appendChild(section);
							System.out.println("Section: "+sectionName+" ("+sectionUrl+")     SubSection: "+subSectionName+" ("+subSectionUrl+")");
						}else{
							// it is bad line
						}
					}else{
						// this is SubSection
						section.appendChild(this.getElement(returnValue, subSectionUrl,subSectionName));
						System.out.println("Section: "+sectionName+" ("+sectionUrl+")     SubSection: "+subSectionName+" ("+subSectionUrl+")");
					}
				}
			}
		}catch(Exception ex){
			System.err.println("Exception: "+ex.getMessage());
		}finally{
		}
		return returnValue;
	}
	
	public AvtozvukSectionXml(){
	}
	
	
	public static void main(String[] args){
		try{
			AvtozvukSectionXml avtozvuk=new AvtozvukSectionXml();
			Document tree=avtozvuk.getXmlDocument();
			System.out.println(avtozvuk.getStringFromXmlDocument(tree));
		}catch(Exception ex){
			System.err.println("Avtozvuk Exception: "+ex.getMessage());
		}
	}
	

}
