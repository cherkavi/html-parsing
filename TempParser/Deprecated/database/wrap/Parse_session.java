package database.wrap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import shop_list.html.parser.engine.ESessionResult;





import java.util.Date;
@Entity
@Table(name="PARSE_SESSION") 
public class Parse_session {
	@Id
	@Column(name="ID")
	@SequenceGenerator(name="generator",sequenceName="GEN_PARSE_SESSION_ID")
	@GeneratedValue(generator="generator",strategy=GenerationType.AUTO)
    	private Integer id;

	@Column(name="ID_SHOP")
    	private Integer id_shop;

	@Column(name="PARSE_BEGIN")
    	private Date parse_begin;

	@Column(name="ID_PARSE_RESULT")
    	private Integer id_parse_result;

	@Column(name="DESCRIPTION")
    	private String description;

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

	/** результат парсинга {@link ESessionResult}}*/
	public Integer getId_parse_result() {
		return id_parse_result;
	}

	/** результат парсинга {@link ESessionResult}}*/
	public void setId_parse_result(Integer idParseResult) {
		id_parse_result = idParseResult;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
