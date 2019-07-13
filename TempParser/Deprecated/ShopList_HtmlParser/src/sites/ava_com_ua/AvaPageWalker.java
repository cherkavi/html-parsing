package sites.ava_com_ua;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import html_parser.Parser;
import html_parser.page.PageWalker;
import html_parser.record.Record;

public class AvaPageWalker extends PageWalker{
	private String charset;
	
	public AvaPageWalker(String charset){
		this.charset=charset;
	}
	
	@Override
	protected Node getNodeFromPage(String url) {
		return this.getXmlNodeFromUrl(url, this.charset, "/html/body/div[2]/div[3]/div[2]/div/form/table/tbody");
	}

	@Override
	protected ArrayList<Record> getRecordsFromNode(Node node) {
		ArrayList<Record> returnValue=new ArrayList<Record>();
		Parser parser=new Parser();
		try{
			if(node.hasChildNodes()){
				NodeList nodeList=node.getChildNodes();
				// walk through all child
				for(int index=0;index<nodeList.getLength();index++){
					Node currentNode=nodeList.item(index);
					if(currentNode.getNodeName().equals("tr")){
						returnValue.add(this.getRecord(parser, currentNode));
					}
				}
			}else{
				// child has no exists
			}
		}catch(NullPointerException ex){
			System.err.println("AvaPageWalker#getRecordsFromNode Exception:"+ex.getMessage());
		}
		return returnValue;
	}

	private AvaRecord getRecord(Parser parser,Node node){
		Node currentNode=parser.getNodeFromNodeAlternative(node, "/td[2]/a");
		if(currentNode instanceof Element){
			return new AvaRecord((Element)currentNode);
		}else{
			return null;
		}
		
	}
}
