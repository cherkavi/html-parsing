package wicket_utility.mapper_pattern;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

/** сообщение, которое может быть перенаправлено между независимыми частями шаблона Mapper */
public class MapperMessage implements Serializable{
	private final static long serialVersionUID=1L;
	/** идентификатор объекта  */
	private String targetId;
	/** параметры объекта-перехватчика */
	private Object parameters;
	/** тело запроса Ajax */
	private AjaxRequestTarget ajaxTarget;
	/** наименовании опции в сообщении (фасад для множества сообщений к одному Interceptor ) */
	private String optionName;
	
	/** сообщение, которое может быть перенаправлено между независимыми частями шаблона Mapper 
	 * @param targetId - идентификатор объекта перехватчика 
	 * @param parameters - параметры 
	 */
	public MapperMessage(String targetId, Object parameters){
		this(targetId, (String)null, parameters);
	}
	
	/** сообщение, которое может быть перенаправлено между независимыми частями шаблона Mapper 
	 * @param targetId - идентификатор объекта перехватчика
	 * @param optionName - name of option 
	 * @param parameters - параметры 
	 */
	public MapperMessage(String targetId, String optionName, Object parameters){
		this.targetId=targetId;
		this.optionName=optionName;
		this.parameters=parameters;
	}

	/** сообщение, которое может быть перенаправлено между независимыми частями шаблона Mapper 
	 * @param targetId - идентификатор объекта перехватчика
	 * @param ajaxTarget - тело Ajax запроса 
	 * @param parameters - параметры 
	 */
	public MapperMessage(String targetId, AjaxRequestTarget ajaxTarget, Object parameters){
		this(targetId, ajaxTarget, null, parameters);
	}
	
	/** сообщение, которое может быть перенаправлено между независимыми частями шаблона Mapper 
	 * @param targetId - идентификатор объекта перехватчика
	 * @param ajaxTarget - тело Ajax запроса
	 * @param optionName - имя опции  
	 * @param parameters - параметры 
	 */
	public MapperMessage(String targetId, AjaxRequestTarget ajaxTarget, String optionName, Object parameters){
		this.targetId=targetId;
		this.ajaxTarget=ajaxTarget;
		this.optionName=optionName;
		this.parameters=parameters;
	}
	
	/** получить уникальный идентификатор */
	public String getTargetId() {
		return targetId;
	}
	
	/**  получить параметры */
	public Object getParameters() {
		return parameters;
	}

	/** получить имя опции для Interceptor */
	public String getOptionName(){
		return this.optionName;
	}
	
	/** получить тело запроса Ajax */
	public AjaxRequestTarget getAjaxTarget() {
		return ajaxTarget;
	}

	/** является ли сообщение типом Ajax запроса */
	public boolean isAjaxRequest(){
		return this.ajaxTarget!=null;
	}
	
}
