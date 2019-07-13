package shop_list.html.parser.engine;

/** перечисления возможных вариантов состояния парсинга (по отдельно взятому парсеру )
 * <table border="1">
 * 	<tr>
 * 		<td> READY(0) </td> <td> готово к работе </td>
 * 	</tr>
 * 	<tr>
 * 		<td> PROCESS(1) </td> <td> в процессе работы  </td>
 * 	</tr>
 * 	<tr>
 * 		<td> STOPPED(2) </td> <td> остановлено  </td>
 * 	</tr>
 * 	<tr>
 * 		<td> DONE_OK(3) </td> <td> выполнено успешно</td>
 * 	</tr>
 * 	<tr>
 * 		<td> DONE_ERROR(4) </td> <td> выполнено с ошибками</td>
 * 	</tr>
 * </table>
 * */
public enum EParseState {
	/** готово к работе  */
	READY(0),
	/** в процессе работы  */
	PROCESS(1),
	/** остановлено  */
	STOPPED(2),
	/** выполнено успешно */
	DONE_OK(3),
	/** выполнено с ошибками */
	DONE_ERROR(4);

	private int databaseKod=0;
	
	private EParseState(int kod){
		this.databaseKod=kod;
	}
	
	public int getKod(){
		return this.databaseKod;
	}
}
