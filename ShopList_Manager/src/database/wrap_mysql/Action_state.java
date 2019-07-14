package database.wrap_mysql;

import java.io.Serializable;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="action_state") 
public class Action_state implements Serializable{
	@Transient
	private final static long serialVersionUID=1L;
	
	@Id
	@Column(name="ID")
	@GeneratedValue
    private Integer id;

	@Column(name="NAME")
	private String name;

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

}
