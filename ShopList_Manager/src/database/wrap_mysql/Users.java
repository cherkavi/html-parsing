package database.wrap_mysql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users") 
public class Users {
	@Id
	@Column(name="ID")
	@GeneratedValue
    private Integer id;

	@Column(name="NAME")
    private String name;

	@Column(name="USER_LOGIN")
    private String user_login;

	@Column(name="USER_PASSWORD")
    private String user_password;

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

	public String getUser_login() {
		return user_login;
	}

	public void setUser_login(String userLogin) {
		user_login = userLogin;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String userPassword) {
		user_password = userPassword;
	}
	
}
