package database.wrap_mysql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * <table border="1">
 * 	<tr>
 * 		<th>Database</th> <th>POJO</th>
 * 		<td>ID</td> <td>id</td>
 * 		<td>ID_ACTIONS</td> <td>id_actions</td>
 * 		<td>ID_SHOP</td> <td>id_shop</td>
 * 		<td>ID_PARSE_SESSION</td> <td>id_parse_session</td>
 * 	</tr>
 * </table> 
 */
@Entity
@Table(name="current_action") 
public class Current_action {
	@Id
	@Column(name="ID")
	@GeneratedValue
    private Integer id;

	@Column(name="ID_ACTIONS")
    private Integer id_actions;

	@Column(name="ID_SHOP")
	private Integer id_shop;
	
	@Column(name="ID_PARSE_SESSION")
	private Integer id_parse_session;

	@OneToOne(targetEntity=Shop_list.class,fetch=FetchType.EAGER)
	@JoinColumn(name="ID_SHOP",nullable=true,columnDefinition="id",insertable=false, updatable=false)
	private Shop_list shop;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId_actions() {
		return id_actions;
	}

	public void setId_actions(Integer idActions) {
		id_actions = idActions;
	}

	public Integer getId_shop() {
		return id_shop;
	}

	public void setId_shop(Integer idShop) {
		id_shop = idShop;
	}

	public Integer getId_parse_session() {
		return id_parse_session;
	}

	public void setId_parse_session(Integer idParseSession) {
		id_parse_session = idParseSession;
	}

	public Shop_list getShop() {
		return shop;
	}

	public void setShop(Shop_list shop) {
		this.shop = shop;
	}

}
