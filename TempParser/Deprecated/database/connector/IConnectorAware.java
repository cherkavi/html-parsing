package database.connector;

/** �������, ����������� ������ ��������� ������� ����������� � ����� ������*/
public interface IConnectorAware {
	/** �������� ���������� � ����� ������ */
	public ConnectWrap getConnector();
}
