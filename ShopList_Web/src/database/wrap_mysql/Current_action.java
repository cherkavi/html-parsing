package database.wrap_mysql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="current_action") 
public class Current_action {
	@Id
	@Column(name="ID")
	@GeneratedValue
    private Integer id;

	@Column(name="ID_SESSION")
    private Integer id_session;

	@Column(name="ID_ACTIONS")
    private Integer id_actions;

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

	public Integer getId_session() {
		return id_session;
	}

	public void setId_session(Integer idSession) {
		id_session = idSession;
	}
}
