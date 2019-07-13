package shop_list.html.parser.engine.multi_page.section_finder.const_finder;

/** ���� ���� ��������  */
public class Pair<K,T> {
	private K name;
	private T value;
	
	public Pair(K name, T value) {
		super();
		this.name = name;
		this.value = value;
	}

	/** ������ ��������  */
	public K getName() {
		return name;
	}

	/** ������ ��������  */
	public T getValue() {
		return value;
	}
	
}
