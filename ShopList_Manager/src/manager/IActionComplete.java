package manager;

import database.wrap_mysql.EActionState;

/** ���������, ������� ������������� �� ��������� ��������� Action */
interface IActionComplete {
	/** �����, ���������� ����������� �� ��������� ��������� Action
	 * @param actionId - ���������� ��� Action
	 * @param state - ��������� ({@link EActionState#DONE}  {@link EActionState#ERROR} {@link EActionState#STOPPED})
	 */
	public void actionComplete(Integer actionId, EActionState state);
}
