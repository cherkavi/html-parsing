package manager.utility;

import java.util.ArrayList;

/** огранниченный ArrayList размером данных */
public class LimitedArrayList<T> extends ArrayList<T>{
	private final static long serialVersionUID=1L;
	private int size=10;
	
	public LimitedArrayList(int size){
		super(size);
		this.size=size;
	}
	
	public void add(int index, T element) {
		if(isCanAddElement()&&(index<this.size)){
			super.add(index,element);
		}
	};
	
	/** можно ли вставить еще один элемент */
	private boolean isCanAddElement(){
		return this.size()<this.size;
	}
	
	public boolean add(T e) {
		if(isCanAddElement()){
			return super.add(e);
		}else{
			return false;
		}
	}

	/** заполнен ли данный объект элементами  */
	public boolean isFull() {
		return this.isCanAddElement()==false;
	};
	
}
