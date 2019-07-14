package manager.utility;

import java.util.ArrayList;

public class UniqueIntegerArrayList {
	private final static long serialVersionUID=1L;
	private ArrayList<Integer> list=new ArrayList<Integer>();

	public void add(int index, Integer element) {
		if(this.isUniqueElement(element)){
			list.add(index,element);
		}
	};
	
	/** можно ли вставить еще один элемент */
	private boolean isUniqueElement(Integer element){
		boolean returnValue=true;
		for(Integer value:list){
			if(value.intValue()==element.intValue()){
				returnValue=false;
				break;
			}
		}
		return returnValue;
	}
	
	public boolean add(Integer e) {
		if(isUniqueElement(e)){
			return list.add(e);
		}else{
			return false;
		}
	}

	public int size() {
		return list.size();	
	}

	/** удалить объект по указанному  */
	public Integer remove(Object value) {
		Integer objectValue=(Integer)value;
		for(int counter=0;counter<this.list.size();counter++){
			if(this.list.get(counter).intValue()==objectValue){
				return this.remove(counter);
			}
		}
		return null;
	}

	public Integer remove(int index) {
		return this.list.remove(index);
	}

	public void clear() {
		this.list.clear();
	};
	
	
}
