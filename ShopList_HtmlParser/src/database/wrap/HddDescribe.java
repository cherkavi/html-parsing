package database.wrap;

import javax.persistence.*;

@Entity
@Table(name="HDD_DESCRIBE")
public class HddDescribe extends BaseDescribe{
	@Id
	@GeneratedValue(generator="generator",strategy=GenerationType.AUTO)
	@SequenceGenerator(name="generator",sequenceName="GEN_HDD_DESCRIBE_ID")
	@Column(name="ID")
	private int id;
	@Column(name="ID_MB")
	private Integer idMb;
	@Column(name="ID_DESCRIBE_NAME")
	private Integer idDescribeName;
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
	 * @return the idMb
	 */
	public Integer getIdMb() {
		return idMb;
	}
	/**
	 * @param idMb the idMb to set
	 */
	public void setIdMb(Integer idMb) {
		this.idMb = idMb;
	}
	/**
	 * @return the idDescribeName
	 */
	public Integer getIdDescribeName() {
		return idDescribeName;
	}
	/**
	 * @param idDescribeName the idDescribeName to set
	 */
	public void setIdDescribeName(Integer idDescribeName) {
		this.idDescribeName = idDescribeName;
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
