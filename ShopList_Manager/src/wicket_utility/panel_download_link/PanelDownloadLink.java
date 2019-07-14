package wicket_utility.panel_download_link;

import java.io.Serializable;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import wicket_utility.mapper_pattern.Mapper;
import wicket_utility.mapper_pattern.MapperMessage;

public class PanelDownloadLink extends Panel{
	private final static long serialVersionUID=1L;
	private final Mapper mapper;
	/** панель, которая содержит Ajax ссылку  
	 * @param id - уникальный идентификатор 
	 * @param title - заголовок для ссылки 
	 * @param action - объект, которому следует передать действие 
	 * @param actionName - имя действия, которое следует передавать в action 
	 * @param actionValue - значение действия, которое следует передавать в action
	 * @param ajaxIndicatorHtmlId - уникальный HTML.id идентификатора 
	 */
	public PanelDownloadLink(String id, 
						 	final String title, 
						 	Mapper mapper,
						 	final String targetId,
						 	final String optionName,
						 	final Serializable value){
		super(id);
		this.mapper=mapper;
		Link<?> link=new Link<Object>("link"){
			private static final long serialVersionUID=1L;
			@Override
			public void onClick() {
				onLinkClick(targetId, optionName, value);
			}
		};
		this.add(link);
		
		link.add(new Label("label", title));
	}
	
	/** реакция на нажатие на ссылку  
	 * @param target - Ajax транспорт 
	 * @param actionName - action Name
	 * @param actionValue - action Value
	 * @param title - имя файла, которое будет возвращего пользователю  
	 */
	private void onLinkClick(String targetId, 
							 String actionName, 
						 	 Object actionValue){
		this.mapper.sendMessageToInterceptor(new MapperMessage(targetId, actionName, actionValue));
	}
}