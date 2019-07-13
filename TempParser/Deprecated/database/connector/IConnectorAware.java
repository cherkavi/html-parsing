package database.connector;

/** объекты, реализующие данный интерфейс владеют соединением с базой данных*/
public interface IConnectorAware {
	/** получить соединение с базой данных */
	public ConnectWrap getConnector();
}
