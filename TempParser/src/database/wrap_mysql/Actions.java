package database.wrap_mysql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.io.Serializable;
import java.util.Date;

/**
 *<table border="1">
 *	<tr>
 *		<th>Database</th>
 *		<th>POJO</th>
 *	</tr>
 *	<tr>
 *		<td>ID</td>
 *		<td>id</td>
 *	</tr>
 *	<tr>
 *		<td>DATE_WRITE</td>
 *		<td>date_write</td>
 *	</tr>
 *	<tr>
 *		<td>ID_ACTION_STATE</td>
 *		<td>id_action_state</td>
 *	</tr>
 *</table> 
 */
@Entity
@Table(name="actions") 
public class Actions implements Serializable{
	@Transient
	private final static long serialVersionUID=1L;
	
	@Id
	@Column(name="ID")
	@GeneratedValue
    private Integer id;

	@Column(name="DATE_WRITE")
    private Date date_write;

	@Column(name="ID_ACTION_STATE")
    private Integer id_action_state;

	@OneToOne(targetEntity=Action_state.class,fetch=FetchType.EAGER)
	@JoinColumn(name="ID_ACTION_STATE",nullable=true,columnDefinition="id",insertable=false, updatable=false)
	private Action_state actionState;
	
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

	public Action_state getActionState() {
		return actionState;
	}

	public void setActionState(Action_state actionState) {
		this.actionState = actionState;
	}

	
}
