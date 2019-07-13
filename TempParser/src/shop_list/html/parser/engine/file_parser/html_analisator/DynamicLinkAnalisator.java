package shop_list.html.parser.engine.file_parser.html_analisator;

import org.w3c.dom.Element;

import org.w3c.dom.Node;

import shop_list.html.parser.engine.ECharset;
import shop_list.html.parser.engine.parser.EParserType;
import shop_list.html.parser.engine.parser.IParser;
import shop_list.html.parser.engine.parser.MozillaAlternativeParser;
import shop_list.html.parser.engine.parser.NekoParser;

/** ������, ������� ����������� �������� � ������� �� ���������� XPath �������, � ������� ���� URL �� ��������  */
public class DynamicLinkAnalisator {
	protected IParser parser;
	
	public DynamicLinkAnalisator(){
		parser=new MozillaAlternativeParser(null);
	}
	
	public DynamicLinkAnalisator(EParserType parserType){
		switch(parserType){
		case Mozilla:{parser=new MozillaAlternativeParser(null);};break;
		case Neko:{parser=new NekoParser();};break;
		}
	}
	
	/** 
	 * �������� HREF �� ���������� ��������
	 * @param url - ������ ���� � �������� 
	 * @param charset - {@link ECharset} - 
	 * @param xpath - ������ XPath �� �������� 
	 * @return 
	 * <ul>
	 * 	<li><b>null</b> - �� ������� �������� ������� </li>
	 * 	<li><b>String</b> - ������� �������� </li>
	 * </ul>
	 */
	public String getUrlFromPageFromElement(String url, String charset, String xpath){
		try{
			Node node=parser.getNodeFromUrl(url, charset, xpath);
			return ((Element)node).getAttribute("href");
		}catch(Exception ex){
			return null;
		}
	}
	
	public static void main(String[] args){
		System.out.println("begin");
		DynamicLinkAnalisator analisator=new DynamicLinkAnalisator();
		System.out.println(""+analisator.getUrlFromPageFromElement("http://www.gb.ua/price/price.asp", ECharset.WIN_1251.getName(), "/html/body/table/tbody/tr[2]/td[2]/table[3]/tbody/tr/td/table/tbody/tr/td/p/a"));
		System.out.println("-end-");
	}
}
