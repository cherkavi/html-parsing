package sites.autozvuk_com_ua;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import html_parser.page.PageWalker;
import html_parser.record.Record;

public class AvtozvukPageWalker extends PageWalker {

	public AvtozvukPageWalker(){
	}

	@Override
	protected Node getNodeFromPage(String url) {
		return this.getXmlNodeFromUrl(url, "windows-1251", "/html/body/table/tbody/tr[4]/td[2]/table/tbody/tr/td/table[2]/tbody/tr[2]/td/table[3]/tbody");
	}

	@Override
	protected ArrayList<Record> getRecordsFromNode(Node node) {
		if((node!=null)&&(node.hasChildNodes())){
			NodeList list=node.getChildNodes();
			if(list.getLength()>2){
				ArrayList<Record> returnValue=new ArrayList<Record>();
				// есть позиции в сформированном листе - обрабатываем
				int passElements=2;
				for(int counter=0;counter<list.getLength();counter++){
					if(list.item(counter) instanceof Element){
						if(passElements<=0){
							returnValue.add(new AvtozvukRecord(list.item(counter)));
						}
						passElements--;
					}
				}
				return returnValue;
			}else{
				// нет позиций в сформированном листе 
				return null;
			}
		}else{
			// нет данных или данные не опознаны 
			return null;
		}
	}
	

	
}
