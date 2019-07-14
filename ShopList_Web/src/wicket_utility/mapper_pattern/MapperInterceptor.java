package wicket_utility.mapper_pattern;

/** ������-����������� ��� ������� */
public class MapperInterceptor {
	private IInterceptor interceptor;

	/** ������-����������� ��� ������� */
	public MapperInterceptor(IInterceptor interceptor){
		this.interceptor=interceptor;
	}

	/** �������� ������-������������ */
	public IInterceptor getInterceptor(){
		return this.interceptor;
	}
	
	// ��� ������� ArrayList ����� ���� �� �������������� � ��� hashCode
/*	@Override
	public int hashCode() {
		if(this.interceptor!=null){
			return this.interceptor.getInterceptorId().length();
		}else{
			return 0;
		}
	}
*/	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MapperInterceptor){
			try{
				return this.interceptor.getInterceptorId().equals( ((MapperInterceptor)obj).interceptor.getInterceptorId());
			}catch(Exception ex){
				return false;
			}
		}else if(obj instanceof String){
			try{
				return this.interceptor.getInterceptorId().equals((String)obj);
			}catch(Exception ex){
				return false;
			}
		}else{
			return false; 
		}
	}
	
}
