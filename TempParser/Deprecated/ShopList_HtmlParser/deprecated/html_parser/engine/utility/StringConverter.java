package html_parser.engine.utility;

/** интерфейс для преобразования строки */
public interface StringConverter {
	/** преобразовать строку по определенному правилу */
	public String convertString(String value);
}
