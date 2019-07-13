package shop_list.html.parser.engine.logger;

public enum ELoggerLevel {
	TRACE(0),
	DEBUG(1),
	INFO(2),
	WARN(3),
	ERROR(4);
	
	private int kod;
	
	private ELoggerLevel(int kod){
		this.kod=kod;
	}
	
	/** �������� ��� ������������ ��� ������� ������ ������������ */
	public int getKod(){
		return this.kod;
	}
	
	/** �������� �� ������������ ������� ������ ��� ������� */
	public boolean isOutput(ELoggerLevel level){
		if(level==null){
			return true;
		}else{
			if(level.kod<kod){
				return false;
			}else{
				return true;
			}
		}
	}
}
