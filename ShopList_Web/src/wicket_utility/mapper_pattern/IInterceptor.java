package wicket_utility.mapper_pattern;

/** реализация интерфейса Перехватчик для приема сообщений  */
public interface IInterceptor {
	/** получить сообщение от внешнего источника  */
	public void getOutsideMessage(MapperMessage message);
	
	/** получить уникальный идентификатор Перехватчика */
	public String getInterceptorId();
}
