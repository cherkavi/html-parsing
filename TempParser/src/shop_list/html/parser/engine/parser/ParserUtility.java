package shop_list.html.parser.engine.parser;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ParserUtility {
	
	public static boolean debug=false;

	protected int getChildElementCount(Node node, String childTagName) {
		int returnValue=0;
		if((node!=null)&&(node instanceof Element)&&( ((Element)node).hasChildNodes() )){
			NodeList list=((Element)node).getChildNodes();
			for(int counter=0;counter<list.getLength();counter++){
				Node currentNode=list.item(counter);
				if((currentNode!=null)&&(currentNode instanceof Element)){
					if(((Element)currentNode).getTagName().equalsIgnoreCase(childTagName)){
						returnValue++;
					}
				}
			}
		}else{
			returnValue=0;
		}
		return returnValue;
	}
	
	/** является ли XPath списком (то есть подразумевается ли множество значений ) */
	private boolean isXPathAsList(String xpath){
		return ((xpath.indexOf('*')>0)||(xpath.indexOf('@')>0));
	}
	
	
	private Node getNodeFromDocumentByStringTokenizer(Node document, StringTokenizer token){
		Node currentNode=document;
		String path=null;
		main_cycle:while(token.hasMoreTokens()){
			String nextElement=processToken(currentNode, token.nextToken().trim());
			if(nextElement.length()==0)continue;
			if(debug==true){
				if(path==null){
					path=nextElement;
				}
				path=path+"/"+nextElement;
				System.out.println("nextElement:"+path);
			}
			int searchIndex=1;
			if(nextElement.indexOf('[')>0){
				searchIndex=Integer.parseInt(nextElement.substring(nextElement.indexOf('[')+1,nextElement.indexOf(']')));
				nextElement=nextElement.substring(0,nextElement.indexOf('['));
			}
			if(currentNode.hasChildNodes()){
				// есть элементы, проверка на имя в XPath пути
				int findIndex=0; // индекс в поиске /tr[3] - третий элемент с заданным именем
				NodeList list=currentNode.getChildNodes();
				for(int counter=0;counter<list.getLength();counter++){
					if(debug==true)System.out.println("   "+list.item(counter).getNodeName());
					if(list.item(counter).getNodeName().equalsIgnoreCase(nextElement)){
						//System.out.println(list.item(counter).getNodeName()+"==="+nextElement);
						findIndex++;
						if(findIndex==searchIndex){
							currentNode=list.item(counter);
							continue main_cycle;
						}
					}
				}
				// элемент не найден по указанному пути
				// INFO вывод ошибочного значения  
				if(debug==true){
					
					if(currentNode instanceof Element){
						String id=((Element)currentNode).getAttribute("id");
						System.out.println("CurrentNode: "+nextElement+"   >>> "+id);
					}else{
						System.out.println("CurrentNode: "+nextElement);
					}
					NodeList childsList=currentNode.getChildNodes();
					for(int counter=0;counter<childsList.getLength();counter++){
						if(childsList.item(counter) instanceof Element){
							System.out.println(counter+" : "+((Element)childsList.item(counter)).getNodeName()+"  id:"+((Element)childsList.item(counter)).getAttribute("id"));
						}
					}
					System.out.println("currentNode#getTextContent: "+currentNode.getTextContent());
				}
				currentNode=null;
				break main_cycle;
			}else{
				// нет элементов, а путь еще есть
				return null;
			}
		}
		return currentNode;
	}
	
	
	/** преобразовать Node в String */
	protected String getStringFromXmlDocument(Node document){
		Writer out=null;
		try{
			javax.xml.transform.TransformerFactory transformer_factory = javax.xml.transform.TransformerFactory.newInstance();  
			javax.xml.transform.Transformer transformer = transformer_factory.newTransformer();  
			javax.xml.transform.dom.DOMSource dom_source = new javax.xml.transform.dom.DOMSource(document); // Pass in your document object here  
			out=new StringWriter();
			//string_writer = new Packages.java.io.StringWriter();  
			javax.xml.transform.stream.StreamResult stream_result = new javax.xml.transform.stream.StreamResult(out);  
			transformer.transform(dom_source, stream_result);  
		}catch(Exception ex){
			System.err.println("getStringFromXmlDocument:"+ex.getMessage());
		}
		return (out==null)?"":out.toString();
	}

	/** INFO исправление глюков XPath - в случае, когда не берет путь из XPath */
	protected  Node getNodeWithDataBlockAlternative(Node node, String xpathToBlockData)  {
		if(isXPathAsList(xpathToBlockData)){
			ArrayList<Node> list=new ArrayList<Node>();
			if(node!=null){
				this.getNodeListByTokenList(list, 
													node, 
													this.getArrayListFromTokens(new StringTokenizer(xpathToBlockData.replaceAll("//", "/"),"/")));
			}
			if(list.size()>0){
				return list.get(0);
			}else{
				return null;
			}
		}else{
			StringTokenizer token=new StringTokenizer(xpathToBlockData.trim().replaceAll("//", "/"),"/");
			Node returnValue=null;
			returnValue=(Node)getNodeFromDocumentByStringTokenizer(node, token);
			return returnValue;
		}
	}
	

	
	/** на основании поискового элемента получить порядковый номер искомого тэга (div[4] => 4)*/
	private int getFindTokenElementNumber(String findToken){
		int indexBegin=findToken.indexOf('[');
		if(indexBegin<0)return 1;
		return Integer.parseInt(findToken.substring(indexBegin+1,findToken.indexOf(']', indexBegin+1)));
	}
	
	/** на основании поискового элемента получить поисковый аттрибут div[@id="find value"] => id */
	private String getFindTokenAttributeName(String findToken){
		if(findToken==null)return "";
		int indexBegin=findToken.indexOf('@');
		if(indexBegin<0)return "";
		return findToken.substring(indexBegin+1,findToken.indexOf('=', indexBegin+1));
	}

	/** на основании поискового элемента получить значение поискового аттрибута div[@id="find value"] => find value */
	private String getFindTokenAttributeValue(String findToken){
		if(findToken==null)return "";
		int indexBegin=findToken.indexOf("=\"");
		if(indexBegin<0)return "";
		int indexEnd=findToken.indexOf('\"', indexBegin+2);
		return findToken.substring(indexBegin+2,indexEnd);
	}

	private String getTagNameFromToken(String findToken){
		if(findToken==null)return "";
		int indexBegin=findToken.indexOf('[');
		if(indexBegin<0)return findToken;
		return findToken.substring(0, indexBegin);
	}
	
	
	/** у переданного Node проверить наличие аттрибута и его эквивалентность на переданное значение  
	 * @param item - Element
	 * @param attributeName - имя аттрибута 
	 * @param attributeValue - значение аттрибута
	 * @return
	 */
	private boolean isNodeAttributeEquals(Node item, String attributeName, String attributeValue) {
		boolean returnValue=false;
		if(item instanceof Element){
			String value=((Element)item).getAttribute(attributeName);
			if(value!=null){
				if(debug==true)System.out.println("Attr equals:"+value+" <> "+attributeValue);
				returnValue=value.equalsIgnoreCase(attributeValue);
			}
		}
		return returnValue;
	}
	
	protected ArrayList<String> getArrayListFromTokens(StringTokenizer tokens){
		ArrayList<String> returnValue=new ArrayList<String>();
		while(tokens.hasMoreElements()){
			returnValue.add(tokens.nextToken());
		}
		return returnValue;
	}
	
	private ArrayList<String> getListAfterIndex(ArrayList<String> tokens, int beginIndexExclude){
		ArrayList<String> returnValue=new ArrayList<String>(tokens.size()-beginIndexExclude);
		for(int index=beginIndexExclude+1;index<tokens.size();index++){
			returnValue.add(tokens.get(index));
		}
		return returnValue;
	}
	
	private ArrayList<String> removeEmptyTokens(ArrayList<String> list){
		ArrayList<String> returnValue=new ArrayList<String>(list.size());
		for(int counter=0;counter<list.size();counter++){
			String currentValue=list.get(counter);
			if(currentValue!=null){
				currentValue=currentValue.trim();
				if(!currentValue.equals("")){
					returnValue.add(currentValue);
				}
			}
		}
		return returnValue;
	}

	/** обработать Token на наличие в нем возможной последовательности: tr[0$] - последний Tag tr, tr[1$] - после последнего tr  */
	private String processToken(Node currentNode, String token){
		int indexSquare=token.indexOf('[');
		if(indexSquare>0){
			int indexEnd=token.indexOf("$", indexSquare);
			if(indexEnd>0){
				String tagName=getTagNameFromToken(token);
				// найдена последовательность для получения "с конца" указанного Node
				int count=getChildElementCount(currentNode, tagName);
				int index=Integer.parseInt(token.substring(indexSquare+1, indexEnd));
				if(index<count){
					return tagName+"["+(count-index)+"]";
				}else{
					return tagName+"["+(count+1)+"]";
				}
			}else{
				return token;
			}
		}else{
			return token;
		}
	}
	
	/** получить список необходимых элементов на основании разделенного на отдельные Token-ы XPath пути */
	protected void getNodeListByTokenList(ArrayList<Node> returnValue, Node rootNode, ArrayList<String> tokens){
		if(returnValue==null)assert false:"#getNodeListNodeByStringTokenizer nodeResultList is null";
		Node currentNode=rootNode;
		boolean flagEnd=false;
		tokens=this.removeEmptyTokens(tokens);
		main_cycle:for(int indexMain=0;indexMain<tokens.size();indexMain++){
			String findToken=processToken(currentNode, tokens.get(indexMain));
			if(debug)System.out.println("->"+findToken);
			flagEnd=indexMain==(tokens.size()-1);
			
			if(findToken.indexOf('[')>0){
				// complex search
				if(findToken.indexOf('@')>0){
					// search by attribute
					String attributeName=this.getFindTokenAttributeName(findToken);
					String attributeValue=this.getFindTokenAttributeValue(findToken);
					String tagName=this.getTagNameFromToken(findToken);
					NodeList list=((NodeList)currentNode);
					boolean findIt=false;
					for(int index=0;index<list.getLength();index++){
						if((list.item(index) instanceof Element)){
							Element tempElement=(Element)list.item(index);
							if(debug)System.out.println("Element:"+tempElement.getTagName()+"  class:"+tempElement.getAttribute("class") );
							if(   tempElement.getTagName().equalsIgnoreCase(tagName) 
								&&this.isNodeAttributeEquals(tempElement, attributeName, attributeValue)){
								findIt=true;
								if(flagEnd==true){
									returnValue.add(list.item(index));
								}else{
									this.getNodeListByTokenList(returnValue, list.item(index), this.getListAfterIndex(tokens, indexMain));
								}
							}
						}
					}
					if(findIt==false)return;
				}else{
					if(findToken.indexOf('*')>0){
						// search all elements
						if(currentNode instanceof NodeList){
							NodeList list=((NodeList)currentNode);
							String tagName=this.getTagNameFromToken(findToken);
							boolean findIt=false;
							for(int index=0;index<list.getLength();index++){
								if(   ( list.item(index) instanceof Element)
									&&( ((Element)list.item(index)).getTagName().equalsIgnoreCase(tagName) )){
									if(flagEnd){
										returnValue.add(list.item(index));
									}else{
										this.getNodeListByTokenList(returnValue, list.item(index), this.getListAfterIndex(tokens, indexMain));
										findIt=true;
									}
								}
							}
							if(findIt==false)return;
						}else{
							// list not consists children
							return;
						}
					}else{
						// search by number
						int count=getFindTokenElementNumber(findToken);
						if(currentNode instanceof NodeList){
							NodeList list=((NodeList)currentNode);
							String tagName=this.getTagNameFromToken(findToken);
							for(int index=0;index<list.getLength();index++){
								if((list.item(index) instanceof Element)&&( ((Element)list.item(index)).getTagName().equalsIgnoreCase(tagName) )){
									count--;
									if(count==0){
										if(flagEnd){
											returnValue.add(list.item(index));
											return;
										}else{
											currentNode=list.item(index);
											continue main_cycle;
										}
									}
								}
							}
							// not found 
							return;
						}else{
							// list not consists children
							return;
						}
					}
				}
			}else{
				// simple search
				if(currentNode instanceof NodeList){
					NodeList list=((NodeList)currentNode);
					for(int index=0;index<list.getLength();index++){
						if((list.item(index) instanceof Element)&&( ((Element)list.item(index)).getTagName().equalsIgnoreCase(findToken) )){
							if(flagEnd){
								returnValue.add(list.item(index));
								// return; - maybe multiply node
							}else{
								currentNode=list.item(index);
								continue main_cycle;
							}
						}
					}
					// not found 
					return;
				}else{
					// list not consists children
					return;
				}
			}
		}
	}
	
}
