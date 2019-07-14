package shop_list.html.parser.engine.record;

import database.wrap_mysql.Parse_record;

/** запись одной позиции по указанному товару  */
public class Record {
	private String name;
	private String description;
	private String url;
	private Float price;
	private Float priceUSD;
	private Float priceEURO;
	
	/** запись одной позиции по указанному товару  
	 * @param name - наименование
	 * @param description - описание
	 * @param url - ссылка на данный ресурс
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

	/** цена товара */
	public Float getPrice() {
		return price;
	}

	@Override
	public String toString(){
		return "Name:"+this.name+" Desc:"+description+" Price:"+this.price+"  Price$:"+this.priceUSD+" Url: "+this.url;
	}
	
	/**  получить объект для записи в базу данных 
	 * @param idSession - уникальный номер сессии, по которой происходит запись
	 * @param idSection - уникальный номер секции ( раздела сайта ), по которой происходит запись
	 * @return
	 */
	public Parse_record getDatabaseRecord(Integer idSession, Integer idSection){
		Parse_record record=new Parse_record();
		record.setId_session(idSession);
		record.setId_section(idSection);
		record.setName(name);
		record.setUrl(url);
		record.setDescription(description);
		record.setAmount(price);
		record.setAmount_usd(priceUSD);
		record.setAmount_euro(priceEURO);
		return record;
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
	
}
