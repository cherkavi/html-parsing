package shops;

import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import shop_list.html.parser.engine.multi_page.ESectionEnd;
import shop_list.html.parser.engine.multi_page.section.INextSection;
import shop_list.html.parser.engine.multi_page.section_finder.ISectionFinder;
import shop_list.html.parser.engine.multi_page.standart.RecordListBaseMultiPage;
import shop_list.html.parser.engine.multi_page.section.NextSection;
import shop_list.html.parser.engine.multi_page.section_finder.section_extractor.IResourceFromElementExtractor;
import shop_list.html.parser.engine.record.Record;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.multi_page.section_finder.direct_finder.*;

public class mobitrade_ua extends RecordListBaseMultiPage {
	// "���������� ������������",
	// "������",
	// "���� � �����",
	// "����������",
	// "���������",
	// "������ � ��������",
	// "��� ��� �������",
	// "�������� �����",
	// "������ ��� �������",
	// "��� ��� �������",
	// "�����������������",
	// "������� �������",
	// "����������",
	// "������� ���������",
	// "������� ������",
	// "������ ��� ���",
	// http://www.mobitrade.ua/audio/headphone.html
	// http://www.mobitrade.ua/audio/headphone/2_12.html
	// http://www.mobitrade.ua/audio/headphone/3_12.html
	// 8 y.e. (64 ���)
	// "�������� ����������",
	// "������� � ��������� ���������",
	// "������ � ��������",
	// "�������� ������ � ���������",
	// "�������� ���������� ��� ���� �����",
	// "������������",
	// "������, ������, ����������",
	// "�������, �����",
	// "����������, �����, ������",
	// "������ �� ��� ���� �����",
	// "�������� ��� ������� ���������� � ������",
	// "������ ��� ���� �����",
	// "������ ��� ������",
	// "����� ��� ����������",
	// "���� � ���������",
	// "���������� � ���������",
	// "��������� ����������",
	// "�����",
	// "�����",
	// "�������",
	// "�����, �������, �����",
	// "����� ��� ��������� ������",
	// "�����, �������, �����",
	// "����� �������, �������� ����������",
	// "��������� ��� ���������",
	// "�������� ��� ���������",
	// "������������ �������",
	// "������� ����������",
	// "����������",
	// "�������",
	// "�����������",
	// "���� ��� ���������",
	// "������������ ������",
	// "���������� � ����������",
	// "������",
	// "��������� ���������",
	// "Skype �������",
	// "�������� �������",
	// "C������� �����",
	// "��������� �����",
	// "��������� ���������",
	// "��������� ����������",
	// "�������",
	// "������������ ����������",
	// "����������",
	// "������",
	// "���������",
	// "���������� ��� ���������� � �������",
	// "����",
	// "���� ����������",
	// "����������������",
	// "������ ��� ������",
	// "�������",
	// "�������� ������",
	// "���������",
	// "������ �����",
	// "������ ������",
	// "��������",
	// "�������� ������",
	// "������ ��� ������� � �������",
	// "������ ��� �������������",
	// "����� ��� �������",
	// "�������",
	// "��������",
	// "������ ��� �������",
	// "��������, �������",
	// "����",
	// "������",
	// "���������, ������������",
	// "����",
	// "�������� ��� �������",
	// "�����������",
	// "������� ��� �������",
	// "��������",
	// "������� ��� �������",
	// "������",
	// "����� ��� �������",
	// "�����",
	// "����-���������, ���������",
	// "������ ��� �������",
	// "���������������� ���������",
	// "������, �������, �����, �����",
	// "������������",
	// "��������� �����",
	// "������� ���������",
	// "��������, ���� ��������",
	// "�������� ������",
	// "������-c������",
	// "������ � ������",
	// "���������",
	// "������� �������",
	// "�������������",
	// "��������� ��� ����",
	// "����",
	// "������� �������� � ������ �������",
	// "������� �����������",
	// "���������, ������",
	// "���������� ��� ��������� ������",
	// "����",
	// "��������� ������",
	// "������� ������",
	// "����������, ��������",
	// "�����",
	// "�������� ������",
	// "�����, �������",
	// "������",
	// "����������� ������",
	// "���������",
	// "�������",
	// "������������� ������� � �������",
	// "���������� �������",
	// "��������",
	// "�������",
	// "�������",
	// "����������",
	// "��������������",
	// "��������� �����, ������",
	// "������������ �����",
	// "������ �����������",
	// "�����������, ���������� ������",
	// "�������� ������������",
	// "����������, �����������",
	// "����������� (����������) � ����������",
	// "�������������� �����������",
	// "������� ����������",
	// "�����������, �������� �������",
	// "�����, ����������, �����������",
	// "������������ � ������������ ������",
	// "���������",
	// "��������������",
	// "�������, ����, �������, ������� �� �������",
	// "���������� (������ 0+)",
	// "���������� (������ 0-1)",
	// "���������� (������ 1)",
	// "���������� (������ 1-3)",
	// "���������� (������ 2-3)",
	// "���������� (������ 3)",
	// "���������� � �����������",
	// "���������� (������ 1-2)",
	// "������� ������������",
	// "������������� �������",
	// "������������ �������",
	// "����������� �������",
	// "�������-������",
	// "���������",
	// "�����������",
	// "�����",
	// "������� ������",
	// "������ ��� ������",
	// "��������, �������",
	// "��������������� �������",
	// "������� ����������",
	// "�����",
	// "��������",
	// "������� ������",
	// "������� ��� ���������",
	// "���������� � ��������",
	// "������",
	// "��������� ��� ���������",
	// "������-�������",
	// "C���� ������� ��� �������",
	// "����������",
	// "�������������",
	// "�������, ��������",
	// "������� ����",
	// "������",
	// "���������� ���������� ����",
	// "����� � ����������",
	// "����������",
	// "�������"
	@Override
	public String getShopUrlStartPage() {
		return "http://www.mobitrade.ua";
	}

