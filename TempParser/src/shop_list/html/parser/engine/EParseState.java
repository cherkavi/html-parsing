package shop_list.html.parser.engine;

/** ������������ ��������� ��������� ��������� �������� (�� �������� ������� ������� )
 * <table border="1">
 * 	<tr>
 * 		<td> READY(0) </td> <td> ������ � ������ </td>
 * 	</tr>
 * 	<tr>
 * 		<td> PROCESS(1) </td> <td> � �������� ������  </td>
 * 	</tr>
 * 	<tr>
 * 		<td> STOPPED(2) </td> <td> �����������  </td>
 * 	</tr>
 * 	<tr>
 * 		<td> DONE_OK(3) </td> <td> ��������� �������</td>
 * 	</tr>
 * 	<tr>
 * 		<td> DONE_ERROR(4) </td> <td> ��������� � ��������</td>
 * 	</tr>
 * </table>
 * */
public enum EParseState {
	/** ������ � ������  */
	READY(0),
	/** � �������� ������  */
	PROCESS(1),
	/** �����������  */
	STOPPED(2),
	/** ��������� ������� */
	DONE_OK(3),
	/** ��������� � �������� */
	DONE_ERROR(4);

	private int databaseKod=0;
	
	private EParseState(int kod){
		this.databaseKod=kod;
	}
	
	public int getKod(){
		return this.databaseKod;
	}
}
