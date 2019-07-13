package database.wrap_mysql;

/** ��������� ���������� �������� �� �������� ������ ��������  */
public enum EActionState {
	/** ������� �������� */
	DONE(1),
	/** �������� � ��������  */
	ERROR(2),
	/** � �������� ����������  */
	IN_PROCESS(3),
	/** �����  */
	NEW(4),
	/** ������������� */
	STOPPED(5);
	
	private int kod=0;
	
	/** ��������� ���������� �������� */
	private EActionState( int kod){
		this.kod=kod;
	}
	
	/** �������� �������� ��� ���� ������  */
	public int getKod(){
		return this.kod;
	}
}
