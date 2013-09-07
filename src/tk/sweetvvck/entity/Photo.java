package tk.sweetvvck.entity;

public class Photo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String path;
	private HouseInfo houseInfo;

	public Photo() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public HouseInfo getHouseInfo() {
		return houseInfo;
	}

	public void setHouseInfo(HouseInfo houseInfo) {
		this.houseInfo = houseInfo;
	}
}