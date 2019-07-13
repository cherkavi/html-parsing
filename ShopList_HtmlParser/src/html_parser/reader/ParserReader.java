package html_parser.reader;

import java.io.IOException;
import java.io.Reader;

public interface ParserReader {
	/** �������� ����� ���� �� ��������� */
	public byte[] getBytes() throws IOException;
	/** �������� ����� ���� �� ��������� � ��������� ���������� CharSet*/
	public byte[] getBytes(String charsetName) throws IOException;
	/** �������� Reader �� �������� ����� ������ ������ */
	public Reader getReader(String charsetName);
	/** ������� Reader */
	public void closeReader();
	
}
