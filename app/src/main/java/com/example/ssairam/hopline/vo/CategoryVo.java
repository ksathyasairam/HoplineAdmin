package com.example.ssairam.hopline.vo;
// Generated 6 Oct, 2016 1:14:11 AM by Hibernate Tools 5.2.0.Beta1

import java.util.List;


public class CategoryVo implements java.io.Serializable {

	private Integer idcategory;
	private ShopVo shop;
	private String name;
	private String subCategoryName;
	private int sortId;
	private String imgUrl;
	private List<ProductVo> products;
	
	public Integer getIdcategory() {
		return idcategory;
	}
	public void setIdcategory(Integer idcategory) {
		this.idcategory = idcategory;
	}
	public ShopVo getShop() {
		return shop;
	}
	public void setShop(ShopVo shop) {
		this.shop = shop;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSubCategoryName() {
		return subCategoryName;
	}
	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}
	public int getSortId() {
		return sortId;
	}
	public void setSortId(int sortId) {
		this.sortId = sortId;
	}
	public List<ProductVo> getProducts() {
		return products;
	}
	public void setProducts(List<ProductVo> products) {
		this.products = products;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}



}