package database.wrap_mysql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
@Entity
@Table(name="actions") 
public class Actions {
	@Id
	@Column(name="ID")
	@GeneratedValue
    private Integer id;

	@Column(name="DATE_WRITE")
    private Date date_write;

	@Column(name="ID_ACTION_STATE")
    private Integer id_action_state;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate_write() {
		return date_write;
	}

	public void setDate_write(Date dateWrite) {
		date_write = dateWrite;
	}

	public Integer getId_action_state() {
		return id_action_state;
	}

	public void setId_action_state(Integer idActionState) {
		id_action_state = idActionState;
	}

}
