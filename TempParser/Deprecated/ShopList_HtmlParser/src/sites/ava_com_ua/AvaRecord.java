package sites.ava_com_ua;

import org.w3c.dom.Element;

import html_parser.record.Record;

/** запись, которая содержит URL на страницу, содержащую описание сайта */
public class AvaRecord extends Record{
	private static final String urlPrefix="http://ava.com.ua";
	private String name;
	private String url;
	
	/** запись, которая содержит URL на страницу, содержащую описание сайта 
	 * @param node - ссылка на элемент Anchor, который содержит необходимые данные о ссылке на страницу описания 
	 * */
	public AvaRecord(Element element){
		this.url=urlPrefix+element.getAttribute("href").trim();
		this.name=element.getTextContent();
		//System.out.println("Url: "+url);
		//System.out.println("Name: "+name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public boolean equals(Object object) {
		if((object!=null)&&(object instanceof AvaRecord)){
			return (((AvaRecord)object).name.equals(name)
					&&((AvaRecord)object).url.equals(url));
		}else{
			return false;
		}
	}
}
