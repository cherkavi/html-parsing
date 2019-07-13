package html_parser.reader;

import java.io.IOException;
import java.io.Reader;

public interface ParserReader {
	/** получить поток байт из источника */
	public byte[] getBytes() throws IOException;
	/** получить поток байт из источника с указанной кодировкой CharSet*/
	public byte[] getBytes(String charsetName) throws IOException;
	/** получить Reader из которого нужно читать данные */
	public Reader getReader(String charsetName);
	/** закрыть Reader */
	public void closeReader();
	
}
