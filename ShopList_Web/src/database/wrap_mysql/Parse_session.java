package database.wrap_mysql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
@Entity
@Table(name="parse_session") 
public class Parse_session {
	@Id
	@Column(name="ID")
	@GeneratedValue
    private Integer id;

	@Column(name="ID_SHOP")
    private Integer id_shop;

	@Column(name="PARSE_BEGIN")
    private Date parse_begin;

	@Column(name="ID_PARSE_RESULT")
    private Integer id_parse_result;

	@Column(name="DESCRIPTION")
    private String description;

	@Column(name="DATE_WRITE")
    private Date date_write;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId_shop() {
		return id_shop;
	}

	public void setId_shop(Integer idShop) {
		id_shop = idShop;
	}

	public Date getParse_begin() {
		return parse_begin;
	}

	public void setParse_begin(Date parseBegin) {
		parse_begin = parseBegin;
	}

	public Integer getId_parse_result() {
		return id_parse_result;
	}

	public void setId_parse_result(Integer idParseResult) {
		id_parse_result = idParseResult;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate_write() {
		return date_write;
	}

	public void setDate_write(Date dateWrite) {
		date_write = dateWrite;
	}
	
}
