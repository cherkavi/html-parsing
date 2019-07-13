package shop_list.html.parser.engine.multi_page.section_finder.section_extractor;

import org.w3c.dom.Element;


import shop_list.html.parser.engine.multi_page.section.INextSection;

public class AnchorResourceExtractor implements IResourceFromElementExtractor{
	private IResourceSectionFactory factory;
	private String preambula;
	public AnchorResourceExtractor(IResourceSectionFactory factory){
		this(factory, null);
	}
	
	public AnchorResourceExtractor(IResourceSectionFactory factory, String preambula){
		this.factory=factory;
		this.preambula=preambula;
	}
	
	/*private String getAttributesAsString(Element element){
		 NamedNodeMap map=element.getAttributes();
		 StringBuffer returnValue=new StringBuffer();
		 for(int counter=0;counter<map.getLength();counter++){
			 Node node=map.item(counter);
			 returnValue.append("( "+node.getNodeName()+" : "+ node.getNodeValue()+" )");
		 };
		 return returnValue.toString();
	}*/
	
	@Override
	public INextSection getResourceSection(Element element) {
		try{
			String name=element.getTextContent().trim();
			String url=null;
			// System.out.println("Href: "+element.getAttribute("href"));
			// System.out.println(getAttributesAsString(element));
			if(this.preambula!=null){
				url=this.preambula+element.getAttribute("href");
			}else{
				url=element.getAttribute("href");
			}
			return this.factory.getResourceSection(name, url);
		}catch(Exception ex){
			System.err.println("AnchorExtractor Exception:"+ex.getMessage());
			return null;
		}
	}

}
