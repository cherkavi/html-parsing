package database.wrap_mysql;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="parse_record") 
public class Parse_record {
	@Id
	@Column(name="ID")
	@GeneratedValue
    private Integer id;

	@Column(name="ID_SESSION")
    private Integer id_session;

	@Column(name="ID_SECTION")
    private Integer id_section;

	@Column(name="URL")
    private String url;

	@Column(name="NAME")
    private String name;

	@Column(name="DESCRIPTION")
    private String description;

	@Column(name="AMOUNT")
    private Float amount;

	@Column(name="AMOUNT_USD")
    private Float amount_usd;

	@Column(name="AMOUNT_EURO")
    private Float amount_euro;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId_session() {
		return id_session;
	}

	public void setId_session(Integer idSession) {
		id_session = idSession;
	}

	public Integer getId_section() {
		return id_section;
	}

	public void setId_section(Integer idSection) {
		id_section = idSection;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public Float getAmount_usd() {
		return amount_usd;
	}

	public void setAmount_usd(Float amountUsd) {
		amount_usd = amountUsd;
	}

	public Float getAmount_euro() {
		return amount_euro;
	}

	public void setAmount_euro(Float amountEuro) {
		amount_euro = amountEuro;
	}
}
