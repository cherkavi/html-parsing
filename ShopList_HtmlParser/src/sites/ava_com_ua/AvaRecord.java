package sites.ava_com_ua;

import org.w3c.dom.Element;

import html_parser.record.Record;

/** ������, ������� �������� URL �� ��������, ���������� �������� ����� */
public class AvaRecord extends Record{
	private static final String urlPrefix="http://ava.com.ua";
	private String name;
	private String url;
	
	/** ������, ������� �������� URL �� ��������, ���������� �������� ����� 
	 * @param node - ������ �� ������� Anchor, ������� �������� ����������� ������ � ������ �� �������� �������� 
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
