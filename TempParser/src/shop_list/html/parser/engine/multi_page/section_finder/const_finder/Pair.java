package shop_list.html.parser.engine.multi_page.section_finder.const_finder;

/** одна пара значений  */
public class Pair<K,T> {
	private K name;
	private T value;
	
	public Pair(K name, T value) {
		super();
		this.name = name;
		this.value = value;
	}

	/** первое значение  */
	public K getName() {
		return name;
	}

	/** второе значение  */
	public T getValue() {
		return value;
	}
	
}
