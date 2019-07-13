package shop_list.html.parser.engine.multi_page.debug;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PrintElement {
	private PrintWriter writer;

	public PrintElement(){
		writer=new PrintWriter(System.out);
	}

	public PrintElement(String fileName){
		try{
			writer=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
		}catch(Exception ex){
			writer=new PrintWriter(System.out);
		}
	}
	
	public void finalize(){
		if(writer!=null){
			try{
				writer.close();
			}catch(Exception ex){
			}
		}
	}
	
	public void outputAsTree(Node node){
		writer.println("==============:"+node.getNodeName()+":==============");
		printElement("",(Element)node,"", "");
		writer.println("==============:end:================");
		writer.flush();
	}

	public void outputAsTree(int index, Node node){
		writer.println("==============:"+index+":"+node.getNodeName()+":==============");
		printElement("",(Element)node,"", "");
		writer.println("==============:end:================");
		writer.flush();
	}
	
	private void printElement(String prefix, Element element, String postfix, String history){
		writer.print(prefix);
		writer.print(element.getTagName());
		writer.print(postfix);
		writer.print("  ");
		writer.print(getAttributesAsString(element));
		String newHistory=history+"/"+element.getTagName()+getAllAttributes(element)+postfix;
		writer.println("   "+history);
		printNodeChild(prefix, element, newHistory);
	}

	/** получить все аттрибуты  */
	private String getAllAttributes(Element element){
		StringBuffer returnValue=new StringBuffer();
		NamedNodeMap map=element.getAttributes();
		for(int index=0;index<map.getLength();index++){
			Node node=map.item(index);
			node.getNodeName();
			node.getNodeValue();
			returnValue.append("[@"+node.getNodeName()+"=\""+node.getNodeValue()+"\"]");
		}
		return returnValue.toString();
	}
	
	private void printText(String prefix, String text, String postfix, String history){
		writer.print(prefix);
		writer.print(text);
		writer.print(postfix);
		writer.print("  ");
		writer.println("   \""+history+"\"");
	}
	
	private String incrementPrefix="    ";
	
	private void printNodeChild(String prefix, Node node, String history){
		if(node.hasChildNodes()){
			NodeList list=node.getChildNodes();
			HashMap<String, Integer> mapElement=new HashMap<String, Integer>();
			for(int counter=0;counter<list.getLength();counter++){
				if(list.item(counter) instanceof Element){
					String postfix="";
					Element currentElement=(Element)list.item(counter);
					String currentElementTagName=currentElement.getTagName();
					Integer quantity=mapElement.get(currentElementTagName);
					if(quantity==null){
						postfix="";
						mapElement.put(currentElementTagName, new Integer(1));
					}else{
						Integer nextValue=quantity.intValue()+1;
						mapElement.put(currentElementTagName, nextValue);
						postfix="["+nextValue+"]";
					}
					printElement(prefix+incrementPrefix, currentElement, postfix, history);
				}else{
					String tempString=list.item(counter).getTextContent().trim();
					if((tempString.length()>0)&&(!tempString.equals("\n"))){
						printText(prefix+incrementPrefix, tempString, "", history);
					}
				}
			}
		}else{
			writer.println(node.getTextContent());
		}
	}
	
	private String getAttributesAsString(Element element){
		 NamedNodeMap map=element.getAttributes();
		 StringBuffer returnValue=new StringBuffer();
		 for(int counter=0;counter<map.getLength();counter++){
			 Node node=map.item(counter);
			 returnValue.append("( "+node.getNodeName()+" : "+ node.getNodeValue()+" )");
		 };
		 return returnValue.toString();
	}

}
