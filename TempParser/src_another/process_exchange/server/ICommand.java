package process_exchange.server;

/** ���������� ��� �������� ������ */
public interface ICommand {
	/** ���������� �������� ������� 
	 * @param value - �������� ������� 
	 * @return - ����� �� �������� �������
	 * <br>
	 * IMPORTANT: �������� ������ ����� ����������� � ������-���������� ������     
	 * */
	public String execute(String value);
}
