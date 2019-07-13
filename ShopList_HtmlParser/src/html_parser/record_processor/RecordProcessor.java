package html_parser.record_processor;

import html_parser.record.Record;

import java.util.ArrayList;

/** ������, ������� ������ ��� ��������������/���� ��������� ����������� ����� �������*/
public abstract class RecordProcessor {
	/** �����, �������� ���������� ���������� �� �������� ������ <b>�����</b> ����������� */
	public abstract void beforeSave(ArrayList<Record> list);

	/** �����, �������� ���������� ���������� �� �������� ������ <b>�����</b> ����������� */
	public abstract void afterSave(ArrayList<Record> block);
}
