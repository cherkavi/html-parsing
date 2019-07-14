package wicket_utility.mapper_pattern;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

/** ���������, ������� ����� ���� �������������� ����� ������������ ������� ������� Mapper */
public class MapperMessage implements Serializable{
	private final static long serialVersionUID=1L;
	/** ������������� �������  */
	private String targetId;
	/** ��������� �������-������������ */
	private Object parameters;
	/** ���� ������� Ajax */
	private AjaxRequestTarget ajaxTarget;
	/** ������������ ����� � ��������� (����� ��� ��������� ��������� � ������ Interceptor ) */
	private String optionName;
	
	/** ���������, ������� ����� ���� �������������� ����� ������������ ������� ������� Mapper 
	 * @param targetId - ������������� ������� ������������ 
	 * @param parameters - ��������� 
	 */
	public MapperMessage(String targetId, Object parameters){
		this(targetId, (String)null, parameters);
	}
	
	/** ���������, ������� ����� ���� �������������� ����� ������������ ������� ������� Mapper 
	 * @param targetId - ������������� ������� ������������
	 * @param optionName - name of option 
	 * @param parameters - ��������� 
	 */
	public MapperMessage(String targetId, String optionName, Object parameters){
		this.targetId=targetId;
		this.optionName=optionName;
		this.parameters=parameters;
	}

	/** ���������, ������� ����� ���� �������������� ����� ������������ ������� ������� Mapper 
	 * @param targetId - ������������� ������� ������������
	 * @param ajaxTarget - ���� Ajax ������� 
	 * @param parameters - ��������� 
	 */
	public MapperMessage(String targetId, AjaxRequestTarget ajaxTarget, Object parameters){
		this(targetId, ajaxTarget, null, parameters);
	}
	
	/** ���������, ������� ����� ���� �������������� ����� ������������ ������� ������� Mapper 
	 * @param targetId - ������������� ������� ������������
	 * @param ajaxTarget - ���� Ajax �������
	 * @param optionName - ��� �����  
	 * @param parameters - ��������� 
	 */
	public MapperMessage(String targetId, AjaxRequestTarget ajaxTarget, String optionName, Object parameters){
		this.targetId=targetId;
		this.ajaxTarget=ajaxTarget;
		this.optionName=optionName;
		this.parameters=parameters;
	}
	
	/** �������� ���������� ������������� */
	public String getTargetId() {
		return targetId;
	}
	
	/**  �������� ��������� */
	public Object getParameters() {
		return parameters;
	}

	/** �������� ��� ����� ��� Interceptor */
	public String getOptionName(){
		return this.optionName;
	}
	
	/** �������� ���� ������� Ajax */
	public AjaxRequestTarget getAjaxTarget() {
		return ajaxTarget;
	}

	/** �������� �� ��������� ����� Ajax ������� */
	public boolean isAjaxRequest(){
		return this.ajaxTarget!=null;
	}
	
}
