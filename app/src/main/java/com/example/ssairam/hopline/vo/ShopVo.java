package com.example.ssairam.hopline.vo;

public class ShopVo implements java.io.Serializable {

	private Integer idshop;
	private String shopName;
	private String phone;
	private String imgUrl;
	private String activeYn;
	public Integer getIdshop() {
		return idshop;
	}
	public void setIdshop(Integer idshop) {
		this.idshop = idshop;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getActiveYn() {
		return activeYn;
	}
	public void setActiveYn(String activeYn) {
		this.activeYn = activeYn;
	}
	
}
