package wicket_utility.mapper_pattern;

/** ���������� ���������� ����������� ��� ������ ���������  */
public interface IInterceptor {
	/** �������� ��������� �� �������� ���������  */
	public void getOutsideMessage(MapperMessage message);
	
	/** �������� ���������� ������������� ������������ */
	public String getInterceptorId();
}
