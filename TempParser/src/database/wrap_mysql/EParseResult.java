package database.wrap_mysql;

/** ��������� �������� �������� ������ ������  */
public enum EParseResult {
	ERROR(1), OK(2), STOPPED(3), NEW(4);
	
	private int id;
	
	private EParseResult(int value) {
		this.id=value;
	}
	
	/** �������� ���������� ��� �� ��������� ���� ������ */
	public int getId(){
		return this.id;
	}
}
