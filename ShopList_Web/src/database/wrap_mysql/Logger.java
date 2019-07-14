package database.wrap_mysql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="logger") 
public class Logger {
	@Id
	@Column(name="ID")
	@GeneratedValue
    private Integer id;

	@Column(name="ID_SESSION")
    private Integer id_session;

	@Column(name="ID_LOGGER_LEVEL")
    private Integer id_logger_level;

	@Column(name="LOGGER_MESSAGE")
    private String logger_message;

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

	public Integer getId_logger_level() {
		return id_logger_level;
	}

	public void setId_logger_level(Integer idLoggerLevel) {
		id_logger_level = idLoggerLevel;
	}

	public String getLogger_message() {
		return logger_message;
	}

	public void setLogger_message(String loggerMessage) {
		logger_message = loggerMessage;
	}
	
}
