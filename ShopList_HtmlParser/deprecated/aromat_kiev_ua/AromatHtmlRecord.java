package aromat_kiev_ua;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import html_parser.element.base.HtmlRecord;

public class AromatHtmlRecord extends HtmlRecord{
	private String brandName;
	private String positionName;
	private String aromat;
	private String aromatNote;
	private String imageUrl;
	private ArrayList<Line> lines=new ArrayList<Line>();
	
	public AromatHtmlRecord(String value) {
		super(value);
	}
	
	
	public AromatHtmlRecord(Node node1,// brandName, positionName 
							Node node2,// aromat, aromatNote,imageUrl
							Node node3,// 
							Node node4,// list
							Node node5,
							Node node6,
							Node node7,
							Node node8){
		this.parseNode1(node1);
		this.parseNode2(node2);
		this.parseNode4(node4);
	}
	
	/** get BrandName, get PositionName */
	private void parseNode1(Node node){
		try{
			NodeList list=node.getChildNodes();
			this.brandName=list.item(0).getTextContent();
			this.positionName=list.item(1).getTextContent();
		}catch(Exception ex){
			System.err.println("Error parse Node1:"+ex.getMessage());
		}
	}
	
	/** get Aromat, get AromatNote , get Image */
	private void parseNode2(Node node){
		try{
			this.imageUrl=this.getNodeByXPath(node, "td[1]/table[1]/tbody/tr/td/a/img/@src").getTextContent();
		}catch(Exception ex){
			System.err.println("Error parse Node1:"+ex.getMessage());
		}
		try{
			this.aromat=this.getNodeByXPath(node, "td[1]/div[2]").getTextContent();
		}catch(Exception ex){
			System.err.println("Error parse Node1:"+ex.getMessage());
		}
		try{
			this.aromatNote=this.getNodeByXPath(node, "td[1]/div[3]").getTextContent();
		}catch(Exception ex){
			System.err.println("Error parse Node1:"+ex.getMessage());
		}
		
	}
	
	private void parseNode4(Node node){
		try{
			NodeList list=(NodeList)this.getNodeListByXPath(node, "td[1]/table[1]/tbody/tr/td/table/tbody/tr");
			for(int counter=0;counter<list.getLength();counter++){
				this.lines.add(new Line(list.item(counter)));
			}
		}catch(Exception ex){
			System.err.println("Error parse Node1:"+ex.getMessage());
		}
	}
	
	@Override 
	public String toString(){
		StringBuffer buffer=new StringBuffer();
		buffer.append("===\nBrand: "+brandName);
		buffer.append("\nModel:"+positionName);
		buffer.append("\nAromat:"+aromat);
		buffer.append("\nAromatNote:"+aromatNote);
		buffer.append("\nImage:"+this.imageUrl);
		for(int counter=0;counter<this.lines.size();counter++){
			buffer.append(this.lines.get(counter).toString());
		}
		return buffer.toString();
	}
	
}

/** класс, который содержит строку заказа */
class Line{
	private String type;
	private String forMen;
	private String value;
	private String price;
	private String exists;
	
	@Override
	public String toString(){
		return "\n  Type:"+type+"\n  forMen:"+forMen+"\n  Value:"+value+"\n  Price:"+price+"\n  Exists:"+exists;
	}
	
	public Line(Node node){
		try{
			NodeList list=node.getChildNodes();
			this.type=list.item(0).getTextContent();
			this.forMen=list.item(1).getTextContent();
			this.value=list.item(2).getTextContent();
			this.price=list.item(3).getTextContent();
			this.exists=list.item(4).getTextContent();
		}catch(Exception ex){
			
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getForMen() {
		return forMen;
	}

	public void setForMen(String forMen) {
		this.forMen = forMen;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getExists() {
		return exists;
	}

	public void setExists(String exists) {
		this.exists = exists;
	}
	
	
}

