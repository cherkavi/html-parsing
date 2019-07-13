package database.wrap;

import javax.persistence.*;

@Entity
@Table(name="VIDEO")
public class Video extends Base{
	@Id
	@GeneratedValue(generator="generator",strategy=GenerationType.AUTO)
	@SequenceGenerator(name="generator",sequenceName="GEN_VIDEO_ID")
	@Column(name="ID")
	private int id;
	@Column(name="NAME",length=255)
	private String name;

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
}
