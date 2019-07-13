package sites.hi_tech_com_ua;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import html_parser.section.SectionXml;

public class HiTechSectionXml extends SectionXml{

	@Override
	public Document getXmlDocument() {
		Document document=this.getNewEmptyDocument();
		Node root=this.getRootElement(document);
		document.appendChild(root);
	
		//Node sectionNode=this.getNodeFormUrl("http://www.hi-tech.com.ua","windows-1251", "//html//body//div[3]//div[3]//div[2]//div");
		Node sectionNode=this.getNodeFromUrlAlternative("http://www.hi-tech.com.ua","utf-8", "//html//body//div[3]//div[3]//div[2]//div");
		NodeList list=sectionNode.getChildNodes();
		for(int counter=0;counter<list.getLength();counter++){
			Node node=this.getXmlNodeFromHtml(document, list.item(counter));
			if(node!=null){
				root.appendChild(node);
			}
		}
		return document;
	}

	
	private Node getXmlNodeFromHtml(Document document, Node node){
		Node returnValue=null;
		if(node instanceof Element){
			Node nodeAnother=this.getNodeFromNodeAlternative(node, "/div/a");
			if(nodeAnother!=null){
				Element element=(Element)nodeAnother;
				returnValue=this.getElement(document, element.getAttribute("href"), element.getAttribute("title"));
				System.out.println("Href: "+element.getAttribute("href"));
				System.out.println("Title: "+element.getAttribute("title"));
			}else{
			}
		}else{
			// is not Element, may be another element
		}
		return returnValue;
	}
	
	public static void main(String[] args){
		HiTechSectionXml sectionXml=new HiTechSectionXml();
		System.out.println(sectionXml.getStringFromXmlDocument(sectionXml.getXmlDocument()));
	}
}
