package html_parser;

import html_parser.record.Record;

/** ����������� �����, ������� ������ ��� ���������� ������� */
public abstract class Saver {
	/** �������� ��� ������ ��� ������ ������ */
	public abstract boolean resetAllRecord();
	/** ��������� ������� ������ */
	public abstract boolean save(String sectionName, Record record);
	/** ��������� ������ ���������� */
	public abstract boolean finish();
	/** ������ ������ ������ - ��������� */
	public abstract boolean begin();
}
