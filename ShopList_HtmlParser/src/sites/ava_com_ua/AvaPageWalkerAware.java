package sites.ava_com_ua;

import html_parser.page.PageWalkerAware;

/** �������� �������� �� ���������� */
public class AvaPageWalkerAware extends PageWalkerAware{
	/** ������� �������� */
	private String baseHref;
	private int startNumber=0;

	public void setStartNumber(int startNumber){
		this.startNumber=startNumber;
	}
	@Override
	public String getUrl(int number) {
		int returnPageNumber=number+startNumber;
		return baseHref+"p"+returnPageNumber+"/";
	}

	@Override
	public void reset(String baseHref) {
		this.baseHref=baseHref.trim();
		if(!this.baseHref.endsWith("/")){
			this.baseHref=this.baseHref+"/";
		}
	}

}
