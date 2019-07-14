package wicket_utility.mapper_pattern;

import java.util.ArrayList;

/** ���������� ������� Mapper */
public class Mapper {
	private ArrayList<MapperInterceptor> interceptors=new ArrayList<MapperInterceptor>();
	
	/** �������� ����������� */
	public void addInterceptor(MapperInterceptor interceptor){
		if(this.interceptors.indexOf(interceptor)<0){
			this.interceptors.add(interceptor);
		}
	}
	
	/** ������� ��� ������������ �� ������� */
	public void clearInterceptors(){
		this.interceptors.clear();
	}
	
	/** ������� ��������� �� ����������� */
	public void sendMessageToInterceptor(MapperMessage message){
		if(message==null)return;
		int index=-1;
		for(int counter=0;counter<this.interceptors.size();counter++){
			if(this.interceptors.get(counter).equals(message.getTargetId())){
				index=counter;
				break;
			}
		}
		// int index=this.interceptors.indexOf(message.getTargetId());
		if(index<0)return;
		this.interceptors.get(index).getInterceptor().getOutsideMessage(message);
	}
	
}
