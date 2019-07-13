package shop_list.html.parser.engine.record;

/** ������ ����� ������� �� ���������� ������  */
public class Record {
	private String name;
	private String description;
	private String url;
	private Float price;
	private Float priceUSD;
	private Float priceEURO;
	
	/** ������ ����� ������� �� ���������� ������
	 * (������� ����� ������ ������ )
	 */
	public Record(){
	}
	
	/** ������ ����� ������� �� ���������� ������  
	 * @param name - ������������
	 * @param description - ��������
	 * @param url - ������ �� ������ ������ � �������� �������� ( �������������, � �� ���������� )
	 * @param price - ���� 
	 * @param priceUSD - ���� � USD 
	 * @param priceEURO - ���� � EURO 
	 */
	public Record(String name, 
				  String description, 
				  String url, 
				  Float price, 
				  Float priceUSD,
				  Float priceEURO){
		this.name=name;
		this.description=description;
		this.url=url;
		this.price=price;
		this.priceUSD=priceUSD;
		this.priceEURO=priceEURO;
	}

	/** ���������� ���� */
	public void setName(String value){
		this.name=value;
	}
	
	/** ��� ������  */
	public String getName() {
		return name;
	}

	/** �������� ��� ������  */
	public String getDescription() {
		return description;
	}

	/** ������ �� url, ��� ��������� ������ �����  */
	public String getUrl() {
		return url;
	}
	/** ���������� ������ �� ��������  */
	public void setUrl(String url) {
		this.url=url;
	}

	/** ���� ������ */
	public Float getPrice() {
		return price;
	}

	/** ���� ������ � USD */
	public Float getPriceUsd() {
		return priceUSD;
	}
	/** ���� ������ � EURO */
	public Float getPriceEuro() {
		return priceEURO;
	}

	@Override
	public String toString(){
		return " === Price:"+this.price+"  >>>Price$:"+this.priceUSD+" >>>Name:"+this.name+" >>>Desc:"+description+" Url:"+this.url+" --- ";
	}
	

	@Override
	public int hashCode() {
		return ((this.name!=null)?this.name.length():0)+((this.description!=null)?this.description.length():0)+((this.url!=null)?this.url.length():0);
	}
	
	@Override
	public boolean equals(Object object) {
		if(object instanceof Record){
			Record destination=(Record)object;
			boolean returnValue=true;
			while(true){
				if(this.compareStringValues(this.name, destination.name)==false){
					returnValue=false;
					break;
				}
				if(this.compareStringValues(this.description, destination.description)==false){
					returnValue=false;
					break;
				}
				if(this.compareStringValues(this.url, destination.url)==false){
					returnValue=false;
					break;
				}
				if(this.compareFloatValues(this.price, destination.price)==false){
					returnValue=false;
					break;
				}
				if(this.compareFloatValues(this.priceUSD, destination.priceUSD)==false){
					returnValue=false;
					break;
				}
				if(this.compareFloatValues(this.priceEURO, destination.priceEURO)==false){
					returnValue=false;
					break;
				}
				break;
			}
			return returnValue;
		}else{
			return false;
		}
	}
	
	/** �������� ��� ��������� ��������, � ���� ��� �������� �������� ����������� ��� ��� ����� null - ������� true */
	private boolean compareStringValues(String source, String destination){
		boolean returnValue=true;
		if((source==null)&&(destination==null)){
			returnValue=true;
		}else if((source!=null)&&(destination!=null)){
			returnValue=source.equals(destination);
		}else{
			// source!=null && destination==null    || source==null && destination !=null 
			returnValue=false;
		}
		return returnValue;
	}
	
	/** �������� ��� Float ��������, � ���� ��� �������� �������� ����������� ��� ��� ����� null - ������� true */
	private boolean compareFloatValues(Float source, Float destination){
		boolean returnValue=true;
		if((source==null)&&(destination==null)){
			returnValue=true;
		}else if((source!=null)&&(destination!=null)){
			returnValue=(source.floatValue()==destination.floatValue()); 
		}else{
			// source!=null && destination==null    || source==null && destination !=null 
			returnValue=false;
		}
		return returnValue;
	}

	public void setPrice(Float price2) {
		this.price=price2;
	}
	public void setPriceUsd(Float price2) {
		this.priceUSD=price2;
	}
	public void setPriceEuro(Float price2) {
		this.priceEURO=price2;
	}
	
}
