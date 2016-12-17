package com.example.ssairam.hopline.vo;

public class UserVo implements java.io.Serializable {

	private Integer iduser;
	private String phone;
	private String name;
	private String mac;

	public Integer getIduser() {
		return iduser;
	}
	public void setIduser(Integer iduser) {
		this.iduser = iduser;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
}
