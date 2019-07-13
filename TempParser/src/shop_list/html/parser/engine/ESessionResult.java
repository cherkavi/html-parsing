package shop_list.html.parser.engine;

/** ��������� ��������  ( PARSE_RESULT )
 * <ul>
 * 	<li> <b>ok</b> - ������� �������� </li>
 * 	<li> <b>error</b> - ������ ���������� </li>
 * </ul> 
 * */
public enum ESessionResult {
	/** ���������� */
	stopped(3),
	/** ������� ������� */
	ok(2),
	/** ������ �������� */
	error(1);

	private int kod;
	
	private ESessionResult(int databaseKod){
		this.kod=databaseKod;
	}
	/** ��� ��� ������ � ������� ���� ������  */
	public int getDatabaseKod(){
		return this.kod;
	}
}
