package process_exchange.server;

/** обработчик для входящих команд */
public interface ICommand {
	/** обработать входящую команду 
	 * @param value - входящая команда 
	 * @return - ответ на входящую команду
	 * <br>
	 * IMPORTANT: действия внутри нужно производить в потоко-защищенном режиме     
	 * */
	public String execute(String value);
}
