package image_parser;

/** ������-�������� ��� ������ ���������� ����������� */
public class DelayImage {
	private int delayMs;
	/** ������-�������� ��� ������ ���������� ����������� */
	public DelayImage(int delayMs){
		this.delayMs=delayMs;
	}
	/** �������� �������� �� ������ � ������������ */
	public int getDelayMs(){
		return this.delayMs;
	}
}
