package com.example.ssairam.hopline.vo;

import java.util.List;


public class OrderProductVo implements java.io.Serializable {

	private Integer idorderProduct;
	private ProductVo product;
	private Integer count;
	private List<OrderProductAddonVo> orderProductAddons;
	
	public Integer getIdorderProduct() {
		return idorderProduct;
	}
	public void setIdorderProduct(Integer idorderProduct) {
		this.idorderProduct = idorderProduct;
	}
	public ProductVo getProduct() {
		return product;
	}
	public void setProduct(ProductVo product) {
		this.product = product;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public List<OrderProductAddonVo> getOrderProductAddons() {
		return orderProductAddons;
	}
	public void setOrderProductAddons(List<OrderProductAddonVo> orderProductAddons) {
		this.orderProductAddons = orderProductAddons;
	}
	
	

	
}
