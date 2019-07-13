package html_parser.record;


/** запись, которая находится в списке на странице 
 * <br>
 * <b> обязательно должен быть переопределен метод <i>equals</i> потому как он будет "говорить" о соответствии одного списка записей другому, при перемещении со страницы на страницу</b>
 * */
public abstract class Record {
	
	protected String getConcatString(String value, int limit){
		if(value==null){
			return null;
		}else{
			if(value.length()>limit){
				return value.substring(0,limit-1);
			}else{
				return value;
			}
		}
	}

	@Override
	public abstract boolean equals(Object object);
}
