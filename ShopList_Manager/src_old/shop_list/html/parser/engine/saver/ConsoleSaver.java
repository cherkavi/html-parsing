package shop_list.html.parser.engine.saver;

import shop_list.html.parser.engine.ESessionResult;
import shop_list.html.parser.engine.record.Record;

public class ConsoleSaver implements ISaver{

	@Override
	public boolean endSession(Integer sessionId, ESessionResult result) {
		System.out.println("SessionId:"+sessionId+"    Result:"+result);
		return true;
	}

	@Override
	public boolean saveRecord(Integer sessionId, 
							  Integer sectionId, 
							  Record record) {
		System.out.println("SessionId:"+sessionId+"    Section:"+sectionId+"   Record:"+record);
		return true;
	}

	@Override
	public Integer startNewSession(int shopId, String description) {
		return new Integer(1);
	}

	@Override
	public Integer getSectionId(String sectionName) {
		return 1;
	}

	@Override
	public int getShopId(String shopUrl) {
		return 0;
	}

}
