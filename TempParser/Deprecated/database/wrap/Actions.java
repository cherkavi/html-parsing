package database.wrap;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="ACTIONS") 
public class Actions {
	@Id
	@Column(name="ID")
	@SequenceGenerator(name="generator",sequenceName="GEN_ACTIONS_ID")
	@GeneratedValue(generator="generator",strategy=GenerationType.AUTO)
    private Integer id;

	@Column(name="DATE_WRITE")
	private Date dateWrite;
	
	@Column(name="ID_ACTION_STATE")
	private Integer idActionState;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDateWrite() {
		return dateWrite;
	}

	public void setDateWrite(Date dateWrite) {
		this.dateWrite = dateWrite;
	}

	public Integer getIdActionState() {
		return idActionState;
	}

	public void setIdActionState(Integer idActionState) {
		this.idActionState = idActionState;
	}
	
	
}
