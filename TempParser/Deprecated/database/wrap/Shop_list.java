package database.wrap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="SHOP_LIST") 
public class Shop_list {
	@Id
	@Column(name="ID")
	@SequenceGenerator(name="generator",sequenceName="GEN_SHOP_LIST_ID")
	@GeneratedValue(generator="generator",strategy=GenerationType.AUTO)
    	private Integer id;

	@Column(name="NAME")
    	private String name;

	@Column(name="START_PAGE")
    	private String start_page;

	@Column(name="DESCRIPTION")
    	private String description;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStart_page() {
		return start_page;
	}

	public void setStart_page(String startPage) {
		start_page = startPage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	
}
