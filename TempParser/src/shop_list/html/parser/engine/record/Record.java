package shop_list.html.parser.engine.record;

/** запись одной позиции по указанному товару  */
public class Record {
	private String name;
	private String description;
	private String url;
	private Float price;
	private Float priceUSD;
	private Float priceEURO;
	
	/** запись одной позиции по указанному товару
	 * (создать новую пустую запись )
	 */
	public Record(){
	}
	
	/** запись одной позиции по указанному товару  
	 * @param name - наименование
	 * @param description - описание
	 * @param url - ссылка на данную запись в масштабе магазина ( относительная, а не абсолютная )
	 * @param price - цена 
	 * @param priceUSD - цена в USD 
	 * @param priceEURO - цена в EURO 
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

	/** установить цену */
	public void setName(String value){
		this.name=value;
	}
	
	/** имя записи  */
	public String getName() {
		return name;
	}

	/** описание для записи  */
	public String getDescription() {
		return description;
	}

	/** ссылка на url, где отображен данный товар  */
	public String getUrl() {
		return url;
	}
	/** установить ссылку на страницу  */
	public void setUrl(String url) {
		this.url=url;
	}

	/** цена товара */
	public Float getPrice() {
		return price;
	}

	/** цена товара в USD */
	public Float getPriceUsd() {
		return priceUSD;
	}
	/** цена товара в EURO */
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
	
	/** сравнить два строковых значения, и если эти значения являются идентичными или оба равны null - вернуть true */
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
	
	/** сравнить два Float значения, и если эти значения являются идентичными или оба равны null - вернуть true */
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
