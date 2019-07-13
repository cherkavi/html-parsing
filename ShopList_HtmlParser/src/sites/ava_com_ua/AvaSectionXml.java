package sites.ava_com_ua;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import html_parser.section.SectionXml;

/** получить XML документ со всеми разделами, которые будут рассматриваться */
public class AvaSectionXml extends SectionXml{

	@Override
	public Document getXmlDocument() {
		Document returnValue=this.getNewEmptyDocument();
		Element rootElement=this.getRootElement(returnValue);
		returnValue.appendChild(rootElement);
		Element element=null;
		// Mother Board
		element=this.getElement(returnValue, "http://ava.com.ua/category/16/106/150/", "motherboard");
		rootElement.appendChild(element);
		element=this.getElement(returnValue, "http://ava.com.ua/category/16/106/143/", "video");
		rootElement.appendChild(element);
		element=this.getElement(returnValue, "http://ava.com.ua/category/16/106/152/", "processor");
		rootElement.appendChild(element);
		element=this.getElement(returnValue, "http://ava.com.ua/category/16/106/146/", "hdd");
		rootElement.appendChild(element);
		element=this.getElement(returnValue, "http://ava.com.ua/category/16/106/151/", "memory");
		rootElement.appendChild(element);
		return returnValue;
	}

}
