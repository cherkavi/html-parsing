package database;

/** ��������� ���������� �������� �� �������� ������ ��������  */
public enum EActionState {
	/** � �������� ����������  */
	IN_PROCESS(0),
	/** ������� �������� */
	DONE(1),
	/** �������� � ��������  */
	ERROR(2),
	/** ����������  */
	STOPPED(3);
	
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
