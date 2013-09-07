package tk.sweetvvck.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class HouseInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private HashMap<String, String> images;
	private Set<Photo> photos = new HashSet<Photo>();
	/**
	 * @author 程科 增加邮件Item是否被翻开属性 2013-07-31
	 */
	private boolean openedFlag;

	public boolean isOpenedFlag() {
		return openedFlag;
	}

	public void setOpenedFlag(boolean openedFlag) {
		this.openedFlag = openedFlag;
	}

	public Set<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(Set<Photo> photos) {
		this.photos = photos;
	}

	private String zone;
	private String place;
	private String location;
	private String houseType;
	private String price;
	private String title;
	private String discription;
	private String contact;
	private String phoneNum;
	private String identity;

	public HashMap<String, String> getImages() {
		return images;
	}

	public void setImages(HashMap<String, String> images) {
		this.images = images;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getHouseType() {
		return houseType;
	}

	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
}
