package database.wrap_mysql;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="shop_list") 
public class Shop_list {
	@Id
	@Column(name="ID")
	@GeneratedValue
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
