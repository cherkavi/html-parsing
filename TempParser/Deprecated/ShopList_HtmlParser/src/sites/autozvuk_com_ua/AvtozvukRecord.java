package sites.autozvuk_com_ua;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import html_parser.record.Record;

public class AvtozvukRecord extends Record {
	private String url;
	private String imageUrl;
	private String name;
	private String description;
	private String price;
	private String priceUSD;
	
	public AvtozvukRecord(Node node){
		parseNode(node);
	}
	
	private void parseNode(Node node){
		if(node.hasChildNodes()){
			try{
				NodeList list=node.getChildNodes();
				/** номер позиции в обрабатываемом объекте */
				int controlValue=0;
				for(int counter=0;counter<list.getLength();counter++){
					Node processNode=list.item(counter);
					if(processNode instanceof Element){
						if(processNode.getNodeName().equalsIgnoreCase("td")){
							this.parseElement(processNode, controlValue);
							controlValue++;
							if(controlValue==4){
								break;
							}
						}
					}
				}
			}catch(Exception ex){
				System.err.println("AvtozvukRecord is parse Node Exception: "+ex.getMessage());
			}
		}else{
			System.err.println("AvtozvukRecord is not parse: ");
		}
	}

	
	private void parseElement(Node node, int position){
		if(position==0){
			// получить url, imageUrl, Name
			try{
				NodeList list=node.getChildNodes();
				for(int counter=0;counter<list.getLength();counter++){
					if(list.item(counter).getNodeName().equalsIgnoreCase("a")){
						Element element=((Element)list.item(counter));
						this.url=element.getAttribute("href").trim();
						this.name=element.getTextContent().trim();
						NodeList subList=element.getChildNodes();
						for(int index=0;index<subList.getLength();index++){
							if(subList.item(index).getNodeName().equalsIgnoreCase("img")){
								this.imageUrl=((Element)subList.item(index)).getAttribute("src").trim();
								break;
							}
						}
						break;
					}
				}
			}catch(Exception ex){
				System.err.println("AvtozvukRecord#parseElement 0 Exception: "+ex.getMessage());
			}
		}
		if(position==1){
			// получить description
			this.description=node.getTextContent().trim();
		}
		if(position==2){
			// получить цены
			NodeList list=node.getChildNodes();
			for(int counter=0;counter<list.getLength();counter++){
				if(list.item(counter).getNodeName().equalsIgnoreCase("b")){
					NodeList subList=list.item(counter).getChildNodes();
					for(int index=0;index<subList.getLength();index++){
						if(subList.item(index).getNodeName().equalsIgnoreCase("font")){
							this.price=subList.item(index).getTextContent().trim();	
						}
						if(subList.item(index).getNodeName().equalsIgnoreCase("div")){
							this.priceUSD=subList.item(index).getTextContent().trim();
						}
					}
					break;
				}
			}
		}
		if(position==3){
			
		}
	}
	
	@Override
	public String toString(){
		StringBuffer value=new StringBuffer();
		//value.append(" Url="+url);
		//value.append(" ImageUrl="+imageUrl);
		value.append(" Name="+name+"    Price:"+price);
		//value.append(" Description="+description);
		//value.append(" Price="+price);
		//value.append(" PriceUSD="+priceUSD);
		return value.toString();
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return this.getConcatString(url,100);
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return this.getConcatString(imageUrl,70);
	}

	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.getConcatString(name,70);
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.getConcatString(description,200);
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the price
	 */
	public Float getPrice() {
		try{
			int spaceIndex=this.price.indexOf(" ");
			return Float.parseFloat(this.price.substring(0,spaceIndex));
		}catch(Exception ex){
			return 0f;
		}
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * @return the priceUSD
	 */
	public Float getPriceUSD() {
		try{
			return Float.parseFloat(this.priceUSD.substring(1));
		}catch(Exception ex){
			return 0f;
		}
	}

	/**
	 * @param priceUSD the priceUSD to set
	 */
	public void setPriceUSD(String priceUSD) {
		this.priceUSD = priceUSD;
	}

	@Override
	public boolean equals(Object object) {
		if((object!=null)&&(object instanceof AvtozvukRecord)){
			AvtozvukRecord checkObject=(AvtozvukRecord)object;
			return (  checkObject.description.equals(description)
					&&checkObject.imageUrl.equals(imageUrl)
					&&checkObject.name.equals(name)
					&&checkObject.price.equals(price)
					&&checkObject.priceUSD.equals(priceUSD)
					&&checkObject.url.equals(url)
					);
		}else{
			return false;
		}
		
	}
	

}
