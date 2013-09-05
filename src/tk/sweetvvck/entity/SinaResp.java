package tk.sweetvvck.entity;

public class SinaResp {

	/*
	 * {"tinyurl":"http://tp2.sinaimg.cn/1621387877/50/5665212522/1","birthday":"0"
	 * ,
	 * "media_type":"sinaweibo","sex":"1","is_verified":"0","university_history"
	 * :[],"work_history":[],"mainurl":
	 * "http://tp2.sinaimg.cn/1621387877/50/5665212522/1"
	 * ,"expires_in":"625966","headurl"
	 * :"http://tp2.sinaimg.cn/1621387877/50/5665212522/1"
	 * ,"city":"朝阳区","hometown_location"
	 * :[],"username":"sweetvvck","province":"北京"
	 * ,"social_uid":3909405914,"access_token"
	 * :"50.994f3a95a784926bf5dd51a9bc2560b2.625966.1379012400.3909405914-391335"
	 * ,"media_uid":"1621387877","hs_history":[]}
	 */
	
	private String tinyurl;
	private String birthday;
	private String media_type;
	private String sex; 
	private String is_verified;
	private String[] university_history;
	private String[] work_history;
	private String mainurl;
	private String city;
	private String[] hometown_location;
	private String username;
	private String province;
	private String social_uid;
	private String[] hs_history;
	private String headurl;
	public String getHeadurl() {
		return headurl;
	}
	public void setHeadurl(String headurl) {
		this.headurl = headurl;
	}
	public String getTinyurl() {
		return tinyurl;
	}
	public void setTinyurl(String tinyurl) {
		this.tinyurl = tinyurl;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getMedia_type() {
		return media_type;
	}
	public void setMedia_type(String media_type) {
		this.media_type = media_type;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getIs_verified() {
		return is_verified;
	}
	public void setIs_verified(String is_verified) {
		this.is_verified = is_verified;
	}
	public String[] getUniversity_history() {
		return university_history;
	}
	public void setUniversity_history(String[] university_history) {
		this.university_history = university_history;
	}
	public String[] getWork_history() {
		return work_history;
	}
	public void setWork_history(String[] work_history) {
		this.work_history = work_history;
	}
	public String getMainurl() {
		return mainurl;
	}
	public void setMainurl(String mainurl) {
		this.mainurl = mainurl;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String[] getHometown_location() {
		return hometown_location;
	}
	public void setHometown_location(String[] hometown_location) {
		this.hometown_location = hometown_location;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getSocial_uid() {
		return social_uid;
	}
	public void setSocial_uid(String social_uid) {
		this.social_uid = social_uid;
	}
	public String[] getHs_history() {
		return hs_history;
	}
	public void setHs_history(String[] hs_history) {
		this.hs_history = hs_history;
	}
	
}
