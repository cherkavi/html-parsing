package manager;

import database.wrap_mysql.EActionState;

/** интерфейс, который сигнализирует об окончании обработки Action */
interface IActionComplete {
	/** метод, получающий уведомление об окончании обработки Action
	 * @param actionId - уникальный код Action
	 * @param state - состояние ({@link EActionState#DONE}  {@link EActionState#ERROR} {@link EActionState#STOPPED})
	 */
	public void actionComplete(Integer actionId, EActionState state);
}
