package html_parser.record;


/** ������, ������� ��������� � ������ �� �������� 
 * <br>
 * <b> ����������� ������ ���� ������������� ����� <i>equals</i> ������ ��� �� ����� "��������" � ������������ ������ ������ ������� �������, ��� ����������� �� �������� �� ��������</b>
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