	@Override
	protected String getCharset() {
		return "windows-1251";
	}

	@Override
	protected ISectionFinder getSectionFinder() throws Exception {
		// import org.w3c.dom.Node;
		// import
		// shop_list.html.parser.engine.multi_page.section_finder.NodeListFinderByUrl;
		// ArrayList<Node> listOfNode=(new NodeListFinderByUrl(parser,
		// getCharset(),
		// "/html/body/div[4]/div/div[2]/div[3]/table/tbody/tr[*]/td[@width="25%"]/div/div/ul/li[*]/a")).getNodeListByUrl(this.getShopUrlStartPage());
		
		ArrayList<Node> listOfNode = this.parser
				.getNodeListFromUrl(
						"http://www.mobitrade.ua",
						"windows-1251",
						"/html/body/div[@id=\"wrap\"]/div[@id=\"content\"]/div[@id=\"left-shadow\"]/div[@id=\"right-shadow\"]/div[@id=\"overflow\"]/div[@id=\"menu-parse\"]/div[@class=\"menu-cont\"]/div[@id=\"mm\"]/div/table[@id=\"main-menu-slide\"]/tbody/tr/td[*]/div[@class=\"simple main-menu-item \"]/div[@class=\"sub-main-menu\"]/div[@class=\"sub-menu-container\"]/div[@class=\"menu-categories\"]/div[@class=\"lines\"]/a");
		removeNodeFromListByTextContent(listOfNode, new
		String[]{
				"�������� ����������",
				"������� � ��������� ���������",
				"������ � ��������",
				"�������� ������ � ���������",
				"�������� ���������� ��� ���� �����",
				"������������",
				"������, ������, ����������",
				"�������, �����",
				"����������, �����, ������",
				"������ �� ��� ���� �����",
				"�������� ��� ������� ���������� � ������",
				"������ ��� ���� �����",
				"������ ��� ������",
				"����� ��� ����������",
				"���� � ���������",
				"���������� � ���������",
				"��������� ����������",
				"�����",
				"�����",
				"�������",
				"�����, �������, �����",
				"����� ��� ��������� ������",
				"�����, �������, �����",
				"����� �������, �������� ����������",
				"��������� ��� ���������",
				"�������� ��� ���������",
				"������������ �������",
				"������� ����������",
				"����������",
				"�������",
				"�����������",
				"���� ��� ���������",
				"������������ ������",
				"���������� � ����������",
				"������",
				"��������� ���������",
				"Skype �������",
				"�������� �������",
				"C������� �����",
				"��������� �����",
				"��������� ���������",
				"��������� ����������",
				"�������",
				"������������ ����������",
				"����������",
				"������",
				"���������",
				"���������� ��� ���������� � �������",
				"����",
				"���� ����������",
				"����������������",
				"������ ��� ������",
				"�������",
				"�������� ������",
				"���������",
				"������ �����",
				"������ ������",
				"��������",
				"�������� ������",
				"������ ��� ������� � �������",
				"������ ��� �������������",
				"����� ��� �������",
				"�������",
				"��������",
				"������ ��� �������",
				"��������, �������",
				"����",
				"������",
				"���������, ������������",
				"����",
				"�������� ��� �������",
				"�����������",
				"������� ��� �������",
				"��������",
				"������� ��� �������",
				"������",
				"����� ��� �������",
				"�����",
				"����-���������, ���������",
				"������ ��� �������",
				"���������������� ���������",
				"������, �������, �����, �����",
				"������������",
				"��������� �����",
				"������� ���������",
				"��������, ���� ��������",
				"�������� ������",
				"������-c������",
				"������ � ������",
				"���������",
				"������� �������",
				"�������������",
				"��������� ��� ����",
				"����",
				"������� �������� � ������ �������",
				"������� �����������",
				"���������, ������",
				"���������� ��� ��������� ������",
				"����",
				"��������� ������",
				"������� ������",
				"����������, ��������",
				"�����",
				"�������� ������",
				"�����, �������",
				"������",
				"����������� ������",
				"���������",
				"�������",
				"������������� ������� � �������",
				"���������� �������",
				"��������",
				"�������",
				"�������",
				"����������",
				"��������������",
				"��������� �����, ������",
				"������������ �����",
				"������ �����������",
				"�����������, ���������� ������",
				"�������� ������������",
				"����������, �����������",
				"����������� (����������) � ����������",
				"�������������� �����������",
				"������� ����������",
				"�����������, �������� �������",
				"�����, ����������, �����������",
				"������������ � ������������ ������",
				"���������",
				"��������������",
				"�������, ����, �������, ������� �� �������",
				"���������� (������ 0+)",
				"���������� (������ 0-1)",
				"���������� (������ 1)",
				"���������� (������ 1-3)",
				"���������� (������ 2-3)",
				"���������� (������ 3)",
				"���������� � �����������",
				"���������� (������ 1-2)",
				"������� ������������",
				"������������� �������",
				"������������ �������",
				"����������� �������",
				"�������-������",
				"���������",
				"�����������",
				"�����",
				"������� ������",
				"������ ��� ������",
				"��������, �������",
				"��������������� �������",
				"������� ����������",
				"�����",
				"��������",
				"������� ������",
				"������� ��� ���������",
				"���������� � ��������",
				"������",
				"��������� ��� ���������",
				"������-�������",
				"C���� ������� ��� �������",
				"����������",
				"�������������",
				"�������, ��������",
				"������� ����",
				"������",
				"���������� ���������� ����",
				"����� � ����������",
				"����������",
				"�������"
				});
		
		return new DirectFinder(listOfNode,
				new IResourceFromElementExtractor() {
					@Override
					public INextSection getResourceSection(Element element) {
						String url = addHttpPreambula(
								"http://www.mobitrade.ua", element
										.getAttribute("href"));
						return new UserSection(element.getTextContent(), url);
					}
				});
	}

