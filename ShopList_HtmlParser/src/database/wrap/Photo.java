package database.wrap;
import javax.persistence.*;

@Entity
@Table(name="PHOTO")
public class Photo {
	@Id
	@GeneratedValue(generator="generator",strategy=GenerationType.AUTO)
	@SequenceGenerator(name="generator",sequenceName="GEN_PHOTO_ID")
	@Column(name="ID")
	private int id;
	@Column(name="FILENAME")
	private String filename;
	@Column(name="FULL_NAME")
	private String fullName;
	@Column(name="HTTP_PATH")
	private String httpPath;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}
	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/**
	 * @return the httpPath
	 */
	public String getHttpPath() {
		return httpPath;
	}
	/**
	 * @param httpPath the httpPath to set
	 */
	public void setHttpPath(String httpPath) {
		this.httpPath = httpPath;
	}
	
	
}
