package image_parser;

import java.io.InputStream;

/** ������, ������� ������� ����������� � ��������� ������ �� ��������� ����������� */
public abstract class ImagePathAware {
	/** ���������� ������ �� ��������� �����������, ������� ����� �������� 
	 * @return 
	 * <li> <b>null</b> -  ������ ��� ������ </li>
	 * <li> <b>String</b> -  ������ �� ��������� ��������  </li>
	 * */
	public abstract String getNextUrl();
	/** ��������� ��������� ���������� URL */
	public abstract boolean saveLastGetUrl(InputStream inputStream);

	/** ��������� ��������� ���������� URL ��� ������ ������ */
	public abstract boolean saveLastGetUrlAsError();
	
	/** ������ �������� ���������� */
	public abstract  boolean begin();
	
	/** ����� �������� ���������� */
	public abstract boolean end();
}