	class UserSection extends NextSection {
		public UserSection(String name, String url) {
			super(name, url);
		}

		private int pageCounter = 0;
		private String preambula=null;
		
		@Override
		public String getUrlToNextPage() {
			pageCounter++;
			if (pageCounter > 1) {
				// http://www.mobitrade.ua/audio/headphone.html
				// http://www.mobitrade.ua/audio/headphone/2_12.html
				// http://www.mobitrade.ua/audio/headphone/3_12.html
				if(preambula==null){
					int index=this.getUrl().lastIndexOf('.');
					preambula=this.getUrl().substring(0, index)+"/";
				}
				return preambula+pageCounter+"_12.html";
			}
			return this.getUrl();
		}
	}


	private ESectionEnd[] conditions = new ESectionEnd[] {
			ESectionEnd.NEXT_RECORDS_REPEAT_LAST,
			ESectionEnd.NEXT_RECORDS_LOAD_ERROR,
			ESectionEnd.NEXT_RECORDS_ZERO_SIZE };

	@Override
	protected ESectionEnd[] getSectionEndConditions() {
		return conditions;
	}

	@Override
	protected String getXmlPathToDataBlock() {
		return "/HTML/BODY/DIV[@id=\"wrap\"]/DIV[@id=\"content\"]/DIV[@id=\"left-shadow\"]/DIV[@id=\"right-shadow\"]/DIV[@id=\"overflow\"]/TABLE/TBODY/TR/TD[2]/DIV[@class=\"main-width\"]/DIV[@id=\"main\"]/DIV[@class=\"ncatalog\"]/DIV[@class=\"items\"]";
	}

	@Override
	protected String getXmlPathToRecordListFromDataBlock() {
		return "/DIV[*]";
	}

	@Override
	protected String recordFromNodeIsPresent() {
		return null;
	}

	@Override
	protected String recordFromNodeIsPresentText() {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToName() {
		return "/DIV[@class=\"ttl2\"]/A";
	}

	@Override
	protected String recordFromNodeInRecordToUrl() {
		return "/DIV[@class=\"ttl2\"]/A";
	}

	@Override
	protected String getAttributeForExtractRecordUrl() {
		return "href";
	}

	@Override
	protected boolean recordIsRemoveStartPageFromUrl() {
		return false;
	}

	@Override
	protected String recordFromNodeInRecordToPrice() {
		return "/DIV[@class=\"actions\"]/DIV[@class=\"price\"]/NOBR";
	}

	@Override
	protected String recordFromNodeInRecordToPriceBeforeConvert(String priceText) {
		// 8 y.e. (64 ���)
		int index=priceText.indexOf('(');
		if(index>0){
			return priceText.substring(index).replaceAll("[^0-9]", "");
		}else{
			return "";
		}
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsd() {
		return "/DIV[@class=\"actions\"]/DIV[@class=\"price\"]/NOBR";
	}

	@Override
	protected String recordFromNodeInRecordToPriceUsdBeforeConvert(
			String priceText) {
		// 8 y.e. (64 ���)
		int index=priceText.indexOf('(');
		if(index>0){
			return priceText.substring(0, index).replaceAll("[^0-9]", "");
		}else{
			return "";
		}
	}

	@Override
	protected String recordFromNodeInRecordToPriceEuro() {
		return null;
	}

	@Override
	protected String recordFromNodeInRecordToPriceEuroBeforeConvert(
			String priceText) {
		return null;
	}

	@Override
	protected Record prepareRecordBeforeSave(Record record)
			throws EParseException {
		return record;
	}
}